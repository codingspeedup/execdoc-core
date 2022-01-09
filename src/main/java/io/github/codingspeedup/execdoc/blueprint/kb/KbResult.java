package io.github.codingspeedup.execdoc.blueprint.kb;

import it.unibo.tuprolog.core.Atom;
import it.unibo.tuprolog.core.Term;
import it.unibo.tuprolog.core.Var;
import it.unibo.tuprolog.solve.Solution;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class KbResult extends ArrayList<Solution> {

    private final List<Var> variables;

    public KbResult() {
        this.variables = null;
    }

    public KbResult(Solution solution) {
        this(solution, null);
    }

    public KbResult(Solution solution, List<Var> variables) {
        add(solution);
        this.variables = variables;
    }

    public KbResult(Collection<? extends Solution> c) {
        this(c, null);
    }

    public KbResult(Collection<? extends Solution> c, List<Var> variables) {
        super(c);
        this.variables = variables;
    }

    public static String asString(Term term) {
        if (term == null) {
            return null;
        }
        Atom atom = term.asAtom();
        return atom == null ? null : atom.getValue();
    }

    public static Integer asInteger(Term term) {
        if (term == null) {
            return null;
        }
        it.unibo.tuprolog.core.Integer numeric = term.asInteger();
        return numeric == null ? null : numeric.getValue().toIntExact();
    }

    public static Object asNumeric(Term term) {
        if (term == null) {
            return null;
        }
        it.unibo.tuprolog.core.Numeric numeric = term.asNumeric();
        return numeric == null ? null : numeric.getValue();
    }

    public static Boolean asBoolean(Term term) {
        if (term == null) {
            return null;
        }
        it.unibo.tuprolog.core.Truth atom = term.asTruth();
        return atom == null ? null : atom.isTrue();
    }

    public List<Term[]> getSubstitutions() {
        List<Term[]> subs = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(variables)) {
            for (Solution solution : getYes()) {
                Term[] entry = new Term[variables.size()];
                for (int i = 0; i < entry.length; ++i) {
                    entry[i] = solution.getSubstitution().get(variables.get(i));
                }
                subs.add(entry);
            }
        }
        return subs;
    }

    public List<Solution> getYes() {
        return this.stream().filter(Solution::isYes).collect(Collectors.toList());
    }

}
