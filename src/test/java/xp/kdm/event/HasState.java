package xp.kdm.event;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class HasState<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, AbstractEventElement> {

    public HasState(M kdmModel, ActionElement from, AbstractEventElement to) {
        super(kdmModel, from, to);
    }

}
