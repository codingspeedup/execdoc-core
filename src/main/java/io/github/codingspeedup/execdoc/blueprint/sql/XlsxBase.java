package io.github.codingspeedup.execdoc.blueprint.sql;

import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxDocument;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor
public class XlsxBase extends XlsxDocument {

    public static final int NAME_ROW_INDEX = 0;
    public static final int TYPE_ROW_INDEX = 1;
    public static final int FIRST_DATA_ROW_INDEX = 2;
    public static final String MANDATORY_MARKER = "*";

    public XlsxBase(File file) {
        super(file);
    }

    public static String normalizeName(String name) {
        name = StringUtils.trimToNull(name);
        return name == null ? null : name.toUpperCase(Locale.ROOT);
    }

    public List<String> getTableNames() {
        return getSheetNames();
    }

    public XlsxBaseTable getTable(String tableName) {
        tableName = normalizeName(tableName);
        Sheet tableSheet = getWorkbook().getSheet(tableName);
        if (tableSheet == null) {
            return null;
        }
        return new XlsxBaseTable(tableSheet);
    }

    public XlsxBaseTable maybeAddTable(String tableName) {
        tableName = normalizeName(tableName);
        Sheet tableSheet = getWorkbook().getSheet(tableName);
        if (tableSheet == null) {
            tableSheet = maybeMakeSheet(tableName);
            tableSheet.createRow(NAME_ROW_INDEX);
            tableSheet.createRow(TYPE_ROW_INDEX);
            tableSheet.createFreezePane(0, TYPE_ROW_INDEX + 1);
        }
        return new XlsxBaseTable(tableSheet);
    }

}
