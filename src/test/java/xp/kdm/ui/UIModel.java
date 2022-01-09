package xp.kdm.ui;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class UIModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractUIElement> uiElement;
}
