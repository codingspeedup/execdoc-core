package xp.kdm.event;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;

public abstract class AbstractEventRelationship<F extends KDMEntity, T extends KDMEntity> extends KDMRelationship<EventModel, F, T> {

    public AbstractEventRelationship(EventModel kdmModel, F from, T to) {
        super(kdmModel, from, to);
    }

}
