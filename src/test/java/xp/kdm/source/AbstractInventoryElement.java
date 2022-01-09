package xp.kdm.source;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public abstract class AbstractInventoryElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractInventoryRelationship> inventoryRelation;
}
