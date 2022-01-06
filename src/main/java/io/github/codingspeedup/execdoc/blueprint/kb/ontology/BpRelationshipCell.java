package io.github.codingspeedup.execdoc.blueprint.kb.ontology;

import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

@NoArgsConstructor
@KbFunctor
public abstract class BpRelationshipCell extends BpElementCell implements BpRelationship {

    @Getter
    @Setter
    private String from;

    @Getter
    @Setter
    private String to;

    public BpRelationshipCell(Cell cell) {
        this(cell, null, (String) null);
    }

    public BpRelationshipCell(Cell cell, Cell from, Cell to) {
        this(cell, KbNames.getAtom(from), KbNames.getAtom(to));
    }

    public BpRelationshipCell(Cell cell, String from, String to) {
        super(cell);
        this.from = from;
        this.to = to;
    }

}
