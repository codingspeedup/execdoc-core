package xp.kdm.build;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class BuildModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractBuildElement> buildElement;
}
