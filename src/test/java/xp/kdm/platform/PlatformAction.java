package xp.kdm.platform;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PlatformAction extends AbstractPlatformElement {

    @Getter()
    @Setter()
    private Set<PlatformEvent> platformElement;

    @Getter()
    @Setter()
    private String kind;
}
