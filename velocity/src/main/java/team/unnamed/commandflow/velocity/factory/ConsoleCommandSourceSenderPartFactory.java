package team.unnamed.commandflow.velocity.factory;

import team.unnamed.commandflow.annotated.part.PartFactory;
import team.unnamed.commandflow.part.CommandPart;
import team.unnamed.commandflow.velocity.part.ConsoleCommandSourceSenderPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ConsoleCommandSourceSenderPartFactory implements PartFactory {

    @Override
    public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
        return new ConsoleCommandSourceSenderPart(name);
    }
}
