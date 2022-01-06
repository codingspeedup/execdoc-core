package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsNamed;

@KbFunctor
public interface BpDataElement extends BpComputationalObject, IsNamed {

    default BpDatatype getType() {
        throw new UnsupportedOperationException();
    }

    default void setType(BpDatatype type) {
        throw new UnsupportedOperationException();
    }

    default Integer getSize() {
        throw new UnsupportedOperationException();
    }

    default void setSize(Integer value) {
        throw new UnsupportedOperationException();
    }

    default String getExt() {
        throw new UnsupportedOperationException();
    }

    default void setExt(String ext) {
        throw new UnsupportedOperationException();
    }

}
