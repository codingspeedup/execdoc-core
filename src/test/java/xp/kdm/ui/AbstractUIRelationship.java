package xp.kdm.ui;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractUIRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<UIModel, F, T> {

    public AbstractUIRelationship(UIModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
