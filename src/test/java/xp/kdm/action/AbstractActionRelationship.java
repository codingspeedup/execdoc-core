package xp.kdm.action;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;
import xp.kdm.kdm.KDMModel;

public abstract class AbstractActionRelationship<M extends KDMModel, F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<M, F, T> {

    public AbstractActionRelationship(M kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
