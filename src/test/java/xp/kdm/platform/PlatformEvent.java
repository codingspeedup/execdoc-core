package xp.kdm.platform;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PlatformEvent extends AbstractPlatformElement {

    @Getter()
    @Setter()
    private String kind;
}
