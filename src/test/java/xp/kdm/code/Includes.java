package xp.kdm.code;

public class Includes extends AbstractCodeRelationship<PreprocessorDirective, AbstractCodeElement> {

    public Includes(CodeModel kdmModel, PreprocessorDirective from, AbstractCodeElement to) {
        super(kdmModel, from, to);
    }

}
