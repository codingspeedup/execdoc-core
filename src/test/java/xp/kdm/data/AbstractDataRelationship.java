package xp.kdm.data;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractDataRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<DataModel, F, T> {

    public AbstractDataRelationship(DataModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
