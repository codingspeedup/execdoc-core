package io.github.codingspeedup.execdoc.miners.resources.java.workflows;

import io.github.codingspeedup.execdoc.toolbox.documents.java.JavaDocument;
import io.github.codingspeedup.execdoc.toolbox.workflow.SharedState;
import lombok.Getter;
import lombok.Setter;

public class JavaMinerState extends SharedState {

    @Getter
    @Setter
    private JavaDocument jDoc;

}
