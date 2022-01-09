package xp.kdm.data;

public class TypedBy extends AbstractDataRelationship<ContentItem, ComplexContentType> {

    public TypedBy(DataModel kdmModel, ContentItem from, ComplexContentType to) {
        super(kdmModel, from, to);
    }

}
