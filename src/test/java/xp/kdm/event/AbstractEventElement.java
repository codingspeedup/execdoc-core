package xp.kdm.event;

import lombok.NoArgsConstructor;
import xp.kdm.code.AbstractCodeElement;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import xp.kdm.action.ActionElement;
import java.util.List;

@NoArgsConstructor()
public abstract class AbstractEventElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractEventRelationship> eventRelation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> implementation;
}
