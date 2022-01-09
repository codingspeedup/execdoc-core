package xp.kdm.platform;

import xp.kdm.action.AbstractActionRelationship;
import xp.kdm.action.ActionElement;
import xp.kdm.kdm.KDMModel;

public class ProducesPlatformEvent<M extends KDMModel> extends AbstractActionRelationship<M, ActionElement, PlatformEvent> {

    public ProducesPlatformEvent(M kdmModel, ActionElement from, PlatformEvent to) {
        super(kdmModel, from, to);
    }

}
