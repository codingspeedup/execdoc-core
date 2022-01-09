package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ArrayType extends DerivedType {

    @Getter()
    @Setter()
    private IndexUnit indexUnit;

    @Getter()
    @Setter()
    private Integer size;
}
