package xp.kdm.ui;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class UIEvent extends AbstractUIElement {

    @Getter()
    @Setter()
    private String kind;
}
