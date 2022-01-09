package xp.kdm.code;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.ExportKind;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.MethodKind;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class MethodUnit extends ControlElement {

    @Getter()
    @Setter()
    private MethodKind kind;

    @Getter()
    @Setter()
    private ExportKind export;

    @Getter()
    @Setter()
    private Boolean isFinal;

    @Getter()
    @Setter()
    private Boolean isStatic;

    @Getter()
    @Setter()
    private Boolean isVirtual;

    @Getter()
    @Setter()
    private Boolean isAbstract;

}
