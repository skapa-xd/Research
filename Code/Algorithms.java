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

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null)
        {
            Node node = network.yangsNode(curr, Budget, shortestPaths, Unvisited);
            if (node == null) 
            {
                break;
            } 
            
            Route.add(node.getID());
            Cost = Cost + node.getNeighbor(curr);
            Prize = Prize + node.getDataPackets();
            Budget = Budget - (node.getNeighbor(curr) * 100);
            Unvisited.remove(node);
            curr = node;
            
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

    public void MARL(List<Node> nodes, int start, double budget, int episodes, int agents, double learningRate, double discountFactor, double tradeoff)
    {
        Network network = new Network();
        Table table = new Table(nodes, nodes.size(), learningRate, discountFactor);
        List<Agent> allAgents = new ArrayList<>();

       
        HashMap<Node, Double> shortestPaths = new HashMap<>(network.findShortestPaths(nodes, nodes.get(0)));
    
        
        Node base = nodes.get(start);
        
        // learning stage
        for(int episode = 0; episode<episodes; episode++)
        {
            for(int agent = 0; agent<agents; agent++)
            {
                Agent a = new Agent(base, budget, false, nodes, shortestPaths);
                allAgents.add(a);
            }

            while(network.isAnyAgentActive(allAgents))
            {
                for(Agent agent : allAgents)
                {
                    if(agent.isDone()== false)
                    {
                        if(agent.agentFeasibleSet().isEmpty())
                        {
                            agent.updateDone(true);
                            agent.addNode(base);
                            agent.addCost(agent.getCurrent(), base);
                            agent.updateBudget(agent.getCurrent(), base);
                            table.updateQvalue(agent.getCurrent().getID(), base.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                            agent.updateCurrent(base);
                        }
                        else
                        {
                            Node node = network.nodeSelection(tradeoff, agent, table, shortestPaths);
                            if (node != null) // Check if node is not null
                            {
                                agent.addNode(node);
                                agent.addCost(agent.getCurrent(), node);
                                agent.updateBudget(agent.getCurrent(), node);
                                agent.updateData(node);
                                table.updateQvalue(agent.getCurrent().getID(), node.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                                agent.updateUnvisited(node);
                                agent.updateCurrent(node);
                            }
                        }
                    }
                }
            }
            Agent best = network.bestAgent(allAgents);
            for(int i = 0; i< best.getRoute().size()-1; i++)
            {
                int state = best.getRoute().get(i);
                int action = best.getRoute().get(i+1);

                table.updateRValue(state, action, best.getDataPackets());
                table.updateQvalue2(state, action);
            }
            
            
            System.out.println("Learning");
            allAgents.clear(); // clears list of agents 
        }

        // execution stage
        Agent bestAgent = new Agent(base, budget, false, nodes, shortestPaths);

        while(bestAgent.isDone()!= true)
        {
            Node curr = bestAgent.getCurrent();
            List<Node> feasible = new ArrayList<>(bestAgent.agentFeasibleSet());

            if(feasible.size()==0)
            {
                if(curr !=  base)
                {
                    bestAgent.addNode(base);
                    bestAgent.addCost(curr, base);
                    bestAgent.updateBudget(curr, base);
                    bestAgent.updateCurrent(base);
                }
                else
                {
                    bestAgent.updateDone(true);
                
                }
            }
            else
            {
                Node next = nodes.get((int)table.getQmaxActionValue(curr.getID(), bestAgent.getBudget(), bestAgent.getUnvisited(), shortestPaths)[0]);
                bestAgent.addNode(next);
                bestAgent.updateUnvisited(next);
                bestAgent.addCost(curr, next);
                bestAgent.updateBudget(curr, next);
                bestAgent.updateData(next);
                bestAgent.updateCurrent(next);
            }
        }

        System.out.println("----------MARL------------");
        System.out.println("Route: " + bestAgent.getRoute());
        System.out.println("Cost: " + bestAgent.getCost());
        System.out.println("Data Collected: " + bestAgent.getDataPackets());
        System.out.println("Budget Reamining: " + bestAgent.getBudget());
        System.out.println("---------------------------");

    }


}
