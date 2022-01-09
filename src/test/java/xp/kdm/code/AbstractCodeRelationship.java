package xp.kdm.code;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractCodeRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<CodeModel, F, T> {

    public AbstractCodeRelationship(CodeModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
