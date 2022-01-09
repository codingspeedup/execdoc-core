package xp.kdm.event;

public class NextState extends AbstractEventRelationship<Transition, State> {

    public NextState(EventModel kdmModel, Transition from, State to) {
        super(kdmModel, from, to);
    }

}
