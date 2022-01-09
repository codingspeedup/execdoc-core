package xp.kdm.source;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class SourceRegion extends Region {

    @Getter()
    @Setter()
    private Integer startLine;

    @Getter()
    @Setter()
    private Integer startPosition;

    @Getter()
    @Setter()
    private Integer endLine;

    @Getter()
    @Setter()
    private Integer endPosition;

    @Getter()
    @Setter()
    private String language;
}
