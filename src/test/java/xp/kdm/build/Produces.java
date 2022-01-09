package xp.kdm.build;

public class Produces extends AbstractBuildRelationship<BuildStep, AbstractBuildElement> {

    public Produces(BuildModel kdmModel, BuildStep from, AbstractBuildElement to) {
        super(kdmModel, from, to);
    }

}
