package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ValueList extends ValueElement {

    @Getter()
    @Setter()
    private List<ValueElement> valueElement;
}
