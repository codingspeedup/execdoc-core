package io.github.codingspeedup.execdoc.reporters.codexray.calldiagram;

import io.github.codingspeedup.execdoc.reporters.codexray.Diagram;
import org.apache.commons.lang3.StringUtils;
import org.jgrapht.graph.DirectedPseudograph;

import java.util.Set;

public class CallDiagram extends Diagram<CallVertex, CallEdge> {

    public CallDiagram() {
        super(new DirectedPseudograph<>(CallEdge.class));
    }

    public boolean addEdge(CallVertex s, CallVertex t) {
        if (getCoreGraph().getAllEdges(s, t).isEmpty()) {
            getCoreGraph().addEdge(s, t, new CallEdge());
            return true;
        }
        return false;
    }

    private void render(StringBuilder dot, CallVertex v) {
        dot.append(v.getVertexId()).append(" [");
        dot.append("label=\"{ ");
        dot.append(v.getMethodName());
        if (v.getMethodSignature() != null) {
            dot.append(" (").append(v.getMethodSignature()).append(")");
        }
        dot.append(" | ").append(v.getTypeSimpleName()).append(" }\"");
        if (v.isFlag(CallVertex.FLAG_START)) {
            dot.append(" color=\"").append("pink").append("\"");
            dot.append(" fillcolor=\"").append("red").append("\"");
            dot.append(" fontcolor=\"").append("white").append("\"");
            dot.append(" style=\"").append("filled,rounded").append("\"");
        }
        if (StringUtils.isNotBlank(v.getUrl())) {
            dot.append(" URL=\"").append(v.getUrl()).append("\"");
        }
        dot.append("]\n");
    }

    private void render(StringBuilder dot, CallEdge e) {
        CallVertex s = this.getCoreGraph().getEdgeSource(e);
        CallVertex t = this.getCoreGraph().getEdgeTarget(e);
        dot.append(s.getVertexId()).append(" -> ").append(t.getVertexId()).append(" [color=\"indigo\"]\n");
    }

    @Override
    public String toPlantUmlScript(Set<CallVertex> vertexSet) {
        StringBuilder dot = new StringBuilder();
        dot.append("@startuml\ndigraph call_graph {\n");
        dot.append("node [style=rounded shape=record]\n");
        vertexSet.forEach(v -> render(dot, v));
        vertexSet.forEach(v -> this.getCoreGraph().outgoingEdgesOf(v).stream()
                .filter(e -> vertexSet.contains(this.getCoreGraph().getEdgeTarget(e)))
                .forEach(e -> render(dot, e)));
        dot.append("}\n@enduml\n");
        return dot.toString();
    }

}
