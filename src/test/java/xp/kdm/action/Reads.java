package xp.kdm.action;

import xp.kdm.code.DataElement;
import xp.kdm.kdm.KDMModel;

public class Reads<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, DataElement> {

    public Reads(M kdmModel, ActionElement from, DataElement to) {
        super(kdmModel, from, to);
    }

}
