import custom_exception.FAEndSetException;
import custom_exception.FAStartSetException;
import custom_exception.REFormatIncorrectException;

import java.util.*;

public class Lex {
    public FA MultipleREs2SingleNFA(List<Token> tokens) throws REFormatIncorrectException {
        List<FA> NFAs = new ArrayList<>();
        for (Token token : tokens) {
            NFAs.add(RE2NFA(token));
        }

        FANode start = new FANode();
        Set<FANode> end = new HashSet<>();
        for (FA NFA : NFAs) {
            start.addOut(new FAEdge('\0', NFA.getStart()));
            Iterator<FANode> nodeIterator = NFA.getEnd();
            while (nodeIterator.hasNext()){
                end.add(nodeIterator.next());
            }
        }
        try {
            return new FA(start, end);
        } catch (FAStartSetException e) {
            e.printStackTrace();
        } catch (FAEndSetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FA RE2NFA(Token token) throws REFormatIncorrectException {
        if (token.getPattern().length() == 0) return null;

        String RE = REInOrder2PostOrder(token.getPattern());

        Stack<FA> stack = new Stack<>();
        for (char c : RE.toCharArray()) {
            FANode start;
            Set<FANode> end = new HashSet<>();
            switch (c){
                case '#':
                    if (stack.size() < 2) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的两个FA取出产生新的FA
                    FA fa2 = stack.pop();
                    FA fa1 = stack.pop();
                    start = fa1.getStart();
                    FANode end1 = fa2.getEnd().next();
                    fa1.getEnd().next().addOut(new FAEdge('\0', fa2.getStart()));
                    end.add(end1);
                    break;
                case '*':
                    if (stack.empty()) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的FA取出产生新的FA
                    start = new FANode();
                    FANode end2 = new FANode();
                    start.addOut(new FAEdge('\0', stack.peek().getStart()));
                    start.addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.pop();
                    end.add(end2);
                    break;
                case '|':
                    if (stack.size() < 2) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

                    //把栈顶的两个FA取出产生新的FA
                    start = new FANode();
                    FANode end3 = new FANode();
                    start.addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();
                    start.addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();
                    end.add(end3);
                    break;
                default:
                    //产生FA
                    start = new FANode();
                    FANode end4 = new FANode();
                    start.addOut(new FAEdge(c, end4));
                    end.add(end4);
                    break;
            }
            //把新的FA压栈
            try {
                stack.push(new FA(start, end));
            } catch (FAStartSetException e) {
                e.printStackTrace();
            } catch (FAEndSetException e) {
                e.printStackTrace();
            }
        }

        if (stack.size() > 1) throw new REFormatIncorrectException("Please check the content of " + RE + ".");

        stack.peek().getEnd().next().setToken(token);
        stack.peek().getEnd().next().setEnd(true);
        return stack.pop();
    }

    public FA NFA2DFA(FA NFA){
        List<Set<FANode>> newNodes = new ArrayList<>();
        newNodes.add(NFA.getEClosure(Set.of(NFA.getStart())));
        Map<Set<FANode>, FANode> reflection = new HashMap<>();
        reflection.put(newNodes.get(0), new FANode());

        //遍历新状态表
        for (int i = 0; i < newNodes.size(); i++) {
            Set<FANode> currentNode = newNodes.get(i);

            //找出当前新状态的所有下一状态
            Map<Character, Set<FANode>> newOuts = new HashMap<>();
            for (FANode node : currentNode) {
                Iterator<FAEdge> edgeIterator = node.getOuts();
                while (edgeIterator.hasNext()){
                    FAEdge edge = edgeIterator.next();
                    if (edge.getC() != 0){ //排除E边
                        if (newOuts.containsKey(edge.getC())){
                            newOuts.get(edge.getC()).add(edge.getNode());
                        }else {
                            Set<FANode> nodes = new HashSet<>();
                            nodes.add(edge.getNode());
                            newOuts.put(edge.getC(), nodes);
                        }
                    }
                }
            }
            for (Character c : newOuts.keySet()) {
                newOuts.put(c, NFA.getEClosure(newOuts.get(c)));
            }

            //将新的状态加入新状态表
            for (Set<FANode> newNode : newOuts.values()) {
                if (!listContains(newNodes, newNode)) {
                    newNodes.add(newNode);
                    reflection.put(newNode, new FANode());
                }
            }

            //记录当前新状态的所有出边
            FANode node = reflection.get(currentNode);
            for (Character c : newOuts.keySet()) {
                node.addOut(new FAEdge(c, reflection.get(newOuts.get(c))));
            }
        }

        //确定初始状态和接受状态
        FANode start = reflection.get(newNodes.get(0));
        Set<FANode> end = new HashSet<>();
        for (Set<FANode> newNode : newNodes) {
            for (FANode node : newNode) {
                if (node.isEnd()){
                    //确定接受状态对应的token, 挑选在.l文件中最先出现的token
                    FANode tokenNode = new FANode();
                    for (FANode node1 : newNode) {
                        if (node1.getTokenPriority() < tokenNode.getTokenPriority()){
                            tokenNode = node1;
                        }
                    }

                    FANode endNode = reflection.get(newNode);
                    endNode.setTokenName(tokenNode.getTokenName());
                    endNode.setTokenPriority(tokenNode.getTokenPriority());
                    endNode.setEnd(true);
                    end.add(endNode);

                    break;
                }
            }
        }
        try {
            return new FA(start, end);
        } catch (FAStartSetException e) {
            e.printStackTrace();
        } catch (FAEndSetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public FA DFA2DFA0(FA DFA){
        return null;
    }

    public String DFA2Code(FA DFA){
        String s = "case :\n" +
                "    if(c == ''){\n" +
                "\n" +
                "        }\n" +
                "\n" +
                "    break;";
        return null;
    }

    public String REInOrder2PostOrder(String RE){
        return RE;
    }

    private boolean listContains(List<Set<FANode>> list, Set<FANode> e){
        for (Set<FANode> eIn : list) {
            if (eIn.containsAll(e) && e.containsAll(eIn)) return true;
        }
        return false;
    }
}