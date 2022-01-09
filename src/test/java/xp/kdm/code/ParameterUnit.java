package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ParameterUnit extends DataElement {

    @Getter()
    @Setter()
    private ParameterKind kind;

    @Getter()
    @Setter()
    private Integer pos;

    @Getter()
    @Setter()
    private Boolean isFinal;
}
