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


}
