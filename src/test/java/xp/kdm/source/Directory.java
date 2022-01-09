package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Directory extends InventoryContainer {

    @Getter()
    @Setter()
    private String path;
}
