package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class FalseFlow<M extends KDMModel> extends ControlFlow<M> {

    public FalseFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
