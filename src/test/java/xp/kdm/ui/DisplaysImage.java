package xp.kdm.ui;

import xp.kdm.action.ActionElement;
import xp.kdm.source.ImageFile;

public class DisplaysImage extends AbstractUIRelationship<ActionElement, ImageFile> {

    public DisplaysImage(UIModel kdmModel, ActionElement from, ImageFile to) {
        super(kdmModel, from, to);
    }

}
