package xp.kdm.source;

import lombok.NoArgsConstructor;
import xp.kdm.core.AnnotatableElement;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class SourceRef extends AnnotatableElement {

    @Getter()
    @Setter()
    private Collection<Region> region;

    @Getter()
    @Setter()
    private String language;

    @Getter()
    @Setter()
    private String snippet;
}
