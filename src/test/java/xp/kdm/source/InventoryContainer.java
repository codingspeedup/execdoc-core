package xp.kdm.source;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class InventoryContainer extends AbstractInventoryElement {

    @Getter()
    @Setter()
    private Set<AbstractInventoryElement> inventoryElement;
}
