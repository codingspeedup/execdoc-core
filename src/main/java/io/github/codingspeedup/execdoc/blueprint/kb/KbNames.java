package io.github.codingspeedup.execdoc.blueprint.kb;

import com.google.common.base.CaseFormat;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;

import java.lang.reflect.Field;

public class KbNames {

    public static final String ANNOTATION_FUNCTOR = "bpAnnotation";
    public static final String ATTRIBUTE_FUNCTOR = "bpAttribute";
    public static final String CODE_ELEMENT_FUNCTOR = "bpCodeElement";
    public static final String DOC_STRING_FUNCTOR = "bpDocString";
    public static final String ITEM_UNIT_FUNCTOR = "bpItemUnit";
    public static final String KEY_FUNCTOR = "bpKey";
    public static final String L10N_FUNCTOR = "bpL10n";
    public static final String NAME_FUNCTOR = "bpName";
    public static final String OWNER_FUNCTOR = "bpOwner";
    public static final String TYPE_FUNCTOR = "bpType";
    public static final String VALUE_FUNCTOR = "bpValue";

    public static String getAtom(Sheet sheet) {
        return "s" + sheet.getWorkbook().getSheetIndex(sheet.getSheetName());
    }

    public static String getAtom(Cell cell) {
        if (cell == null) {
            return null;
        }
        String name = getAtom(cell.getSheet());
        return name + CellReference.convertNumToColString(cell.getColumnIndex()) + (cell.getRowIndex() + 1);
    }

    public static String getFunctor(Class<?> typePredicate) {
        String functor;
        KbFunctor functorAnnotation = typePredicate.getAnnotation(KbFunctor.class);
        if (functorAnnotation == null) {
            functor = typePredicate.getName();
        } else {
            String valueName = functorAnnotation.value();
            if (StringUtils.isBlank(valueName)) {
                functor = typePredicate.getSimpleName();
                if (BpRelationship.class.isAssignableFrom(typePredicate)) {
                    functor = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, functor);
                }
            } else {
                functor = valueName;
            }
        }

        return functor;
    }

    public static String getFunctor(Field fieldPredicate) {
        KbFunctor functorAnnotation = fieldPredicate.getAnnotation(KbFunctor.class);
        if (functorAnnotation == null) {
            return null;
        }
        String valueName = functorAnnotation.value();
        return StringUtils.isBlank(valueName) ? "bp" + StringUtils.capitalize(fieldPredicate.getName()) : valueName;
    }

}
