package io.github.codingspeedup.execdoc.toolbox.workflow;

import lombok.Getter;

public class Pipeline<S extends SharedState, I, O> implements Handler<S, I, O> {

    @Getter
    private final S sharedState;
    private final Handler<S, I, O> currentHandler;

    public Pipeline(S sharedState, Handler<S, I, O> currentHandler) {
        this.sharedState = sharedState;
        currentHandler.setSharedState(this.sharedState);
        this.currentHandler = currentHandler;
    }

    public <K> Pipeline<S, I, K> extend(Handler<S, O, K> newHandler) {
        newHandler.setSharedState(this.sharedState);
        return new Pipeline<>(sharedState, input -> newHandler.process(currentHandler.process(input)));
    }

    public O process(I input) {
        return currentHandler.process(input);
    }

}
