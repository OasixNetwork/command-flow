package team.unnamed.commandflow.bukkit.part;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.bukkit.BukkitCommonConstants;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.exception.CommandException;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import java.util.Objects;

public class ConsoleCommandSenderPart implements CommandPart {

    private final String name;

    public ConsoleCommandSenderPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void parse(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        CommandSender sender = context.getObject(CommandSender.class, BukkitCommonConstants.SENDER_NAMESPACE);

        if (sender != null) {
            if (sender instanceof ConsoleCommandSender) {
                context.setValue(this, sender);

                return;
            }

            throw new ArgumentParseException(Component.translatable("sender.only-console"));
        }

        throw new CommandException(Component.translatable("sender.unknown"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConsoleCommandSenderPart)) return false;
        ConsoleCommandSenderPart that = (ConsoleCommandSenderPart) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
