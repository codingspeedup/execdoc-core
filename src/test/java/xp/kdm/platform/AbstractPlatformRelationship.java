package xp.kdm.platform;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractPlatformRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<PlatformModel, F, T> {

    public AbstractPlatformRelationship(PlatformModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
