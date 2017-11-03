import org.junit.Test;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class LexTest {
    private Lex lex;

    @org.junit.Before
    public void setUp() throws Exception {
        lex = new Lex();
    }

    @Test
    public void RE2NFA() throws Exception {
        FA nfa = lex.RE2NFA("ab*|");
        Set<FANode> nodes = nfa.getEClosure(Set.of(nfa.getStart()));
        FA dfa = lex.NFA2DFA(nfa);
        System.out.println();
    }

    @Test
    public void NFA2DFA() throws Exception {
    }
}