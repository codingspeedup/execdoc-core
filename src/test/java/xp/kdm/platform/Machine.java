package xp.kdm.platform;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Machine extends DeploymentElement {

    @Getter()
    @Setter()
    private Set<DeployedComponent> deployedComponent;

    @Getter()
    @Setter()
    private Set<DeployedResource> deployedResource;
}
