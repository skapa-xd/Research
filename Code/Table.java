import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Table 
{
    private double[][] QTable;
    private double[][] RTable;
    private List<Node> nodes;
    private double learningRate;
    private double discountFactor;
    private int W = 100;
    Network network = new Network();


    public Table(List<Node> nodes, int totalNodes, double learningRate, double discountFactor)
    {
        this.nodes = nodes;
        this.QTable = new double[totalNodes][totalNodes];
        this.RTable = new double[totalNodes][totalNodes];
        this.discountFactor = discountFactor;
        this.learningRate = learningRate;

        // intitalize QTable to prize/cost 
        for(int state = 0; state<totalNodes; state++)
        {
            Node s = nodes.get(state);
            for(int action = 0; action<totalNodes; action++)
            {
                if(state != action)
                {
                    Node a = nodes.get(action);
                    double val = (s.getDataPackets() + a.getDataPackets())/s.getNeighbor(a);
                    QTable[state][action] = val;
                }
                else
                {
                    QTable[state][action] = 0;
                }
                
            }
        }

        // initialize R table;
        for(int state = 0; state<totalNodes; state++)
        {
            Node s = nodes.get(state);
            for(int action = 0; action<totalNodes; action++)
            {
                if(state == action)
                {
                    RTable[state][action] = 0;
                }
                else
                {
                    Node a = nodes.get(action);
                    if(a.getDataPackets()==0)
                    {
                        RTable[state][action] = 0;
                    }
                    else
                    {
                        RTable[state][action] = -(s.getNeighbor(a)/a.getDataPackets());
                    }
                }
                
            }
        }
    }

    public double getQvalue(int currentNode, int nextNode)
    {
        return this.QTable[currentNode][nextNode];
    }

    public void setQvalue(int currentNode, int nextNode, double value)
    {
        this.QTable[currentNode][nextNode] = value;
    }

    public double[] getQmaxActionValue(int state, double budget, HashSet<Node> univisited, HashMap<Node, Double> shortestPaths)
    {
        double[] ans = new double[2];
        double max = Double.NEGATIVE_INFINITY;
        int action = 0;
        Node current = nodes.get(state);
        for(int i = 0; i<QTable.length; i++)
        {
            Node next = nodes.get(i);
            if(current==next)
            {
                continue;
            }
            if(network.isFeasible(current, next, budget, shortestPaths, univisited))
            {
                double val = QTable[state][i];
                if(val > max)
                {
                    max = val;
                    action = i;
                }
            }
                
            
        }
        ans[0] = action;
        ans[1] = max;
        return ans; 
    }

    public void updateQvalue(int state, int action, double budget, HashSet<Node> unvisited, HashMap<Node, Double> shortestPaths)
    {
        this.QTable[state][action] = (1-learningRate)*(getQvalue(state, action)) + learningRate * discountFactor* getQmaxActionValue(action, budget, unvisited,shortestPaths)[1];
    }

    public void updateQvalue2(int state, int action)
    {
        this.QTable[state][action] = (1-learningRate)*(getQvalue(state, action)) + learningRate * (getRvalue(state, action) + (discountFactor*(getQmaxValue(action))));
    }

    public int getQmaxAction(int state)
    {
        double max = Double.NEGATIVE_INFINITY;
        int action = 0;

        for(int i = 0; i<QTable.length; i++)
        {
            double val = QTable[state][i];
            if(val > max)
            {
                max = val;
                action = i;
            }
            
        }
        return action; 
    }

    public double getQmaxValue(int state)
    {
        double max = Double.NEGATIVE_INFINITY;
       

        for(int i = 0; i<QTable.length; i++)
        {
            double val = QTable[state][i];
            if(val > max)
            {
                max = val;
            }
            
        }
        return max; 
    }

    

    public double getRvalue(int state, int action)
    {
        return RTable[state][action];
    }

    public void updateRValue(int state, int action, int maxPrize)
    {
        RTable[state][action] = getRvalue(state, action) + W/maxPrize;
    }

    
}
