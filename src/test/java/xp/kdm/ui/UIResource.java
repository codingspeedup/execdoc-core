package xp.kdm.ui;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class UIResource extends AbstractUIElement {

    @Getter()
    @Setter()
    private Set<AbstractUIElement> uiElement;
}
