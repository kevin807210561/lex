import custom_exception.FAEndSetException;
import custom_exception.FAStartSetException;
import custom_exception.REFormatIncorrectException;

import java.util.*;

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
                    FANode start1 = fa1.getStart();
                    FANode end1 = fa2.getEnd().next();
                    fa1.getEnd().next().addOut(new FAEdge('\0', fa2.getStart()));

                    //把新的FA压栈
                    try {
                        Set<FANode> end = new HashSet<>();
                        end.add(end1);
                        stack.push(new FA(start1, end));
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
                    start2.addOut(new FAEdge('\0', stack.peek().getStart()));
                    start2.addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end2));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.pop();

                    //把新的FA压栈
                    try {
                        Set<FANode> end = new HashSet<>();
                        end.add(end2);
                        stack.push(new FA(start2, end));
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
                    start3.addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();
                    start3.addOut(new FAEdge('\0', stack.peek().getStart()));
                    stack.peek().getEnd().next().addOut(new FAEdge('\0', end3));
                    stack.pop();

                    //把新的FA压栈
                    try {
                        Set<FANode> end = new HashSet<>();
                        end.add(end3);
                        stack.push(new FA(start3, end));
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
                        Set<FANode> end = new HashSet<>();
                        end.add(end4);
                        stack.push(new FA(start4, end));
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

        FANode start = reflection.get(newNodes.get(0));
        Set<FANode> end = new HashSet<>();
        for (Set<FANode> newNode : newNodes) {
            if (newNode.contains(NFA.getEnd().next())) end.add(reflection.get(newNode));
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

    private boolean listContains(List<Set<FANode>> list, Set<FANode> e){
        for (Set<FANode> eIn : list) {
            if (eIn.containsAll(e) && e.containsAll(eIn)) return true;
        }
        return false;
    }
}