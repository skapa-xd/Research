import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

public class Network 
{
    
    public int getRandomNumberUsingNextInt(int min, int max) // helper for the randomness generation
    {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public List<Node> generateNodes(int width, int length, int totalNodes, int tr) // generates nodes randomly in the given sensor network parameters
    {
        Random random = new Random();
        List<Node> nodes = new ArrayList<>(); // stores all nodes 
        nodes.add(new Node(0, 0, false, 0)); // start node at 0,0
    
        for (int i = 1; i < totalNodes; i++) {
            Node node; // create a new node
            int x= 0;
            int y= 0;
            boolean isValidLocation = false;
            
            
            while (!isValidLocation) 
            {
                x = random.nextInt(width + 1); 
                y = random.nextInt(length + 1);
                isValidLocation = true; 

                for (Node existingNode : nodes) 
                {
                    double distance = distance(x, y, existingNode.getX(), existingNode.getY());
                    if (distance <= tr) 
                    {
                        isValidLocation = false; // Too close to an existing node, regenerate
                        break;
                    }
                }
            }
            
            
            int data = getRandomNumberUsingNextInt(0,100); // DataNodes can hold dataPackets from 500 to 1000

            if (data == 0) { 
                node = new Node(x, y, false, data); 
            } else {
                node = new Node(x, y, true, data); 
            }
            nodes.add(node);
        }
        return nodes;
    }

    public double distance(double x1, double y1, double x2, double y2) // Calculates the Euclidian Distance
    {
        return (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2))); 
    }

    public void addEdges(List<Node> nodes, int CSPradius) // creates the graph
    {
        for (int i = 0; i < nodes.size(); i++) 
        {
            for (int j = i + 1; j < nodes.size(); j++) 
            { 
                double d =  Math.round(distance(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(j).getX(), nodes.get(j).getY()) * 100.0) / 100.0;

                
                nodes.get(i).setNeighbor(nodes.get(j), d);
                nodes.get(j).setNeighbor(nodes.get(i), d); 
                     
                
                if(d<= CSPradius) // FOR CSP
                {
                    nodes.get(i).setRangeNeighbor(nodes.get(j), nodes.get(j).getDataPackets());
                    nodes.get(j).setRangeNeighbor(nodes.get(i), nodes.get(i).getDataPackets());
                }
            }
        }
        for(Node node : nodes) // for CSP
        {
            int prize = node.getDataPackets();
            for(Node curr : node.getRangeNeighborList())
            {
                prize = prize + curr.getDataPackets();
            }
            node.setTotalDataPackets(prize);
        }
    
    }

    
    public HashMap<Node, Double> findShortestPaths(List<Node> nodes, Node source) {
        HashMap<Node, Double> distances = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>((n1, n2) -> Double.compare(distances.get(n1), distances.get(n2)));

        // Initialize distances to infinity
        for (Node node : nodes) {
            distances.put(node, Double.POSITIVE_INFINITY);
        }

        distances.put(source, 0.0);
        pq.add(source);

        while (!pq.isEmpty()) {
            Node node = pq.poll();

            for (Node neighbor : node.getNeighborsList()) {
                double newDistance = distances.get(node) + node.getNeighbor(neighbor);
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, Math.round(newDistance *100.0)/100.0);
                    pq.add(neighbor);
                }
            }
        }

        return distances;
    }

    public Set<Node> feasibleSet(Node current,  double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited)
    {
        HashSet<Node> feasible = new HashSet<>();

        for(Node node : current.getNeighborsList())
        {
            if(budget >= (node.getNeighbor(current) + shortestPaths.get(node))*100 && unvisited.contains(node))
            {
                feasible.add(node);
            }
        }
        return feasible;
    }

    public boolean isFeasible(Node curr, Node next,  double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited)
    {
        if(budget >= (curr.getNeighbor(next) + shortestPaths.get(next))*100 && unvisited.contains(next))
        {
            return true;
        }
        return false;
    }

    public Node prizeCostRatio(Node current, double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited)
    {
        Node bestNode = null;
        double bestRatio = Double.NEGATIVE_INFINITY;

        for(Node node: current.getNeighborsList())
        {
            if(isFeasible(current,node, budget, shortestPaths, unvisited))
            {
                double ratio = (double)node.getDataPackets()/current.getNeighbor(node);
                if(ratio>bestRatio)
                {
                    bestRatio = ratio;
                    bestNode = node;
                }
            }
        }

        return bestNode;
    }

     public Node bestPrizeCostRatioNodeCSP(Node current, double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited, HashSet<Node> collected)
    {
        int P = 0;
        Node bestNode = null;
        double bestRatio = Double.NEGATIVE_INFINITY;

        for(Node node : current.getNeighborsList())
        {
            if(feasibleSet(current, budget, shortestPaths, unvisited).contains(node))
            {
                int prize = node.getTotalDataPackets();
                if(collected.contains(node))
                    {
                        prize = prize - node.getDataPackets();
                    }
                double cost = node.getNeighbor(current);
                for(Node n: node.getRangeNeighborList())
                {
                    if(collected.contains(n))
                    {
                        prize = prize - n.getDataPackets();
                    }
                }
                double ratio = prize/cost;
                if(ratio>bestRatio)
                {
                    P = prize;
                    bestRatio = ratio;
                    bestNode = node;
                }
            }
        }
        if(bestNode != null)
        {
            bestNode.setDP2(P);
        }
        return bestNode;
    } 

    public Node bestPrizeNodeCSP(Node current, double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited, HashSet<Node> collected) 
    {
        
        int max = Integer.MIN_VALUE;
        Node best = null;
        for(Node node: current.getNeighborsList())
        {
          
            if(feasibleSet(current, budget, shortestPaths, unvisited).contains(node))
            { 
                    int prize = 0;
                    if(!collected.contains(node))
                    {
                        prize = prize + node.getDataPackets();
                    }
                    for(Node n : node.getRangeNeighborList())
                    {
                        if(!collected.contains(n))
                        {
                            prize = prize + n.getDataPackets();
    
                        }
                    }
                    if(prize > max)
                    {
                        max = prize;
                        best = node;
                       
                    }
            }    
        }
        if(best != null)
        {
            best.setDP(max);
            
        }
        
        return best;
    } 

    public Node yangsNode(Node current, double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited)
    {
        double dist = Double.MAX_VALUE;
        Node bestNode = null;

        for(Node node : current.getNeighborsList())
        {
            if(isFeasible(current, node, budget, shortestPaths, unvisited))
            {
                double d = current.getNeighbor(node);
                if(d<dist)
                {
                    dist = d;
                    bestNode = node;
                }
            }
        }

        return bestNode;
    }

    public Map<Node, Double> exploitation(Agent a, Table table) 
    {

        double delta = 1;
        double beta =2;
        HashSet<Node> feasibleSet = new HashSet<>(a.agentFeasibleSet());
        Node curNode = a.getCurrent();
        Map<Node, Double> d = new HashMap<>();
        for (Node neighbor : feasibleSet) 
        {
            double qVal = table.getQvalue(curNode.getID(), neighbor.getID());
            double cost = curNode.getNeighbor(neighbor);
            double prize = neighbor.getDataPackets();
            double res = (Math.pow(qVal, delta) * prize) / Math.pow(cost, beta);
            d.put(neighbor,res<0?0:res);
        }
        return sortByValueDescending(d);    
    }
    private <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) 
    {
        Map<K, V> sortedMap = new LinkedHashMap<>();
        map.entrySet()
           .stream()
           .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
           .forEachOrdered(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
        return sortedMap;
    }

    public Node exploration(Agent agent, Table table) 
    {
        double delta = 1;
        double beta =2;
        Random random = new Random();
        List<Node> feasibleSet = new ArrayList<>(agent.agentFeasibleSet());
        List<Double> probabilities = new ArrayList<>();
        double total = 0;

        for (Node n : feasibleSet) {
            double Q = table.getQvalue(agent.getCurrent().getID(), n.getID());
            double w = agent.getCurrent().getNeighbor(n);
            double d = n.getDataPackets();
            double prob = Math.pow(Q, delta) * d / Math.pow(w, beta);
            probabilities.add(prob);
            total += prob; // Accumulate total probability
        }

        // Normalize probabilities
        for (int i = 0; i < probabilities.size(); i++) {
            probabilities.set(i, probabilities.get(i) / total);
        }

        double selectedProbability = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (int i = 0; i < probabilities.size(); i++) {
            cumulativeProbability += probabilities.get(i);
            if (selectedProbability <= cumulativeProbability) {
                return feasibleSet.get(i);
            }
        }
        return feasibleSet.get(random.nextInt(0, feasibleSet.size()));
    }

    

    public boolean isAnyAgentActive(List<Agent> agents)
    {
        for(Agent agent : agents)
        {
            if(agent.isDone() == false)
            {
                return true;
            }
        }
        return false;
    }

    public Agent bestAgent(List<Agent> agents)
    {
        Agent best = null;
        int dataPackets = 0;
       
        for(Agent agent : agents)
        {
            int d = agent.getDataPackets();
            if(d>dataPackets)
            {
                dataPackets = d;
                best = agent;
            }
        }

        return best;
    }

   

    
    
    
    
    
}
    

