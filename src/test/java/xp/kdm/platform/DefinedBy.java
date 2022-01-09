package xp.kdm.platform;

import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;
import xp.kdm.code.CodeItem;
import xp.kdm.kdm.KDMModel;

public class DefinedBy<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, CodeItem> {

    public DefinedBy(M kdmModel, ActionElement from, CodeItem to) {
        super(kdmModel, from, to);
    }

}
