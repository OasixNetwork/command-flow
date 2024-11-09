package team.unnamed.commandflow.part.defaults;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.ContextSnapshot;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.part.SinglePartWrapper;
import team.unnamed.commandflow.stack.ArgumentStack;
import team.unnamed.commandflow.stack.StackSnapshot;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.Nullable;

public class ValueFlagPart implements SinglePartWrapper {

    private final CommandPart part;
    private final String name;
    private final String shortName;
    private final boolean allowFullName;

    public ValueFlagPart(String shortName, boolean allowFullName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = allowFullName;

        this.part = part;
    }

    public ValueFlagPart(String shortName, CommandPart part) {
        this.name = part.getName();
        this.shortName = shortName;
        this.allowFullName = false;

        this.part = part;
    }

    @Override
    public @Nullable Component getLineRepresentation() {
        TextComponent.Builder builder = Component.text()
                .append(Component.text("["))
                .append(Component.text("-" + shortName + " "));

        if (part.getLineRepresentation() != null) {
            builder.append(part.getLineRepresentation());
        }

        builder.append(Component.text("]"));

        return builder.build();
    }

    @Override
    public CommandPart getPart() {
        return part;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        StackSnapshot snapshot = stack.getSnapshot();

        boolean found = false;

        while (stack.hasNext()) {
            String arg = stack.next();

            if (!arg.startsWith("-")) {
                continue;
            }

            if (arg.equals("--" + name) && allowFullName) {
                found = parseValueFlag(context, stack);

                break;
            }

            if (arg.equals("-" + shortName)) {
                found = parseValueFlag(context, stack);

                break;
            }
        }

        if (!found) {
            context.setValue(this, false);
        }

        stack.applySnapshot(snapshot, false);
    }

    private boolean parseValueFlag(CommandContext context, ArgumentStack stack) {
        stack.remove();
        int oldArgumentsLeft = stack.getArgumentsLeft();
        StackSnapshot beforeParseStack = stack.getSnapshot();

        // parse the next parts
        part.parse(context, stack, this);

        int usedArguments = oldArgumentsLeft - stack.getArgumentsLeft();

        if (usedArguments != 0) {
            stack.applySnapshot(beforeParseStack);
            // Otherwise it deletes the old cursor(element to remove - 1)
            stack.next();

            for (int i = 0; i < usedArguments; i++) {
                stack.remove();
            }

        }
        return true;
    }

    public String getShortName() {
        return shortName;
    }

    public boolean allowsFullName() {
        return allowFullName;
    }

}

