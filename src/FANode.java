import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FANode {
    private List<FAEdge> outs;
    private String tokenName;
    private int tokenPriority;
    private boolean isEnd;

    public FANode(){
        this.outs = new ArrayList<>();
        tokenName = null;
        tokenPriority = Integer.MAX_VALUE;
        isEnd = false;
    }

    public FANode(List<FAEdge> outs) {
        this.outs = outs;
    }

    public void addOut(FAEdge out) {
        this.outs.add(out);
    }

    public Iterator<FAEdge> getOuts(){
        return outs.iterator();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setToken(Token token){
        this.tokenName = token.getName();
        this.tokenPriority = token.getPriority();
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public int getTokenPriority() {
        return tokenPriority;
    }

    public void setTokenPriority(int tokenPriority) {
        this.tokenPriority = tokenPriority;
    }
}