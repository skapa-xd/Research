import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Node
{
    private int id; // node id
    private int x; // node x co-ordinate
    private int y; // node y co-ordinate
    private boolean isDataNode; // returns true if node is data node, false if storage node
    private int dataPackets; // number of data packets of the node only
    private int totalDataPackets; // number of data packets of all nodes which fall within the CSP range
    private int dp; // changing data packets CSP1 DYNAMIC 
    private int dp2; // changing data packets CSP2 DYNAMIC
    private static int counter = 0; // id counter
    private HashMap<Node, Double> neighbors; // node neighbors, calculated by transmission range definition
    private HashMap<Node, Double> rangeNeighbors; // node ranfe neighbors, calculated by robot range definition
    private double budgetCSP1 = 0; // budget for CSP1

    public Node(int x, int y, boolean isDataNode, int dataPackets) // initializing the node
    {
        this.x = x;
        this.y =y;
        this.isDataNode = isDataNode;
        this.dataPackets = dataPackets;
        this.totalDataPackets = dataPackets;
        this.dp = 0;
        this.dp2 =0;
        this.id = counter++;
        this.neighbors = new HashMap<>();
        this.rangeNeighbors = new HashMap<>();
        this.budgetCSP1 = 0;
    }
    public Node(int id, int x, int y, boolean isDataNode, int dataPackets) // initializing the node
    {
        this.x = x;
        this.y =y;
        this.isDataNode = isDataNode;
        this.dataPackets = dataPackets;
        this.totalDataPackets = dataPackets;
        this.dp = 0;
        this.dp2 =0;
        this.id = id;
        this.neighbors = new HashMap<>();
        this.rangeNeighbors = new HashMap<>();
    }

    public int getID() // returns id of node
    {
        return this.id; 
    }
    
    public void setID(int id) // sets id to the node
    {
        this.id = id;
    }

    public void setLocation(int x, int y) // sets location of node
    {
        this.x = x;
        this.y = y;
    }

    public int[] getLocation() // return location of node in array
    {
        return new int[]{x, y};
    }

    public int getX()  // returns x co-ordinate
    {
        return this.x;
    }

    public int getY()  // returns y co-ordinate
    {
        return this.y;
    }

    public boolean isDataNode() // returns true if it is data node, false if storage node
    {
        return this.isDataNode;
    }

    public int getDataPackets() // returns data packets of node only
    {
        return this.dataPackets;
    }

    public int getTotalDataPackets() // returns data packets from all nodes which fall within the range given by CSP
    {
        return this.totalDataPackets;
    }

    public void setTotalDataPackets(int dataPackets) // sets the total data packets of the node.
    {
        this.totalDataPackets = dataPackets;
    }

    public void setDP(int dataPackets)
    {
        this.dp = dataPackets;
    }

    public int getDP()
    {
        return this.dp;
    }
    
    public void setDP2(int dataPackets)
    {
        this.dp2 = dataPackets;
    }

    public int getDP2()
    {
        return this.dp2;
    }

    public void setNeighbor(Node node, double cost) // set the given node as the current node's neighbor with the cost
    {
        this.neighbors.put(node, cost);
    }

    public double getNeighbor(Node node) // returns the cost of given neighbor
    {
        return this.neighbors.get(node);
    }

    public void setRangeNeighbor(Node node, double data)
    {
        this.rangeNeighbors.put(node, data);
    }

    public List<Node> getNeighborsList() // returns the list of all neighbors of the node
    {
        Set<Node> keySet = this.neighbors.keySet(); 
        ArrayList<Node> listOfKeys = new ArrayList<Node>(keySet);
        return listOfKeys;
    } 

    public List<Integer> getNeighbors() // returns the list of all neighbors in INTEGER HUMAN READABLE lol
    {
        Set<Node> keySet = this.neighbors.keySet(); 
        ArrayList<Integer> listOfKeys = new ArrayList<Integer>();

        for(Node node : keySet)
        {
            listOfKeys.add(node.getID());
        }
        return listOfKeys;
    } 

    public List<Node> getRangeNeighborList() // returns list of neighbors which fall in the range of CSP
    {
        Set<Node> keySet = this.rangeNeighbors.keySet(); 
        ArrayList<Node> listOfKeys = new ArrayList<Node>(keySet);
        return listOfKeys;
    }

    public List<Integer> getRangeNeighbors() // returns the list of all neighbors in INTEGER HUMAN READABLE lol
    {
        Set<Node> keySet = this.rangeNeighbors.keySet(); 
        ArrayList<Integer> listOfKeys = new ArrayList<Integer>();

        for(Node node : keySet)
        {
            listOfKeys.add(node.getID());
        }
        return listOfKeys;
    } 

    public void nodeInfo() // prints the node information
    {
        String s  = String.format("Node id: %d {x: %d} {y: %d} {is data node: %b} {data packets: %2d} {total data packets: %2d} {Range Neighbor: %s}",
        getID(), getX(), getY(), isDataNode(), getDataPackets(), getTotalDataPackets(), Arrays.toString(getRangeNeighbors().toArray()));
        System.out.println(s);
    }

   /*  public void setBudgetCSP1(double budget) // sets the budget for CSP1
    {
        this.budgetCSP1 = budget;
    }

    public double getBudgetCSP1() // sets the budget for CSP1
    {
        return this.budgetCSP1;
    } */


}