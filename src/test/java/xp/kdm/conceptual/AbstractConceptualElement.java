package xp.kdm.conceptual;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import xp.kdm.action.ActionElement;

@NoArgsConstructor()
public abstract class AbstractConceptualElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<KDMEntity> implementation;

    @Getter()
    @Setter()
    private Set<AbstractConceptualRelationship> conceptualRelation;

    @Getter()
    @Setter()
    private Set<ActionElement> abstraction;
}
