package xp.kdm.build;

public class DescribedBy extends AbstractBuildRelationship<BuildStep, BuildDescription> {

    public DescribedBy(BuildModel kdmModel, BuildStep from, BuildDescription to) {
        super(kdmModel, from, to);
    }

}
