package xp.kdm.data;

import lombok.NoArgsConstructor;
import xp.kdm.action.ActionElement;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DataAction extends AbstractDataElement {

    @Getter()
    @Setter()
    private Set<ActionElement> implementation;

    @Getter()
    @Setter()
    private Set<DataEvent> dataElement;

    @Getter()
    @Setter()
    private String kind;
}
