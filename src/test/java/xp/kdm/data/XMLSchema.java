package xp.kdm.data;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class XMLSchema extends AbstractDataElement {

    @Getter()
    @Setter()
    private Set<AbstractContentElement> contentElement;
}
