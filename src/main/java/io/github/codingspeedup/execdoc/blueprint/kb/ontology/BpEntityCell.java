package io.github.codingspeedup.execdoc.blueprint.kb.ontology;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class BpEntityCell extends BpElementCell implements BpEntity {

    public BpEntityCell(Cell cell) {
        super(cell);
    }

}
