package xp.kdm.conceptual;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ConceptualRole extends AbstractConceptualElement {

    @Getter()
    @Setter()
    private AbstractConceptualElement conceptualElement;
}
