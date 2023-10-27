import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main 
{
    public static class Edge {
        int to;
        double tr;

        public Edge(int to, double tr) {
            this.to = to;
            this.tr = tr;
        }

        public int getTO(){
            return to;
        }
        public double getTr()
        {
            return tr;
        }
    }
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
        for (int i = 1; i <= sensorNodes; i++) {
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
    public static double transmissionCost(double distance) {
        double elec = 100; 
        double amp = 0.1;
        double k = 3200;
        return (2 * elec * k) + (amp * k * Math.pow(distance, 2));
    }

    // Add Edges
    public static List<List<Edge>> findEdges(List<Node> nodes, double Tr) {
        int sensorNodes = nodes.size();
        List<List<Edge>> adjacencyList = new ArrayList<>(sensorNodes);

        for (int i = 0; i < sensorNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }

        for (int i = 0; i < sensorNodes; i++) {
            for (int j = i + 1; j < sensorNodes; j++) {
                double d = distance(nodes.get(i).getX(), nodes.get(i).getY(), nodes.get(j).getX(), nodes.get(j).getY());
                if (d <= Tr) {
                    double tr = transmissionCost(d);
                    adjacencyList.get(i).add(new Edge(j, tr));
                    adjacencyList.get(j).add(new Edge(i, tr));
                }
            }
        }
        return adjacencyList;
    }







    
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        List<List<Edge>> edges = new ArrayList<>();

        nodes = generateNodes(2000, 2000, 150, 50);
        edges = findEdges(nodes, 250);


        for (Node node : nodes) {
            System.out.println(node.toString());
        }
        for (int i = 0; i < edges.size(); i++) {
            List<Edge> edge = edges.get(i);
            for (Edge edg : edge) {
                System.out.println("Edge from Node " + i + " to Node " + edg.getTO() + " has transmission cost: " + edg.getTr());
            }
        }

}
}
    

