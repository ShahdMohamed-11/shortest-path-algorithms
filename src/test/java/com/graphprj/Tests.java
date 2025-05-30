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
    public void FloydSmallGraph() {
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
    public void FloydMediumGraph() {
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
    public void FloydLargeGraph() {
        int n = largeGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(largeGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertTrue("Large graph should not have negative cycles", result);
        assertEquals("Distance 0->6 should be 5", 5.0, fw.getCosts()[0][6], 0.001);
        assertEquals("Distance 2->6 should be 9", 9.0, fw.getCosts()[2][6], 0.001);
    }
    
    @Test
    public void FloydNegativeWeights() {
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
    public void FloydNegativeCycle() {
        int n = negativeCycleGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(negativeCycleGraph, costs, predecessors);
        boolean result = fw.run();
        
        assertFalse("Graph with negative cycle should be detected", result);
    }
    
    @Test
    public void FloydDisconnectedGraph() {
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
    public void FloydPathReconstruction() {
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
    public void FloydNoPath() {
        int n = disconnectedGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];
        
        FloydWarshall fw = new FloydWarshall(disconnectedGraph, costs, predecessors);
        fw.run();
        
        List<Integer> path = fw.getPath(0, 2);
        assertNull("Path 0->2 should not exist", path);
    }
    
    @Test
    public void FloydIdentityPath() {
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
    public void FloydWarshallCompare1() {
    double[][] smallGraph = {
        {0,   10,  INF, 5,   INF},
        {INF, 0,   1,   2,   INF},
        {INF, INF, 0,   INF, 4},
        {INF, 3,   9,   0,   2},
        {7,   INF, 6,   INF, 0}
    };

    int n = smallGraph.length;
    double[][] costs = new double[n][n];
    Integer[][] predecessors = new Integer[n][n];

    FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
    boolean result = fw.run();

    assertTrue("Graph should not have negative weight cycles jjj", result);
    assertEquals("Distance 0->1 should be 8", 8.0, fw.getCosts()[0][1], 0.001);
    assertEquals("Distance 0->2 should be 9", 9.0, fw.getCosts()[0][2], 0.001);
    assertEquals("Distance 1->0 should be 11", 11.0, fw.getCosts()[1][0], 0.001);
    assertEquals("Distance 2->0 should be 11", 11.0, fw.getCosts()[2][0], 0.001);
    }
    @Test
    public void FloydPerformanceCompare1() {

        double[][] smallGraph = {
            {0,   10,  INF, 5,   INF},
            {INF, 0,   1,   2,   INF},
            {INF, INF, 0,   INF, 4},
            {INF, 3,   9,   0,   2},
            {7,   INF, 6,   INF, 0}
        };

        int n = smallGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        long start = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
        fw.run();
        long end = System.nanoTime();

        long duration = end - start;
        System.out.println("Floyd-Warshall on 5 nodes: " + duration + " ns");

        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1s
    }

    @Test
    public void FloydWarshallCompare2() {
        double INF = Double.POSITIVE_INFINITY;

        double[][] smallGraph = {
            // 0     1     2     3     4     5     6
            { 0,    1,   INF,  INF,  INF,  INF,  INF }, // 0
            { INF,  0,    3,    2,    1,   INF,  INF }, // 1
            { INF, INF,  0,   INF,    4,   INF,  INF }, // 2
            { INF, INF, INF,   0,     2,   INF,  INF }, // 3
            { INF, INF, INF,  INF,    0,    3,   INF }, // 4
            { INF, INF, INF,  INF,  INF,    0,   INF }, // 5
            { INF, INF, INF,   1,   INF,  INF,    0   }  // 6
        };

        int n = smallGraph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        FloydWarshall fw = new FloydWarshall(smallGraph, costs, predecessors);
        boolean result = fw.run();

        assertTrue("Graph should not have negative weight cycles", result);
        assertEquals("Distance 0->4 should be 2", 2.0, fw.getCosts()[0][4], 0.001);
        assertEquals("Distance 0->5 should be 5", 5.0, fw.getCosts()[0][5], 0.001);
        assertEquals("Distance 6->4 should be 3", 3.0, fw.getCosts()[6][4], 0.001);
        assertEquals("Distance 1->5 should be 4", 4.0, fw.getCosts()[1][5], 0.001);
    }
    @Test
    public void FloydPerformanceCompare2() {

        double[][] graph = {
            { 0,    1,   INF,  INF,  INF,  INF,  INF },
            { INF,  0,    3,    2,    1,   INF,  INF },
            { INF, INF,  0,   INF,    4,   INF,  INF },
            { INF, INF, INF,   0,     2,   INF,  INF },
            { INF, INF, INF,  INF,    0,    3,   INF },
            { INF, INF, INF,  INF,  INF,    0,   INF },
            { INF, INF, INF,   1,   INF,  INF,    0 }
        };

        int n = graph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        long start = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        fw.run();
        long end = System.nanoTime();

        long duration = end - start;
        System.out.println("Floyd-Warshall on 7 nodes: " + duration + " ns");

        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1s
    }
    
    @Test
    public void FloydWarshallCompare3() {
        double INF = Double.POSITIVE_INFINITY;

        double[][] graph = {
            { 0,    1,    5,   INF,  INF,  INF,  INF,  2 },  // 0
            { INF,  0,    2,    2,    1,   INF,  INF,  INF },  // 1
            { INF, INF,   0,    1,    2,   INF,  INF,  INF },  // 2
            { INF, INF, INF,    0,    1,    3,   INF,  INF },  // 3
            { INF, INF, INF,   INF,   0,    1,    2,   INF },  // 4
            { INF, INF, INF,   INF,  INF,    0,   INF,    4 },  // 5
            { INF, INF, INF,   INF,   INF,   INF,    0,    1 },  // 6
            { 2,   INF, INF,   INF,   INF,   4,    1,    0 }   // 7
        };

        int n = graph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        boolean result = fw.run();

        assertTrue("Graph should not have negative weight cycles", result);
        assertEquals("Distance 0->4 should be 2", 2.0, fw.getCosts()[0][4], 0.001);
        assertEquals("Distance 0->5 should be 3", 3.0, fw.getCosts()[0][5], 0.001);
        assertEquals("Distance 6->4 should be 5", 5.0, fw.getCosts()[6][4], 0.001);
        assertEquals("Distance 1->7 should be 4", 4.0, fw.getCosts()[1][7], 0.001);
    }

    @Test
    public void FloydPerformanceCompare3() {
        double INF = Double.POSITIVE_INFINITY;

        double[][] graph = {
            { 0,    1,    5,   INF,  INF,  INF,  INF,  2 },
            { INF,  0,    2,    2,    1,   INF,  INF,  INF },
            { INF, INF,   0,    1,    2,   INF,  INF,  INF },
            { INF, INF, INF,    0,    1,    3,   INF,  INF },
            { INF, INF, INF,   INF,   0,    1,    2,   INF },
            { INF, INF, INF,   INF,  INF,    0,   INF,    4 },
            { INF, INF, INF,   INF,   INF,   INF,    0,    1 },
            { 2,   INF, INF,   INF,   INF,   4,    1,    0 }
        };

        int n = graph.length;
        double[][] costs = new double[n][n];
        Integer[][] predecessors = new Integer[n][n];

        long start = System.nanoTime();
        FloydWarshall fw = new FloydWarshall(graph, costs, predecessors);
        fw.run();
        long end = System.nanoTime();

        long duration = end - start;
        System.out.println("Floyd-Warshall on 8 nodes: " + duration + " ns");

        assertTrue("Algorithm should complete in reasonable time", duration < 1000000000); // 1s
    }


    @Test
    public void FloydPerformanceSmall() {
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
    public void FloydPerformanceMedium() {
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
    public void FloydPerformanceLarge() {
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
    public void BellmanSimpleGraph() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,5);
        edges.add(1,2,3);
        int[] cost = new int[3];
        int[] parent = new int[3];

        BellmanFord b = new BellmanFord(3, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(0, cost[0]);
        assertEquals(5, cost[1]);
        assertEquals(8, cost[2]);
    }

    @Test
    public void BellmanNegativeEdgeNoCycle() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,4);
        edges.add(1,2,-2);
        edges.add(2,3,3);
        int[] cost = new int[4];
        int[] parent = new int[4];

        BellmanFord b = new BellmanFord(4, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(5, cost[3]);
    }

    @Test
    public void BellmanNegativeCycle() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,1);
        edges.add(1,2,-1);
        edges.add(2,0,-1);
        int[] cost = new int[3];
        int[] parent = new int[3];

        BellmanFord b = new BellmanFord(3, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertFalse(result);
    }

    @Test
    public void BellmanDisconnectedNode() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,2);
        int[] cost = new int[3];
        int[] parent = new int[3];

        BellmanFord b = new BellmanFord(3, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(Integer.MAX_VALUE, cost[2]);
    }

    @Test
    public void BellmanSingleNode() {
        EdgeGraph edges = new EdgeGraph();
        int[] cost = new int[1];
        int[] parent = new int[1];

        BellmanFord b = new BellmanFord(1, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(0, cost[0]);
    }

    @Test
    public void BellmanMultiplePaths() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,10);
        edges.add(0,2,5);
        edges.add(2,1,2);
        edges.add(1,3,1);
        edges.add(2,3,9);
        int[] cost = new int[4];
        int[] parent = new int[4];

        BellmanFord b = new BellmanFord(4, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(7, cost[1]);
        assertEquals(8, cost[3]);
    }

    @Test
    public void BellmanCycleNoNegative() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,2);
        edges.add(1,2,2);
        edges.add(2,0,1);
        edges.add(2,3,5);
        int[] cost = new int[4];
        int[] parent = new int[4];

        BellmanFord b = new BellmanFord(4, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(4, cost[2]);
        assertEquals(9, cost[3]);
    }


    @Test
    public void BellmanZeroWeightEdges() {
        EdgeGraph edges = new EdgeGraph();
        edges.add(0,1,0);
        edges.add(1,2,0);
        edges.add(2,3,0);
        int[] cost = new int[4];
        int[] parent = new int[4];

        BellmanFord b = new BellmanFord(4, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(0, cost[3]);
    }

    @Test
    public void BellmanLargeInput() {
        int n = 100;
        EdgeGraph edges = new EdgeGraph();
        for (int i = 0; i < n - 1; i++) {
            edges.add(i,i+1,1);
        }

        int[] cost = new int[n];
        int[] parent = new int[n];
        BellmanFord b = new BellmanFord(n, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();
        assertTrue(result);
        assertEquals(99, cost[99]);
    }


    @Test
    public void testAlgorithmComparison() {
        System.out.println("Running algorithm comparison tests...");


        System.out.println ("graph with 5 nodes ");
         testDijkstra();
         FloydPerformanceCompare1();
         test1Bellman();
         

        System.out.println ("graph with 7 nodes ");
         test2Dijkstra();
         FloydPerformanceCompare2();
         test2Bellman();;

        System.out.println ("graph with 8 nodes ");
         test3Dijkstra();
         FloydPerformanceCompare3();
         test3Bellman();


    }

    @Test
    public void test1Bellman() {
        int numNodes = 5;
        EdgeGraph edges = new EdgeGraph();

        edges.add(0,1,10);
        edges.add(0,3,5);
        edges.add(1,2,1);
        edges.add(1,3,2);
        edges.add(2,4,4);
        edges.add(3,1,3);
        edges.add(3,2,9);
        edges.add(3,1,3);
        edges.add(4,0,7);
        edges.add(3,4,2);
        edges.add(4,2,6);

        int[] cost = new int[numNodes];
        int[] parent = new int[numNodes];

        long startTime = System.nanoTime();

        BellmanFord b = new BellmanFord(numNodes, edges.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println("Bellman-Ford on 5 nodes: " + duration + " ns");

        assertEquals("Distance from S to S ", 0, cost[0]);  // S to S
        assertEquals("Distance from S to A ", 8, cost[1]);  // S to A (via C)
        assertEquals("Distance from S to B ", 9, cost[2]); // S to B (via C to A to B)
        assertEquals("Distance from S to C ", 5, cost[3]);  // S to C
        assertEquals("Distance from S to D ", 7, cost[4]);  // S to D

        List<Integer> pathToD = b.getPath(4);
        assertEquals("Path from S to D should be ", Arrays.asList(0, 3,4), pathToD);
    }

    @Test
    public void test2Bellman() {
        int numNodes = 7;
        EdgeGraph graph = new EdgeGraph();

        graph.add(0, 1, 1);
        graph.add(1, 2, 3);
        graph.add(1, 3, 2);
        graph.add(1, 4, 1);
        graph.add(2, 4, 4);
        graph.add(3, 4, 2);
        graph.add(4, 5, 3);
        graph.add(6, 3, 1);

        int[] cost = new int[numNodes];
        int[] parent = new int[numNodes];
        //time
        long startTime = System.nanoTime();
        BellmanFord b = new BellmanFord(numNodes, graph.getGraph().toArray(new Edge[0]), 6, cost, parent);
        boolean result = b.getShortestDistances();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Bellman-Ford on 7 nodes: " + duration + " ns");
        assertEquals("Distance from G to A", Integer.MAX_VALUE, cost[0]); //no path
        assertEquals("Distance from G to B", Integer.MAX_VALUE, cost[1]);
        assertEquals("Distance from G to C", Integer.MAX_VALUE, cost[2]);
        assertEquals("Distance from G to D", 1, cost[3]);
        assertEquals("Distance from G to E", 3, cost[4]);
        assertEquals("Distance from G to F", 6, cost[5]);
        assertEquals("Distance from G to G", 0, cost[6]);

        List<Integer> expectedPathToF = Arrays.asList(6, 3, 4, 5);
        assertEquals("Path from G to F should be", expectedPathToF, b.getPath( 5));
    }

    @Test
    public void test3Bellman() {
        int num_nodes = 8;
        EdgeGraph graph = new EdgeGraph();

        graph.add(0, 1, 1);
        graph.add(0, 2, 5);
        graph.add(1, 2, 2);
        graph.add(1, 3, 2);
        graph.add(1, 4, 1);
        graph.add(2, 3, 1);
        graph.add(2, 4, 2);
        graph.add(3, 4, 1);
        graph.add(3, 5, 3);
        graph.add(4, 5, 1);
        graph.add(4, 6, 2);
        graph.add(5, 7, 4);
        graph.add(6, 7, 1);
        graph.add(7, 0, 2);

        int[] cost = new int[num_nodes];
        int[] parent = new int[num_nodes];

        long startTime = System.nanoTime();

        BellmanFord b = new BellmanFord(num_nodes, graph.getGraph().toArray(new Edge[0]), 0, cost, parent);
        boolean result = b.getShortestDistances();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Bellman-Ford on 8 nodes: " + duration + " ns");

        int[] expectedDistances = {
                0,
                1,
                3,
                3,
                2,
                3,
                4,
                5
        };

        assertArrayEquals(expectedDistances, cost);

        List<Integer> path = b.getPath( 7);
        List<Integer> expectedPath = Arrays.asList(0, 1, 4, 6, 7);
        assertEquals(expectedPath, path);
    }
    

    @Test
    public void testDijkstra() {
        int numNodes = 5;
        WeightedGraph graph = new WeightedGraph(numNodes);

        graph.addEdge(0,1,10);
        graph.addEdge(0,3,5);
        graph.addEdge(1,2,1);
        graph.addEdge(1,3,2);
        graph.addEdge(2,4,4);
        graph.addEdge(3,1,3);
        graph.addEdge(3,2,9);
        graph.addEdge(3,1,3);
        graph.addEdge(4,0,7);
        graph.addEdge(3,4,2);
        graph.addEdge(4,2,6);

        int[] costArr = new int[numNodes];
        int[] parent = new int[numNodes];

        long startTime = System.nanoTime();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, numNodes, 0, costArr, parent);
        dijkstra.getCostarr();

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        System.out.println("Dijkstra on 5 nodes: " + duration + " ns");

        assertEquals("Distance from S to S ", 0, costArr[0]);  // S to S
        assertEquals("Distance from S to A ", 8, costArr[1]);  // S to A (via C)
        assertEquals("Distance from S to B ", 9, costArr[2]); // S to B (via C to A to B)
        assertEquals("Distance from S to C ", 5, costArr[3]);  // S to C
        assertEquals("Distance from S to D ", 7, costArr[4]);  // S to D

        List<Integer> pathToD = dijkstra.shortestPath(0, 4);
        assertEquals("Path from S to D should be ", Arrays.asList(0, 3,4), pathToD);
    }


    @Test
    public void test2Dijkstra() {
        int numNodes = 7;
        WeightedGraph graph = new WeightedGraph(numNodes);

        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 3);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 4, 1);
        graph.addEdge(2, 4, 4);
        graph.addEdge(3, 4, 2);
        graph.addEdge(4, 5, 3);
        graph.addEdge(6, 3, 1);

        int[] costArr = new int[numNodes];
        int[] parent = new int[numNodes];
        //time
        long startTime = System.nanoTime();
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, numNodes, 6, costArr, parent);
        dijkstra.getCostarr();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Dijkstra on 7 nodes: " + duration + " ns");
        assertEquals("Distance from G to A", Integer.MAX_VALUE, costArr[0]); //no path
        assertEquals("Distance from G to B", Integer.MAX_VALUE, costArr[1]);
        assertEquals("Distance from G to C", Integer.MAX_VALUE, costArr[2]);
        assertEquals("Distance from G to D", 1, costArr[3]);
        assertEquals("Distance from G to E", 3, costArr[4]);
        assertEquals("Distance from G to F", 6, costArr[5]);
        assertEquals("Distance from G to G", 0, costArr[6]);



        List<Integer> expectedPathToF = Arrays.asList(6, 3, 4, 5);
        assertEquals("Path from G to F should be", expectedPathToF, dijkstra.shortestPath(6, 5));

    }

    @Test
    public void test3Dijkstra() {
        int num_nodes = 8;
        WeightedGraph graph = new WeightedGraph(num_nodes);

        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 5);
        graph.addEdge(1, 2, 2);
        graph.addEdge(1, 3, 2);
        graph.addEdge(1, 4, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(2, 4, 2);
        graph.addEdge(3, 4, 1);
        graph.addEdge(3, 5, 3);
        graph.addEdge(4, 5, 1);
        graph.addEdge(4, 6, 2);
        graph.addEdge(5, 7, 4);
        graph.addEdge(6, 7, 1);
        graph.addEdge(7, 0, 2);

        int[] costarr = new int[num_nodes];
        int[] parent = new int[num_nodes];

        long startTime = System.nanoTime();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph, num_nodes, 0, costarr, parent);
        int[] distances = dijkstra.getCostarr();
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("Dijkstra on 8 nodes: " + duration + " ns");

        int[] expectedDistances = {
                0,
                1,
                3,
                3,
                2,
                3,
                4,
                5
        };

        assertArrayEquals(expectedDistances, distances);


        List<Integer> path = dijkstra.shortestPath(0, 7);
        List<Integer> expectedPath = Arrays.asList(0, 1, 4, 6, 7);
        assertEquals(expectedPath, path);
    }
}
