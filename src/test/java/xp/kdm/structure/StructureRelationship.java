package xp.kdm.structure;

import xp.kdm.core.KDMEntity;

public class StructureRelationship extends AbstractStructureRelationship<AbstractStructureElement , KDMEntity> {

    public StructureRelationship(StructureModel kdmModel, AbstractStructureElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
