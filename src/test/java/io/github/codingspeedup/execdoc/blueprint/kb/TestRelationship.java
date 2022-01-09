package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import lombok.Getter;
import lombok.Setter;

@KbFunctor
public class TestRelationship implements BpRelationship {

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    private String from;

    @Getter
    @Setter
    private String to;

    @Getter
    @Setter
    @KbFunctor
    private String description;

}
