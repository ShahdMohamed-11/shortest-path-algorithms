import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FloydWarshall {
    private double[][] costs;
    private Integer[][] predecessors;
    private int n;
    private static final double INF = Double.POSITIVE_INFINITY;

    public FloydWarshall(double[][] graph, double[][] costs, Integer[][] predecessors) {
        this.n = graph.length;
        this.costs = costs;
        this.predecessors = predecessors;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.costs[i][j] = graph[i][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && graph[i][j] != INF) {
                    this.predecessors[i][j] = i;
                }
            }
        }
    }

    public boolean run() {

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (costs[i][k] + costs[k][j] < costs[i][j]) {
                        costs[i][j] = costs[i][k] + costs[k][j];
                        predecessors[i][j] = predecessors[k][j];
                    }
                }
            }
        }


        for (int i = 0; i < n; i++) {
            if (costs[i][i] < 0) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getPath(int i, int j) {
        if (costs[i][j] == INF) {
            return null;
        }

        List<Integer> path = new ArrayList<>();
        reconstructPath(i, j, path);
        return path;
    }

    private void reconstructPath(int i, int j, List<Integer> path) {
        if (i == j) {
            path.add(i);
        } else if (predecessors[i][j] == null) {
            path.add(i);
            path.add(j);
        } else {
            reconstructPath(i, predecessors[i][j], path);
            path.add(j);
        }
    }

    public double[][] getCosts() {
        return costs;
    }

    public Integer[][] getPredecessors() {
        return predecessors;
    }

    public static void main(String[] args) {

       Scanner scanner = new Scanner(System.in);

        int V = scanner.nextInt();
        int E = scanner.nextInt();


        double[][] graph = new double[V][V];
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                graph[i][j] = (i == j) ? 0 : FloydWarshall.INF;
            }
        }


        for (int i = 0; i < E; i++) {
            int src = scanner.nextInt();
            int dest = scanner.nextInt();
            double weight = scanner.nextDouble();
            graph[src][dest] = weight;
        }

        scanner.close();


        double[][] costs = new double[V][V];
        Integer[][] predecessors = new Integer[V][V];


        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        boolean result = fw.run();


        if (result) {
            System.out.println("No negative cycles found.");
            System.out.println("Shortest path distances:");
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {
                    System.out.printf("%5s ", costs[i][j] == FloydWarshall.INF ? "INF" : costs[i][j]);
                }
                System.out.println();
            }

            System.out.println("\nShortest path from 3 to 1:");
            List<Integer> path = fw.getPath(0, 3);
            if (path != null) {
                System.out.println(path);
            } else {
                System.out.println("No path exists");
            }
        } else {
            System.out.println("Negative cycle detected in the graph.");
        }
    }

}