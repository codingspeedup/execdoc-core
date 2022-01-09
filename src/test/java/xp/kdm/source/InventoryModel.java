package xp.kdm.source;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class InventoryModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractInventoryElement> inventoryElement;
}
