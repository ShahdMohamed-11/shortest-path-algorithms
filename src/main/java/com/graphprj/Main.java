package com.graphprj;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    static int srcoption = -1;
    static int allpairsoption = -1;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.print("Enter input file name: ");
            String fileName = input.nextLine();

            int n = 0, m = 0;
            List<Edge> edgeList = new ArrayList<>();
            double[][] adjMatrix = null;
            boolean hasNegative = false;

            try (Scanner fileScanner = new Scanner(new File(fileName))) {
                n = fileScanner.nextInt();
                m = fileScanner.nextInt();

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

            System.out.println("Choose mode:");
            System.out.println("1 - Single Source Shortest Path");
            System.out.println("2 - All Pairs Shortest Paths");
            System.out.println("3 - Negative Cycle Detection");
            System.out.println("0 - Exit");

            int mode = input.nextInt();
            input.nextLine();

            if (mode == 0) {
                System.out.println("Exiting program.");
                break;
            }

            switch (mode) {
                case 1:
                    System.out.println("Choose algorithm for Single Source:");
                    System.out.println("1 - Bellman-Ford");
                    System.out.println("2 - Floyd-Warshall");
                    System.out.println("3 - Dijkstra"); //added heree

                    srcoption = input.nextInt();
                    input.nextLine();

                    System.out.print("Enter source node: ");
                    int source = input.nextInt();
                    input.nextLine();

                    int[] costarr = new int[n];
                    int[] parent = new int[n];
                    double[][] fwCosts = null;
                    Integer[][] fwPreds = null;

                    if (srcoption == 1) {
                        BellmanFord bf = new BellmanFord(n, edgeList.toArray(new Edge[0]), source, costarr, parent);
                        if (bf.getShortestDistances()) {
                            System.out.println("Shortest distances (Bellman-Ford):");
                            for (int i = 0; i < costarr.length; i++) {
                                System.out.println("To node " + i + " = " + costarr[i]);
                            }
                        } else {
                            System.out.println("Graph contains a negative-weight cycle.");
                            break;
                        }
                    } else if (srcoption == 2) {
                        fwCosts = new double[n][n];
                        fwPreds = new Integer[n][n];
                        FloydWarshall fw = new FloydWarshall(adjMatrix, fwCosts, fwPreds);
                        fw.run();
                        System.out.println("Shortest distances from node " + source + " (Floyd-Warshall):");
                        for (int j = 0; j < n; j++) {
                            System.out.printf("To node %d = %s\n", j,
                                    fwCosts[source][j] == Double.POSITIVE_INFINITY ? "INF" : fwCosts[source][j]);
                        }
                    } else if (srcoption == 3) {
                        WeightedGraph g = new WeightedGraph(n);
                        for (int u = 0; u < n; u++) {
                            for (int v = 0; v < n; v++) {
                                if (adjMatrix[u][v] != Double.POSITIVE_INFINITY && u != v) {
                                    g.addEdge(u, v, (int) adjMatrix[u][v]);
                                }
                            }
                        }

                        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g, n, source, costarr, parent);
                        dijkstra.getCostarr();

                        System.out.println("Minimum distances from node " + source + " (Dijkstra):");
                        for (int i = 0; i < n; i++) {
                            System.out.println("To node " + i + " = " + costarr[i]);
                        }
                    } else {
                        System.out.println("Invalid or unsupported option.");
                        break;
                    }

                    // === Submenu ===
                    while (true) {
                        System.out.println("\nSingle Source Submenu:");
                        System.out.println("1 - Get cost to a specific node");
                        System.out.println("2 - Get path to a specific node");
                        System.out.println("0 - Exit Submenu");
                        int sub = input.nextInt();
                        input.nextLine();

                        if (sub == 0) break;

                        System.out.print("Enter destination node: ");
                        int dest = input.nextInt();
                        input.nextLine();

                        if (sub == 1) {
                            if (srcoption == 2) {
                                System.out.println("Cost: " + fwCosts[source][dest]);
                            } else {
                                System.out.println("Cost: " + costarr[dest]);
                            }
                        } else if (sub == 2) {
                            List<Integer> path = new ArrayList<>();
                            Integer current = dest;
                            if (srcoption == 2) {
                                while (current != null && current != source) {
                                    path.add(current);
                                    current = fwPreds[source][current];
                                }
                            } else {
                                while (current != source && parent[current] != current) {
                                    path.add(current);
                                    current = parent[current];
                                }
                            }
                            path.add(source);
                            Collections.reverse(path);
                            System.out.println("Path: " + path);
                        }
                    }
                    break;

                case 2:
                    System.out.println("Choose algorithm for All Pairs:");
                    System.out.println("1 - Bellman-Ford (on every node)");
                    System.out.println("2 - Floyd-Warshall");
                    System.out.println("3 - Dijkstra (on every node)");


                    allpairsoption = input.nextInt();
                    input.nextLine();

                    double[][] allCosts = new double[n][n];
                    Integer[][] allParents = new Integer[n][n];

                    if (allpairsoption == 1) {
                        for (int s = 0; s < n; s++) {
                            int[] dist = new int[n];
                            int[] parentArr = new int[n];
                            BellmanFord bf = new BellmanFord(n, edgeList.toArray(new Edge[0]), s, dist, parentArr);
                            if (bf.getShortestDistances()) {
                                for (int i = 0; i < n; i++) {
                                    allCosts[s][i] = dist[i];
                                    allParents[s][i] = parentArr[i];
                                }
                            } else {
                                System.out.println("Negative cycle detected from node " + s);
                            }
                        }
                    } else if (allpairsoption == 2) {
                        FloydWarshall fw = new FloydWarshall(adjMatrix, allCosts, allParents);
                        boolean result = fw.run();
                        if (!result) {
                            System.out.println("Negative cycle detected.");
                            break;
                        }
                        System.out.println("All Pairs Shortest Paths (Floyd-Warshall) stored.");
                    } else if (allpairsoption == 3) {
                        for (int s = 0; s < n; s++) {
                            WeightedGraph g = new WeightedGraph(n);
                            for (int u = 0; u < n; u++) {
                                for (int v = 0; v < n; v++) {
                                    if (adjMatrix[u][v] != Double.POSITIVE_INFINITY && u != v) {
                                        g.addEdge(u, v, (int) adjMatrix[u][v]);
                                    }
                                }
                            }

                            int[] dist = new int[n];
                            int[] parentArr = new int[n];
                            DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(g, n, s, dist, parentArr);
                            dijkstra.getCostarr();

                            for (int i = 0; i < n; i++) {
                                allCosts[s][i] = dist[i];
                                allParents[s][i] = parentArr[i];
                            }
                        }
                    } else {
                        System.out.println("Invalid or unsupported option.");
                        break;
                    }

                    // === All-Pairs Submenu ===
                    while (true) {
                        System.out.println("\nAll Pairs Submenu:");
                        System.out.println("1 - Get cost from A to B");
                        System.out.println("2 - Get path from A to B");
                        System.out.println("0 - Exit Submenu");
                        int sub = input.nextInt();
                        input.nextLine();

                        if (sub == 0) break;

                        System.out.print("Enter source node: ");
                        int a = input.nextInt();
                        input.nextLine();

                        System.out.print("Enter destination node: ");
                        int b = input.nextInt();
                        input.nextLine();

                        if (sub == 1) {
                            System.out.println("Cost: " + allCosts[a][b]);
                        } else if (sub == 2) {
                            List<Integer> path = new ArrayList<>();
                            Integer current = b;
                            while (current != null && current != a) {
                                path.add(current);
                                current = allParents[a][current];
                            }
                            path.add(a);
                            Collections.reverse(path);
                            System.out.println("Path: " + path);
                        }
                    }
                    break;

                case 3:
                    System.out.println("Choose algorithm for Negative Cycle Detection:");
                    System.out.println("1 - Bellman-Ford");
                    System.out.println("2 - Floyd-Warshall");

                    int negOption = input.nextInt();
                    input.nextLine();

                    if (negOption == 1) {
                        for (int s = 0; s < n; s++) {
                            int[] dist = new int[n];
                            int[] parentArr = new int[n];
                            BellmanFord bf = new BellmanFord(n, edgeList.toArray(new Edge[0]), s, dist, parentArr);
                            if (!bf.getShortestDistances()) {
                                System.out.println("Negative cycle detected by Bellman-Ford from node " + s);
                                break;
                            }
                        }
                    } else if (negOption == 2) {
                        double[][] costs = new double[n][n];
                        Integer[][] preds = new Integer[n][n];
                        FloydWarshall fw = new FloydWarshall(adjMatrix, costs, preds);
                        if (!fw.run()) {
                            System.out.println("Negative cycle detected by Floyd-Warshall.");
                        } else {
                            System.out.println("No negative cycle detected.");
                        }
                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;

                default:
                    System.out.println("Invalid mode selection.");
            }

            System.out.println(); // Extra space
        }

        input.close();
    }
}
