package xp.kdm.build;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor()
public class BuildDescription extends BuildResource {

    @Getter()
    @Setter()
    private String text;
}
