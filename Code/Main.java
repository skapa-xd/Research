// THIS IS THE MAIN FUNCTION WHERE YOU CAN RUN STUFF

/* Did not create a program yet so comment out what you dont need */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class Main
{
    public static void main(String[] args) throws IOException 
    {
        // intializers
        Network network = new Network();
        Algorithms algorithms = new Algorithms();
        NetworkFileManager file = new NetworkFileManager(); 
        Map<Node, Double> shortest = new HashMap<>();
        
        
        // variables
        double B = 110; // budget
        int min =800; // min data packets (used for network longevity)
        int max = 1000; // max data packets (used for network longevity)
        int start = 0;
        

        List<Node> nodes = file.loadNetwork("/E:/Research/Networks/N6.txt"); // load from a file 
        //List<Node> nodes =  network.generateNodes(1000, 1000, 10, 50); // generate random network
        network.addEdges(nodes, 200); // make the network aware of neighbors

        shortest = network.findShortestPaths(nodes, nodes.get(start)); // assigns hashmap to shortest

        //prints node info
        for(Node node : nodes)
        {
            node.nodeInfo();
        }

        //prints shortest path to each node from the start
        for(Node node: shortest.keySet())
        {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() +" from node " + start +  ": " + cost);
        }

        // prints the total data available to be collected
        network.totalDataAvailable(nodes);

        // algorithms
       /*  List<Integer> tsp1 = algorithms.gTSP1(nodes, 0, B);
        List<Integer> tsp2 =  algorithms.gTSP2(nodes, 0, B);
        
        List<Integer> csp1 = algorithms.greedy1CSP(nodes, 0, B);
        List<Integer> csp2 = algorithms.greedy1CSP(nodes, 0, B);

        List<Edge> yang = algorithms.nYangs(nodes, 0, B);  */

        /* PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        System.setOut(out); */

        List<Integer> marl = algorithms.MARL(nodes, 0, B, 3000,30, 0.1, 0.3, 0.5);
        algorithms.PMARL(nodes, 0, B, 3000,30, 0.1, 0.3, 0.5);

        ILP ilp = new ILP(nodes);

        ilp.print2DString();
        ilp.printPrize();
        ilp.printSize();
            

        // visualizes the routes
       /*  Visualizer visualize = new Visualizer(nodes, 1000, 1000, marl);
        visualize.run(); */

        //file.saveNetwork(nodes);
        /* Visualizer visualize1 = new Visualizer(nodes, 1000, 1000, yang);
            visualize1.run();
    */

        
        
        
        

        

        
    
      
      
    } 
    
}


