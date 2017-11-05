package data_structure;

public class FAEdge {
    private char c;
    private FANode node;

    public FAEdge(char c, FANode node){
        this.c = c;
        this.node = node;
    }

    public char getC() {
        return c;
    }

    public void setC(char c) {
        this.c = c;
    }

    public FANode getNode() {
        return node;
    }

    public void setNode(FANode node) {
        this.node = node;
    }
}
