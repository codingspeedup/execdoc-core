package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ReferenceableRegion extends Region {

    @Getter()
    @Setter()
    private String ref;
}
