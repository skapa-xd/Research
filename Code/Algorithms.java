import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class Algorithms 
{
    /* COVERING SALESMAN SOLUTION 1, transmission range is considered and neighbor nodes are considered here. Here the robot will always go to the node with highest data packets
    (self + unvisited neighbor nodes) just like tsp1 */
    public List<Integer> greedy1CSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget*3600; // convert from Watt Hours to Joules
        List<Integer> Route  = new ArrayList<>(); // route of the robot
        HashSet<Node> Unvisited = new HashSet<>(nodes); // set of nodes the robot has yet to visit
        HashSet<Node> Collected = new HashSet<>(); // set of nodes who's data packets are collected
        int Prize = 0; // total data packets collected
        double Cost = 0; // total data packets collected

        Node s = nodes.get(start); // get start node
        Node curr = nodes.get(start); // get curr node
        Unvisited.remove(curr); // remove the curr node, i.e start node
        Route.add(curr.getID()); // add to route
        Collected.add(curr); // add to collected (just added no data packets at start)

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr); // gives shortest paths from curr/start nodes to all nodes

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null) // run untill all nodes visited or no feasible nodes available from current node
        {
            Node node = network.bestPrizeNodeCSP(curr, Budget, shortestPaths, Unvisited, Collected); // gives the best next node (max data packets from self + neighbor)
            if(node == null)
            {
                break; // all nodes visited or collected
            }
            Route.add(node.getID()); 
            Cost = Cost + node.getNeighbor(curr);
            int currPrize = 0;
            double battery = node.getNodeBattery(); // adjust battery of nodes, because transimission of data packets require energy
            for(Node n : node.getRangeNeighborList()) // check the collected to avoid adding repeated data packets
            {
                if(Collected.contains(n))
                {
                    continue;
                }
                currPrize = currPrize + n.getDataPackets();
                battery = battery - (100*n.getDataPackets()*Math.pow(n.getNeighbor(node),2)*3200)/1000000000000L; // ??TODO?? HAVE TO ADD THE EQUATION OF CONVERSION HERE
            }
            node.setNodeBattery(battery); // update the battery
            if(!Collected.contains(node))
            {
                currPrize = currPrize + node.getDataPackets();
            }
            Prize = Prize  + currPrize;
            Budget = Budget - node.getNeighbor(curr)*100 - 2*100*currPrize*3200/1000000000; // here we are deducting the battery consumption of the robot to receive within range
            Collected.add(node); 
            Unvisited.remove(node);
            Collected.addAll(node.getRangeNeighborList()); 
            curr = node;
        }
        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr)*100; // cost to travel 1m = 100J

        System.out.println("----------gCSP1------------");
        System.out.println("Route: " + Route);
        System.out.println("Cost: " + Cost);
        System.out.println("Data Collected: " + Prize);
        System.out.println("Collected from :" + Collected.size());
        System.out.println("Budget Reamining: " + Budget/3600);
        System.out.println("Budget USed: " + (budget*3600 - Budget)/3600);  // conversion back to WH
        System.out.println("---------------------------");
       
        return Route;
    }

    /* COVERING SALESMAN SOLUTION 2, transmission range is considered and neighbor nodes are considered here. Here the robot will always go to the node with higest prize cost ratio,
     ratio is calculated by total prize at node (self + neighboring) / distance to reach node */
    public List<Integer> greedy2CSP(List<Node> nodes, int start, double budget)
    {
        Network network = new Network();

        double Budget = budget*3600; // convert from Watt Hours to Joules
        List<Integer> Route  = new ArrayList<>(); // route of the robot
        HashSet<Node> Unvisited = new HashSet<>(nodes); // set of nodes the robot has yet to visit
        HashSet<Node> Collected = new HashSet<>(); // set of nodes who's data packets are collected
        int Prize = 0; // total data packets collected
        double Cost = 0; // total data packets collected

        
        Node s = nodes.get(start); // get start node
        Node curr = nodes.get(start); // get curr node
        Unvisited.remove(curr); // remove the curr node, i.e start node
        Collected.add(curr); // add to collected (just added no data packets at start)
        Route.add(curr.getID()); // add to route
        

        HashMap<Node, Double> shortestPaths = network.findShortestPaths(nodes, curr); // gives shortest paths from curr/start nodes to all nodes

        while(!Unvisited.isEmpty() && network.feasibleSet(curr, Budget, shortestPaths, Unvisited)!= null) // run untill all nodes visited or no feasible nodes available from current node
        {
            Node node = network.bestPrizeCostRatioNodeCSP(curr, Budget, shortestPaths, Unvisited, Collected); // gives the best next node max(data packets from self + neighbor)/distance
        
            if(node == null)
            {
                break; // all nodes visited or collected
            }
            Route.add(node.getID());
            Cost = Cost + node.getNeighbor(curr);
            int currPrize = 0;
            double battery = node.getNodeBattery(); // adjust battery of nodes, because transimission of data packets require energy
            for(Node n : node.getRangeNeighborList()) // check the collected to avoid adding repeated data packets
            {
                if(Collected.contains(n))
                {
                    continue;
                }
                currPrize = currPrize + n.getDataPackets();
                battery = battery - (100*n.getDataPackets()*Math.pow(n.getNeighbor(node),2)*3200)/1000000000000L; // ??TODO?? HAVE TO ADD THE EQUATION OF CONVERSION HERE
            }
            node.setNodeBattery(battery); // update the battery 
            if(!Collected.contains(node))
            {
                currPrize = currPrize + node.getDataPackets();
            }
            Budget = Budget- node.getNeighbor(curr)*100 -2*100*currPrize*3200/1000000000; // here we are deducting the battery consumption of the robot to receive within range
            Prize = Prize + currPrize;
            Collected.add(node);
            Collected.addAll(node.getRangeNeighborList());
            Unvisited.remove(node);
            curr = node;
        }
        Route.add(s.getID());
        Cost = Cost + shortestPaths.get(curr);
        Budget = Budget - shortestPaths.get(curr)*100; // cost to travel 1m = 100J

        System.out.println("----------gCSP2------------");
        System.out.println("Route: " + Route);
        System.out.println("Cost: " + Cost);
        System.out.println("Data Collected: " + Prize);
        System.out.println("Collected from :" + Collected.size());
        System.out.println("Budget Reamining: " + Budget/3600);
        System.out.println("Budget USed: " + (budget*3600 - Budget)/3600); // conversion back to WH
        System.out.println("---------------------------");

        return Route;    
    }

    // GEOMETRIC TSP 1, robot will always go to the node with highest data packets
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

        List<Node> sortedNodes = new ArrayList<>(Unvisited); // sort the nodes in descending order so that it is easy to track and traverse
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
        System.out.println("Budget Reamining: " + Budget/3600);
        System.out.println("Budget USed: " + (budget*3600 - Budget)/3600);
        System.out.println("---------------------------");

        return Route;
    }

    // GEOMETRIC TSP 2, robot will always go to the node with highest prize cost ration calculated as data packets / distance
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
                Node node = network.prizeCostRatio(curr, Budget, shortestPaths, Unvisited); // gets the node with highest prize cost ratio and selects it as next node
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
            System.out.println("Budget Reamining: " + Budget/3600);
            System.out.println("Budget USed: " + (budget*3600 - Budget)/3600);
            System.out.println("---------------------------");

            return Route;
    }

    public List<Edge> nYangs(List<Node> nodes,int start, double budget)
    {
        int Prize = 0;
        Set<Node> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        Node startingNode = nodes.get(start);
        double Budget = budget * 3600;
        double totalCost = 0;

        visited.add(startingNode);

        // minimum spanning tree algorithm
        while (visited.size() < nodes.size()) {
            Edge minEdge = null;
            double minCost = Double.MAX_VALUE;
            for (Node node : visited) {
                for (Node neighbor : node.getNeighborsList()) {
                    if (!visited.contains(neighbor)) {
                        double cost = node.getNeighbor(neighbor);
                        if (cost < minCost) {
                            minCost = cost;
                            minEdge = new Edge(node, neighbor, cost);
                        }
                    }
                }
            }
            if (minEdge != null && (totalCost + minEdge.cost*100)*2 <= Budget) {
                totalCost += minEdge.cost*100;
                visited.add(minEdge.end);
                mstEdges.add(minEdge);
                Prize = Prize + minEdge.end.getDataPackets();
            } 
            else 
            {
                break; 
            }
        }
        
        List<Integer> Route = new ArrayList<>();
        for(Node n : visited)
        {
            Route.add(n.getID());
        }

        System.out.println("-----------Yang--------------");
        System.out.println("Route: " + Route); //THIS ROUTE IS NOT IN ORDER
        System.out.println("Cost: " + totalCost/100);
        System.out.println("Data Collected: " + Prize);
        System.out.println("Budget Reamining: " + Budget/3600);
        System.out.println("Budget USed: " + (budget*3600 - Budget)/3600);
        System.out.println("-----------------------------");

        return mstEdges; // RETURNS list of Edge (use this class)
    }

    // Multi-Agent Reinforcement Learning Algorithm
    public List<Integer> MARL(List<Node> nodes, int start, double budget, int episodes, int agents, double learningRate, double discountFactor, double tradeoff)
    {
        Network network = new Network();
        Table table = new Table(nodes, nodes.size(), learningRate, discountFactor); // initialize a Q table, R table with size of nodes list
        List<Agent> allAgents = new ArrayList<>();

       
        HashMap<Node, Double> shortestPaths = new HashMap<>(network.findShortestPaths(nodes, nodes.get(start)));
        Node base = nodes.get(start);
        
        // LEARNING STAGE
        // in this stage the agents update the Q table and R table 
        for(int episode = 0; episode<episodes; episode++)
        {
            for(int agent = 0; agent<agents; agent++) // initializes agents 
            {
                Agent a = new Agent(base, budget, false, nodes, shortestPaths);
                allAgents.add(a);
            }

            while(network.isAnyAgentActive(allAgents)) // works until all are finished
            {
                for(Agent agent : allAgents)
                {
                    if(agent.isDone()== false)
                    {
                        if(agent.agentFeasibleSet().isEmpty()) // if no feasible node then agent terminates
                        {
                            agent.updateDone(true);
                            agent.addNode(base);
                            agent.addCost(agent.getCurrent(), base);
                            agent.updateBudget(agent.getCurrent(), base);
                            table.updateQvalue(agent.getCurrent().getID(), base.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                            agent.updateCurrent(base);
                        }
                        else // take a random variable from 0 to 1 and decide the action, either EXPLORE or EXPLOIT
                        {
                            Random random = new Random();
                            double rand = random.nextDouble();
                            Node next;

                            if(rand <= tradeoff)
                            {
                                next = network.exploitation(agent, table).entrySet().iterator().next().getKey(); // this is a linked hashmap, in linked hashmap you can store the key value pair in asc/desc order

                            }
                            else
                            {
                                next = network.exploration(agent, table);
                            }
                                agent.addNode(next);
                                agent.addCost(agent.getCurrent(), next);
                                agent.updateBudget(agent.getCurrent(), next);
                                agent.updateData(next);
                                table.updateQvalue(agent.getCurrent().getID(), next.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                                agent.updateUnvisited(next);
                                agent.updateCurrent(next);  
                        }
                    }
                }
            }
            // at end of every episode the best agent (collected more data packets) will be selected and the route will be reflected in Q table 
            Agent best = network.bestAgent(allAgents);
            for(int i = 0; i< best.getRoute().size()-1; i++)
            {
                int state = best.getRoute().get(i);
                int action = best.getRoute().get(i+1);

                table.updateRValue(state, action, best.getDataPackets());
                table.updateQvalue2(state, action, best.getBudget(), best.getUnvisited(), shortestPaths);
            }
            allAgents.clear(); // clears list of agents 
        }

        //EXECUTION STAGE, here we just chose the max value from q table and choose the corresponding action
        Agent bestAgent = new Agent(base, budget, false, nodes, shortestPaths);

        while(bestAgent.agentFeasibleSet().size()!=0)
        {
            Node next = table.getQMaxValue(bestAgent.getCurrent().getID(), bestAgent.getBudget(), bestAgent.getUnvisited(), shortestPaths);
            //System.out.println("Current" + bestAgent.getCurrent().getID() + "Next" + next.getID() + "Budget" + bestAgent.getBudget());
            bestAgent.addNode(next);
            bestAgent.addCost(bestAgent.getCurrent(), next);
            bestAgent.updateBudget(bestAgent.getCurrent(), next);
            bestAgent.updateData(next);
            bestAgent.updateUnvisited(next);
            bestAgent.updateCurrent(next);  
        }

        bestAgent.addNode(base);
        bestAgent.addCost(bestAgent.getCurrent(), base);
        bestAgent.updateBudget(bestAgent.getCurrent(), base);
        
        System.out.println("----------MARL------------");
        System.out.println("Route: " + bestAgent.getRoute());
        System.out.println("Cost: " + bestAgent.getCost());
        System.out.println("Data Collected: " + bestAgent.getDataPackets());
        System.out.println("Budget Reamining: " + bestAgent.getBudget()/3600);
        System.out.println("Budget USed: " + (budget*3600 - bestAgent.getBudget())/3600);
        System.out.println("---------------------------");

        return bestAgent.getRoute(); 
    }

    public List<Integer> MARLv1(List<Node> nodes, int start, double budget, int episodes, int agents, double learningRate, double discountFactor, double tradeoff)
    {
        Network network = new Network();
        Table table = new Table(nodes, nodes.size(), learningRate, discountFactor);
        //table.printTable();
        List<Agent> allAgents = new ArrayList<>();

       
        HashMap<Node, Double> shortestPaths = new HashMap<>(network.findShortestPaths(nodes, nodes.get(start)));
       
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
                            Random random = new Random();
                            double rand = random.nextDouble();
                            Node next;

                            if(rand <= tradeoff)
                            {
                                next = network.exploitation(agent, table).entrySet().iterator().next().getKey();

                            }
                            else{
                                next = network.exploration(agent, table);
                            }
                                agent.addNode(next);
                                agent.addCost(agent.getCurrent(), next);
                                agent.updateBudget(agent.getCurrent(), next);
                                agent.updateData(next);
                                //System.out.println("For state," + agent.getCurrent().getID() +" action "+ next.getID() + "Before: " + table.getQvalue(agent.getCurrent().getID(), next.getID()) + " Episode: " + episode);
                                table.updateQvalue(agent.getCurrent().getID(), next.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                               // System.out.println("For state," + agent.getCurrent().getID() +" action "+ next.getID() + "After: " + table.getQvalue(agent.getCurrent().getID(), next.getID()) + " Episode: " + episode);
                                agent.updateUnvisited(next);
                                agent.updateCurrent(next);
                            
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
                //System.out.println("R value" + table.getRvalue(state, action)+ " Episode: " + episode);
                table.updateQvalue2(state, action, best.getBudget(), best.getUnvisited(), shortestPaths);
                //System.out.println("Q value" + table.getQvalue(state, action) + " Episode : " + episode);
            }
    
            
            allAgents.clear(); // clears list of agents 
        }

        //execution stage
        Agent bestAgent = new Agent(base, budget, false, nodes, shortestPaths);


        while(bestAgent.agentFeasibleSet().size()!=0)
        {
            Node next = table.getQMaxValue(bestAgent.getCurrent().getID(), bestAgent.getBudget(), bestAgent.getUnvisited(), shortestPaths);
            //System.out.println("Current" + bestAgent.getCurrent().getID() + "Next" + next.getID() + "Budget" + bestAgent.getBudget());
            bestAgent.addNode(next);
            bestAgent.addCost(bestAgent.getCurrent(), next);
            bestAgent.updateBudget(bestAgent.getCurrent(), next);
            bestAgent.updateData(next);
            bestAgent.updateUnvisited(next);
            bestAgent.updateCurrent(next);  
        }
        bestAgent.addNode(base);
        bestAgent.addCost(bestAgent.getCurrent(), base);
        bestAgent.updateBudget(bestAgent.getCurrent(), base);
        

        System.out.println("----------MARL------------");
        System.out.println("Route: " + bestAgent.getRoute());
        System.out.println("Cost: " + bestAgent.getCost());
        System.out.println("Data Collected: " + bestAgent.getDataPackets());
        System.out.println("Budget Reamining: " + bestAgent.getBudget()/3600);
        System.out.println("Budget USed: " + (budget*3600 - bestAgent.getBudget())/3600);
        System.out.println("---------------------------");

        return bestAgent.getRoute(); 

    }

    public List<Integer> MARLv2(List<Node> nodes, int start, double budget, int episodes, int agents, double learningRate, double discountFactor, double tradeoff)
    {
        Network network = new Network();
        Table table = new Table(nodes, nodes.size(), learningRate, discountFactor);
        //table.printTable();
        List<Agent> allAgents = new ArrayList<>();

       
        HashMap<Node, Double> shortestPaths = new HashMap<>(network.findShortestPaths(nodes, nodes.get(start)));
       
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
                            Random random = new Random();
                            double rand = random.nextDouble();
                            Node next;

                            if(rand <= tradeoff)
                            {
                                next = network.exploitation(agent, table).entrySet().iterator().next().getKey();

                            }
                            else{
                                next = network.exploration(agent, table);
                            }
                                agent.addNode(next);
                                agent.addCost(agent.getCurrent(), next);
                                agent.updateBudget(agent.getCurrent(), next);
                                agent.updateData(next);
                                //System.out.println("For state," + agent.getCurrent().getID() +" action "+ next.getID() + "Before: " + table.getQvalue(agent.getCurrent().getID(), next.getID()) + " Episode: " + episode);
                                table.updateQvalue(agent.getCurrent().getID(), next.getID(), agent.getBudget(), agent.getUnvisited(), shortestPaths);
                               // System.out.println("For state," + agent.getCurrent().getID() +" action "+ next.getID() + "After: " + table.getQvalue(agent.getCurrent().getID(), next.getID()) + " Episode: " + episode);
                                agent.updateUnvisited(next);
                                agent.updateCurrent(next);
                            
                        }
                    }
                }
            }
            Agent best = network.bestAgent1(allAgents);
            for(int i = 0; i< best.getRoute().size()-1; i++)
            {
                int state = best.getRoute().get(i);
                int action = best.getRoute().get(i+1);

                table.updateRValue(state, action, best.getDataPackets());
                //System.out.println("R value" + table.getRvalue(state, action)+ " Episode: " + episode);
                table.updateQvalue2(state, action, best.getBudget(), best.getUnvisited(), shortestPaths);
                //System.out.println("Q value" + table.getQvalue(state, action) + " Episode : " + episode);
            }
    
            
            allAgents.clear(); // clears list of agents 
        }

        //execution stage
        Agent bestAgent = new Agent(base, budget, false, nodes, shortestPaths);


        while(bestAgent.agentFeasibleSet().size()!=0)
        {
            Node next = table.getQMaxValue(bestAgent.getCurrent().getID(), bestAgent.getBudget(), bestAgent.getUnvisited(), shortestPaths);
            //System.out.println("Current" + bestAgent.getCurrent().getID() + "Next" + next.getID() + "Budget" + bestAgent.getBudget());
            bestAgent.addNode(next);
            bestAgent.addCost(bestAgent.getCurrent(), next);
            bestAgent.updateBudget(bestAgent.getCurrent(), next);
            bestAgent.updateData(next);
            bestAgent.updateUnvisited(next);
            bestAgent.updateCurrent(next);  
        }
        bestAgent.addNode(base);
        bestAgent.addCost(bestAgent.getCurrent(), base);
        bestAgent.updateBudget(bestAgent.getCurrent(), base);
        

        System.out.println("----------MARL------------");
        System.out.println("Route: " + bestAgent.getRoute());
        System.out.println("Cost: " + bestAgent.getCost());
        System.out.println("Data Collected: " + bestAgent.getDataPackets());
        System.out.println("Budget Reamining: " + bestAgent.getBudget()/3600);
        System.out.println("Budget USed: " + (budget*3600 - bestAgent.getBudget())/3600);
        System.out.println("---------------------------");

        return bestAgent.getRoute(); 

    }

    public int getHoursTSP2(List<Node> nodes, int min, int max)
    {
        int hours = 0;
        
        Network network = new Network();
        while(network.isNodeDead(nodes) == false)
        {
            for(Node n : nodes)
            {
                int data = network.getRandomNumberUsingNextInt(min, max);
                n.setDataPackets(data);
                double battery = n.getNodeBattery();
                battery = battery - data*0.108;
                n.setNodeBattery(battery);              
            }
            hours++;
            gTSP2(nodes, 0, 800);
        }
        return hours;
    }

    public int getHoursCSP2(List<Node> nodes, int min, int max)
    {
        int hours = 0;
    
        
        Network network = new Network();
        network.resetNodeBattery(nodes);
        while(network.isNodeDead(nodes) == false)
        {
            for(Node n : nodes)
            {
                int data = network.getRandomNumberUsingNextInt(min, max);
                n.setDataPackets(data);
                double battery = n.getNodeBattery();
                
                battery = battery - data*0.108;

                n.setNodeBattery(battery);              
            }
            network.setTotalPrizeNode(nodes);
            hours++;
            greedy2CSP(nodes, 0, 800);
        }
        return hours;
    }
}
