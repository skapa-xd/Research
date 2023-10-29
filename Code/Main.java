import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.PriorityQueue;


public class Main 
{
    
    // Generate Nodes for Sensor Network
    public static List<Node> generateNodes(int width, int length, int sensorNodes, int dataNodes)
    {
        Set<Integer> dataNodeSet = new HashSet<>(); // To store the dataNode ID
        Random random = new Random();
        List<Node> nodes = new ArrayList<>();
        
        while (dataNodeSet.size() < dataNodes) { // Adds all dataNodes in dataNodeSet
            int randomNode = random.nextInt(1, sensorNodes);
            dataNodeSet.add(randomNode);
        }
        for (int i = 0; i < sensorNodes; i++) {
            int x = random.nextInt(width + 1); // Generate a random x-coordinate
            int y = random.nextInt(length + 1); // Generate a random y-coordinate
            Node node;
            int data = random.nextInt(500, 1000); // DataNodes can hold dataPackets from 500 to 1000

            if (dataNodeSet.contains(i)) {
                node = new Node(x, y, true, data); // Data node
            } else {
                node = new Node(x, y, false, 0); // Not a Data node
            }
            node.setID(i);
            nodes.add(node);
        }
        return nodes;
    }
    
    // Calculate the Euclidiean Distance
    public static double distance(double x1, double y1, double x2, double y2)
    {
        return (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }

    // Transmission Cost
    public static double transmissionCost(double distance) 
    {
        double elec = 100; 
        double amp = 0.1;
        double k = 3200;
        return (2 * elec * k) + (amp * k * Math.pow(distance, 2));
    }

    // Add Edges
    public static List<List<Edge>> addEdges(List<Node> nodes, double Tr) 
    {
        int sensorNodes = nodes.size();
        Edge edge, edge1; 
        List<List<Edge>> adjacencyList = new ArrayList<>(sensorNodes);

        for (int i = 0; i < sensorNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (int i = 0; i < sensorNodes; i++) {
            for (int j = i + 1; j < sensorNodes; j++) {
                double d = distance(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(j).getX(), nodes.get(j).getY());
                if (d <= Tr) {
                    
                    double tr = transmissionCost(d);
                    edge = new Edge(i,j,tr);
                    edge1 = new Edge(j,i, tr);
                    adjacencyList.get(i).add(edge);
                    adjacencyList.get(j).add(edge1);
                }
            }
        }
        return adjacencyList;
    }

    // Create an adjaceny list
    public static Map<Integer, List<Integer>>createAdjacencyList(List<Node> nodes, List<List<Edge>> edges)
    {
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            adjList.put(i, new ArrayList<>());
        }

        for (List<Edge> edgeList : edges) {
            for (Edge edge : edgeList) {
                int src = edge.from;
                int dst = edge.to;
                adjList.get(src).add(dst);
                
            }
        } 
        return adjList;   
    }

    // Check Connectivity
    public static boolean isConnected(List<Node> nodes,Map<Integer, List<Integer>> edges )
    {
        int n = nodes.size();
        Set<Integer> visited = new HashSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(0);

        while (!stack.isEmpty()) {
            int node = stack.pop();
            visited.add(node);

            for (int neighbor : edges.get(node)) {
                if (!visited.contains(neighbor)) {
                    stack.push(neighbor);
                }
            }
        }
        if (visited.size() == n) {
            return true;
        } else {
            return false;
        }
    }

    // Shortest Path
    public static List<Integer> dijkstra(List<List<Edge>> adjacencyList, int start, int end) {
        int n = adjacencyList.size();
        double[] distance = new double[n];
        int[] previous = new int[n];
        Arrays.fill(distance, Integer.MAX_VALUE);
        Arrays.fill(previous, -1);
        distance[start] = 0;

        PriorityQueue<Integer> minHeap = new PriorityQueue<>(n, Comparator.comparingDouble(i -> distance[i]));
        minHeap.offer(start);

        while (!minHeap.isEmpty()) {
            int currentNode = minHeap.poll();

            for (Edge edge : adjacencyList.get(currentNode)) {
                int neighbor = edge.getTO();
                double weight = edge.getTr();

                double newDistance = distance[currentNode] + weight;
                if (newDistance < distance[neighbor]) {
                    distance[neighbor] = newDistance;
                    previous[neighbor] = currentNode;
                    minHeap.offer(neighbor);
                }
            }
        }

        if (distance[end] == Integer.MAX_VALUE) {
            return null; // No path found.
        }

        List<Integer> shortestPath = new ArrayList<>();
        int currentNode = end;

        while (currentNode != -1) {
            shortestPath.add(currentNode);
            currentNode = previous[currentNode];
        }

        Collections.reverse(shortestPath);
        return shortestPath;
    }

    







    
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        List<List<Edge>> edges = new ArrayList<>();
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        boolean connected;
        List<Integer> ans = new ArrayList<>();

        nodes = generateNodes(10, 10, 10, 5);
        edges = addEdges(nodes, 7);
        adjList = createAdjacencyList(nodes, edges);
        connected = isConnected(nodes, adjList);
        ans = dijkstra(edges, 1, 9);


        System.out.println(connected);
        for (Node node : nodes) {
            System.out.println(node.toString());
        }
        for (int i = 0; i < edges.size(); i++) {
            List<Edge> edge = edges.get(i);
            for (Edge edg : edge) {
                System.out.println("Edge from Node " + i + " to Node " + edg.getTO() + " has transmission cost: " + edg.getTr());
            }
        }
        for (int node : adjList.keySet()) {
            List<Integer> neighbors = adjList.get(node);
            System.out.print("Node " + node + " is adjacent to: ");
            for (int neighbor : neighbors) {
                System.out.print(neighbor + " ");
            }
            System.out.println(); 
        }
        System.out.println(ans);

}
}
    

