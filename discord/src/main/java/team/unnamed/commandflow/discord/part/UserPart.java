package team.unnamed.commandflow.discord.part;

import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.discord.DiscordCommandManager;
import team.unnamed.commandflow.discord.utils.ArgumentsUtils;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.kyori.adventure.text.Component;

import java.util.Collections;
import java.util.List;

public class UserPart implements ArgumentPart {

    private final String name;

    public UserPart(String name) {
        this.name = name;
    }

    @Override
    public List<? extends User> parseValue(CommandContext context, ArgumentStack stack, CommandPart parent) throws ArgumentParseException {
        Message message = context.getObject(Message.class, DiscordCommandManager.MESSAGE_NAMESPACE);
        Guild guild = message.getGuild();

        String target = stack.next();

        User user = null;

        if (ArgumentsUtils.isValidSnowflake(target)) {
            user = guild.getJDA().getUserById(target);
        }

        if (user == null && ArgumentsUtils.isValidTag(target)) {
            user = guild.getJDA().getUserByTag(target);
        }

        if (user == null && ArgumentsUtils.isUserMention(target)) {
            String id = target.substring(3, target.length() - 1);
            user = guild.getJDA().getUserById(id);
        }

        if (user == null) {
            throw new ArgumentParseException(Component.translatable("unknown.user"))
                    .setArgument(this);
        }

        return Collections.singletonList(user);
    }

    @Override
    public String getName() {
        return name;
    }
}
