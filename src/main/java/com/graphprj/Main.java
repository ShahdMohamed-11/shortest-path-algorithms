package com.graphprj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Load graph from file
            System.out.print("Enter input file name: ");
            String fileName = input.nextLine();

            int n = 0, m = 0;
            List<Edge> edgeList = new ArrayList<>();
            double[][] adjMatrix = null;
            boolean hasNegative = false;

            try (Scanner fileScanner = new Scanner(new File(fileName))) {
                n = fileScanner.nextInt(); // number of nodes
                m = fileScanner.nextInt(); // number of edges

                adjMatrix = new double[n][n];
                for (int i = 0; i < n; i++) {
                    Arrays.fill(adjMatrix[i], Double.POSITIVE_INFINITY);
                    adjMatrix[i][i] = 0;
                }

                for (int i = 0; i < m; i++) {
                    int u = fileScanner.nextInt();
                    int v = fileScanner.nextInt();
                    int w = fileScanner.nextInt();

                    edgeList.add(new Edge(u, v, w));
                    adjMatrix[u][v] = w;

                    if (w < 0) {
                        hasNegative = true;
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println("File not found.");
                continue;
            }

            System.out.println("Negative edge detected: " + hasNegative);
            System.out.println("Choose algorithm:");
            if (hasNegative) {
                System.out.println("1 - Bellman-Ford");
                System.out.println("2 - Floyd-Warshall");
            } else {
                System.out.println("1 - Bellman-Ford");
                System.out.println("2 - Floyd-Warshall");
                System.out.println("3 - Dijkstra");
            }
            System.out.println("0 - Exit");

            int choice = input.nextInt();
            input.nextLine(); // consume newline

            switch (choice) {
                case 1: // Bellman-Ford
                System.out.print("Enter source node: ");
                int sourceBF = input.nextInt();
                input.nextLine(); // consume newline
            
                int[] bfDist = new int[n];
                int[] bfParent = new int[n];
            
                BellmanFord bf = new BellmanFord(n, edgeList.toArray(new Edge[0]), sourceBF, bfDist, bfParent);
            
                if (bf.getShortestDistances()) {
                    System.out.println("Shortest distances (Bellman-Ford):");
                    for (int i = 0; i < bfDist.length; i++) {
                        System.out.println("To node " + i + " = " + bfDist[i]);
                    }
                } else {
                    System.out.println("Graph contains a negative-weight cycle.");
                }
                break;

                case 2: // Floyd-Warshall
                    double[][] costs = new double[n][n];
                    Integer[][] preds = new Integer[n][n];
                    FloydWarshall fw = new FloydWarshall(adjMatrix, costs, preds);
                    boolean result = fw.run();
                    if (!result) {
                        System.out.println("Negative cycle detected in Floyd-Warshall.");
                        break;
                    }

                    System.out.println("Shortest path distances (Floyd-Warshall):");
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            System.out.printf("%5s ", fw.costs[i][j] == Double.POSITIVE_INFINITY ? "INF" : fw.costs[i][j]);
                        }
                        System.out.println();
                    }
                    break;

                case 3: // Dijkstra
                    if (hasNegative) {
                        System.out.println("Dijkstra is not suitable for graphs with negative weights.");
                        break;
                    }

                    System.out.print("Enter source node: ");
                    int sourceD = input.nextInt();
                    input.nextLine(); // consume newline

                    WeightedGraph g = new WeightedGraph(n);
                    for (int u = 0; u < n; u++) {
                        for (int v = 0; v < n; v++) {
                            if (adjMatrix[u][v] != Double.POSITIVE_INFINITY && u != v) {
                                g.addEdge(u, v, (int) adjMatrix[u][v]);
                            }
                        }
                    }

                    int[] costarr = new int[n];
                    int[] parent = new int[n];
                    DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g, n, sourceD, costarr, parent);
                    dijkstra.getCostarr();

                    System.out.println("Minimum distances from node " + sourceD + " (Dijkstra):");
                    for (int i = 0; i < n; i++) {
                        System.out.println("To node " + i + " = " + costarr[i]);
                    }
                    break;

                case 0:
                    exit = true;
                    System.out.println("Exiting program.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

            System.out.println(); // extra line for readability
        }

        input.close();
    }
}
