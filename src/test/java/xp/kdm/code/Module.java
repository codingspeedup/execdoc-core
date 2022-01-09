package xp.kdm.code;

import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

@NoArgsConstructor()
public class Module extends CodeItem {

    private Map<String, AbstractCodeElement> codeElement;

    public Map<String, AbstractCodeElement> getCodeElement() {
        if (codeElement == null) {
            codeElement = new LinkedHashMap<>();
        }
        return codeElement;
    }

}
