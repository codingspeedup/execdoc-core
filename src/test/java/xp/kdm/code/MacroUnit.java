package xp.kdm.code;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class MacroUnit extends PreprocessorDirective {

    @Getter()
    @Setter()
    private MacroKind kind;
}
