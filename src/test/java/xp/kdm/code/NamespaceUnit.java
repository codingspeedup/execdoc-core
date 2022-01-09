package xp.kdm.code;

import lombok.NoArgsConstructor;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class NamespaceUnit extends CodeItem {

    @Getter()
    @Setter()
    private Set<CodeItem> groupedCode;
}
