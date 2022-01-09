package xp.kdm.ui;

import xp.kdm.core.KDMEntity;

public class UIRelationship extends AbstractUIRelationship<AbstractUIElement, KDMEntity> {

    public UIRelationship(UIModel kdmModel, AbstractUIElement from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
