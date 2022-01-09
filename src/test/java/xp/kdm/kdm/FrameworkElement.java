package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ModelElement;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class FrameworkElement extends ModelElement {

    @Getter()
    @Setter()
    private Collection<ExtensionFamily> extensionFamily;

    @Getter()
    @Setter()
    private String name;
}
