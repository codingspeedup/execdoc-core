package io.github.codingspeedup.execdoc.toolbox.workflow;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.graph.DirectedAcyclicGraph;

public class Workflow<S extends SharedState, I, O> implements Handler<S, I, O> {

    private static final String DEFAULT_EDGE = "";

    @Getter
    private final S sharedState;
    private final Handler<S, Object, Object> start;
    private final DirectedAcyclicGraph<Handler<S, Object, Object>, String> handlerGraph;

    @SuppressWarnings({"unchecked"})
    public Workflow(S sharedState, Handler<S, I, ?> start) {
        this.sharedState = sharedState;
        this.start = (Handler<S, Object, Object>) start;
        start.setSharedState(sharedState);
        handlerGraph = new DirectedAcyclicGraph<>(String.class);
        handlerGraph.addVertex(this.start);
    }

    public <K> void connect(Handler<S, I, K> from, Handler<S, K, ?> to) {
        this.connect(from, to, null);
    }

    @SuppressWarnings({"unchecked"})
    public <K> void connect(Handler<S, I, K> from, Handler<S, K, ?> to, Class<? extends WorkflowException> event) {
        from.setSharedState(this.sharedState);
        to.setSharedState(this.sharedState);
        Handler<S, Object, Object> fromVertex = (Handler<S, Object, Object>) from;
        Handler<S, Object, Object> toVertex = (Handler<S, Object, Object>) to;
        handlerGraph.addVertex(fromVertex);
        handlerGraph.addVertex(toVertex);
        String eventName = handlerGraph.getEdge(fromVertex, toVertex);
        if (StringUtils.isNotBlank(eventName)) {
            throw new UnsupportedOperationException("Connection '" + eventName + "' already exists between " + from.getClass().getName() + " and " + to.getClass().getName());
        }
        eventName = event == null ? DEFAULT_EDGE : event.getName();
        handlerGraph.addEdge(fromVertex, toVertex, eventName);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public O process(I input) {
        return (O) execute(input);
    }

    public Object execute(I input) {
        Handler<S, Object, Object> currentHandler = start;
        Object lastResult = input;
        while (currentHandler != null) {
            String nextEdge;
            try {
                lastResult = currentHandler.process(lastResult);
                nextEdge = DEFAULT_EDGE;
            } catch (WorkflowException event) {
                lastResult = event.getResult();
                nextEdge = event.getClass().getName();
            }
            Handler<S, Object, Object> nextHandler = null;
            for (Handler<S, Object, Object> handler : handlerGraph.getDescendants(currentHandler)) {
                String edge = handlerGraph.getEdge(currentHandler, handler);
                if (DEFAULT_EDGE.equals(edge)) {
                    nextHandler = handler;
                }
                if (nextEdge.equals(edge)) {
                    nextHandler = handler;
                    break;
                }
            }
            currentHandler = nextHandler;
        }
        return lastResult;
    }

}
