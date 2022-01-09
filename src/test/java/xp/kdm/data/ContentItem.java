package xp.kdm.data;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@NoArgsConstructor()
public class ContentItem extends AbstractContentElement {

    @Getter()
    @Setter()
    private ComplexContentType type;

    @Getter()
    @Setter()
    private Set<AbstractContentElement> contentElement;
}
