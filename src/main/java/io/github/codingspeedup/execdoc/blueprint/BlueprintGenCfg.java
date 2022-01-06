package io.github.codingspeedup.execdoc.blueprint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@NoArgsConstructor
@Getter
@Setter
public abstract class BlueprintGenCfg {

    private File destinationFolder;
    private boolean force;

}
