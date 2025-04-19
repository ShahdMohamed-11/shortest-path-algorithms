package com.graphprj;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BellmanFord {
    private int[] shortestpath ;
    private int[] predecessor;
    private int v;
    private int startnode;
    private Edge[] graph;

    public BellmanFord(int numverdices ,Edge[] data ,int start ,int[] costarr, int[] parent){
        this.v = numverdices;
        this.graph = data;
        this.startnode = start;
        this.shortestpath = costarr;
        this.predecessor = parent;
    }

    public boolean getShortestDistances() {

        Arrays.fill(shortestpath, Integer.MAX_VALUE);
        Arrays.fill(predecessor, -1);
        shortestpath[startnode] = 0;

        for(int i=0 ; i<v ; i++){
            for(Edge k : graph){
                if(shortestpath[k.getFrom()] + k.getWeight() < shortestpath[k.getTo()]){
                    if(i == v-1){
                        System.out.println("Negative Cycle Detected !!");
                        return false;
                    }
                    shortestpath[k.getTo()] = shortestpath[k.getFrom()] + k.getWeight();
                    predecessor[k.getTo()] = k.getFrom();
                }
            }
        }

        return true;
    }

    public List<Integer> getPath(int target) {

        List<Integer> path = new ArrayList<>();

        if (shortestpath[target] == Integer.MAX_VALUE) {
            return path;
        }

        for (int at = target; at != -1; at = predecessor[at]) {
            path.add(0, at); // insert at the beginning
        }

        return path;
    }

//    public static void main(String[] args) {
//
//        EdgeGraph g = new EdgeGraph();
//        g.add(0,1,5);
//        g.add(1,2,1);
//        g.add(1,3,2);
//        g.add(2,4,1);
//        g.add(4,3,-1);
//
//        BellmanFord b = new BellmanFord(5,g.getGraph().toArray(new Edge[0]), 0);
//        int[] res = b.getShortestDistances();
//        List<Integer> p = b.getPath(2);
//
//        for(int i : res){
//            System.out.println(i);
//        }
//
//        for(int i : p){
//            System.out.println(i);
//        }
//
//
//    }
}