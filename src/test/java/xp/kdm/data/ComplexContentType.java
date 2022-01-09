package xp.kdm.data;

import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ComplexContentType extends AbstractContentElement {

    @Getter()
    @Setter()
    private List<AbstractContentElement> contentElement;
}
