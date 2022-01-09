package xp.kdm.code;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor()
public class EnumeratedType extends Datatype {

    private List<Value> value;

    public List<Value> getValue() {
        if (value == null) {
            value = new ArrayList<>();
        }
        return value;
    }

}
