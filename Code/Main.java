
import java.util.*;

public class Main 
{
    public static void main(String[] args) {
        Network network = new Network();
        Algorithms algorithms = new Algorithms();
        int totalPacketsAvailable = 0;

       List<Node> nodes =  network.generateNodes(2000, 2000, 200, 100); 

       /* List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, 0, false, 0));
       nodes.add(new Node(1, 2, true, 60));
       nodes.add(new Node(3, 2, true, 90));
       nodes.add(new Node(2, 3, true, 880));
       nodes.add(new Node(1, 4, true, 710));
       nodes.add(new Node(3, 5, true, 40));
       nodes.add(new Node(4, 6, true, 70));
       nodes.add(new Node(5, 2, true, 10));
       nodes.add(new Node(2, 6, true, 30));
       nodes.add(new Node(4, 4, true, 100)); */

       Map<Node, Double> shortest = new HashMap<>();

       network.addEdges(nodes, 300, 300);
       shortest = network.findShortestPaths(nodes, nodes.get(0));

       for(Node node : nodes)
       {
            node.nodeInfo();
       }

       for(Node node: shortest.keySet())
       {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() + ": " + cost);
       }

       for(Node node : nodes)
       {
          totalPacketsAvailable =  totalPacketsAvailable + node.getDataPackets();
       }

      
       System.out.println(totalPacketsAvailable);

       //algorithms.greedy1TSP(nodes, 0, 1000000);
      
       //algorithms.greedy2TSP(nodes, 0, 1000000);
       System.out.println("**************G1**********");
       algorithms.greedy1CSP(nodes, 0, 1000000); 
       System.out.println("**************G2**********");
       algorithms.greedy2CSP(nodes, 0, 1000000); 
      //  algorithms.greedy1CSPprizeQuota(nodes, 0, 1000000, totalPacketsAvailable);
      
      System.out.println("**************P1**********");
       algorithms.pollingPapproach(nodes, 0, 2000);
       
    }
    
}


