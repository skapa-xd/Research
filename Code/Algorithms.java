import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class Algorithms 
{


    public void greedy1TSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();
        double Budget = budget*0.002;
        List<Integer> Route  = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);
        int Prize = 0;
        double Cost = 0;

        
        Node s = nodes.get(start);
        Node curr = nodes.get(start);
        Unvisited.remove(curr);
        Route.add(curr.getID());

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        List<Node> sortedNodes = new ArrayList<>(Unvisited);
        sortedNodes.sort((n1, n2) -> Integer.compare(n2.getDataPackets(), n1.getDataPackets()));

        for(Node node : sortedNodes)
        {
            if(Unvisited.isEmpty() || network.feasibleSet(curr, Budget, shortestPaths, Unvisited)== null)
            {
                break;
            }
            if(network.feasibleSet(curr, Budget, shortestPaths, Unvisited).contains(node))
            {
                Route.add(node.getID());
                Cost = Cost + node.getNeighbor(curr);
                Prize = Prize + node.getDataPackets();
                Budget = Budget- node.getNeighbor(curr);
                Unvisited.remove(node);
                curr = node;
            }
        }

        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr);

        System.out.println(Route);
        System.out.println(Cost);
        System.out.println(Prize);
        System.out.println(Budget);
    }

    public void greedy2TSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();
        double Budget = budget*0.002;
        List<Integer> Route  = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);
        int Prize = 0;
        double Cost = 0;

        
        Node s = nodes.get(start);
        Node curr = nodes.get(start);
        Unvisited.remove(curr);
        Route.add(curr.getID());

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null)
        {
            Node node = network.prizeCostRatio(curr, Budget, shortestPaths, Unvisited);
            if(node == null)
            {
                break;
            }
            Route.add(node.getID());
            Cost = Cost + node.getNeighbor(curr);
            Prize = Prize + node.getDataPackets();
            Budget = Budget- node.getNeighbor(curr);
            Unvisited.remove(node);
            curr = node;
        }
        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr);

        System.out.println(Route);
        System.out.println(Cost);
        System.out.println(Prize);
        System.out.println(Budget);
    }

    public void greedy1CSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget*0.002;
        List<Integer> Route  = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);
        HashSet<Node> Collected = new HashSet<>();
        int Prize = 0;
        double Cost = 0;

        
        Node s = nodes.get(start);
        Node curr = nodes.get(start);
        Unvisited.remove(curr);
        Route.add(curr.getID());
        Collected.add(curr);

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null)
        {
            Node node = network.bestPrizeNodeCSP(curr, Budget, shortestPaths, Unvisited, Collected);
            if(node == null)
            {
                break;
            }
            Route.add(node.getID());
            Cost = Cost + node.getNeighbor(curr);
            Prize = Prize  + node.getDP();
            Budget = Budget - node.getNeighbor(curr);
            Collected.add(node);
            Unvisited.remove(node);
            Collected.addAll(node.getRangeNeighborList());
            curr = node;
        }
        
        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr);

        System.out.println("Collected :" + Prize);
        System.out.println("Collected from :" + Collected.size());
        System.out.println(Route);
        System.out.println("Visited: " + (nodes.size() - Unvisited.size()));
    }

    public void greedy2CSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();
        double Budget = budget*0.002;
        List<Integer> Route  = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);
        HashSet<Node> Collected = new HashSet<>();
        int Prize = 0;
        double Cost = 0;

        
        Node s = nodes.get(start);
        Node curr = nodes.get(start);
        Unvisited.remove(curr);
        Collected.add(curr);
        Route.add(curr.getID());
        

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null)
        {
            Node node = network.bestPrizeCostRatioNodeCSP(curr, Budget, shortestPaths, Unvisited, Collected);
        
            if(node == null)
            {
                break;
            }
            Route.add(node.getID());
            Cost = Cost + node.getNeighbor(curr);
            Prize = Prize + node.getDP2();
            Budget = Budget- node.getNeighbor(curr);
            Collected.add(node);
            Collected.addAll(node.getRangeNeighborList());
            Unvisited.remove(node);
            

            curr = node;
        }
        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr);

        System.out.println("Collected :" + Prize);
        System.out.println("Collected from :" + Collected.size());
        System.out.println(Route);
        System.out.println("Visited: " + (nodes.size() - Unvisited.size()));
        
        System.out.println(Budget);
        
    }

    // New Geometeric TSP
    public List<Integer> gTSP1(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget * 3600; // budget will be given in Kilo Watt Hours, so this converts it to joules
        double Cost = 0;
        int Prize = 0;
        List<Integer> Route = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);

        Node base = nodes.get(0);
        Node curr = base;
        Route.add(base.getID());
        Unvisited.remove(base);

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        List<Node> sortedNodes = new ArrayList<>(Unvisited);
        sortedNodes.sort((n1, n2) -> Integer.compare(n2.getDataPackets(), n1.getDataPackets()));

        for(Node node : sortedNodes)
        {
            if(Unvisited.isEmpty())
            {
                break;
            }
            else if(network.isFeasible(curr,node, Budget, shortestPaths, Unvisited))
            {
                Route.add(node.getID());
                Cost = Cost + node.getNeighbor(curr);
                Prize = Prize + node.getDataPackets();
                Budget = Budget- (node.getNeighbor(curr)*100);
                Unvisited.remove(node);
                curr = node;
            }
            else
            {
                continue;
            }
        }
        Route.add(base.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr)*100;

        System.out.println("----------gTSP1------------");
        System.out.println("Route: " + Route);
        System.out.println("Cost: " + Cost);
        System.out.println("Data Collected: " + Prize);
        System.out.println("Budget Reamining: " + Budget);
        System.out.println("---------------------------");

        return Route;
    }

    public List<Integer> gTSP2(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget * 3600; // budget will be given in Kilo Watt Hours, so this converts it to joules
        double Cost = 0;
        int Prize = 0;
        List<Integer> Route = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);

        Node base = nodes.get(0);
        Node curr = base;
        Route.add(base.getID());
        Unvisited.remove(base);

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null)
            {
                Node node = network.prizeCostRatio(curr, Budget, shortestPaths, Unvisited);
                if(node == null)
                {
                    break;
                }
                Route.add(node.getID());
                Cost = Cost + node.getNeighbor(curr);
                Prize = Prize + node.getDataPackets();
                Budget = Budget- node.getNeighbor(curr)*100;
                Unvisited.remove(node);
                curr = node;
            }
            Route.add(base.getID());
            Cost = Cost + shortestPaths.get(curr);
            Budget = Budget - shortestPaths.get(curr)*100;

            System.out.println("----------gTSP2------------");
            System.out.println("Route: " + Route);
            System.out.println("Cost: " + Cost);
            System.out.println("Data Collected: " + Prize);
            System.out.println("Budget Reamining: " + Budget);
            System.out.println("---------------------------");

            return Route;
    }

    public List<Integer> gYang(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget * 3600; // convert budget to Joules
        double Cost = 0;
        int Prize = 0;
        List<Integer> Route = new ArrayList<>();
        HashSet<Node> Unvisited = new HashSet<>(nodes);

        final Node base = nodes.get(0);
        Node curr = base;
        Route.add(base.getID());
        Unvisited.remove(base);

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr);

        List<Node> sortedNodes = new ArrayList<>(Unvisited);
        // Sort nodes by distance to current node
        sortedNodes.sort((n1, n2) -> Double.compare(base.getNeighbor(n1), base.getNeighbor(n2)));

        for (Node node : sortedNodes) 
        {
            if (Unvisited.isEmpty()) 
            {
                break;
            } 
            else if (network.isFeasible(curr, node, Budget, shortestPaths, Unvisited))
            {
                Route.add(node.getID());
                Cost = Cost + node.getNeighbor(curr);
                Prize = Prize + node.getDataPackets();
                Budget = Budget - (node.getNeighbor(curr) * 100);
                Unvisited.remove(node);
                curr = node;
            } 
            else 
            {
                continue;
            }
        }

        Route.add(base.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr) * 100;

        System.out.println("-----------Yang--------------");
        System.out.println("Route: " + Route);
        System.out.println("Cost: " + Cost);
        System.out.println("Data Collected: " + Prize);
        System.out.println("Budget Reamining: " + Budget);
        System.out.println("-----------------------------");

        return Route;

    }

    


}
