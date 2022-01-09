package io.github.codingspeedup.execdoc.blueprint.kb;

import it.unibo.tuprolog.core.Clause;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BpKbTest {

    private BpKb kb;

    @BeforeEach
    void init() {
        kb = new BpKb();
    }

    @Test
    void learn_functor_arguments() {
        Clause clause = kb.learn("foo", "bar");
        assertTrue(kb.getTheory().contains(clause));
    }

    @Test
    void learn_entity() {
        TestEntity exEnt = new TestEntity();
        exEnt.setSelfReference(exEnt);
        exEnt.setDescription("Lorem ipsum");
        exEnt.setBooleanRaw(true);
        exEnt.setBooleanObject(Boolean.FALSE);
        exEnt.setIntRaw(10);
        exEnt.setIntObject(11);
        exEnt.setLongRaw(20L);
        exEnt.setLongObject(21L);
        exEnt.setFloatRaw(10.5F);
        exEnt.setFloatObject(11.5F);
        exEnt.setDoubleRaw(20.5D);
        exEnt.setDoubleObject(21.5D);
        exEnt.setPairStringString(Pair.of("foo", "bar"));
        exEnt.setTripleStringStringString(Triple.of("qux", "quux", "quuux"));

        TestEntity exEnt2 = new TestEntity();
        exEnt.setOtherReference(exEnt2);
        exEnt2.setOtherReference(exEnt);

        String exId = kb.learn(exEnt);
        String exId2 = kb.learn(exEnt2);
        Set<String> entIds = kb.solveEntities(TestEntity.class);
        assertTrue(entIds.contains(exId));
        assertTrue(entIds.contains(exId2));

        TestEntity acEnt = kb.solveEntity(TestEntity.class, exId);
        TestEntity acEnt2 = kb.solveEntity(TestEntity.class, exId2);

        assertEquals(exId, acEnt.getKbId());
        assertEquals(exId, acEnt.getSelfReference().getKbId());

        assertEquals(exId2, acEnt2.getKbId());
        assertNull(acEnt2.getSelfReference());

        assertEquals(exId2, acEnt.getOtherReference().getKbId());
        assertEquals(exId, acEnt2.getOtherReference().getKbId());

        assertEquals("Lorem ipsum", acEnt.getDescription());
        assertTrue(acEnt.isBooleanRaw());
        assertFalse(acEnt.getBooleanObject());
        assertEquals(10, acEnt.getIntRaw());
        assertEquals(11, acEnt.getIntObject());
        assertEquals(20, acEnt.getLongRaw());
        assertEquals(21, acEnt.getLongObject());
        assertEquals(10.5F, acEnt.getFloatRaw(), 0.001);
        assertEquals(11.5F, acEnt.getFloatObject(), 0.001);
        assertEquals(20.5D, acEnt.getDoubleRaw(), 0.001);
        assertEquals(21.5D, acEnt.getDoubleObject(), 0.001);
        assertEquals(exEnt.getPairStringString(), acEnt.getPairStringString());
        assertEquals(exEnt.getTripleStringStringString(), acEnt.getTripleStringStringString());
    }

}