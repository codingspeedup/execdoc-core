package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtensionElement;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class ExtendedValue extends ExtensionElement {

    @Getter()
    @Setter()
    private TagDefinition tag;
}
