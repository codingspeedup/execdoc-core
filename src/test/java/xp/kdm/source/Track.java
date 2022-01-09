package xp.kdm.source;

import lombok.NoArgsConstructor;
import xp.kdm.core.KDMEntity;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Track extends AbstractInventoryElement {

    @Getter()
    @Setter()
    private KDMEntity owner;

    @Getter()
    @Setter()
    private String description;
}
