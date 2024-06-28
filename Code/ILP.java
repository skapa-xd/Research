// This class is used to get the data for ILP by converting to 2D Matrix

import java.util.Arrays;
import java.util.List;

public class ILP 
{
    List<Node> nodes;
    double[][] distanceMatrix;
    int[] prize;
    
    public ILP(List<Node> nodes)
    {
        this.nodes = nodes;
        prize = new int[nodes.size()];
        for(int i = 0; i < nodes.size(); i++)
        {
            prize[i] = nodes.get(i).getDataPackets();
        }
        this.distanceMatrix = new double[nodes.size()][nodes.size()];
        for(int i =0; i<nodes.size(); i++)
        {
            for(int j = 0; j < nodes.size(); j++)
            {
                if(i==j)
                {
                    this.distanceMatrix[i][j] = 9999.99;
                }
                else
                {
                    this.distanceMatrix[i][j] = nodes.get(i).getNeighbor(nodes.get(j));
                }
            }
        }
    }

    public void printDist() // ilp format printing 
    {
        for (int i = 0; i < distanceMatrix.length; i++) 
        {
            for (int j = 0; j < distanceMatrix[i].length; j++) 
            {
                System.out.print(distanceMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void print2DString() // prints array of arrays
    {
        System.out.println(Arrays.deepToString(distanceMatrix));
    }

    public void printPrize() // array of prizes
    {
        System.out.println(Arrays.toString(prize));
    }

    public void printSize() // prints size of nodes
    {
        System.out.println(prize.length);
    }
    
    
}
