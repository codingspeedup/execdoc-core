package io.github.codingspeedup.execdoc.reporters.codexray.classdiagram;

import io.github.codingspeedup.execdoc.reporters.codexray.DiagramEdge;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClassEdge extends DiagramEdge {

    private ClassRelation relation = ClassRelation.ASSOCIATION;

    public ClassEdge(ClassRelation relation) {
        this.relation = relation;
    }

    @Override
    public boolean isSimilarWith(DiagramEdge other) {
        if (super.isSimilarWith(other)) {
            return this.relation == ((ClassEdge) other).relation;
        }
        return false;
    }

}
