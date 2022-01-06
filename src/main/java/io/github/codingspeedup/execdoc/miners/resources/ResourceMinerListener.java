package io.github.codingspeedup.execdoc.miners.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.workflow.SharedState;

public interface ResourceMinerListener {

    void onResourceDiscovered(Resource resource);

    default void onWorkflowCompleted(SharedState sharedState) {
    }

}
