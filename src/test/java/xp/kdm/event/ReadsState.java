package xp.kdm.event;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class ReadsState<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, State> {

    public ReadsState(M kdmModel, ActionElement from, State to) {
        super(kdmModel, from, to);
    }

}
