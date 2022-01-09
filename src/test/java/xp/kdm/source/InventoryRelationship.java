package xp.kdm.source;

import xp.kdm.core.KDMEntity;

public class InventoryRelationship extends AbstractInventoryRelationship<AbstractInventoryElement, KDMEntity> {

    public InventoryRelationship(InventoryModel kdmModel, AbstractInventoryElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
