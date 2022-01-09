package xp.kdm.build;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class AbstractBuildElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractBuildRelationship> buildRelation;
}
