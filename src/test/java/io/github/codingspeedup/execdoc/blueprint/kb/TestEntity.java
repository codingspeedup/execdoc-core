package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;

@KbFunctor
public class TestEntity implements BpEntity {

    @Getter
    @KbFunctor(T1 = String.class)
    private final Set<String> finalSet = new HashSet<>();

    @Getter
    @KbFunctor(T1 = String.class)
    private final List<String> finalList = new ArrayList<>();

    @Getter
    @KbFunctor(T1 = String.class, T2 = String.class)
    private final Map<String, String> finalMap = new HashMap<>();

    @Getter
    @Setter
    private String kbId;

    @Getter
    @Setter
    @KbFunctor("self")
    private TestEntity selfReference;

    @Getter
    @Setter
    @KbFunctor("other")
    private TestEntity otherReference;

    @Getter
    @Setter
    @KbFunctor
    private String description;

    @Getter
    @Setter
    @KbFunctor
    private boolean booleanRaw;

    @Getter
    @Setter
    @KbFunctor
    private Boolean booleanObject;

    @Getter
    @Setter
    @KbFunctor
    private int intRaw;

    @Getter
    @Setter
    @KbFunctor
    private Integer intObject;

    @Getter
    @Setter
    @KbFunctor
    private long longRaw;

    @Getter
    @Setter
    @KbFunctor
    private Long longObject;

    @Getter
    @Setter
    @KbFunctor
    private float floatRaw;

    @Getter
    @Setter
    @KbFunctor
    private Float floatObject;

    @Getter
    @Setter
    @KbFunctor
    private double doubleRaw;

    @Getter
    @Setter
    @KbFunctor
    private Double doubleObject;

    @Getter
    @Setter
    @KbFunctor(T1 = String.class, T2 = String.class)
    private Pair<String, String> pairStringString;

    @Getter
    @Setter
    @KbFunctor(T1 = String.class, T2 = String.class, T3 = String.class)
    private Triple<String, String, String> tripleStringStringString;

    @Getter
    @Setter
    @KbFunctor(T1 = String.class, T2 = TreeSet.class)
    private Set<String> nonFinalSet;

    @Getter
    @Setter
    @KbFunctor(T1 = String.class, T2 = LinkedList.class)
    private List<String> nonFinalList;

    @Getter
    @Setter
    @KbFunctor(T1 = String.class, T2 = String.class, T3 = TreeMap.class)
    private Map<String, String> nonFinalMap;

}
