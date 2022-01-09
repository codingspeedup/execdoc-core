package xp.kdm.code;

public class HasType extends AbstractCodeRelationship<CodeItem, Datatype> {

    public HasType(CodeModel kdmModel, CodeItem from, Datatype to) {
        super(kdmModel, from, to);
    }

}
