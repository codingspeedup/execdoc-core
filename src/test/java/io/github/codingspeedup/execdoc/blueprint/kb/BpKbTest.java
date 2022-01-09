package io.github.codingspeedup.execdoc.blueprint.kb;

import it.unibo.tuprolog.core.Clause;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

        exEnt.getFinalSet().add("foo");
        exEnt.setNonFinalSet(new TreeSet<>());
        exEnt.getNonFinalSet().add("bar");

        exEnt.getFinalList().add("foo");
        exEnt.getFinalList().add("bar");
        exEnt.getFinalList().add("baz");
        exEnt.setNonFinalList(new LinkedList<>());
        exEnt.getNonFinalList().add("foo");
        exEnt.getNonFinalList().add("bar");
        exEnt.getNonFinalList().add("baz");

        exEnt.getFinalMap().put("foo", "bar");
        exEnt.setNonFinalMap(new TreeMap<>());
        exEnt.getNonFinalMap().put("foo", "bar");

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
        assertEquals(TreeSet.class, acEnt.getNonFinalSet().getClass());
        assertEquals("bar", acEnt.getNonFinalSet().iterator().next());
        assertEquals("foo", acEnt.getFinalSet().iterator().next());
        assertEquals(LinkedList.class, acEnt.getNonFinalList().getClass());
        assertEquals("baz", acEnt.getNonFinalList().get(2));
        assertEquals("baz", acEnt.getFinalList().get(2));
        assertEquals(TreeMap.class, acEnt.getNonFinalMap().getClass());
        assertEquals("bar", acEnt.getNonFinalMap().get("foo"));
        assertEquals("bar", acEnt.getFinalMap().get("foo"));
    }

    @Test
    void learn_relationship() {
        TestRelationship exRel = new TestRelationship();
        exRel.setFrom(kb.learn(new TestEntity()));
        exRel.setTo(kb.learn(new TestEntity()));
        exRel.setDescription("Lorem ipsum...");
        String exId = kb.learn(exRel);
        assertTrue(kb.solveRelationships(TestRelationship.class).stream().map(Triple::getLeft).collect(Collectors.toSet()).contains(exId));

        TestRelationship acRel = kb.solveRelationship(TestRelationship.class, exId);
        assertEquals(exRel.getFrom(), acRel.getFrom());
        assertEquals(exRel.getTo(), acRel.getTo());
        assertEquals(exRel.getDescription(), acRel.getDescription());

        assertEquals(2, kb.findFunctors(exId).size());
    }

}