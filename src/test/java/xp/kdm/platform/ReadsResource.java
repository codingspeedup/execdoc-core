package xp.kdm.platform;

import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;
import xp.kdm.kdm.KDMModel;

public class ReadsResource<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, PlatformResource> {

    public ReadsResource(M kdmModel, ActionElement from, PlatformResource to) {
        super(kdmModel, from, to);
    }

}
