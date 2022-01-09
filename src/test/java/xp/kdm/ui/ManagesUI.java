package xp.kdm.ui;

import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;
import xp.kdm.kdm.KDMModel;

public class ManagesUI<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIResource> {

    public ManagesUI(M kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
