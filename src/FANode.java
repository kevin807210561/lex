import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FANode {
    private List<FAEdge> outs;

    public FANode(){
        this.outs = new ArrayList<>();
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
}