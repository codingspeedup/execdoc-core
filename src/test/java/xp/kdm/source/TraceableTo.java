package xp.kdm.source;

import xp.kdm.core.KDMEntity;

public class TraceableTo extends AbstractInventoryRelationship<Track, KDMEntity> {

    public TraceableTo(InventoryModel kdmModel, Track from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
