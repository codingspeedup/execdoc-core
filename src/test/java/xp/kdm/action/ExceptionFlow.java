package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class ExceptionFlow<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ActionElement> {

    public ExceptionFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
