package xp.kdm.build;

public class SuppliedBy extends AbstractBuildRelationship<AbstractBuildElement, Supplier> {

    public SuppliedBy(BuildModel kdmModel, AbstractBuildElement from, Supplier to) {
        super(kdmModel, from, to);
    }

}
