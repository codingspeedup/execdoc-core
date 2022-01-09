package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;

@KbFunctor
public interface BpRelationship extends BpElement {

    String getFrom();

    void setFrom(String kbId);

    String getTo();

    void setTo(String kbId);

    default void setFrom(BpEntity entity) { setFrom(entity.getKbId()); }

    default void setTo(BpEntity entity) { setTo(entity.getKbId()); }

}
