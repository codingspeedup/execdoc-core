package xp.kdm.platform;

import xp.kdm.action.ActionElement;

public class Loads extends AbstractPlatformRelationship<ActionElement, DeployedComponent> {

    public Loads(PlatformModel kdmModel, ActionElement from, DeployedComponent to) {
        super(kdmModel, from, to);
    }

}
