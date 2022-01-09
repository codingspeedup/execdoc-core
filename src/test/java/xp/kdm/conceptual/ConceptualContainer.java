package xp.kdm.conceptual;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ConceptualContainer extends AbstractConceptualElement {

    @Getter()
    @Setter()
    private Set<AbstractConceptualElement> conceptualElement;
}
