package io.github.codingspeedup.execdoc.toolbox.documents.xlsx;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XlsxUtilTest {

    @Test
    public void rowIndexToName() {
        assertEquals("1", XlsxUtil.rowIndexToName(0));
    }

    @Test
    public void rowNameToIndex() {
        assertEquals(0, XlsxUtil.rowNameToIndex("1"));
    }

    @Test
    public void columnIndexToName() {
        assertEquals("A", XlsxUtil.columnIndexToName(0));
        assertEquals("K", XlsxUtil.columnIndexToName(10));
        assertEquals("Z", XlsxUtil.columnIndexToName(25));
        assertEquals("AA", XlsxUtil.columnIndexToName(26));
        assertEquals("ZZ", XlsxUtil.columnIndexToName(701));
        assertEquals("AAA", XlsxUtil.columnIndexToName(702));
    }

    @Test
    public void columnNameToIndex() {
        assertEquals(0, XlsxUtil.columnNameToIndex("A"));
        assertEquals(10, XlsxUtil.columnNameToIndex("K"));
        assertEquals(25, XlsxUtil.columnNameToIndex("Z"));
        assertEquals(26, XlsxUtil.columnNameToIndex("AA"));
        assertEquals(701, XlsxUtil.columnNameToIndex("ZZ"));
        assertEquals(702, XlsxUtil.columnNameToIndex("AAA"));
    }

}