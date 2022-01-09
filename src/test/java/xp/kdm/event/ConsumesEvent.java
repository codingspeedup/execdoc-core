package xp.kdm.event;

public class ConsumesEvent extends AbstractEventRelationship<Transition, Event> {

    public ConsumesEvent(EventModel kdmModel, Transition from, Event to) {
        super(kdmModel, from, to);
    }

}
