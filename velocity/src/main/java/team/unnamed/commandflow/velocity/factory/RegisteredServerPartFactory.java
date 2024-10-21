package team.unnamed.commandflow.velocity.factory;

import com.velocitypowered.api.proxy.ProxyServer;
import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.velocity.part.RegisteredServerPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class RegisteredServerPartFactory implements PartFactory {

    private final ProxyServer proxyServer;

    public RegisteredServerPartFactory(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new RegisteredServerPart(proxyServer, name);
    }
}
