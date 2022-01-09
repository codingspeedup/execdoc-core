package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class BinaryRegion extends Region {

    @Getter()
    @Setter()
    private Integer startAddr;

    @Getter()
    @Setter()
    private Integer endAddr;
}
