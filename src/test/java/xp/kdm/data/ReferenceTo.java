package xp.kdm.data;

public class ReferenceTo extends AbstractDataRelationship<ContentItem, ContentItem> {

    public ReferenceTo(DataModel kdmModel, ContentItem from, ContentItem to) {
        super(kdmModel, from, to);
    }

}
