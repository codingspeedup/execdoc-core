package io.github.codingspeedup.execdoc.toolbox.workflow;

public interface Handler<S extends SharedState, I, O> {

    default S getSharedState() {
        return null;
    }

    O process(I input);

    default void setSharedState(S state) {
    }

}
