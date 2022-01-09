package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpElement;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import io.github.codingspeedup.execdoc.toolbox.utilities.NumberUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.UuidUtility;
import it.unibo.tuprolog.core.*;
import it.unibo.tuprolog.solve.Solver;
import it.unibo.tuprolog.solve.classic.ClassicSolverFactory;
import it.unibo.tuprolog.theory.Theory;
import it.unibo.tuprolog.theory.parsing.ClausesParser;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.jetbrains.annotations.NotNull;

import java.lang.Integer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class BpKb {

    private static final Var X = Var.of("X");
    private static final Var Y = Var.of("Y");
    private static final Var Z = Var.of("Z");
    private static final Var Z1 = Var.of("Z1");
    private static final Var Z2 = Var.of("Z2");
    private static final Var Z3 = Var.of("Z3");

    private static final Method LIST_ADD;
    private static final Method SET_ADD;
    private static final Method MAP_PUT;

    static {
        try {
            LIST_ADD = List.class.getMethod("add", Object.class);
            SET_ADD = Set.class.getMethod("add", Object.class);
            MAP_PUT = Map.class.getMethod("put", Object.class, Object.class);
        } catch (NoSuchMethodException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    @Getter
    private final Theory theory = Theory.empty().toMutableTheory();
    private final Set<String> learnedPredicates = new HashSet<>();
    private final Set<String> learnedElements = new HashSet<>();

    private Solver solver;

    public BpKb() {
        learnedPredicates.add(KbNames.getFunctor(BpEntity.class));
    }

    static String ensureKbId(BpElement element) {
        if (StringUtils.isBlank(element.getKbId())) {
            element.setKbId(UuidUtility.nextUuid());
        }
        return element.getKbId();
    }

    public void learn(Clause clause) {
        theory.plus(clause);
        solver = null;
    }

    public Clause learn(String functor, Object... arguments) {
        Clause clause = Clause.of(structOf(functor, arguments));
        learn(clause);
        return clause;
    }

    public String learn(BpEntity entity) {
        if (entity == null) {
            return null;
        }
        String functor = learnTypeHierarchy(entity.getClass());
        String kbId = ensureKbId(entity);
        if (!learnedElements.contains(kbId)) {
            learn(functor, Atom.of(kbId));
            learnedElements.add(kbId);
            learnFields(entity.getClass(), entity);
            entity.kbStore(this);
        }
        return kbId;
    }

    public String learn(BpRelationship relationship) {
        if (relationship == null || StringUtils.isBlank(relationship.getFrom()) || StringUtils.isBlank(relationship.getTo())) {
            return null;
        }
        String functor = learnTypeHierarchy(relationship.getClass());
        String kbId = ensureKbId(relationship);
        if (!learnedElements.contains(kbId)) {
            learn(functor, kbId, relationship.getFrom(), relationship.getTo());
            learnedElements.add(kbId);
            learnFields(relationship.getClass(), relationship);
            relationship.kbStore(this);
        }
        return kbId;
    }

    public Set<String> findFunctors(String ofAtom) {
        return findFunctors(ofAtom, 0);
    }

    public Set<String> findFunctors(String ofAtom, int index) {
        Set<String> functors = new HashSet<>();
        for (Clause clause : theory.getClauses()) {
            Struct head = clause.getHead();
            if (head != null && head.getArity() > index) {
                Term arg = head.getArgs().get(index);
                if (arg.isAtom()) {
                    if (ofAtom.equals(KbResult.asString(arg))) {
                        functors.add(head.getFunctor());
                    }
                }
            }
        }
        return functors;
    }

    public KbResult solve(boolean findAll, String... goal) {
        String clauseString = Arrays.stream(goal).filter(Objects::nonNull).collect(Collectors.joining()).trim();
        if (!clauseString.endsWith(".")) {
            clauseString = clauseString + ".";
        }
        List<Clause> clauses = ClausesParser.getWithStandardOperators().parseClauses(clauseString);
        return solve(clauses.get(0).getHead(), findAll);
    }

    public KbResult solveList(String functor, Object... args) {
        return solve(true, functor, args);
    }

    public KbResult solveOnce(String functor, Object... args) {
        return solve(false, functor, args);
    }

    public <E extends BpEntity> Set<String> solveEntities(Class<E> entityType) {
        String functor = learnTypeHierarchy(entityType);
        KbResult sr = solveList(functor, X);
        return sr.getSubstitutions().stream().map(s -> KbResult.asString(s[0])).collect(Collectors.toSet());
    }

    @SneakyThrows
    public <E extends BpEntity> E solveEntity(Class<E> entityType, String kbId) {
        E entity = null;
        String functor = learnTypeHierarchy(entityType);
        KbResult solution = solveOnce(functor, kbId);
        if (!solution.getYes().isEmpty()) {
            entity = entityType.getConstructor().newInstance();
            entity.setKbId(kbId);
            recallFields(entityType, entity);
            entity.kbRetrieve(this);
        }
        return entity;
    }

    public <E extends BpEntity> E solveEntity(Class<E> entityType) {
        Set<String> ids = solveEntities(entityType);
        if (ids.size() != 1) {
            throw new UnsupportedOperationException("Found " + ids.size() + " entities");
        }
        return solveEntity(entityType, ids.iterator().next());
    }

    public <E extends BpRelationship> Set<Triple<String, String, String>> solveRelationships(Class<E> relationshipType) {
        String entityPredicate = learnTypeHierarchy(relationshipType);
        KbResult solutions = solveList(entityPredicate, X, Y, Z);
        return solutions.getSubstitutions().stream().map(s -> Triple.of(KbResult.asString(s[0]), KbResult.asString(s[1]), KbResult.asString(s[2]))).collect(Collectors.toSet());
    }

    @SneakyThrows
    public <R extends BpRelationship> R solveRelationship(Class<R> relationshipType, String kbId) {
        R relationship = null;
        String functor = KbNames.getFunctor(relationshipType);
        KbResult solution = solveOnce(functor, kbId, Y, Z);
        if (!solution.getYes().isEmpty()) {
            Term[] subst = solution.getSubstitutions().get(0);
            relationship = relationshipType.getConstructor().newInstance();
            relationship.setKbId(kbId);
            relationship.setFrom(KbResult.asString(subst[0]));
            relationship.setTo(KbResult.asString(subst[1]));
            recallFields(relationshipType, relationship);
            relationship.kbRetrieve(this);
        }
        return relationship;
    }

    public String listTheory() {
        String kb = getTheory().toString(true);
        return kb.replace(" :- true.", ".");
    }

    private KbResult solve(Struct goal, boolean solveList) {
        if (solver == null) {
            solver = ClassicSolverFactory.INSTANCE.mutableSolverOf(theory);
        }
        if (solveList) {
            return new KbResult(solver.solveList(goal));
        }
        return new KbResult(solver.solveOnce(goal));
    }

    private KbResult solve(boolean solveList, String functor, Object... args) {
        List<Var> varList = new ArrayList<>();

        Map<String, Var> varMap = new HashMap<>();
        List<Term> terms = new ArrayList<>();
        for (Object arg : args) {
            if (arg instanceof String) {
                String id = ((String) arg).trim();
                if (id.charAt(0) == Character.toUpperCase(id.charAt(0))) {
                    Var var = varMap.computeIfAbsent(id, Var::of);
                    varList.add(var);
                    terms.add(var);
                } else {
                    terms.add(Struct.of(id));
                }
            } else if (arg instanceof Var) {
                varList.add((Var) arg);
                terms.add((Var) arg);
            } else if (arg instanceof Term) {
                terms.add((Term) arg);
            } else if (arg instanceof Integer) {
                terms.add(it.unibo.tuprolog.core.Integer.of((Integer) arg));
            } else if (arg instanceof Long) {
                terms.add(it.unibo.tuprolog.core.Integer.of((Long) arg));
            } else if (arg instanceof Float) {
                terms.add(it.unibo.tuprolog.core.Real.of((Float) arg));
            } else if (arg instanceof Double) {
                terms.add(it.unibo.tuprolog.core.Real.of((Double) arg));
            } else {
                break;
            }
        }
        return new KbResult(solve(Struct.of(functor, terms), solveList), varList);
    }

    private Struct structOf(String functor, Object... arguments) {
        List<Term> terms = new ArrayList<>();
        for (Object value : arguments) {
            if (value == null) {
                terms.add(Empty.block());
            } else if (value instanceof Term) {
                terms.add((Term) value);
            } else if (value instanceof String) {
                terms.add(Atom.of((String) value));
            } else if (value instanceof Integer) {
                terms.add(it.unibo.tuprolog.core.Integer.of((Integer) value));
            } else if (value instanceof BpEntity) {
                terms.add(Atom.of(learn((BpEntity) value)));
            } else if (value instanceof Boolean) {
                terms.add(Truth.of((Boolean) value));
            } else if (value instanceof Long) {
                terms.add(it.unibo.tuprolog.core.Integer.of((Long) value));
            } else if (value instanceof Double) {
                terms.add(Real.of((Double) value));
            } else if (value instanceof Float) {
                terms.add(Real.of((Float) value));
            } else if (value instanceof Cell) {
                terms.add(Atom.of(KbNames.getAtom((Cell) value)));
            } else if (value instanceof Sheet) {
                terms.add(Atom.of(KbNames.getAtom((Sheet) value)));
            } else {
                throw new UnsupportedOperationException("Undefined mapping for " + value.getClass().getSimpleName());
            }
        }
        return Struct.of(functor, terms);
    }

    private String learnTypeHierarchy(Class<?> childType) {
        String childPredicate = KbNames.getFunctor(childType);
        if (learnedPredicates.contains(childPredicate)) {
            return childPredicate;
        }
        learnedPredicates.add(childPredicate);
        for (Class<?> parent : childType.getInterfaces()) {
            if (BpEntity.class.isAssignableFrom(parent)) {
                String parentPredicate = learnTypeHierarchy(parent);
                learn(Clause.of(Struct.of(parentPredicate, X), Struct.of(childPredicate, X)));
            }
            if (BpRelationship.class.isAssignableFrom(parent)) {
                String parentPredicate = learnTypeHierarchy(parent);
                learn(Clause.of(Struct.of(parentPredicate, X, Y, Z), Struct.of(childPredicate, X, Y, Z)));
            }
        }
        Class<?> parent = childType.getSuperclass();
        if (parent != null) {
            if (BpEntity.class.isAssignableFrom(parent)) {
                String parentPredicate = learnTypeHierarchy(parent);
                learn(Clause.of(Struct.of(parentPredicate, X), Struct.of(childPredicate, X)));
            }
            if (BpRelationship.class.isAssignableFrom(parent)) {
                String parentPredicate = learnTypeHierarchy(parent);
                learn(Clause.of(Struct.of(parentPredicate, X, Y, Z), Struct.of(childPredicate, X, Y, Z)));
            }
        }
        return childPredicate;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void learnFields(Class<? extends BpElement> entityType, BpElement entity) {
        Class<?> parent = entityType.getSuperclass();
        if (parent != null && BpElement.class.isAssignableFrom(parent)) {
            learnFields((Class<? extends BpElement>) parent, entity);
        }
        Atom entityKbId = Atom.of(entity.getKbId());
        for (Field field : entityType.getDeclaredFields()) {
            String predicate = KbNames.getFunctor(field);
            if (predicate != null) {
                field.setAccessible(true);
                Object value = field.get(entity);
                if (value != null) {
                    if (value instanceof Pair) {
                        Pair<?, ?> valueAsPair = (Pair<?, ?>) value;
                        learn(predicate, entityKbId, valueAsPair.getLeft(), valueAsPair.getRight());
                    } else if (value instanceof Triple) {
                        Triple<?, ?, ?> valueAsTriple = (Triple<?, ?, ?>) value;
                        learn(predicate, entityKbId, valueAsTriple.getLeft(), valueAsTriple.getMiddle(), valueAsTriple.getRight());
                    } else if (value instanceof Set) {
                        Set<?> valueAsSet = (Set<?>) value;
                        for (Object item : valueAsSet) {
                            learn(predicate, entityKbId, item);
                        }
                    } else if (value instanceof List) {
                        List<?> valueAsList = (List<?>) value;
                        for (int i = 0; i < valueAsList.size(); ++i) {
                            learn(predicate, entityKbId, i, valueAsList.get(i));
                        }
                    } else if (value instanceof Map) {
                        Map<?, ?> valueAsMap = (Map<?, ?>) value;
                        for (Map.Entry<?, ?> entry : valueAsMap.entrySet()) {
                            learn(predicate, entityKbId, entry.getKey(), entry.getValue());
                        }
                    } else {
                        learn(predicate, entityKbId, value);
                    }
                }
            }
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T extends BpElement> void recallFields(Class<T> entityType, BpElement entity) {
        Class<?> parent = entityType.getSuperclass();
        if (parent != null && BpElement.class.isAssignableFrom(parent)) {
            recallFields((Class<? extends BpElement>) parent, entity);
        }
        Atom entityKbId = Atom.of(entity.getKbId());
        for (Field field : entityType.getDeclaredFields()) {
            String predicate = KbNames.getFunctor(field);
            if (predicate != null) {
                KbFunctor kbFunctor = field.getAnnotation(KbFunctor.class);
                field.setAccessible(true);
                if (Pair.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveOnce(predicate, entityKbId, Z1, Z2).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Pair<?, ?> valueAsPair = Pair.of(
                                asType(kbFunctor.T1(), subs.get(0)[0]),
                                asType(kbFunctor.T2(), subs.get(0)[1]));
                        field.set(entity, valueAsPair);
                    }
                } else if (Triple.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveOnce(predicate, entityKbId, Z1, Z3, Z3).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Triple<?, ?, ?> valueAsTriple = Triple.of(
                                asType(kbFunctor.T1(), subs.get(0)[0]),
                                asType(kbFunctor.T2(), subs.get(0)[1]),
                                asType(kbFunctor.T3(), subs.get(0)[2]));
                        field.set(entity, valueAsTriple);
                    }
                } else if (Set.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Set<?> valueAsSet = (Set<?>) field.get(entity);
                        if (valueAsSet == null) {
                            valueAsSet = new HashSet<>();
                            field.set(entity, valueAsSet);
                        }
                        for (Term[] sub : subs) {
                            SET_ADD.invoke(valueAsSet, asType(kbFunctor.T1(), sub[0]));
                        }
                    }
                } else if (List.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, X, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        subs.sort((s1, s2) -> {
                            int i1 = NumberUtility.toIntOrZero((Integer) asType(Integer.class, s1[0]));
                            int i2 = NumberUtility.toIntOrZero((Integer) asType(Integer.class, s2[0]));
                            return Integer.compare(i1, i2);
                        });
                        List<?> valueAsList = (List<?>) field.get(entity);
                        if (valueAsList == null) {
                            valueAsList = new ArrayList<>();
                            field.set(entity, valueAsList);
                        }
                        for (Term[] sub : subs) {
                            LIST_ADD.invoke(valueAsList, asType(kbFunctor.T1(), sub[1]));
                        }
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, Y, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Map<?, ?> valueAsMap = (Map<?, ?>) field.get(entity);
                        if (valueAsMap == null) {
                            valueAsMap = new HashMap<>();
                            field.set(entity, valueAsMap);
                        }
                        for (Term[] sub : subs) {
                            MAP_PUT.invoke(valueAsMap,
                                    asType(kbFunctor.T1(), sub[0]),
                                    asType(kbFunctor.T2(), sub[1]));
                        }
                    }
                } else {
                    List<Term[]> subs = solveOnce(predicate, entityKbId, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Class<?> type = kbFunctor.T1();
                        if (type.equals(NullPointerException.class)) {
                            type = field.getType();
                        }
                        field.set(entity, asType(type, subs.get(0)[0]));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object asType(Class<?> type, Term term) {
        if (String.class.isAssignableFrom(type)) {
            return KbResult.asString(term);
        }
        if (Integer.class.isAssignableFrom(type)) {
            return KbResult.asInteger(term);
        }
        if (BpEntity.class.isAssignableFrom(type)) {
            String kbId = KbResult.asString(term);
            if (StringUtils.isNotBlank(kbId)) {
                return solveEntity((Class<? extends BpEntity>) type, kbId);
            }
        }
        if (Boolean.class.isAssignableFrom(type)) {
            return KbResult.asBoolean(term);
        }
        if (BpRelationship.class.isAssignableFrom(type)) {
            String kbId = KbResult.asString(term);
            if (StringUtils.isNotBlank(kbId)) {
                return solveRelationship((Class<? extends BpRelationship>) type, kbId);
            }
        }
        throw new UnsupportedOperationException("Undefined conversion to " + type + " from " + term);
    }

}
