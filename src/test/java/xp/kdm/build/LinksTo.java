package xp.kdm.build;

public class LinksTo extends AbstractBuildRelationship<SymbolicLink, AbstractBuildElement> {

    public LinksTo(BuildModel kdmModel, SymbolicLink from, AbstractBuildElement to) {
        super(kdmModel, from, to);
    }

}
