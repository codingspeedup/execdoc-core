package xp.kdm.platform;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PlatformModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractPlatformElement> platformElement;
}
