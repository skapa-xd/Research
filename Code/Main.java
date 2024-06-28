
import java.io.IOException;
import java.util.*;
import java.util.Collections;

public class Main 
{
    public static void main(String[] args) 
    {
      Network network = new Network();
      Algorithms algorithms = new Algorithms();
      NetworkToText write = new NetworkToText();
      LoadNetworkfromFile load = new LoadNetworkfromFile(); 
      
      double B = 2000;
      int min =800;
      int max = 1000;
      
      
      
      String fileName = "node_info_" + System.currentTimeMillis() + ".txt";
      Map<Node, Double> shortest = new HashMap<>();

      List<Node> nodes = load.loadNetwork("/E:/Research/Networks/Network1.txt"); 
     
      //List<Node> nodes =  network.generateNodes(1000, 1000, 20, 50); 
      network.addEdges(nodes, 200);
      
      shortest = network.findShortestPaths(nodes, nodes.get(0));

      //prints node info
       for(Node node : nodes)
       {
            node.nodeInfo();
       }

         //prints shortest path to each node
       for(Node node: shortest.keySet())
       {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() + ": " + cost);
       }

       // prints the total data available to be collected
       network.totalDataAvailable(nodes);


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
     // List<Integer> tsp1 = algorithms.gTSP1(nodes, 0, B);
      
      //List<Integer> csp1 = algorithms.greedy1CSP(nodes, 0, B);
      /* long tsp2Start = System.currentTimeMillis();
      List<Integer> tsp2 =  algorithms.gTSP2(nodes, 0, B);
      long tsp2End = System.currentTimeMillis();
      System.out.println("TSP2 took " + (tsp2End - tsp2Start) + " milliseconds"); */
      //List<Integer> csp2 = algorithms.greedy2CSP(nodes, 0, B);
       /* int hours  = algorithms.getHoursTSP2(nodes, min ,max);
       System.out.println("TSP First Node dies: " + hours); */

       //List<Integer> yangOld = algorithms.gYang(nodes, 0, B);
     /*  long yangStart = System.currentTimeMillis();
       List<Edge> yang = algorithms.nYangs(nodes, 0, B); 
        long yangEnd = System.currentTimeMillis();
        System.out.println("Yang took " + (yangEnd - yangStart) + " milliseconds");
        */
       /* int hours2 = algorithms.getHoursCSP2(nodes, min, max);
      System.out.println("CSP First Node dies: " + hours2); */

        long startTime  = System.currentTimeMillis();
       List<Integer> marl = algorithms.MARL(nodes, 0, B, 10000,10, 0.1, 0.3, 0.5);
       long endTime = System.currentTimeMillis();
      System.out.println("MARL took " + (endTime - startTime) + " milliseconds"); 

      List<Integer> marl2 = algorithms.MARLv1(nodes, 0, B, 10000,10, 0.1, 0.3, 0.5);
      List<Integer> marl3 = algorithms.MARLv2(nodes, 0, B,10000,10, 0.1, 0.3, 0.5);


      // visualizes the routes
     /*  Visualizer visualize = new Visualizer(nodes, 1000, 1000, tsp1, tsp2, yang, marl);
      visualize.run(); */

      /* Visualizer visualize1 = new Visualizer(nodes, 1000, 1000, yang);
        visualize1.run();
  */

      /* HashSet<Integer> ms = new HashSet<Integer>();
      for(Edge edge: yang)
      {
            ms.add(edge.getStart().getID());
            ms.add(edge.getEnd().getID());
      }
      List<Node> nodes2 = new ArrayList<Node>();
      for(int n : ms)
      {
          nodes2.add(nodes.get(n));
      }
      Collections.sort(nodes2, (node1, node2) -> Integer.compare(node1.getID(), node2.getID()));
      for(Node node: nodes2)
      {
          System.out.println(node.getID());
      }
      
      

      ILP ilp = new ILP(nodes2);
      ilp.printDist();
      ilp.printPrize(); 
      ilp.printSize(); */

      /* ILP ilp1 = new ILP(nodes);
      ilp1.printDist();
      ilp1.printPrize();
      ilp1.printSize(); */ 

       /* for(int i = 0; i < nodes.size(); i++)
      {
          System.out.println("" + nodes.get(i).getX() + " "+  nodes.get(i).getY());
      }

      for(int i = 0; i < nodes.size(); i++)
      {
          System.out.println(String.format("%d (%d)", nodes.get(i).getID(), nodes.get(i).getDataPackets()));
      }
      for(int i = 0; i < nodes.size(); i++)
      {
          System.out.println(String.format("(%d)", nodes.get(i).getDataPackets()));
      } */
    
     
      
   } 
    
}


