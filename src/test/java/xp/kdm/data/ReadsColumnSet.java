package xp.kdm.data;

import xp.kdm.kdm.KDMModel;
import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;

public class ReadsColumnSet<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, ColumnSet> {

    public ReadsColumnSet(M kdmModel, ActionElement from, ColumnSet to) {
        super(kdmModel, from, to);
    }

}
