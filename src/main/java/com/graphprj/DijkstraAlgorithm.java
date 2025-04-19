package com.graphprj;
import java.util.*;

public class DijkstraAlgorithm {
    private WeightedGraph graph;
    private PriorityQueue<Node> unvisitednodes;
    private Node[] ArrayOFNodes;
    private int[] parent;
    private int[] costarr;
    int sourceid;

    public DijkstraAlgorithm(WeightedGraph graph, int num_nodes, int sourceid, int[] costarr, int[] parent) {
        this.graph = graph;
        this.parent = parent;
        this.costarr = costarr;
        this.sourceid = sourceid;
        ArrayOFNodes = new Node[num_nodes];
        unvisitednodes = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));

        for (int i = 0; i < num_nodes; i++) {
            ArrayOFNodes[i] = new Node(i, Integer.MAX_VALUE);
            ArrayOFNodes[i].distance = Integer.MAX_VALUE;
            ArrayOFNodes[i].visited = false;
        }
    }

    public Node getnode(int id) {
        return ArrayOFNodes[id];
    }

    public int[] getCostarr() {
        Node currentnode = getnode(sourceid);
        currentnode.distance = 0;
        unvisitednodes.add(currentnode);

        while (!unvisitednodes.isEmpty()) {
            currentnode = unvisitednodes.poll();
            if (currentnode.visited) continue;
            currentnode.visited = true;

            int currentnodeid = currentnode.id;

            for (int i = 0; i < graph.num_nodes; i++) {
                if (graph.adjacencymatrix[currentnodeid][i] != 0 && !ArrayOFNodes[i].visited) {
                    int newdistance = ArrayOFNodes[currentnodeid].distance + graph.adjacencymatrix[currentnodeid][i];

                    if (ArrayOFNodes[i].distance > newdistance) {
                        ArrayOFNodes[i].distance = newdistance;
                        parent[i] = currentnodeid;
                        unvisitednodes.add(ArrayOFNodes[i]);
                    }
                }
            }
        }

        for (int i = 0; i < graph.num_nodes; i++) {
            costarr[i] = ArrayOFNodes[i].distance;
        }

        return costarr;
    }

    public List<Integer> shortestPath(int sourceid, int destinationid) {
        List<Integer> path = new ArrayList<>();
        int current = destinationid;

        if (ArrayOFNodes[destinationid].distance == Integer.MAX_VALUE) {
            return path;
        }

        while (current != sourceid) {
            path.add(current);
            current = parent[current];
        }

        path.add(sourceid);
        Collections.reverse(path);
        return path;
    }



    public static void main(String[] args) {

        int num_nodes = 6;

        WeightedGraph graph = new WeightedGraph(num_nodes);

        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 4);
        graph.addEdge(1, 4, 2);
        graph.addEdge(2, 4, 3);
        graph.addEdge(3, 5, 2);
        graph.addEdge(4, 3, 2);
        graph.addEdge(4,5,2);


        int[] costarr = new int[num_nodes];
        int[] parent = new int[num_nodes];

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, num_nodes, 0,costarr, parent);
        dijkstra.getCostarr();
        System.out.println("Minimum distances from node 0:");
        for (int i = 0; i < num_nodes; i++) {
            System.out.println("To node " + i + " = " + costarr[i]);
        }
        List<Integer> path = dijkstra.shortestPath(0, 5);
        System.out.println("\nShortest path from 0 to 4: " + path);
    }




}
