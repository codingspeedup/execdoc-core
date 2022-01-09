package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class TemplateUnit extends TemplateElement {

    @Getter()
    @Setter()
    private List<AbstractCodeElement> codeElement;
}
