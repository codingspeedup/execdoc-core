package xp.kdm.action;

import xp.kdm.code.CodeItem;
import xp.kdm.kdm.KDMModel;

public class Calls<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, CodeItem> {

    public Calls(M kdmModel, ActionElement from, CodeItem to) {
        super(kdmModel, from, to);
    }

}
