package xp.kdm.ui;

import lombok.NoArgsConstructor;
import xp.kdm.action.ActionElement;
import xp.kdm.code.AbstractCodeElement;
import xp.kdm.core.KDMEntity;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor()
public abstract class AbstractUIElement extends KDMEntity {

    @Getter()
    @Setter()
    private Set<AbstractUIRelationship> uiRelation;

    @Getter()
    @Setter()
    private Set<AbstractCodeElement> implementation;

    @Getter()
    @Setter()
    private List<ActionElement> abstraction;
}
