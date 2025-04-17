public class Node {
    int id;
    int distance;
    boolean visited;

    public Node(int id, int distance) {
        this.id = id;
        this.distance = distance;
        visited=false;
    }
}
