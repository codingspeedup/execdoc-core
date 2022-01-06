package io.github.codingspeedup.execdoc.toolbox.workflow;

import lombok.Getter;
import lombok.Setter;

public class WorkflowException extends RuntimeException {

    @Getter
    private final Handler<?, ?, ?> handler;

    @Getter
    @Setter
    private Object result;

    public WorkflowException(Handler<?, ?, ?> handler) {
        this(handler, null, null);
    }

    public WorkflowException(Handler<?, ?, ?> handler, String message) {
        this(handler, message, null);
    }

    public WorkflowException(Handler<?, ?, ?> handler, Throwable cause) {
        this(handler, null, cause);
    }

    public WorkflowException(Handler<?, ?, ?> handler, String message, Throwable cause) {
        super(message, cause);
        this.handler = handler;
    }

    @Override
    public String getMessage() {
        return ">> " + handler.getClass().getSimpleName() + " >> " + super.getMessage();
    }

}
