package team.unnamed.commandflow.velocity.part;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.Component;
import team.unnamed.commandflow.CommandContext;
import team.unnamed.commandflow.exception.ArgumentParseException;
import team.unnamed.commandflow.part.ArgumentPart;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisteredServerPart implements ArgumentPart {

    private final ProxyServer proxyServer;
    private final String name;

    public RegisteredServerPart(ProxyServer proxyServer, String name) {
        this.proxyServer = proxyServer;
        this.name = name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        List<String> servers = new ArrayList<>();

        if (prefix == null) {
            for (RegisteredServer server : proxyServer.getAllServers()) {
                servers.add(server.getServerInfo().getName());
            }
            return servers;
        }

        for (RegisteredServer server : proxyServer.matchServer(prefix)) {
            servers.add(server.getServerInfo().getName());
        }

        return servers;
    }

    @Override
    public List<?> parseValue(CommandContext context, ArgumentStack stack, CommandPart caller) throws ArgumentParseException {
        String target = stack.next();

        RegisteredServer server = proxyServer.getServer(target).orElse(null);

        if (server == null) {
            throw new ArgumentParseException(Component.translatable("server.not-found", Component.text(target)))
                    .setArgument(this);
        }

        return Collections.singletonList(server);
    }

    @Override
    public String getName() {
        return name;
    }
}
