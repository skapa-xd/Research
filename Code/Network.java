import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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

    public void addEdges(List<Node> nodes, int Tr, int radius) // creates the graph
    {
        for (int i = 0; i < nodes.size(); i++) 
        {
            for (int j = i + 1; j < nodes.size(); j++) 
            { 
                double d =  Math.round(distance(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(j).getX(), nodes.get(j).getY()) * 100.0) / 100.0;
                if (d <= Tr)  // if d is less than transmission range, there is a connection, here Tr is transmission range of sensor nodes.
                {
                    nodes.get(i).setNeighbor(nodes.get(j), d);
                    nodes.get(j).setNeighbor(nodes.get(i), d);  
                }
                if(d<= radius) // FOR CSP
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
            if(budget >= node.getNeighbor(current) + shortestPaths.get(node) && unvisited.contains(node))
            {
                feasible.add(node);
            }
        }
        return feasible;
    }

    public Node prizeCostRatio(Node current, double budget, HashMap<Node, Double> shortestPaths, HashSet<Node> unvisited)
    {
        Node bestNode = null;
        double bestRatio = Double.NEGATIVE_INFINITY;

        for(Node node: current.getNeighborsList())
        {
            if(feasibleSet(current, budget, shortestPaths, unvisited).contains(node))
            {
                double ratio = (double)node.getDataPackets()/node.getNeighbor(current);
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
                    int prize = node.getTotalDataPackets();
                    if(collected.contains(node))
                    {
                        prize = prize - node.getDataPackets();
                    }
                    for(Node n : node.getRangeNeighborList())
                    {
                        if(collected.contains(n))
                        {
                            prize = prize - n.getDataPackets();
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

   

    
    
    
    
    
}
    

