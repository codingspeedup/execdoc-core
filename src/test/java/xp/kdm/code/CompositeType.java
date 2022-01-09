package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class CompositeType extends Datatype {

    @Getter()
    @Setter()
    private List<ItemUnit> itemUnit;
}
