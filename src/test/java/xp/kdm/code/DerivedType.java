package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DerivedType extends Datatype {

    @Getter()
    @Setter()
    private ItemUnit itemUnit;
}
