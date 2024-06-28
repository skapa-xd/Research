// THIS IS THE MAIN FUNCTION WHERE YOU CAN RUN STUFF

/* Did not create a program yet so comment out what you dont need */

import java.io.IOException;
import java.util.*;
import java.util.Collections;


public class Main
{
    public static void main(String[] args) 
    {
        // intializers
        Network network = new Network();
        Algorithms algorithms = new Algorithms();
        NetworkFileManager file = new NetworkFileManager(); 
        Map<Node, Double> shortest = new HashMap<>();
        
        // variables
        double B = 2000; // budget
        int min =800; // min data packets (used for network longevity)
        int max = 1000; // max data packets (used for network longevity)
        int start = 0;
        

        //List<Node> nodes = file.loadNetwork("/E:/Research/Networks/Network1.txt"); // load from a file 
        List<Node> nodes =  network.generateNodes(1000, 1000, 20, 50); // generate random network
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
        List<Integer> tsp1 = algorithms.gTSP1(nodes, 0, B);
        List<Integer> tsp2 =  algorithms.gTSP2(nodes, 0, B);
        
        List<Integer> csp1 = algorithms.greedy1CSP(nodes, 0, B);
        List<Integer> csp2 = algorithms.greedy1CSP(nodes, 0, B);

        List<Edge> yang = algorithms.nYangs(nodes, 0, B); 
        
        List<Integer> marl = algorithms.MARL(nodes, 0, B, 100,10, 0.1, 0.3, 0.5);
            

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

        ILP ilp1 = new ILP(nodes);
        ilp1.printDist();
        ilp1.printPrize();
        ilp1.printSize(); 

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


