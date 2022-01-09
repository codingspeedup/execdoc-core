package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class RangeType extends DerivedType {

    @Getter()
    @Setter()
    private Value lower;

    @Getter()
    @Setter()
    private Value upper;
}
