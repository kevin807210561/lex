import custom_exception.REFormatIncorrectException;
import data_structure.FA;
import data_structure.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, REFormatIncorrectException {
        File file = new File(System.getProperty("user.dir") + "/src/input/my_rule.l");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        List<Token> tokens = new ArrayList<>();
        String line;
        int i = 0;
        while ((line = bufferedReader.readLine()) != null){
            i++;
            tokens.add(new Token(line.split(":")[0], line.split(":")[1], i, "none"));
        }

        Lex lex = new Lex();
        FA nfa = lex.MultipleREs2SingleNFA(tokens);
        FA dfa = lex.NFA2DFA(nfa);
        System.out.println(lex.DFA2Code(dfa));;
    }
}
