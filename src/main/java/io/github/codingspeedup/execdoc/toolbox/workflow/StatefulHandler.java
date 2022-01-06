package io.github.codingspeedup.execdoc.toolbox.workflow;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public abstract class StatefulHandler<S extends SharedState, I, O> implements Handler<S, I, O> {

    @Getter
    @Setter
    private S sharedState;

    public StatefulHandler(S sharedState) {
        this.sharedState = sharedState;
    }

}
