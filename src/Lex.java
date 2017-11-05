import custom_exception.FAEndSetException;
import custom_exception.FAStartSetException;
import custom_exception.REFormatIncorrectException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public String DFA2Code(FA DFA){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("import java.io.BufferedReader;\n" +
                "import java.io.FileNotFoundException;\n" +
                "import java.io.FileReader;\n" +
                "import java.io.IOException;\n" +
                "import java.util.List;\n" +
                "import java.util.Scanner;\n" +
                "import java.util.stream.Collectors;\n" +
                "\n" +
                "public class LexicalAnalyser {\n" +
                "    private List<String> input;\n" +
                "    private int startState;\n" +
                "    private int currentState;\n" +
                "    private boolean isBegin;\n" +
                "    private int i;\n" +
                "    private int j;\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        while (true) {\n" +
                "            try {\n" +
                "                System.out.println(\"Please input the path of the file to be analysied:\");\n" +
                "                BufferedReader fileReader = new BufferedReader(new FileReader(new Scanner(System.in).nextLine()));\n" +
                "                LexicalAnalyser lexicalAnalyser = new LexicalAnalyser(fileReader.lines().collect(Collectors.toList()), ");
        stringBuilder.append(DFA.getStart().hashCode());
        stringBuilder.append(");\n" +
                "                lexicalAnalyser.getToken();\n" +
                "            } catch (FileNotFoundException e) {\n" +
                "                System.out.println(\"File input invalid.\");\n" +
                "            } catch (IOException e) {\n" +
                "                System.out.println(\"An I/O error happened.\");\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public LexicalAnalyser(List<String> input, int startState) {\n" +
                "        this.input = input;\n" +
                "        for (int i = 0; i < input.size() - 1; i++) {\n" +
                "            this.input.set(i, this.input.get(i).concat(\"\" + '\\n'));\n" +
                "        }\n" +
                "        this.input.set(this.input.size() - 1, this.input.get(this.input.size() - 1).concat(\"\" + '\\0'));\n" +
                "        this.startState = startState;\n" +
                "        this.currentState = startState;\n" +
                "        this.isBegin = true;\n" +
                "        i = 0;\n" +
                "        j = 0;\n" +
                "    }\n" +
                "\n" +
                "    public void getToken() throws IOException {\n" +
                "        int c;\n" +
                "        for (i = 0; i < input.size(); i++) {\n" +
                "            for (j = 0; j < input.get(i).length(); j++) {\n" +
                "                c = input.get(i).charAt(j);\n" +
                "                switch (currentState) {");
        stringBuilder.append(DFA2CodeHelper(DFA.getStart(), true));
        stringBuilder.append("}\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    private void fail() throws IOException {\n" +
                "        System.out.println();\n" +
                "        System.out.print(\"An error occurred. Please check the content near \\\"\" + input.get(i).substring(j, Math.min(j + 20, input.get(i).length())) + \"\\\" at line \" + (i + 1) + \".\");\n" +
                "        System.out.println();\n" +
                "        System.exit(-1);\n" +
                "    }\n" +
                "\n" +
                "    private void accept(String tokenName) throws IOException {\n" +
                "        System.out.println(\"<\" + tokenName + \"> \");\n" +
                "        currentState = startState;\n" +
                "        isBegin = true;\n" +
                "        if (i != input.size() - 1 || j != input.get(i).length() - 1)j--;\n" +
                "    }\n" +
                "}");

        File file = new File("LexicalAnalyser.java");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(stringBuilder.toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String DFA2CodeHelper(FANode node, boolean isBegin){
        if (node == null || node.isScanned())
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("case ");
        stringBuilder.append(node.hashCode());
        stringBuilder.append(":");
        Iterator<FAEdge> edgeIterator = node.getOuts();
        while (edgeIterator.hasNext()){
            FAEdge edge = edgeIterator.next();
            stringBuilder.append("if(c == '");
            stringBuilder.append(edge.getC());
            stringBuilder.append("'){ currentState = ");
            stringBuilder.append(edge.getNode().hashCode());
            stringBuilder.append(";isBegin = false;}else ");
        }
        if (isBegin && node.isEnd()){
            if (node.getOuts().hasNext()) stringBuilder.append("{");
            //accept
            stringBuilder.append("if(isBegin) fail();else ");
            stringBuilder.append("accept(\"");
            stringBuilder.append(node.getTokenName());
            stringBuilder.append("\");");
            if (node.getOuts().hasNext()) stringBuilder.append("}");
        }else if (node.isEnd()){
            if (node.getOuts().hasNext()) stringBuilder.append("{");
            //accept
            stringBuilder.append("accept(\"");
            stringBuilder.append(node.getTokenName());
            stringBuilder.append("\");");
            if (node.getOuts().hasNext()) stringBuilder.append("}");
        }else {
            if (node.getOuts().hasNext()) stringBuilder.append("{");
            //fail
            stringBuilder.append("fail();");
            if (node.getOuts().hasNext()) stringBuilder.append("}");
        }
        stringBuilder.append("break;");
        node.setScanned(true);
        System.out.println(node.hashCode());

        Iterator<FAEdge> edges = node.getOuts();
        while (edges.hasNext()){
            stringBuilder.append(DFA2CodeHelper(edges.next().getNode(), false));
        }

        return stringBuilder.toString();
    }

    private String REInOrder2PostOrder(String RE){
        return RE;
    }

    private boolean listContains(List<Set<FANode>> list, Set<FANode> e){
        for (Set<FANode> eIn : list) {
            if (eIn.containsAll(e) && e.containsAll(eIn)) return true;
        }
        return false;
    }
}