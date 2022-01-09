package xp.kdm.platform;

import xp.kdm.core.KDMEntity;

public class BindsTo extends AbstractPlatformRelationship<PlatformResource, KDMEntity> {

    public BindsTo(PlatformModel kdmModel, PlatformResource from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
