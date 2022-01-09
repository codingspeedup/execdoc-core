package xp.kdm.core;

import xp.kdm.kdm.KDMModel;

public abstract class KDMRelationship<M extends KDMModel, F extends KDMEntity, T extends KDMEntity> extends ModelElement {

    private final M modelProperty;

    public KDMRelationship(M kdmModel, F from, T to) {
        kdmModel.addRelationship(from, this, to);
        this.modelProperty = kdmModel;
    }

    public M getModel() {
        return modelProperty;
    }

    public F getFrom() {
        return (F) modelProperty.getRelationshipFrom(this);
    }

    public T getTo() {
        return (T) modelProperty.getRelationshipTo(this);
    }

}
