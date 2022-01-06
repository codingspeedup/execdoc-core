package io.github.codingspeedup.execdoc.blueprint.generators;

import io.github.codingspeedup.execdoc.blueprint.Blueprint;
import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.ui.BpL10nLabel;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class L10nBundlesGenerator {

    private final BpKb bpKb;

    public L10nBundlesGenerator(Blueprint<?> bp) {
        this.bpKb = bp.compileKb();
    }

    public Map<String, Properties> getLabels() {
        Map<String, Properties> translations = new HashMap<>();
        for (String labelId : bpKb.solveEntities(BpL10nLabel.class)) {
            BpL10nLabel l10nLabel = bpKb.solveEntity(BpL10nLabel.class, labelId);
            for (Map.Entry<String, String> l10n : l10nLabel.getL10n().entrySet()) {
                Properties translation = translations.computeIfAbsent(l10n.getKey(), lang -> new Properties());
                translation.put(l10nLabel.getName(), l10n.getValue());
            }
        }
        return translations;
    }

}
