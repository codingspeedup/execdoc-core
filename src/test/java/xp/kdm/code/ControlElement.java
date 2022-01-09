package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor()
public class ControlElement extends ComputationalObject {

    @Getter()
    @Setter()
    private Datatype type;

    @Getter()
    @Setter()
    private List<AbstractCodeElement> codeElement;
}
