package team.unnamed.commandflow.velocity.factory;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import team.unnamed.commandflow.annotated.part.AbstractModule;
import team.unnamed.commandflow.annotated.part.Key;
import team.unnamed.commandflow.annotated.annotation.Sender;

public class VelocityModule extends AbstractModule {

    private final ProxyServer proxyServer;

    public VelocityModule(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void configure() {
        bindFactory(CommandSource.class, new CommandSourcePartFactory());
        bindFactory(Player.class, new PlayerPartFactory(proxyServer));
        bindFactory(new Key(Player.class, Sender.class), new PlayerSenderPartFactory());
        bindFactory(RegisteredServer.class, new RegisteredServerPartFactory(proxyServer));
        bindFactory(new Key(ConsoleCommandSource.class, Sender.class), new ConsoleCommandSourceSenderPartFactory());
    }
}
