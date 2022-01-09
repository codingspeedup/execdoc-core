package xp.kdm.platform;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DeployedResource extends DeploymentElement {

    @Getter()
    @Setter()
    private Set<PlatformResource> platformElement;
}
