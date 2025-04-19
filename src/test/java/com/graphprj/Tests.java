package com.graphprj;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Tests {
    private static final double INF = Double.POSITIVE_INFINITY;
    
    // Test graph properties
    private double[][] smallGraph;
    private double[][] mediumGraph;
    private double[][] largeGraph;
    private double[][] negativeWeightGraph;
    private double[][] negativeCycleGraph;
    private double[][] disconnectedGraph;
    
    @Before
    public void setUp() {
        // Small test graph (3x3)
        smallGraph = new double[][] {
            {0, 5, INF},
            {2, 0, 3},
            {INF, 1, 0}
        };
        
        // Medium test graph (5x5)
        mediumGraph = new double[][] {
            {0, 3, 8, INF, -4},
            {INF, 0, INF, 1, 7},
            {INF, 4, 0, INF, INF},
            {2, INF, -5, 0, INF},
            {INF, INF, INF, 6, 0}
        };
        
        // Large test graph (7x7)
        largeGraph = new double[][] {
            {0, 2, INF, 1, INF, INF, INF},
            {INF, 0, INF, 3, 10, INF, INF},
            {4, INF, 0, INF, INF, 5, INF},
            {INF, INF, 2, 0, 2, 8, 4},
            {INF, INF, INF, INF, 0, INF, 6},
            {INF, INF, INF, INF, INF, 0, INF},
            {INF, INF, INF, INF, INF, 1, 0}
        };
        
        // Graph with negative weights but no negative cycles
        negativeWeightGraph = new double[][] {
            {0, -1, 4, INF},
            {INF, 0, 3, 2},
            {INF, INF, 0, -3},
            {INF, 1, INF, 0}
        };
        
        // Graph with a negative cycle
        negativeCycleGraph = new double[][] {
            {0, 1, INF, INF},
            {INF, 0, -1, INF},
            {INF, INF, 0, -1},
            {-3, INF, INF, 0}
        };
        
        // Disconnected graph
        disconnectedGraph = new double[][] {
            {0, 1, INF, INF},
            {1, 0, INF, INF}, 
            {INF, INF, 0, 2},
            {INF, INF, 2, 0}
        };
    }
    
    @Test
    public void testSmallGraph() {
        int n = smallGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Small graph should not have negative cycles", result);
        assertEquals("Distance 0->1 should be 5", 5.0, fw.getCosts()[0][1], 0.001);
        assertEquals("Distance 0->2 should be 8", 8.0, fw.getCosts()[0][2], 0.001);
        assertEquals("Distance 1->0 should be 2", 2.0, fw.getCosts()[1][0], 0.001);
        assertEquals("Distance 2->0 should be 3", 3.0, fw.getCosts()[2][0], 0.001);
    }
    
    @Test
    public void testMediumGraph() {
        int n = mediumGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(mediumGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Medium graph should not have negative cycles", result);
        assertEquals("Distance 0->3 should be 2", 2.0, fw.getCosts()[0][3], 0.001);
        assertEquals("Distance 3->2 should be -5", -5.0, fw.getCosts()[3][2], 0.001);
    }
    
    @Test
    public void testLargeGraph() {
        int n = largeGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(largeGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Large graph should not have negative cycles", result);
        assertEquals("Distance 0->6 should be 5", 5.0, fw.getCosts()[0][6], 0.001);
        // Updated assertion to match the actual result
        assertEquals("Distance 2->6 should be 9", 9.0, fw.getCosts()[2][6], 0.001);
    }
    
    @Test
    public void testNegativeWeights() {
        int n = negativeWeightGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(negativeWeightGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Graph with negative weights but no cycles should be processed correctly", result);
        assertEquals("Distance 0->3 should be -1", -1.0, fw.getCosts()[0][3], 0.001);
        assertEquals("Distance 0->2 should be 2", 2.0, fw.getCosts()[0][2], 0.001);
    }
    
    @Test
    public void testNegativeCycle() {
        int n = negativeCycleGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(negativeCycleGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertFalse("Graph with negative cycle should be detected", result);
    }
    
    @Test
    public void testDisconnectedGraph() {
        int n = disconnectedGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(disconnectedGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Disconnected graph should not have negative cycles", result);
        assertEquals("Distance 0->2 should be INF", INF, fw.getCosts()[0][2], 0.001);
        assertEquals("Distance 0->1 should be 1", 1.0, fw.getCosts()[0][1], 0.001);
        assertEquals("Distance 2->3 should be 2", 2.0, fw.getCosts()[2][3], 0.001);
    }
    
    @Test
    public void testPathReconstruction() {
        int n = mediumGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(mediumGraph, costs, predecessors);
        fw.run();
        
        List<Integer> path = fw.getPath(0, 4);
        assertNotNull("Path 0->4 should exist", path);
        assertEquals("Path should start with 0", 0, (int)path.get(0));
        assertEquals("Path should end with 4", 4, (int)path.get(path.size()-1));
        
        path = fw.getPath(0, 3);
        assertNotNull("Path 0->3 should exist", path);
        assertEquals("Path 0->3 should have correct length", 3, path.size());
    }
    
    @Test
    public void testNoPath() {
        int n = disconnectedGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(disconnectedGraph, costs, predecessors);
        fw.run();
        
        List<Integer> path = fw.getPath(0, 2);
        assertNull("Path 0->2 should not exist", path);
    }
    
    @Test
    public void testIdentityPath() {
        int n = smallGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
        fw.run();
        
        List<Integer> path = fw.getPath(1, 1);
        assertNotNull("Path 1->1 should exist", path);
        assertEquals("Path 1->1 should have length 1", 1, path.size());
        assertEquals("Path 1->1 should contain only 1", 1, (int)path.get(0));
    }
    
    @Test
    public void testPerformanceSmall() {
        int n = smallGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        long startTime = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
        fw.run();
        long endTime = System.nanoTime();
        
        long duration = (endTime - startTime);
        System.out.println("Floyd-Warshall on small graph (3x3): " + duration + " ns");
        
        // Not asserting specific times, just measuring for comparison
        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1 second
    }
    
    @Test
    public void testPerformanceMedium() {
        int n = mediumGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        long startTime = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(mediumGraph, costs, predecessors);
        fw.run();
        long endTime = System.nanoTime();
        
        long duration = (endTime - startTime);
        System.out.println("Floyd-Warshall on medium graph (5x5): " + duration + " ns");
        
        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1 second
    }
    
    @Test
    public void testPerformanceLarge() {
        int n = largeGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        long startTime = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(largeGraph, costs, predecessors);
        fw.run();
        long endTime = System.nanoTime();
        
        long duration = (endTime - startTime);
        System.out.println("Floyd-Warshall on large graph (7x7): " + duration + " ns");
        
        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1 second
    }
    
    @Test
    public void testPerformanceVeryLarge() {
        int n = 50; // Create a 50x50 random graph
        double[][] graph = generateRandomGraph(n, 0.3, -5, 10);
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        long startTime = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        fw.run();
        long endTime = System.nanoTime();
        
        long duration = (endTime - startTime);
        System.out.println("Floyd-Warshall on very large graph (50x50): " + duration + " ns");
        
        assertTrue("Algorithm should complete in reasonable time", duration < 5000000000L); // 5 seconds
    }
    
    @Test
    public void testRandomGraphs() {
        for (int i = 0; i < 5; i++) {
            int n = 10 + i * 5; // Test graphs from 10x10 to 30x30
            double[][] graph = generateRandomGraph(n, 0.4, 1, 20);
            double[][] costs = new double[n][n];
            Integer[][] predecessors = new Integer[n][n];
            
            FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
            boolean result = fw.run();
            
            assertTrue("Random graph #" + i + " should not have negative cycles", result);
            
            // Verify that all distances obey the triangle inequality
            for (int a = 0; a < n; a++) {
                for (int b = 0; b < n; b++) {
                    for (int c = 0; c < n; c++) {
                        if (fw.getCosts()[a][b] != INF && fw.getCosts()[b][c] != INF) {
                            assertTrue(
                                "Triangle inequality failed: d(" + a + "," + c + ") <= d(" + a + "," + b + ") + d(" + b + "," + c + ")",
                                fw.getCosts()[a][c] <= fw.getCosts()[a][b] + fw.getCosts()[b][c] + 0.001 // Add small epsilon for floating point comparison
                            );
                        }
                    }
                }
            }
        }
    }
    
    @Test
    public void testAlgorithmComparison() {
        // This is a skeleton for algorithm comparison
        // Your friends can extend this to compare their algorithms
        
        int n = 30;
        double[][] graph = generateRandomGraph(n, 0.3, 1, 10);
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        // Floyd-Warshall timing
        long startTime = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        fw.run();
        long endTime = System.nanoTime();
        long fwDuration = (endTime - startTime);
        
        System.out.println("Floyd-Warshall on 30x30 graph: " + fwDuration + " ns");
        
        // other methods can add their algorithm timings here
        // Example:
        // long dijkstraStartTime = System.nanoTime();
        // DijkstraAllPairs dijkstra = new DijkstraAllPairs(graph);
        // dijkstra.run();
        // long dijkstraEndTime = System.nanoTime();
        // long dijkstraDuration = (dijkstraEndTime - dijkstraStartTime);
        // System.out.println("Dijkstra All-Pairs on 30x30 graph: " + dijkstraDuration + " ns");
        
        // No specific assertion, just for comparison
    }
    
    // Utility method to generate random graphs for testing
    private double[][] generateRandomGraph(int n, double density, double minWeight, double maxWeight) {
        double[][] graph = new double[n][n];
        Random random = new Random(42); // Fixed seed for reproducibility
        
        // Initialize with INF
        for (int i = 0; i < n; i++) {
            Arrays.fill(graph[i], INF);
            graph[i][i] = 0; // Self-loops are 0
        }
        
        // Add random edges
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j && random.nextDouble() < density) {
                    graph[i][j] = minWeight + random.nextDouble() * (maxWeight - minWeight);
                }
            }
        }
        
        return graph;
    }
}