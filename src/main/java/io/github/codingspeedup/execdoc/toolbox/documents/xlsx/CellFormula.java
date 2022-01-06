package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CellFormula {

    private String source;

    public CellFormula(String source) {
        this.source = source;
    }

}
