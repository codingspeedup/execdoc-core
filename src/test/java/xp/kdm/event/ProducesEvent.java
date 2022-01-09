package xp.kdm.event;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class ProducesEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, Event> {

    public ProducesEvent(M kdmModel, ActionElement from, Event to) {
        super(kdmModel, from, to);
    }

}
