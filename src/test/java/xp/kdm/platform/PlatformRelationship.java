package xp.kdm.platform;

import xp.kdm.core.KDMEntity;

public class PlatformRelationship extends AbstractPlatformRelationship<AbstractPlatformElement, KDMEntity> {

    public PlatformRelationship(PlatformModel kdmModel, AbstractPlatformElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
