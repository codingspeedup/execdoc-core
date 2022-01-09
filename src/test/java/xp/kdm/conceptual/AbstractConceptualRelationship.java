package xp.kdm.conceptual;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractConceptualRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<ConceptualModel, F, T> {

    public AbstractConceptualRelationship(ConceptualModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
