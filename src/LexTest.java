import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class LexTest {
    private Lex lex;

    @org.junit.Before
    public void setUp() throws Exception {
        lex = new Lex();
    }

    @Test
    public void RE2NFA() throws Exception {
//        FA nfa = lex.RE2NFA(new Token("random", "ab*|", 1, "none"));
        FA nfa = lex.MultipleREs2SingleNFA(List.of(new Token("random1", "fcv|*|", 1, "none"), new Token("random0", "abcd###", 0, "none")));
        Set<FANode> nodes = nfa.getEClosure(Set.of(nfa.getStart()));
        FA dfa = lex.NFA2DFA(nfa);

        char c = (char)-1;

        for (int i = 0; i < 10; i++) {
            System.out.println(dfa.hashCode());
        }
        System.out.println(lex.DFA2Code(dfa));


        String a = "b";
        a = a.concat("" + '\0');
        System.out.println();
    }
}