package xp.kdm.ui;

import xp.kdm.action.ActionElement;
import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;

public class ReadsUI<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, UIResource> {

    public ReadsUI(M kdmModel, ActionElement from, UIResource to) {
        super(kdmModel, from, to);
    }

}
