import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class NetworkToText
 {

  public void writeToFile(List<Node> nodes, String filename) throws IOException 
  {
    FileWriter writer = new FileWriter(filename);

    try 
    {
      for (Node node : nodes) 
      {
        int nodeId = node.getID();
        int x = node.getX();
        int y = node.getY();
        int dataPackets = node.getDataPackets();
        writer.write(String.format("%d %d %d %d\n", nodeId, x, y, dataPackets));
      }
    } 
    finally 
    {
      writer.close();
    }
  }
}