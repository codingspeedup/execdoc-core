package xp.kdm.build;

public class Consumes extends AbstractBuildRelationship<BuildStep, AbstractBuildElement> {

    public Consumes(BuildModel kdmModel, BuildStep from, AbstractBuildElement to) {
        super(kdmModel, from, to);
    }

}
