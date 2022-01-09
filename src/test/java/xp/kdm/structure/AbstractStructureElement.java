package xp.kdm.structure;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class AbstractStructureElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<KDMEntity> implementation;

    @Getter()
    @Setter()
    private Set<AbstractStructureElement> structureElement;

    @Getter()
    @Setter()
    private Set<AbstractStructureRelationship> structureRelationship;
}
