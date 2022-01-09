package xp.kdm.source;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractInventoryRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<InventoryModel, F, T> {

    public AbstractInventoryRelationship(InventoryModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
