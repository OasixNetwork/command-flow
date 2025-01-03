package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.ContextSnapshot;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.exception.NoMoreArgumentsException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.PartsWrapper;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.SimpleArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptionalPart implements CommandPart, SinglePartWrapper {

    private final CommandPart part;
    private final List<String> defaultValues;
    private final boolean considerInvalidAsEmpty;
    private final boolean showSuggestions;

    public OptionalPart(CommandPart part, boolean showSuggestions) {
        this(part, true, showSuggestions);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty, boolean showSuggestions) {
        this(part, considerInvalidAsEmpty, new ArrayList<>(), showSuggestions);
    }

    public OptionalPart(CommandPart part, List<String> defaultValues, boolean showSuggestions) {
        this(part, false, defaultValues, showSuggestions);
    }

    public OptionalPart(CommandPart part, boolean considerInvalidAsEmpty, List<String> defaultValues, boolean showSuggestions) {
        this.part = part;
        this.defaultValues = defaultValues;
        this.considerInvalidAsEmpty = considerInvalidAsEmpty;
        this.showSuggestions = showSuggestions;
    }

    @Override
    public String getName() {
        return part.getName() + "-optional";
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        Component partLineRepresent = part.getLineRepresentation();

        if (partLineRepresent == null) {
            return null;
        }
        return Component.text()
                .append(Component.text("["))
                .append(partLineRepresent)
                .append(Component.text("]"))
                .build();
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();
        ContextSnapshot contextSnapshot = context.getSnapshot();

        try {
            part.parse(context, stack, caller);
        } catch (ArgumentParseException | NoMoreArgumentsException e) {
            if (shouldRewind(caller, e)) {
                throw e;
            }

            stack.applySnapshot(snapshot);
            context.applySnapshot(contextSnapshot);

            if (!defaultValues.isEmpty()) {
                try {
                    part.parse(context, new SimpleArgumentStack(defaultValues), this);
                } catch (ArgumentParseException | NoMoreArgumentsException ignored) {
                }
            }
        }
    }

    private boolean shouldRewind(CommandPart caller, CommandException e) {
        boolean isLast = true;

        if (caller instanceof PartsWrapper) {
            List<CommandPart> parts = ((PartsWrapper) caller).getParts();

            isLast = parts.indexOf(this) == parts.size() - 1;
        }

        return isLast && !(e instanceof NoMoreArgumentsException) && !considerInvalidAsEmpty;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        return showSuggestions ? part.getSuggestions(commandContext, stack) : Collections.emptyList();
    }

    @Override
    public boolean isAsync() {
        return part.isAsync();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    public List<String> getDefaultValues() {
        return defaultValues;
    }

}
