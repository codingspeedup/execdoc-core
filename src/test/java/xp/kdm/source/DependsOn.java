package xp.kdm.source;

public class DependsOn extends AbstractInventoryRelationship<AbstractInventoryElement, AbstractInventoryElement> {

    public DependsOn(InventoryModel kdmModel, AbstractInventoryElement from, AbstractInventoryElement to) {
        super(kdmModel, from, to);
    }

}
