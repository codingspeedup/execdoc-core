package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsNamed;

@KbFunctor
public interface BpMethodUnit extends BpControlElement, IsNamed {

    default MethodKind getKind() {
        throw new UnsupportedOperationException();
    }

    default void setKind(MethodKind value) {
        throw new UnsupportedOperationException();
    }

    default ExportKind getExport() {
        throw new UnsupportedOperationException();
    }

    default void setExport(ExportKind value) {
        throw new UnsupportedOperationException();
    }

    default Boolean getFinal() {
        throw new UnsupportedOperationException();
    }

    default void setFinal(Boolean value) {
        throw new UnsupportedOperationException();
    }

    default Boolean getStatic() {
        throw new UnsupportedOperationException();
    }

    default void setStatic(Boolean value) {
        throw new UnsupportedOperationException();
    }

    default Boolean getVirtual() {
        throw new UnsupportedOperationException();
    }

    default void setVirtual(Boolean value) {
        throw new UnsupportedOperationException();
    }

    default Boolean getAbstract() {
        throw new UnsupportedOperationException();
    }

    default void setAbstract(Boolean value) {
        throw new UnsupportedOperationException();
    }

}
