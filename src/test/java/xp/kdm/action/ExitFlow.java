package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class ExitFlow<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ActionElement> {

    public ExitFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
