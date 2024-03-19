
import java.io.IOException;
import java.util.*;

public class Main 
{
    public static void main(String[] args) 
    {
      Network network = new Network();
      Algorithms algorithms = new Algorithms();
      NetworkToText write = new NetworkToText();
      LoadNetworkfromFile load = new LoadNetworkfromFile("/E:/Research/Code/node_info_1710808412207.txt"); 
      
      
      
      int totalPacketsAvailable = 0;
      String fileName = "node_info_" + System.currentTimeMillis() + ".txt";
      Map<Node, Double> shortest = new HashMap<>();

      List<Node> nodes = load.loadNetwork(); 
      //List<Node> nodes =  network.generateNodes(10000, 10000, 100, 50); 
      network.addEdges(nodes, 150);
      shortest = network.findShortestPaths(nodes, nodes.get(0));

      //prints node info
       /* for(Node node : nodes)
       {
            node.nodeInfo();
       }
 */
         //prints shortest path to each node
       /* for(Node node: shortest.keySet())
       {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() + ": " + cost);
       } */

       // prints the total data available to be collected
       for(Node node : nodes)
       {
          totalPacketsAvailable =  totalPacketsAvailable + node.getDataPackets();
       }
       System.out.println("Total Packets Available to Collect: " + totalPacketsAvailable);


       // writes node information to a file
      /*  try 
       {
          write.writeToFile(nodes, fileName);
          System.out.println("Node information written to " + fileName);
       } 
       catch (IOException e) 
       {
          System.err.println("Error writing to file: " + e.getMessage());
       } */

         // runs the algorithms
      /*  List<Integer> route1 = algorithms.gTSP1(nodes, 0,1000);
       List<Integer> route2 =  algorithms.gTSP2(nodes, 0, 1000);
       List<Integer> route3 = algorithms.gYang(nodes, 0, 1000); */

       long startTime  = System.currentTimeMillis();
       List<Integer> route4 = algorithms.MARL(nodes, 0, 2000, 10000, 20, 0.5, 0.3, 0.5);
       long endTime = System.currentTimeMillis();
         System.out.println("MARL took " + (endTime - startTime) + " milliseconds");


      // visualizes the routes
     /*  Visualizer visualize = new Visualizer(nodes, 10000, 10000, route1, route2, route3, route4);
      visualize.run(); */
 
       
      
       


       
      
       
    }
    
}


