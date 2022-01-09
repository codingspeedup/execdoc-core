package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class StorableUnit extends DataElement {

    @Getter()
    @Setter()
    private StorableKind kind;

    @Getter()
    @Setter()
    private Boolean isStatic;
}
