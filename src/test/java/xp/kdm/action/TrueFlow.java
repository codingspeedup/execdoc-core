package xp.kdm.action;

import xp.kdm.kdm.KDMModel;

public class TrueFlow<M extends KDMModel> extends ControlFlow<M> {

    public TrueFlow(M kdmModel, ActionElement from, ActionElement to) {
        super(kdmModel, from, to);
    }

}
