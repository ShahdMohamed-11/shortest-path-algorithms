import java.util.Arrays;
import java.util.List;

public class BellmanFord {
    private int[] shortestpath ;
    private int v;
    private int startnode;
    private Edge[] graph;

    public BellmanFord(int numverdices ,Edge[] data ,int start){
        this.v = numverdices;
        this.graph = data;
        this.startnode = start;
        shortestpath = new int[numverdices];
    }

    public int[] getShortestpath() {

        Arrays.fill(shortestpath, Integer.MAX_VALUE);
        shortestpath[startnode] = 0;

        for(int i=0 ; i<v ; i++){
            for(Edge k : graph){
                if(shortestpath[k.getFrom()] + k.getWeight() < shortestpath[k.getTo()]){
                    if(i == v-1){
                        System.out.println("Negative Cycle Detected !!");
                        break;
                    }
                    shortestpath[k.getTo()] = shortestpath[k.getFrom()] + k.getWeight();
                }
            }
        }

        return shortestpath;
    }

    public static void main(String[] args) {

        EdgeGraph g = new EdgeGraph();
        g.add(0,1,5);
        g.add(1,2,1);
        g.add(1,3,2);
        g.add(2,4,1);
        g.add(4,3,-1);

        BellmanFord b = new BellmanFord(5,g.getGraph().toArray(new Edge[0]), 0);
        int[] res = b.getShortestpath();

        for(int i : res){
            System.out.println(i);
        }


    }
}
