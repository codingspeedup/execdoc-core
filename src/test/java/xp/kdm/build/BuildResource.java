package xp.kdm.build;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class BuildResource extends AbstractBuildElement {

    @Getter()
    @Setter()
    private Set<KDMEntity> implementation;

    @Getter()
    @Setter()
    private Set<AbstractBuildElement> groupedBuild;

    @Getter()
    @Setter()
    private Set<AbstractBuildElement> buildElement;
}
