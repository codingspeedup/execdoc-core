package xp.kdm.structure;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class StructureModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractStructureElement> structureElement;
}
