package com.graphprj;
import java.util.ArrayList;
import java.util.List;

public class EdgeGraph {

    private List<Edge> graph;

    public EdgeGraph() {
        this.graph = new ArrayList<>();
    }

    public void add(int from, int to, int weight) {
        Edge e = new Edge(from, to, weight);
        graph.add(e);
    }

    public List<Edge> getGraph() {
        return graph;
    }
}

