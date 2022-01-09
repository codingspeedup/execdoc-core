package xp.kdm.action;

import xp.kdm.code.Datatype;
import xp.kdm.kdm.KDMModel;

public class UsesType<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, Datatype> {

    public UsesType(M kdmModel, ActionElement from, Datatype to) {
        super(kdmModel, from, to);
    }

}
