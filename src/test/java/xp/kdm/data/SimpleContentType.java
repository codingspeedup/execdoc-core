package xp.kdm.data;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class SimpleContentType extends ComplexContentType {

    @Getter()
    @Setter()
    private ComplexContentType type;

    @Getter()
    @Setter()
    private String kind;
}
