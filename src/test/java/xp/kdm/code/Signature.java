package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Signature extends Datatype {

    @Getter()
    @Setter()
    private List<ParameterUnit> parameterUnit;
}
