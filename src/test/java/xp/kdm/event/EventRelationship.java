package xp.kdm.event;

import xp.kdm.core.KDMEntity;

public class EventRelationship extends AbstractEventRelationship<AbstractEventElement, KDMEntity> {

    public EventRelationship(EventModel kdmModel, AbstractEventElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
