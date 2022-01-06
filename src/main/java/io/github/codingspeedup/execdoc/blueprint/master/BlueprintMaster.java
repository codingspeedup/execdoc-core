package io.github.codingspeedup.execdoc.blueprint.master;

import io.github.codingspeedup.execdoc.blueprint.kb.BpKb;
import io.github.codingspeedup.execdoc.blueprint.master.cells.CellStyles;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.BlueprintSheet;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.SheetNameComparator;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.SystemSheet;
import io.github.codingspeedup.execdoc.blueprint.master.sheets.core.TocSheet;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxDocument;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BlueprintMaster extends XlsxDocument {

    public static final String INSTANTIABLE_SHEET_MARKER = "\u27f6";

    @Getter
    private final Map<String, Class<? extends BlueprintSheet>> sheetRegistry = new LinkedHashMap<>();
    @Getter
    private final CellStyles cellStyles;
    private final Map<String, BlueprintSheet> modelSheets = new LinkedHashMap<>();

    public BlueprintMaster(File file) {
        super(file);
        registerSheet(TocSheet.class);

        registerSheets();
        cellStyles = new CellStyles(getWorkbook());
        if (getWorkbook().getNumberOfSheets() == 0) {
            sheetRegistry.values().forEach(sheetClass -> createSheet(sheetClass, ""));
            normalize();
        }
        wrapSheets();
    }

    @SneakyThrows
    public static boolean isSingleton(Class<? extends BlueprintSheet> sheetClass) {
        return !getNameMarker(sheetClass).contains(INSTANTIABLE_SHEET_MARKER);
    }

    @SneakyThrows
    public static String getNameMarker(Class<? extends BlueprintSheet> sheetClass) {
        return (String) sheetClass.getDeclaredField("NAME_MARKER").get(null);
    }

    @SneakyThrows
    protected final void registerSheet(Class<? extends BlueprintSheet> sheetClass) {
        sheetRegistry.put(getNameMarker(sheetClass), sheetClass);
    }

    protected abstract void registerSheets();

    public void normalize() {
        if (getWorkbook().getSheet(TocSheet.NAME_MARKER) == null) {
            getWorkbook().createSheet(TocSheet.NAME_MARKER);
        }
        if (getWorkbook().getSheet(SystemSheet.NAME_MARKER) == null) {
            getWorkbook().createSheet(SystemSheet.NAME_MARKER);
        }
        sortSheets();
        modelSheets.clear();
        wrapSheets();
        for (Map.Entry<String, BlueprintSheet> entry : modelSheets.entrySet()) {
            BlueprintSheet blueprintSheet = entry.getValue();
            blueprintSheet.normalize();
            int blueprintSheetIndex = getWorkbook().getSheetIndex(blueprintSheet.getSheet());
            getWorkbook().setSheetHidden(blueprintSheetIndex, blueprintSheet.isHidden());
        }
        XSSFFormulaEvaluator.evaluateAllFormulaCells(getWorkbook());
        getWorkbook().setActiveSheet(getSheetIndex(TocSheet.NAME_MARKER));
    }

    public <T extends BlueprintSheet> T getSheet(Class<T> sheetClass) {
        return getSheet(sheetClass, "");
    }

    @SuppressWarnings("unchecked")
    public <T extends BlueprintSheet> T getSheet(Class<T> sheetClass, String instanceName) {
        String sheetName = getNameMarker(sheetClass) + instanceName;
        return (T) modelSheets.get(sheetName);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends BlueprintSheet> List<T> getSheets(Class<T> sheetClass) {
        List<T> sheets = new ArrayList<>();
        for (Map.Entry<String, BlueprintSheet> entry : modelSheets.entrySet()) {
            if (sheetClass.isAssignableFrom(entry.getValue().getClass())) {
                sheets.add((T) entry.getValue());
            }
        }
        return sheets;
    }

    public BlueprintSheet getSheet(String sheetName) {
        return modelSheets.get(sheetName);
    }

    public BlueprintSheet getSheet(Sheet sheet) {
        return modelSheets.get(sheet.getSheetName());
    }

    public BlueprintSheet getSheet(Row row) {
        return modelSheets.get(row.getSheet().getSheetName());
    }

    public BlueprintSheet getSheet(Cell cell) {
        return modelSheets.get(cell.getSheet().getSheetName());
    }

    @SuppressWarnings({"unchecked"})
    public <T extends BlueprintSheet> T maybeCreateSheet(Class<T> sheetClass, String instanceName) {
        String sheetMarker = getNameMarker(sheetClass);
        if (!sheetMarker.endsWith(INSTANTIABLE_SHEET_MARKER)) {
            throw new UnsupportedOperationException("Sheet " + sheetClass.getName() + " is not instantiable");
        }
        String sheetName = sheetMarker + instanceName;
        if (modelSheets.containsKey(sheetName)) {
            return getSheet(sheetClass, instanceName);
        }
        return (T) createSheet(sheetClass, instanceName);
    }

    @SneakyThrows
    private BlueprintSheet createSheet(Class<? extends BlueprintSheet> sheetClass, String instanceName) {
        String sheetMarker = getNameMarker(sheetClass);
        String sheetName = sheetMarker;
        if (sheetMarker.endsWith(INSTANTIABLE_SHEET_MARKER)) {
            sheetName = sheetName + instanceName;
        }
        if (getWorkbook().getSheet(sheetName) != null) {
            throw new UnsupportedOperationException("Sheet " + sheetName + " already exists!");
        }
        Sheet sheet = maybeMakeSheet(sheetName);
        Constructor<? extends BlueprintSheet> constructor = sheetClass.getConstructor(BlueprintMaster.class, Sheet.class);
        BlueprintSheet sheetInstance = constructor.newInstance(this, sheet);
        sheetInstance.initialize();
        if (SheetNameComparator.isModelSheet(sheetMarker, instanceName)) {
            modelSheets.put(sheetName, sheetInstance);
        }
        return sheetInstance;
    }

    private void sortSheets() {
        List<String> sheets = new ArrayList<>();
        visitSheets((__, sheet, ___) -> {
            sheets.add(sheet.getSheetName());
            return false;
        });

        List<String> markers = new ArrayList<>();
        for (Map.Entry<String, Class<? extends BlueprintSheet>> entry : sheetRegistry.entrySet()) {
            String sheetMarker = entry.getKey();
            markers.add(sheetMarker);
            if (!sheets.contains(sheetMarker)) {
                createSheet(entry.getValue(), "");
                sheets.add(sheetMarker);
            }
        }

        sheets.sort(new SheetNameComparator(markers));
        for (int i = 0; i < sheets.size(); ++i) {
            maybeMakeSheet(sheets.get(i), i);
        }
    }

    private void wrapSheets() {
        visitSheets((__, sheet, ___) -> {
            String sheetName = sheet.getSheetName();
            for (Map.Entry<String, Class<? extends BlueprintSheet>> entry : sheetRegistry.entrySet()) {
                String sheetMarker = entry.getKey();
                if (sheetName.startsWith(sheetMarker)) {
                    String instanceName = sheetName.substring(sheetMarker.length());
                    if (!SheetNameComparator.isModelSheet(sheetMarker, instanceName)) {
                        continue;
                    }
                    Class<? extends BlueprintSheet> sheetClass = entry.getValue();
                    try {
                        modelSheets.put(sheetName, ConstructorUtils.invokeConstructor(sheetClass, new Object[]{this, sheet}));
                    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        System.err.println("There was an error reading sheet " + sheetName + " " + e.getMessage());
                    }
                }
            }
            return false;
        });
    }

    public void train(BpKb bpKb) {
        for (Map.Entry<String, BlueprintSheet> entry : modelSheets.entrySet()) {
            entry.getValue().expand(bpKb);
        }
    }

}
