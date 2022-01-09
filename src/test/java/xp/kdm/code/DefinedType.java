package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@NoArgsConstructor()
public class DefinedType extends Datatype {

    @Getter()
    @Setter()
    private Datatype type;

    @Getter()
    @Setter()
    private Set<Datatype> codeElement;
}
