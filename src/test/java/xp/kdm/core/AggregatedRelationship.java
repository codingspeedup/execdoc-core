package xp.kdm.core;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Collection;

@NoArgsConstructor()
public class AggregatedRelationship extends ModelElement {

    @Getter()
    @Setter()
    private KDMEntity from;

    @Getter()
    @Setter()
    private KDMEntity to;

    @Getter()
    @Setter()
    private Collection<KDMRelationship> relation;

    @Getter()
    @Setter()
    private Integer density;
}
