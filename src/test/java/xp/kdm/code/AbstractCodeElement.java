package xp.kdm.code;

import xp.kdm.action.EntryFlow;
import xp.kdm.core.KDMEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor()
public abstract class AbstractCodeElement extends KDMEntity {

    private List<CommentUnit> comment;

    @Getter()
    @Setter()
    private Set<AbstractCodeRelationship<?, ?>> codeRelation;

    @Getter()
    @Setter()
    private Set<EntryFlow<?>> entryFlow;

    public List<CommentUnit> getComment() {
        if (comment == null) {
            comment = new ArrayList<>();
        }
        return comment;
    }

}
