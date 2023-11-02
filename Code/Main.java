import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main 
{   
    public static void main(String[] args) 
    {
        SensorNetwork network = new SensorNetwork();
        

        List<Node> nodes = new ArrayList<>();
        List<List<Edge>> edges = new ArrayList<>();
        Map<Integer, List<Integer>> adjList = new HashMap<>();
        boolean connected;
        List<Integer> ans = new ArrayList<>();
        double cost;

        nodes = network.generateNodes(10, 10, 10, 5);
        edges = network.addEdges(nodes, 5);
        connected = network.isConnected(edges);
        ans = network.dijkstra(edges, 1, 9);
        cost = network.totalEnergy(edges, ans);



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
        System.out.println(cost);

}
}
    

