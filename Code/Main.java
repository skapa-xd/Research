// THIS IS THE MAIN FUNCTION WHERE YOU CAN RUN STUFF

/* Did not create a program yet so comment out what you dont need */

import java.io.IOException;
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
        double B = 55.55; // budget
        /* int min =800; // min data packets (used for network longevity)
        int max = 1000; // max data packets (used for network longevity) */
        int start = 0;
        

        List<Node> nodes = file.loadNetwork("/E:/Research/Networks/N1.txt"); // load from a file 
        //List<Node> nodes =  network.generateNodes(1000, 1000, 100, 50,true); // generate random network, end == true means that start and end are same 0,0; 
        // if it is false then start will be 0,0 and end will be width,length
        network.addEdges(nodes, 100); // make the network aware of neighbors

        shortest = network.findShortestPaths(nodes, nodes.get(start)); // assigns hashmap to shortest

        //prints node info
        for(Node node : nodes)
        {
            node.nodeInfo();
        }

        //prints shortest path to each node from the start
        /* for(Node node: shortest.keySet())
        {
            double cost = shortest.get(node);
            System.out.println("Shortest path to Node " + node.getID() +" from node " + start +  ": " + cost);
        } */

        // prints the total data available to be collected
        network.totalDataAvailable(nodes);
        System.out.println("The budget for robots is " + B + "Wh");

        // algorithms
        //List<Integer> tsp1 = algorithms.gTSP1(nodes, 0, B);
        
        /* long startT = System.currentTimeMillis();
        List<Integer> tsp2 =  algorithms.gTSP2(nodes, 0, B, false);
        long endT = System.currentTimeMillis();
        double timeT = (double)endT - startT ;
        System.out.println("TSP time is " + timeT); */
        
        //List<Integer> csp1 = algorithms.greedy1CSP(nodes, 0, B);
        long startCSP2 = System.currentTimeMillis();
        List<Integer> csp2 = algorithms.greedy2CSP(nodes, 0, B);
        long endCSP2 = System.currentTimeMillis();
        System.out.println("CSP 2 time is :" + (endCSP2-startCSP2));
        
        /* long startY = System.currentTimeMillis();
        List<Edge> yang = algorithms.nYangs(nodes, 0, B); 
        long endY = System.currentTimeMillis();
        double timeY = (double)endY - startY ;
        System.out.println("Yang time is " + timeY); */

        

        
        
       /*  long startC = System.currentTimeMillis();
        Pair cmarl = algorithms.CMARL(nodes,0,99, false, B, 1000,30, 0.3, 0.7, 0.5);
        long endC = System.currentTimeMillis();
        double timeC = (double)endC - startC ;
        System.out.println("CMARL time is " + timeC); */
        
    
        /* List<Node> nodes1 = network.dynamicNetwork(nodes); // makes it dynamic

        long startC1 = System.currentTimeMillis();
        Pair cmarl1 = algorithms.CMARL(nodes1,99,0, false, B, 1000,30, 0.3, 0.7, 0.5);
        long endC1 = System.currentTimeMillis();
        double timeC1 = (double)endC1 - startC1;
        System.out.println("CMARL time is " + timeC1/1000);

        long startM = System.currentTimeMillis();
        List<Integer> marl = algorithms.MARL(nodes1, 99,0, false, B, 1000,30, 0.1, 0.7, 0.5);
        long endM = System.currentTimeMillis();
        double timeM = (double)endM - startM ;
        System.out.println("MARL time is " + timeM/1000); */

       

        
       

        /* long startTL = System.currentTimeMillis();
        algorithms.TL1( nodes1, cmarl);
        long endTL = System.currentTimeMillis();
        double timeTL = (double)endTL - startTL ;
        System.out.println("Transfer Learning time is " + timeTL);  */

        
        
        
       /*  Visualizer visualize1 = new Visualizer(nodes, 10000, 10000);
        visualize1.run(); */
        

        ILP ilp = new ILP( nodes);

        ilp.print2DString();
        ilp.printPrize();
        ilp.printSize();
/* 
        for(Node n : nodes)
        {
            System.out.println(n.getX()+ " "+ n.getY());
        }
        for(Node n : nodes)
        {
            System.out.println(n.getID()+ "(" + n.getDataPackets() + ")");
        } */
            

        // visualizes the routes
        Visualizer visualize = new Visualizer(nodes, 1000, 1000, csp2);
        visualize.run();

        
        
        
        /* Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to save the network? 0/1");
        int ans = scanner.nextInt();
        if(ans==1)
        {
            file.saveNetwork(nodes);
            System.out.println("Network saved");
        }
        else
        {
            System.exit(0);
        } */
        
        
        // TODO compare antq with c-marl(transfer learning)

        
        
        
        

        

        
    
      
      
    } 
    
}


