package xp.kdm.event;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class EventResource extends AbstractEventElement {

    @Getter()
    @Setter()
    private Set<AbstractEventElement> eventElement;
}
