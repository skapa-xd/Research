import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Agent 
{
    private Node curr;
    private double budget;
    private int dataPackets;
    private HashSet<Node> unvisited;
    private List<Integer> route;
    private boolean isDone;
    private List<Node> nodes;
    double cost;
    HashMap<Node, Double> shortestPaths;
    Network network = new Network();


    public Agent(Node curr, double budget, boolean isDone, List<Node> nodes, HashMap<Node, Double> shortestPaths)
    {
        
        this.curr = curr;
        this.budget = budget*3600; // WH to Joules
        this.unvisited = new HashSet<>(nodes);
        unvisited.remove(curr);
        this.isDone = isDone;
        this.nodes = nodes;
        this.shortestPaths = shortestPaths;
        this.dataPackets = 0;
        this.cost= 0;
        this.route = new ArrayList<>();
        this.route.add(curr.getID());
    }

    public void addNode(Node node)
    {
        route.add(node.getID());
    }
    public void addCost(Node current, Node next)
    {
        if(current == next)
        {
            cost = cost + 0;
        }
        else
        {
            cost = cost + current.getNeighbor(next);
        }
        
    }
    public double getCost()
    {
        return this.cost;
    }
    
    public void updateBudget(Node current, Node next)
    {
        this.budget = this.budget - (current.getNeighbor(next)*100);
    }
    public void updateData(Node current)
    {
        this.dataPackets = this.dataPackets + current.getDataPackets();
    }
    public void updateCurrent(Node current)
    {
        this.curr = current;
    }

    public boolean isDone()
    {
        return this.isDone;
    }

    public void updateDone(boolean done)
    {
        this.isDone = done;
    }

    public Set<Node> agentFeasibleSet()
    {
        return network.feasibleSet(this.curr, this.budget, this.shortestPaths, this.unvisited);
    }

    public Node getCurrent()
    {
        return this.curr;
    }

    public double getBudget()
    {
        return this.budget;
    }

    public HashSet<Node> getUnvisited()
    {
        return this.unvisited;
    }

    public void updateUnvisited(Node node)
    {
        this.unvisited.remove(node);
    }

    public int getDataPackets()
    {
        return this.dataPackets;
    }

    public List<Integer> getRoute()
    {
        return this.route;
    }


    
}
