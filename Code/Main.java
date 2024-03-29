
import java.io.IOException;
import java.util.*;

public class Main 
{
    public static void main(String[] args) 
    {
      Network network = new Network();
      Algorithms algorithms = new Algorithms();
      NetworkToText write = new NetworkToText();
      LoadNetworkfromFile load = new LoadNetworkfromFile("/E:/Research/Networks/13th March 11.10AM.txt"); 
      
      
      
      String fileName = "node_info_" + System.currentTimeMillis() + ".txt";
      Map<Node, Double> shortest = new HashMap<>();

      List<Node> nodes = load.loadNetwork(); 
      //List<Node> nodes =  network.generateNodes(10000, 10000, 100, 50); 
      network.addEdges(nodes, 200);
      shortest = network.findShortestPaths(nodes, nodes.get(0));

      //prints node info
       for(Node node : nodes)
       {
            node.nodeInfo();
       }

         //prints shortest path to each node
       /* for(Node node: shortest.keySet())
       {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() + ": " + cost);
       } */

       // prints the total data available to be collected
       network.totalDataAvailable(nodes);


       // writes node information to a file
       /* try 
       {
          write.writeToFile(nodes, fileName);
          System.out.println("Node information written to " + fileName);
       } 
       catch (IOException e) 
       {
          System.err.println("Error writing to file: " + e.getMessage());
       } */

         // runs the algorithms
      //List<Integer> tsp1 = algorithms.gTSP1(nodes, 0,1000);
       
      //List<Integer> tsp2 =  algorithms.gTSP2(nodes, 0, 1000);
      /*  int hours  = algorithms.getHoursTSP2(nodes, 800 ,1000);
       System.out.println("TSP First Node dies: " + hours); */
      
      // List<Integer> yang = algorithms.gYang(nodes, 0, 1000); 
      // List<Integer> csp1 = algorithms.greedy1CSP(nodes, 0, 1000);
       

      // List<Integer> csp2 = algorithms.greedy2CSP(nodes, 0, 1000);
      /*  int hours2 = algorithms.getHoursCSP2(nodes, 800, 1000);
      System.out.println("CSP First Node dies: " + hours2); */

       long startTime  = System.currentTimeMillis();
       List<Integer> marl = algorithms.MARL(nodes, 0, 70, 10000, 10, 0.1, 0.3, 0.5);
       long endTime = System.currentTimeMillis();
         System.out.println("MARL took " + (endTime - startTime) + " milliseconds"); 


      // visualizes the routes
      /* Visualizer visualize = new Visualizer(nodes, 10000, 10000, tsp1, tsp2, yang, marl, csp1, csp2);
      visualize.run(); */
 
       
      
       


       
      
       
    }
    
}


