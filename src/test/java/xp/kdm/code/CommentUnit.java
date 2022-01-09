package xp.kdm.code;

import xp.kdm.core.ModelElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor()
public class CommentUnit extends ModelElement {

    @Getter()
    @Setter()
    private String text;

    public CommentUnit(String text) {
        this.text = text;
    }

}
