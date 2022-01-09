package xp.kdm.action;

import xp.kdm.code.ComputationalObject;
import xp.kdm.kdm.KDMModel;

public class Addresses<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ComputationalObject> {

    public Addresses(M kdmModel, ActionElement from, ComputationalObject to) {
        super(kdmModel, from, to);
    }

}
