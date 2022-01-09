package xp.kdm.data;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ContentRestriction extends AbstractContentElement {

    @Getter()
    @Setter()
    private String kind;

    @Getter()
    @Setter()
    private String value;
}
