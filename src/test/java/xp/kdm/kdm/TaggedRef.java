package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtendableElement;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TaggedRef extends ExtendedValue {

    @Getter()
    @Setter()
    private ExtendableElement reference;
}
