package xp.kdm.data;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import xp.kdm.action.ActionElement;

import java.util.List;

@NoArgsConstructor()
public abstract class AbstractDataElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractDataRelationship> dataRelation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;
}
