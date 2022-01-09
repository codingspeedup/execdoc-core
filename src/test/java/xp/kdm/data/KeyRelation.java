package xp.kdm.data;

public class KeyRelation extends AbstractDataRelationship<ReferenceKey, UniqueKey> {

    public KeyRelation(DataModel kdmModel, ReferenceKey from, UniqueKey to) {
        super(kdmModel, from, to);
    }

}
