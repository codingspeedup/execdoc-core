package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class InventoryItem extends AbstractInventoryElement {

    @Getter()
    @Setter()
    private String version;

    @Getter()
    @Setter()
    private String path;

    @Getter()
    @Setter()
    private String format;

    @Getter()
    @Setter()
    private String md5;
}
