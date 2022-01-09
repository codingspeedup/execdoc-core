package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class SourceFile extends InventoryItem {

    @Getter()
    @Setter()
    private String language;

    @Getter()
    @Setter()
    private String encoding;
}
