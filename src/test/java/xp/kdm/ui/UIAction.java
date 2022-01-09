package xp.kdm.ui;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class UIAction extends AbstractUIElement {

    @Getter()
    @Setter()
    private Set<UIEvent> uiElement;

    @Getter()
    @Setter()
    private String kind;
}
