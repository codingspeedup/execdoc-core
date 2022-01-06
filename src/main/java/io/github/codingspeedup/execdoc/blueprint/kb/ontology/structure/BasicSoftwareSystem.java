package io.github.codingspeedup.execdoc.blueprint.kb.ontology.structure;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.structure.BpSoftwareSystem;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@KbFunctor
public class BasicSoftwareSystem implements BpSoftwareSystem {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    public BasicSoftwareSystem(BlueprintMaster master) {
        setKbId(master.getFile().getParentFile().getName());
    }

}
