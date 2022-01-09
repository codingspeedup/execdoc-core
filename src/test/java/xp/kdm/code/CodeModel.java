package xp.kdm.code;

import lombok.NoArgsConstructor;
import xp.kdm.kdm.KDMModel;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class CodeModel extends KDMModel {

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> codeElement;
}
