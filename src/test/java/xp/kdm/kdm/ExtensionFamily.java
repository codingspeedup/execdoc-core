package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtensionElement;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@NoArgsConstructor()
public class ExtensionFamily extends ExtensionElement {

    @Getter()
    @Setter()
    private FrameworkElement owner;

    @Getter()
    @Setter()
    private Collection<Stereotype> stereotype;

    @Getter()
    @Setter()
    private String name;
}
