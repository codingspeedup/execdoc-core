package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import xp.kdm.core.ExtendableElement;
import xp.kdm.core.ModelElement;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Audit extends ExtendableElement {

    @Getter()
    @Setter()
    private ModelElement owner;

    @Getter()
    @Setter()
    private String description;

    @Getter()
    @Setter()
    private String author;

    @Getter()
    @Setter()
    private String date;
}
