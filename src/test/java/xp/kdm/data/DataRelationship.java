package xp.kdm.data;

import xp.kdm.core.KDMEntity;

public class DataRelationship extends AbstractDataRelationship<AbstractDataElement, KDMEntity> {

    public DataRelationship(DataModel kdmModel, AbstractDataElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
