package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class CallableUnit extends ControlElement {

    @Getter()
    @Setter()
    private CallableKind kind;

    @Getter()
    @Setter()
    private Boolean isStatic;
}
