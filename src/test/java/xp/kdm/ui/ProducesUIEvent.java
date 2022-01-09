package xp.kdm.ui;

import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;
import xp.kdm.kdm.KDMModel;

public class ProducesUIEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIEvent> {

    public ProducesUIEvent(M kdmModel, ActionElement from, UIEvent to) {
        super(kdmModel, from, to);
    }

}
