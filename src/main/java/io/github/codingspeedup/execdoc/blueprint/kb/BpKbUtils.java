package io.github.codingspeedup.execdoc.blueprint.kb;

import io.github.codingspeedup.execdoc.blueprint.kb.taxonomy.BpElement;
import io.github.codingspeedup.execdoc.toolbox.utilities.StringUtility;
import io.github.codingspeedup.execdoc.toolbox.utilities.UuidUtility;
import it.unibo.tuprolog.core.*;
import it.unibo.tuprolog.core.parsing.ParseException;
import it.unibo.tuprolog.theory.parsing.ClausesParser;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.lang.Integer;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class BpKbUtils {

    public static String ensureKbId(BpElement element) {
        if (StringUtils.isBlank(element.getKbId())) {
            element.setKbId(UuidUtility.nextUuid());
        }
        return element.getKbId();
    }

    public static List<Clause> parseClauses(Object... statement) {
        String source = null;
        if (ArrayUtils.isNotEmpty(statement)) {
            source = Arrays.stream(statement)
                    .filter(Objects::nonNull)
                    .map(word -> {
                        if (word instanceof Class) {
                            return StringUtility.simpleQuote(KbNames.getFunctor((Class<?>) word));
                        }
                        return String.valueOf(word);
                    })
                    .collect(Collectors.joining(" "));
        }
        if (StringUtils.isBlank(source)) {
            return new ArrayList<>();
        }
        if (!source.endsWith(".")) {
            source = source + ".";
        }
        try {
            return ClausesParser.getWithStandardOperators().parseClauses(source);
        } catch (ParseException pEx) {
            throw new UnsupportedOperationException("For source input:\n" + source, pEx);
        }
    }

    public static Struct parseStruct(Object... statement) {
        List<Clause> clauses = parseClauses(statement);
        if (CollectionUtils.isNotEmpty(clauses)) {
            if (clauses.size() == 1) {
                Clause clause = clauses.get(0);
                if (!clause.getBody().isTruth()) {
                    throw new UnsupportedOperationException("Statement clause has a non trivial body");
                }
                return clause.getHead();
            } else {
                throw new UnsupportedOperationException("Statement contains more than one clause");
            }
        }
        return null;
    }


    public static Pair<Struct, List<Var>> structOf(boolean varStrings, Object functor, Object... args) {
        String functorName;
        if (functor instanceof String) {
            functorName = (String) functor;
        } else if (functor instanceof Class) {
            functorName = KbNames.getFunctor((Class<?>) functor);
        } else {
            throw new UnsupportedOperationException("Unsupported functor type " + functor);
        }

        List<Var> varList = new ArrayList<>();
        Map<String, Var> varMap = new HashMap<>();

        List<Term> terms = new ArrayList<>();
        if (ArrayUtils.isNotEmpty(args)) {
            for (Object arg : args) {
                if (arg == null) {
                    throw new UnsupportedOperationException("Undefined mapping for null");
                } else if (arg instanceof String) {
                    String foo = ((String) arg).trim();
                    if (varStrings && foo.charAt(0) == Character.toUpperCase(foo.charAt(0))) {
                        Var var = varMap.computeIfAbsent(foo, Var::of);
                        varList.add(var);
                        terms.add(var);
                    } else {
                        terms.add(Atom.of(foo));
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
                } else if (arg instanceof Boolean) {
                    terms.add(Truth.of((Boolean) arg));
                } else if (arg instanceof Cell) {
                    terms.add(Atom.of(KbNames.getAtom((Cell) arg)));
                } else if (arg instanceof Sheet) {
                    terms.add(Atom.of(KbNames.getAtom((Sheet) arg)));
                } else if (arg instanceof BpElement) {
                    String kbId = ((BpElement) arg).getKbId();
                    if (StringUtils.isBlank(kbId)) {
                        throw new UnsupportedOperationException("Entity kbId is not set");
                    }
                    terms.add(Atom.of(kbId));
                } else {
                    throw new UnsupportedOperationException("Undefined mapping for " + arg.getClass().getName());
                }
            }
        }

        return Pair.of(Struct.of(functorName, terms), varList);
    }

}
