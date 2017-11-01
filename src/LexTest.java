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

    @org.junit.Test
    public void RE2NFA() throws Exception {
        FA fa = lex.RE2NFA("ab*|");
        Set<FANode> nodes = fa.getEClosure(Set.of(fa.getStart().next()));
        System.out.println();
    }
}