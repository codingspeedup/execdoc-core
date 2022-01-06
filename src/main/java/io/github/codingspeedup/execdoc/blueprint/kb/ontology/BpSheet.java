package io.github.codingspeedup.execdoc.blueprint.kb.ontology;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.master.BlueprintMaster;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Sheet;

@NoArgsConstructor
@KbFunctor
public class BpSheet implements BpEntity {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor(value = KbNames.NAME_FUNCTOR, T1 = String.class, T2 = String.class)
    private Pair<String, String> name;

    public BpSheet(Sheet sheet) {
        this.kbId = KbNames.getAtom(sheet);
        String[] nameParts = sheet.getSheetName().split(BlueprintMaster.INSTANTIABLE_SHEET_MARKER, 2);
        if (nameParts.length == 1) {
            name = Pair.of(nameParts[0], null);
        } else {
            name = Pair.of(nameParts[0], nameParts[1]);
        }
    }

}
