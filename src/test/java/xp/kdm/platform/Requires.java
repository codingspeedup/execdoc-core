package xp.kdm.platform;

public class Requires extends AbstractPlatformRelationship<DeployedComponent, AbstractPlatformElement> {

    public Requires(PlatformModel kdmModel, DeployedComponent from, AbstractPlatformElement to) {
        super(kdmModel, from, to);
    }

}
