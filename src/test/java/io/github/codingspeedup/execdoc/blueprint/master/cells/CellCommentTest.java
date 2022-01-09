package io.github.codingspeedup.execdoc.blueprint.master.cells;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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