package io.github.codingspeedup.execdoc.blueprint.master.cells;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CellCommentTest {

    @Test
    public void parse() {
        assertEquals("abc", CellComment.parse("abc*/").getDocumentation());
        assertEquals("abc", CellComment.parse("/*abc").getDocumentation());
        assertEquals("abc", CellComment.parse("/*abc*/").getDocumentation());
        assertEquals("", CellComment.parse("/**/").getDocumentation());
        assertNull(CellComment.parse("/*/").getDocumentation());
        assertNull(CellComment.parse("abc").getDocumentation());
        assertNull(CellComment.parse("").getDocumentation());
    }

}