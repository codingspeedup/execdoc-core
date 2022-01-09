package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class CharType extends PrimitiveType {

    @Getter()
    @Setter()
    private String charset;
}
