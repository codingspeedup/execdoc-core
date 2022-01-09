package xp.kdm.code;

import xp.kdm.core.KDMEntity;

public class CodeRelationship extends AbstractCodeRelationship<CodeItem, KDMEntity> {

    public CodeRelationship(CodeModel kdmModel, CodeItem from, KDMEntity to) {
        super(kdmModel, from, to);
    }

}
