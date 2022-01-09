package xp.kdm.core;

import xp.kdm.kdm.KDMModel;
import xp.kdm.source.SourceRef;
import xp.kdm.source.Track;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor()
public abstract class KDMEntity extends ModelElement {

    @Getter()
    @Setter()
    private Collection<AggregatedRelationship> aggregatedRelation;

    @Getter()
    @Setter()
    private Collection<SourceRef> source;

    @Getter()
    @Setter()
    private Collection<Track> track;

    @Getter()
    private Set<KDMEntity> groupedElement;

    @Getter()
    private Collection<KDMEntity> groupProperty;

    @Getter()
    private Set<KDMRelationship<?, ?, ?>> ownedRelation;

    @Getter()
    @Setter()
    private String name;

    private KDMModel modelProperty;
    private KDMEntity ownerProperty;
    private Set<KDMEntity> ownedElement;

    public KDMEntity(String name) {
        this.name = name;
    }

    public KDMEntity(KDMModel modelProperty) {
        this(null, modelProperty);
    }

    public KDMEntity(String name, KDMModel modelProperty) {
        this.name = name;
        modelProperty.addEntity(this);
    }

    public KDMModel getModel() {
        return modelProperty;
    }

    public void setModel(KDMModel modelProperty) {
        if (this.modelProperty == modelProperty) {
            return;
        }
        if (this.modelProperty == null) {
            this.modelProperty = modelProperty;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public KDMEntity getOwner() {
        return ownerProperty;
    }

    public void setOwner(KDMEntity owner) {
        if (ownerProperty != null) {
            ownerProperty.ownedElement.remove(this);
        }
        ownerProperty = owner;
        if (ownerProperty.ownedElement == null) {
            ownerProperty.ownedElement = new HashSet<>();
        }
        ownerProperty.ownedElement.add(this);
    }

    public Set<KDMEntity> getOwnedElement() {
        return Collections.unmodifiableSet(ownedElement);
    }

    public void createAggregation(KDMEntity otherEntity) {
        throw new UnsupportedOperationException();
    }

    public void deleteAggregation(AggregatedRelationship aggregation) {
        throw new UnsupportedOperationException();
    }

}
