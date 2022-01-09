package xp.kdm.data;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class ProducesDataEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, DataEvent> {

    public ProducesDataEvent(M kdmModel, ActionElement from, DataEvent to) {
        super(kdmModel, from, to);
    }

}
