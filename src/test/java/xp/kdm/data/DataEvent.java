package xp.kdm.data;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class DataEvent extends DataResource {

    @Getter()
    @Setter()
    private String kind;
}
