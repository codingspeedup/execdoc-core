package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtensionElement;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TagDefinition extends ExtensionElement {

    @Getter()
    @Setter()
    private Stereotype owner;

    @Getter()
    @Setter()
    private String tag;

    @Getter()
    @Setter()
    private String type;
}
