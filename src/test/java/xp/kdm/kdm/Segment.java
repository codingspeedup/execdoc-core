package xp.kdm.kdm;

import lombok.NoArgsConstructor;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class Segment extends FrameworkElement {

    @Getter()
    @Setter()
    private Collection<Segment> segment;

    @Getter()
    @Setter()
    private Collection<KDMModel> model;
}
