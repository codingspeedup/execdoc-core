package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TaggedValue extends ExtendedValue {

    @Getter()
    @Setter()
    private String value;
}
