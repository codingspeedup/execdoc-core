package xp.kdm.build;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractBuildRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<BuildModel, F, T> {

    public AbstractBuildRelationship(BuildModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
