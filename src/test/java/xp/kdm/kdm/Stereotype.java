package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtensionElement;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor()
public class Stereotype extends ExtensionElement {

    @Getter()
    @Setter()
    private ExtensionFamily owner;

    @Getter()
    @Setter()
    private Collection<TagDefinition> tag;

    @Getter()
    @Setter()
    private String name;

    @Getter()
    @Setter()
    private String type;
}
