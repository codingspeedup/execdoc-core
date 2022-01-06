package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.data;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code.BpItemUnit;

import java.util.List;

@KbFunctor
public interface BpColumnSet extends BpDataContainer {

    List<? extends BpItemUnit> getItemUnit();

}
