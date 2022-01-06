package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;

@KbFunctor
public interface BpElement {

    String getKbId();

    void setKbId(String kbId);

    default void kbStore(BpKb bpKb) {
    }

    default void kbRetrieve(BpKb bpKb) {
    }

}
