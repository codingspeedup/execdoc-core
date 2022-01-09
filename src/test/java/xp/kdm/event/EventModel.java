package xp.kdm.event;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class EventModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractEventElement> eventElement;
}
