package com.graphprj;

import java.util.ArrayList;
import java.util.List;

public class FloydWarshall {
    public double[][] costs;
    public Integer[][] predecessors;
    private int n;
    private static final double INF = Double.POSITIVE_INFINITY;

    public FloydWarshall(double[][] graph, double[][] costs, Integer[][] predecessors) {
        this.n = graph.length;
        this.costs = costs;
        this.predecessors = predecessors;

        // Initialize cost matrix with input graph values
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.costs[i][j] = graph[i][j];
                
                // Initialize predecessors matrix
                if (i != j && graph[i][j] != INF) {
                    this.predecessors[i][j] = i;
                } else {
                    this.predecessors[i][j] = null;
                }
            }
        }
    }

    public boolean run() {
        // Main Floyd-Warshall algorithm
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (costs[i][k] != INF && costs[k][j] != INF) {
                        double newDistance = costs[i][k] + costs[k][j];
                        if (newDistance < costs[i][j]) {
                            costs[i][j] = newDistance;
                            predecessors[i][j] = predecessors[k][j];
                        }
                    }
                }
            }
        }

        // Check for negative cycles
        for (int i = 0; i < n; i++) {
            if (costs[i][i] < 0) {
                return false;  // Negative cycle detected
            }
        }
        
        return true;  // No negative cycles
    }

    public List<Integer> getPath(int start, int end) {
        // Check if there's a path
        if (costs[start][end] == INF) {
            return null;
        }
        
        // Self-loop case
        if (start == end) {
            List<Integer> path = new ArrayList<>();
            path.add(start);
            return path;
        }
        
        // Reconstruct the path
        List<Integer> path = new ArrayList<>();
        path.add(start);
        reconstructPath(start, end, path);
        return path;
    }

    private void reconstructPath(int start, int end, List<Integer> path) {
        if (predecessors[start][end] == null) {
            // Direct edge
            path.add(end);
            return;
        }
        
        int intermediate = predecessors[start][end];
        
        if (intermediate == start) {
            // Direct edge
            path.add(end);
        } else {
            // Path goes through intermediate node
            // Remove the start node if it's the last element (to avoid duplicates)
            if (!path.isEmpty() && path.get(path.size() - 1) == start) {
                path.remove(path.size() - 1);
            }
            
            // Add start if path is empty
            if (path.isEmpty()) {
                path.add(start);
            }
            
            // First segment: start -> intermediate
            reconstructPath(start, intermediate, path);
            
            // Second segment: intermediate -> end
            reconstructPath(intermediate, end, path);
        }
    }

    public double[][] getCosts() {
        return costs;
    }

    public Integer[][] getPredecessors() {
        return predecessors;
    }

    public static void main(String[] args) {
        // Example graph (adjacency matrix)
        double[][] graph = {
            {0, 2, INF, 1, INF, INF, INF},
            {INF, 0, INF, 3, 10, INF, INF},
            {4, INF, 0, INF, INF, 5, INF},
            {INF, INF, 2, 0, 2, 8, 4},
            {INF, INF, INF, INF, 0, INF, 6},
            {INF, INF, INF, INF, INF, 0, INF},
            {INF, INF, INF, INF, INF, 1, 0}
        };

        int n = graph.length;
        
        // Initialize empty matrices
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        boolean result = fw.run();

        if (result) {
            System.out.println("No negative cycles found.");
            System.out.println("Shortest path distances:");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.printf("%5s ", fw.costs[i][j] == INF ? "INF" : fw.costs[i][j]);
                }
                System.out.println();
            }

            // Print paths between all pairs
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j && fw.costs[i][j] != INF) {
                        List<Integer> path = fw.getPath(i, j);
                        System.out.println("Path from " + i + " to " + j + ": " + path);
                    }
                }
            }

            System.out.println("Shortest path distances (Floyd-Warshall):");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.printf("%5s ", fw.costs[i][j] == Double.POSITIVE_INFINITY ? "INF" : fw.costs[i][j]);
                }
                System.out.println();
            }

        } else {
            System.out.println("Negative cycle detected in the graph.");
        }
    }
}