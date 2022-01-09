package xp.kdm.data;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DataContainer extends DataResource {

    @Getter()
    @Setter()
    private Set<DataResource> dataElement;
}
