package io.github.codingspeedup.execdoc.miners.resources;

import io.github.codingspeedup.execdoc.toolbox.resources.Resource;
import io.github.codingspeedup.execdoc.toolbox.workflow.SharedState;

public class DefaultResourceMinerListener implements ResourceMinerListener {

    @Override
    public void onResourceDiscovered(Resource resource) {
        System.out.println(resource.getDescription());
    }

    @Override
    public void onWorkflowCompleted(SharedState workflowState) {
        System.out.println(workflowState);
    }

}
