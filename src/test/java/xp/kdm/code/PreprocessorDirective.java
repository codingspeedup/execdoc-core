package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class PreprocessorDirective extends AbstractCodeElement {

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> codeElement;
}
