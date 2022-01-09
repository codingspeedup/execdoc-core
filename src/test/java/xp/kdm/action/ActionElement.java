package xp.kdm.action;

import lombok.NoArgsConstructor;
import xp.kdm.code.AbstractCodeElement;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class ActionElement extends AbstractCodeElement {

    @Getter()
    @Setter()
    private List<AbstractActionRelationship> actionRelation;

    @Getter()
    @Setter()
    private List<AbstractCodeElement> codeElement;

    @Getter()
    @Setter()
    private String kind;
}
