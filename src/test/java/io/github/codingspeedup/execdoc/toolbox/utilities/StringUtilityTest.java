package io.github.codingspeedup.execdoc.toolbox.utilities;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilityTest {

    @Test
    public void splitLines() {
        assertEquals(1, StringUtility.splitLines("").length);
        assertEquals(2, StringUtility.splitLines("Lorem\nipsum").length);
        assertEquals(2, StringUtility.splitLines("Lorem\r\nipsum").length);
    }

    @Test
    public void toBasicL10nKey() {
        assertEquals("1-lorem-ipsum", StringUtility.toBasicL10nKey("\t1 LorÈm ?  ipsum..\n."));
    }

}