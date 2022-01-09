package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class Flow<M extends KDMModel> extends ControlFlow<M> {

    public Flow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
