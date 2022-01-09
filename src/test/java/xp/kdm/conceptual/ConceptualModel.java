package xp.kdm.conceptual;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ConceptualModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractConceptualElement> conceptualElement;
}
