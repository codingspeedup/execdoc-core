package io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.code;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;

import java.util.List;

@KbFunctor
public interface BpClassUnit extends BpDatatype {

    default Boolean getAbstract() {
        throw new UnsupportedOperationException();
    }

    default void setAbstract(Boolean value) {
        throw new UnsupportedOperationException();
    }

    default Boolean getFinal() {
        throw new UnsupportedOperationException();
    }

    default void setFinal(Boolean value) {
        throw new UnsupportedOperationException();
    }

    default ExportKind getExportKind() {
        throw new UnsupportedOperationException();
    }

    default void setExportKind(ExportKind value) {
        throw new UnsupportedOperationException();
    }

    default List<? extends BpAbstractCodeElement> getCodeElement() {
        throw new UnsupportedOperationException();
    }

}
