package data_structure;

import custom_exception.FAEndSetException;
import custom_exception.FAStartSetException;

import java.util.*;

public class FA {
    private FANode start;
    private Set<FANode> end;

    public FA(FANode start, Set<FANode> end) throws FAStartSetException, FAEndSetException {
        if (start == null) throw new FAStartSetException();
        if (end.isEmpty()) throw new FAEndSetException();
        this.start = start;
        this.end = end;
    }

    public FANode getStart() {
        return start;
    }

    public Iterator<FANode> getEnd() {
        return end.iterator();
    }

    public Set<FANode> getEClosure(Set<FANode> nodes){
        List<FANode> nodes1 = new ArrayList<>();
        nodes1.addAll(nodes);

        for (int i = 0; i < nodes1.size(); i++) {
            Iterator<FAEdge> edgeIterator = nodes1.get(i).getOuts();
            while (edgeIterator.hasNext()){
                FAEdge edge = edgeIterator.next();
                if (edge.getC() == 0 && !nodes1.contains(edge.getNode())) nodes1.add(edge.getNode());
            }
        }

        Set<FANode> res = new LinkedHashSet<>();
        res.addAll(nodes1);
        return res;
    }
}
