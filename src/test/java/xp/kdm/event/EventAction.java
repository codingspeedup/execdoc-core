package xp.kdm.event;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class EventAction extends AbstractEventElement {

    @Getter()
    @Setter()
    private Set<Event> eventElement;

    @Getter()
    @Setter()
    private String kind;
}
