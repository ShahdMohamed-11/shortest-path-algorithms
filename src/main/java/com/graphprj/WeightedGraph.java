package com.graphprj;

public class WeightedGraph {

    int num_nodes;
    int [][] adjacencymatrix;


    public int getNum_nodes() {
        return num_nodes;
    }
    public WeightedGraph(int num_nodes) {
        this.num_nodes = num_nodes;
        adjacencymatrix=new int[num_nodes][num_nodes];
        for(int i=0;i<num_nodes;i++){
            for (int j=0;j<num_nodes;j++){
                adjacencymatrix[i][j]=0;
            }
        }
    }

    public void addEdge(int sourceindex,int destinationindex,int weight){
        adjacencymatrix[sourceindex][destinationindex]=weight;
    }



}

