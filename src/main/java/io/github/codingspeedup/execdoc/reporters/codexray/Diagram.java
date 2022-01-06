package io.github.codingspeedup.execdoc.reporters.codexray;

import lombok.AccessLevel;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class Diagram<V extends DiagramVertex, E extends DiagramEdge> {

    @Getter(AccessLevel.PROTECTED)
    private final Graph<V, E> coreGraph;

    protected Diagram(Graph<V, E> coreGraph) {
        this.coreGraph = coreGraph;
    }

    public Optional<V> getVertex(String vertexId) {
        return coreGraph.vertexSet().stream().filter(v -> v.getVertexId().equals(vertexId)).findFirst();
    }

    public V addVertex(V v) {
        coreGraph.addVertex(v);
        return v;
    }

    public boolean addEdge(V s, V t, E e) {
        Optional<E> optional = coreGraph.getAllEdges(s, t).stream().filter(edge -> edge.isSimilarWith(e)).findFirst();
        if (optional.isPresent()) {
            return false;
        }
        return coreGraph.addEdge(s, t, e);
    }

    public Set<V> vertexSet() {
        return coreGraph.vertexSet();
    }

    public List<Set<V>> connectedSets() {
        List<Set<V>> ccSet = new ConnectivityInspector<>(coreGraph).connectedSets();
        ccSet.sort(Comparator.comparingInt(Set::size));
        return ccSet;
    }

    public abstract String toPlantUmlScript(Set<V> vertexSet);

    public String toPlantUmlScript() {
        return toPlantUmlScript(coreGraph.vertexSet());
    }

    @Override
    public String toString() {
        return coreGraph.toString();
    }

}
