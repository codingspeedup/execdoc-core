package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class ControlFlow<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ActionElement> {

    public ControlFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
