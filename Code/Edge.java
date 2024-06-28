public class Edge 
{
    Node start;
    Node end;
    double cost;

    public Edge(Node start, Node end, double cost) {
        this.start = start;
        this.end = end;
        this.cost = cost;
    }

    public Node getStart() {
        return start;
    }
    public Node getEnd() {
        return end;
    }
}