package io.github.codingspeedup.execdoc.blueprint.kb.ontology.ui;

import io.github.codingspeedup.execdoc.blueprint.kb.KbFunctor;
import io.github.codingspeedup.execdoc.blueprint.kb.KbNames;
import io.github.codingspeedup.execdoc.blueprint.kb.ontology.BpEntityCell;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.IsNamed;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.ui.BpAbstractUiElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@KbFunctor
public class BpL10nLabel extends BpEntityCell implements BpAbstractUiElement, IsNamed {

    public static final String DEFAULT_LANGUAGE_KEY = "";

    @Getter
    @KbFunctor(value = KbNames.L10N_FUNCTOR, T1 = String.class, T2 = String.class)
    private final Map<String, String> l10n = new HashMap<>();

    @Getter
    @Setter
    @KbFunctor(KbNames.NAME_FUNCTOR)
    private String name;

    public BpL10nLabel(Cell cell) {
        super(cell);
    }

    public String getTranslation() {
        return l10n.get(DEFAULT_LANGUAGE_KEY);
    }

    public String getTranslation(String languageKey) {
        String translation = l10n.get(DEFAULT_LANGUAGE_KEY);
        return translation == null ? getTranslation() : translation;
    }

}
