package xp.kdm.platform;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import xp.kdm.action.ActionElement;
import java.util.List;
import xp.kdm.code.AbstractCodeElement;

@NoArgsConstructor()
public abstract class AbstractPlatformElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractPlatformRelationship> relation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> implementation;
}
