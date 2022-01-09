package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@NoArgsConstructor()
public class DataElement extends ComputationalObject {

    @Getter()
    @Setter()
    private Datatype type;

    @Getter()
    @Setter()
    private Set<Datatype> codeElement;

    @Getter()
    @Setter()
    private String ext;

    @Getter()
    @Setter()
    private Integer size;
}
