package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpElement;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import io.github.codingspeedup.execdoc.toolbox.documents.xlsx.XlsxDocument;
import it.unibo.tuprolog.core.Clause;
import it.unibo.tuprolog.core.Struct;
import it.unibo.tuprolog.core.Var;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BpKbUtilsTest {

    private static final Var X = Var.of("X");

    @Test
    void ensureKbId() {
        assertThrows(NullPointerException.class, () -> BpKbUtils.ensureKbId(null));
        BpElement bpElt = new BpElement() {
            @Getter
            @Setter
            private String kbId;
        };
        assertNull(bpElt.getKbId());
        String kbId = BpKbUtils.ensureKbId(bpElt);
        assertNotNull(kbId);
        assertEquals(kbId, bpElt.getKbId());
        assertEquals(kbId, BpKbUtils.ensureKbId(bpElt));
    }

    @Test
    void parseClauses() {
        List<Clause> clauses;

        clauses = BpKbUtils.parseClauses();
        assertTrue(clauses.isEmpty());
        clauses = BpKbUtils.parseClauses(null);
        assertTrue(clauses.isEmpty());
        clauses = BpKbUtils.parseClauses("", null, "\t", "\n");
        assertTrue(clauses.isEmpty());

        clauses = BpKbUtils.parseClauses("atom");
        assertEquals(1, clauses.size());

        clauses = BpKbUtils.parseClauses("hello(world)");
        assertEquals(1, clauses.size());

        clauses = BpKbUtils.parseClauses("hello(world)", "\n.", "foo(bar)", ".");
        assertEquals(2, clauses.size());

        clauses = BpKbUtils.parseClauses(BpRelationship.class, "(", 12, ",", "twelve", ")");
        assertEquals(1, clauses.size());

        clauses = BpKbUtils.parseClauses(BpEntity.class, "(bar)");
        assertEquals(1, clauses.size());

        assertThrows(UnsupportedOperationException.class, () -> BpKbUtils.parseClauses(Object.class, "(", new String[]{"abc"}, ",", Collections.emptyMap(), ")"));
        assertThrows(UnsupportedOperationException.class, () -> BpKbUtils.parseClauses("."));
    }

    @Test
    void parseStruct() {
        assertNull(BpKbUtils.parseStruct());
        assertThrows(UnsupportedOperationException.class, () -> BpKbUtils.parseStruct("hello(world)", "\n.", "foo(bar)"));
        assertThrows(UnsupportedOperationException.class, () -> BpKbUtils.parseStruct("hello(world) :- foo(bar)"));
        assertEquals("foo", BpKbUtils.parseStruct("foo").toString());
        assertEquals("'BpEntity'(bar)", BpKbUtils.parseStruct(BpEntity.class, "(bar)").toString());
    }

    @Test
    void structOf_functor() {
        Pair<Struct, List<Var>> structVar;

        structVar = BpKbUtils.structOf(true, "foo");
        assertEquals("foo", structVar.getLeft().toString());

        structVar = BpKbUtils.structOf(true,BpEntity.class);
        assertEquals("'BpEntity'", structVar.getLeft().toString());

        structVar = BpKbUtils.structOf(true,"foo", null);
        assertEquals("foo", structVar.getLeft().toString());

        assertThrows(UnsupportedOperationException.class, () -> BpKbUtils.structOf(true,new Object()));
    }

    @Test
    void structOf_null() {
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> BpKbUtils.structOf(true,"foo", null, null));
        assertEquals("Undefined mapping for null", thrown.getMessage());
    }

    @Test
    void structOf_unsupported() {
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> BpKbUtils.structOf(true,"foo", new Object()));
        assertEquals("Undefined mapping for java.lang.Object", thrown.getMessage());
    }

    @Test
    void structOf_strings() {
        Pair<Struct, List<Var>> structVar;

        structVar = BpKbUtils.structOf(true,"foo", "a", "Z", "b", "Y", "Z");
        assertEquals("foo(a, Z_0, b, Y_0, Z_0)", structVar.getLeft().toString());
        assertEquals(3, structVar.getRight().size());

        structVar = BpKbUtils.structOf(false,"foo", "a", "Z", "b", "Y", "Z");
        assertEquals("foo(a, 'Z', b, 'Y', 'Z')", structVar.getLeft().toString());
        assertEquals(0, structVar.getRight().size());
    }

    @Test
    void structOf_terms() {
        Struct s = BpKbUtils.parseStruct("foo(bar)");
        Pair<Struct, List<Var>> structVar;
        structVar = BpKbUtils.structOf(true,"foo", X, s, X);
        assertEquals("foo(X_0, foo(bar), X_0)", structVar.getLeft().toString());
        assertEquals(2, structVar.getRight().size());
    }

    @Test
    void structOf_raw_primitives() {
        Pair<Struct, List<Var>> structVar;
        structVar = BpKbUtils.structOf(true,"foo", 1, 2L, 3F, 5.6D, false);
        assertEquals("foo(1, 2, 3.0, 5.6, false)", structVar.getLeft().toString());
    }

    @Test
    void structOf_boxed_primitives() {
        Pair<Struct, List<Var>> structVar;
        structVar = BpKbUtils.structOf(true,"foo", Integer.valueOf(1), Long.valueOf(2), Float.valueOf(3f), Double.valueOf(5.6), Boolean.TRUE);
        assertEquals("foo(1, 2, 3.0, 5.6, true)", structVar.getLeft().toString());
    }

    @Test
    void structOf_xlsx() {
        XlsxDocument xlsx = new XlsxDocument();
        Sheet sheet = xlsx.maybeMakeSheet("start");
        Cell cell = sheet.createRow(0).createCell(0);
        Pair<Struct, List<Var>> structVar;
        structVar = BpKbUtils.structOf(true,"contains", sheet, cell);
        assertEquals("contains(s0, s0A1)", structVar.getLeft().toString());
    }

    @Test
    void structOf_element() {
        BpElement bpElt = new BpElement() {
            @Getter
            @Setter
            private String kbId;
        };
        Exception thrown = assertThrows(UnsupportedOperationException.class,
                () -> BpKbUtils.structOf(true,"elt", bpElt));
        assertEquals("Entity kbId is not set", thrown.getMessage());
        String kbId = "$" + BpKbUtils.ensureKbId(bpElt);
        bpElt.setKbId(kbId);
        Pair<Struct, List<Var>> structVar = BpKbUtils.structOf(true,"elt", bpElt);
        assertEquals("elt('" + kbId + "')", structVar.getLeft().toString());
    }

}