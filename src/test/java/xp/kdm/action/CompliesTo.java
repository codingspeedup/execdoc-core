package xp.kdm.action;

import xp.kdm.code.CodeItem;
import xp.kdm.kdm.KDMModel;

public class CompliesTo<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, CodeItem> {

    public CompliesTo(M kdmModel, ActionElement from, CodeItem to) {
        super(kdmModel, from, to);
    }

}
