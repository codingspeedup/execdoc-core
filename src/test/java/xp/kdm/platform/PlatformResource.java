package xp.kdm.platform;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PlatformResource extends AbstractPlatformElement {

    @Getter()
    @Setter()
    private Set<AbstractPlatformElement> platformElement;
}
