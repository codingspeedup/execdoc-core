package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpElement;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpEntity;
import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpRelationship;
import io.github.codingspeedup.execdoc.toolbox.utilities.NumberUtility;
import it.unibo.tuprolog.core.*;
import it.unibo.tuprolog.solve.Solver;
import it.unibo.tuprolog.solve.classic.ClassicSolverFactory;
import it.unibo.tuprolog.theory.Theory;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

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

    private Solver solver;

    public BpKb() {
        learnedPredicates.add(KbNames.getFunctor(BpEntity.class));
    }

    public String listTheory() {
        String kb = getTheory().toString(true);
        return kb.replace(" :- true.", ".");
    }

    public void learn(Clause clause) {
        theory.plus(clause);
        solver = null;
    }

    public Clause learn(Object functor, Object... arguments) {
        Clause clause = Clause.of(BpKbUtils.structOf(false, functor, arguments).getLeft());
        learn(clause);
        return clause;
    }

    public Clause learn(Struct struct) {
        Clause clause = Clause.of(struct);
        learn(clause);
        return clause;
    }

    public String learn(BpEntity entity) {
        if (entity == null) {
            return null;
        }
        String functor = learnTypeHierarchy(entity.getClass());
        String kbId = BpKbUtils.ensureKbId(entity);
        Struct declaration = BpKbUtils.structOf(false, functor, Atom.of(kbId)).getLeft();
        if (!theory.contains(declaration)) {
            learn(declaration);
            learnFields(entity.getClass(), entity);
            entity.kbStore(this);
        }
        return kbId;
    }

    public String learn(BpRelationship relationship) {
        if (relationship == null) {
            return null;
        }
        if (StringUtils.isBlank(relationship.getFrom())) {
            throw new UnsupportedOperationException("Undefined source");
        }
        if (StringUtils.isBlank(relationship.getTo())) {
            throw new UnsupportedOperationException("Undefined target");
        }
        String functor = learnTypeHierarchy(relationship.getClass());
        String kbId = BpKbUtils.ensureKbId(relationship);
        Struct declaration = BpKbUtils.structOf(false, functor, Atom.of(kbId), relationship.getFrom(), relationship.getTo()).getLeft();
        if (!theory.contains(declaration)) {
            learn(declaration);
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

    public KbResult solve(boolean solveList, Struct goal) {
        if (solver == null) {
            solver = ClassicSolverFactory.INSTANCE.mutableSolverOf(theory);
        }
        if (solveList) {
            return new KbResult(solver.solveList(goal));
        }
        return new KbResult(solver.solveOnce(goal));
    }

    public KbResult solve(boolean solveList, Object... goal) {
        return solve(solveList, BpKbUtils.parseStruct(goal));
    }

    public KbResult solveList(Object functor, Object... args) {
        return solve(true, functor, args);
    }

    public KbResult solveOnce(Object functor, Object... args) {
        return solve(false, functor, args);
    }

    public <E extends BpEntity> Set<String> solveEntities(Class<E> entityType) {
        String functor = learnTypeHierarchy(entityType);
        KbResult sr = solveList(functor, X);
        return sr.getSubstitutions().stream().map(s -> KbResult.asString(s[0])).collect(Collectors.toSet());
    }

    public <E extends BpEntity> E solveEntity(Class<E> entityType, String kbId) {
        return solveEntity(new HashMap<>(), entityType, kbId);
    }

    public <E extends BpEntity> E solveEntity(Class<E> entityType) {
        Set<String> ids = solveEntities(entityType);
        if (ids.size() != 1) {
            throw new UnsupportedOperationException("Found " + ids.size() + " entities");
        }
        return solveEntity(new HashMap<>(), entityType, ids.iterator().next());
    }

    public <R extends BpRelationship> Set<Triple<String, String, String>> solveRelationships(Class<R> relationshipType) {
        KbResult solutions = solveList(relationshipType, X, Y, Z);
        return solutions.getSubstitutions().stream().map(s -> Triple.of(KbResult.asString(s[0]), KbResult.asString(s[1]), KbResult.asString(s[2]))).collect(Collectors.toSet());
    }

    public <R extends BpRelationship> R solveRelationship(Class<R> relationshipType, String kbId) {
        return solveRelationship(new HashMap<>(), relationshipType, kbId);
    }

    private KbResult solve(boolean solveList, Object functor, Object... args) {
        Pair<Struct, List<Var>> structVar = BpKbUtils.structOf(true, functor, args);
        return new KbResult(solve(solveList, structVar.getLeft()), structVar.getRight());
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
                    } else if (value instanceof BpEntity) {
                        Atom otherKbId = Atom.of(learn((BpEntity) value));
                        learn(predicate, entityKbId, otherKbId);
                    } else if (value instanceof BpRelationship) {
                        Atom otherKbId = Atom.of(learn((BpRelationship) value));
                        learn(predicate, entityKbId, otherKbId);
                    } else {
                        learn(predicate, entityKbId, value);
                    }
                }
            }
        }
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    private <E extends BpEntity> E solveEntity(Map<String, BpElement> discovered, Class<E> entityType, String kbId) {
        Struct goal = BpKbUtils.structOf(true, entityType, Atom.of(kbId)).getLeft();
        String goalKey = goal.toString();
        if (discovered.containsKey(goalKey)) {
            return (E) discovered.get(goalKey);
        }
        E entity = null;
        KbResult solution = solve(false, goal);
        if (CollectionUtils.isNotEmpty(solution.getYes())) {
            entity = entityType.getConstructor().newInstance();
            discovered.put(goalKey, entity);
            entity.setKbId(kbId);
            recallFields(discovered, entityType, entity);
            entity.kbRetrieve(this);
        }
        return entity;
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    private <R extends BpRelationship> R solveRelationship(Map<String, BpElement> discovered, Class<R> relationshipType, String kbId) {
        Pair<Struct, List<Var>> structVar = BpKbUtils.structOf(true, relationshipType, Atom.of(kbId), Y, Z);
        Struct goal = structVar.getLeft();
        String goalKey = goal.toString();
        if (discovered.containsKey(goalKey)) {
            return (R) discovered.get(goalKey);
        }
        R relationship = null;
        KbResult solution = new KbResult(solve(false, goal), structVar.getRight());
        List<Term[]> substitutions = solution.getSubstitutions();
        if (CollectionUtils.isNotEmpty(substitutions)) {
            Term[] subst = substitutions.get(0);
            relationship = relationshipType.getConstructor().newInstance();
            discovered.put(goalKey, relationship);
            relationship.setKbId(kbId);
            relationship.setFrom(KbResult.asString(subst[0]));
            relationship.setTo(KbResult.asString(subst[1]));
            recallFields(discovered, relationshipType, relationship);
            relationship.kbRetrieve(this);
        }
        return relationship;
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T extends BpElement> void recallFields(Map<String, BpElement> discovered, Class<T> entityType, BpElement entity) {
        Class<?> parent = entityType.getSuperclass();
        if (parent != null && BpElement.class.isAssignableFrom(parent)) {
            recallFields(discovered, (Class<? extends BpElement>) parent, entity);
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
                                asType(discovered, kbFunctor.T1(), subs.get(0)[0]),
                                asType(discovered, kbFunctor.T2(), subs.get(0)[1]));
                        field.set(entity, valueAsPair);
                    }
                } else if (Triple.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveOnce(predicate, entityKbId, Z1, Z2, Z3).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Triple<?, ?, ?> valueAsTriple = Triple.of(
                                asType(discovered, kbFunctor.T1(), subs.get(0)[0]),
                                asType(discovered, kbFunctor.T2(), subs.get(0)[1]),
                                asType(discovered, kbFunctor.T3(), subs.get(0)[2]));
                        field.set(entity, valueAsTriple);
                    }
                } else if (Set.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Set<?> valueAsSet = (Set<?>) field.get(entity);
                        if (valueAsSet == null) {
                            if (NullPointerException.class.equals(kbFunctor.T2())) {
                                valueAsSet = new HashSet<>();
                            } else {
                                valueAsSet = (Set<?>) kbFunctor.T2().getConstructor().newInstance();
                            }
                            field.set(entity, valueAsSet);
                        }
                        for (Term[] sub : subs) {
                            SET_ADD.invoke(valueAsSet, asType(discovered, kbFunctor.T1(), sub[0]));
                        }
                    }
                } else if (List.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, X, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        subs.sort((s1, s2) -> {
                            int i1 = NumberUtility.toIntOrZero((Integer) asType(discovered, Integer.class, s1[0]));
                            int i2 = NumberUtility.toIntOrZero((Integer) asType(discovered, Integer.class, s2[0]));
                            return Integer.compare(i1, i2);
                        });
                        List<?> valueAsList = (List<?>) field.get(entity);
                        if (valueAsList == null) {
                            if (NullPointerException.class.equals(kbFunctor.T2())) {
                                valueAsList = new ArrayList<>();
                            } else {
                                valueAsList = (List<?>) kbFunctor.T2().getConstructor().newInstance();
                            }
                            field.set(entity, valueAsList);
                        }
                        for (Term[] sub : subs) {
                            LIST_ADD.invoke(valueAsList, asType(discovered, kbFunctor.T1(), sub[1]));
                        }
                    }
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    List<Term[]> subs = solveList(predicate, entityKbId, Y, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Map<?, ?> valueAsMap = (Map<?, ?>) field.get(entity);
                        if (valueAsMap == null) {
                            if (NullPointerException.class.equals(kbFunctor.T3())) {
                                valueAsMap = new HashMap<>();
                            } else {
                                valueAsMap = (Map<?, ?>) kbFunctor.T3().getConstructor().newInstance();
                            }
                            field.set(entity, valueAsMap);
                        }
                        for (Term[] sub : subs) {
                            MAP_PUT.invoke(valueAsMap,
                                    asType(discovered, kbFunctor.T1(), sub[0]),
                                    asType(discovered, kbFunctor.T2(), sub[1]));
                        }
                    }
                } else {
                    List<Term[]> subs = solveOnce(predicate, entityKbId, Z).getSubstitutions();
                    if (CollectionUtils.isNotEmpty(subs)) {
                        Class<?> type = kbFunctor.T1();
                        if (type.equals(NullPointerException.class)) {
                            type = field.getType();
                        }
                        field.set(entity, asType(discovered, type, subs.get(0)[0]));
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object asType(Map<String, BpElement> discovered, Class<?> type, Term term) {
        if (BpEntity.class.isAssignableFrom(type)) {
            String kbId = KbResult.asString(term);
            if (StringUtils.isNotBlank(kbId)) {
                return solveEntity(discovered, (Class<? extends BpEntity>) type, kbId);
            }
        }
        if (String.class.isAssignableFrom(type)) {
            return KbResult.asString(term);
        }
        if (Integer.class.isAssignableFrom(type) || int.class.equals(type)) {
            return ((org.gciatto.kt.math.BigInteger) KbResult.asNumeric(term)).toIntExact();
        }
        if (Long.class.isAssignableFrom(type) || long.class.equals(type)) {
            return ((org.gciatto.kt.math.BigInteger) KbResult.asNumeric(term)).toLongExact();
        }
        if (Float.class.isAssignableFrom(type) || float.class.equals(type)) {
            return ((org.gciatto.kt.math.BigDecimal) KbResult.asNumeric(term)).toFloat();
        }
        if (Double.class.isAssignableFrom(type) || double.class.equals(type)) {
            return ((org.gciatto.kt.math.BigDecimal) KbResult.asNumeric(term)).toDouble();
        }
        if (Boolean.class.isAssignableFrom(type) || boolean.class.equals(type)) {
            return KbResult.asBoolean(term);
        }
        if (BpRelationship.class.isAssignableFrom(type)) {
            String kbId = KbResult.asString(term);
            if (StringUtils.isNotBlank(kbId)) {
                return solveRelationship(discovered, (Class<? extends BpRelationship>) type, kbId);
            }
        }
        throw new UnsupportedOperationException("Undefined conversion to " + type + " from " + term);
    }

}
