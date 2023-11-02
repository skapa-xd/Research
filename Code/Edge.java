import java.util.List;

public class Edge 
{
    int from;
    int to;
    double tr;

    public Edge(int from, int to, double tr) {
        this.to = to;
        this.tr = tr;
        this.from = from;
    }
    

    public String toString()
    {
        return " From : "+ getFrom() + "To : " + getTO() + "Cost : " + getTr();
    }

    public int getFrom()
    {
        return from;
    }
    public int getTO(){
        return to;
    }
    public double getTr()
    {
        return tr;
    }
    public double getTr(int from, int to, List<Edge> edges)
    {
        for(Edge edge : edges)
        {
            int currFrom = edge.getFrom();
            int currTo = edge.getTO();
            if(currFrom == from && currTo == to)
            {
                return edge.getTr();
            }
        }
        return 0;
    }


}
