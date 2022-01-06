package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface BpEnumeratedType extends BpDatatype {

    List<? extends BpValue> getValue();

}
