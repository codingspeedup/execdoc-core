package xp.kdm.code;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.ExportKind;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor()
public class ClassUnit extends Datatype {

    private Map<String, AbstractCodeElement> codeElement;

    @Getter()
    @Setter()
    private Boolean isAbstract;

    @Getter()
    @Setter()
    private Boolean isFinal;

    @Getter()
    @Setter()
    private ExportKind exportKind;

    public Map<String, AbstractCodeElement> getCodeElement() {
        if (codeElement == null) {
            codeElement = new LinkedHashMap<>();
        }
        return codeElement;
    }

}
