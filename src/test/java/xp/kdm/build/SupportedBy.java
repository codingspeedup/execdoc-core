package xp.kdm.build;

public class SupportedBy extends AbstractBuildRelationship<BuildStep, Tool> {

    public SupportedBy(BuildModel kdmModel, BuildStep from, Tool to) {
        super(kdmModel, from, to);
    }

}
