package xp.kdm.data;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class HasContent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, AbstractDataElement> {

    public HasContent(M kdmModel, ActionElement from, AbstractDataElement to) {
        super(kdmModel, from, to);
    }

}
