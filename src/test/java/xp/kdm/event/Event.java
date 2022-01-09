package xp.kdm.event;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Event extends AbstractEventElement {

    @Getter()
    @Setter()
    private String kind;
}
