package io.github.codingspeedup.execdoc.reporters.codexray;

public abstract class DiagramEdge {

    public boolean isSimilarWith(DiagramEdge other) {
        if (other == null) {
            return false;
        }
        return this.getClass().getName().equals(other.getClass().getName());
    }

    @Override
    public String toString() {
        return " -> ";
    }

}
