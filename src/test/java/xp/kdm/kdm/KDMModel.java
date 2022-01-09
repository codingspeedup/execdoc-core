package xp.kdm.kdm;

import xp.kdm.core.KDMEntity;
import xp.kdm.core.KDMRelationship;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.Set;

public abstract class KDMModel extends FrameworkElement {

    private final Graph<KDMEntity, KDMRelationship> relationshipGraph;

    @Getter()
    private Set<KDMEntity> ownedElement;

    public KDMModel() {
        relationshipGraph = new DefaultDirectedGraph<>(KDMRelationship.class);
    }

    public KDMEntity getRelationshipFrom(KDMRelationship kdmRelationship) {
        return relationshipGraph.getEdgeSource(kdmRelationship);
    }

    public KDMEntity getRelationshipTo(KDMRelationship kdmRelationship) {
        return relationshipGraph.getEdgeTarget(kdmRelationship);
    }

    public boolean addEntity(KDMEntity kdmEntity) {
        kdmEntity.setModel(this);
        return relationshipGraph.addVertex(kdmEntity);
    }

    public boolean addRelationship(KDMEntity fromEntity, KDMRelationship kdmRelationship, KDMEntity toEntity) {
        if (kdmRelationship.getModel() != null) {
            throw new UnsupportedOperationException();
        }
        addEntity(fromEntity);
        addEntity(toEntity);
        return relationshipGraph.addEdge(fromEntity, toEntity, kdmRelationship);
    }

}
