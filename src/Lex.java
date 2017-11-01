import custom_exception.FAEndSetException;
import custom_exception.FAStartSetException;
import custom_exception.REFormatIncorrectException;

import java.util.Set;
import java.util.Stack;

public class Lex {
    public String REInOrder2PostOrder(String RE){
        return RE;
    }

    public FA RE2NFA(String RE) throws REFormatIncorrectException {
        if (RE.length() == 0) return null;

        RE = REInOrder2PostOrder(RE);

        Stack<FA> stack = new Stack<>();
        for (char c : RE.toCharArray()) {
            switch (c){
                case '#':
                    if (stack.size() < 2) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的两个FA取出产生新的FA
                    FA fa2 = stack.pop();
                    FA fa1 = stack.pop();
                    FANode start1 = fa1.getStart().next();
                    FANode end1 = fa2.getEnd().next();
                    fa1.getEnd().next().addOut(new FAEdge('\0', fa2.getStart().next()));

                    //把新的FA压栈
                    try {
                        stack.push(new FA(Set.of(start1), Set.of(end1)));
                    } catch (FAStartSetException e) {
                        e.printStackTrace();
                    } catch (FAEndSetException e) {
                        e.printStackTrace();
                    }
                    break;
                case '*':
                    if (stack.empty()) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的FA取出产生新的FA
                    FANode start2 = new FANode();
                    FANode end2 = new FANode();
                    start2.addOut(new FAEdge('\0', stack.peek().getStart().next()));
                    start2.addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', stack.peek().getStart().next()));
                    stack.pop();

                    //把新的FA压栈
                    try {
                        stack.push(new FA(Set.of(start2), Set.of(end2)));
                    } catch (FAStartSetException e) {
                        e.printStackTrace();
                    } catch (FAEndSetException e) {
                        e.printStackTrace();
                    }
                    break;
                case '|':
                    if (stack.size() < 2) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的两个FA取出产生新的FA
                    FANode start3 = new FANode();
                    FANode end3 = new FANode();
                    start3.addOut(new FAEdge('\0', stack.peek().getStart().next()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();
                    start3.addOut(new FAEdge('\0', stack.peek().getStart().next()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();

                    //把新的FA压栈
                    try {
                        stack.push(new FA(Set.of(start3), Set.of(end3)));
                    } catch (FAStartSetException e) {
                        e.printStackTrace();
                    } catch (FAEndSetException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    //产生FA
                    FANode start4 = new FANode();
                    FANode end4 = new FANode();
                    start4.addOut(new FAEdge(c, end4));

                    //把FA压栈
                    try {
                        stack.push(new FA(Set.of(start4), Set.of(end4)));
                    } catch (FAStartSetException e) {
                        e.printStackTrace();
                    } catch (FAEndSetException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        if (stack.size() > 1) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

        return stack.pop();
    }

    public FA NFA2DFA(FA NFA){

    }
}