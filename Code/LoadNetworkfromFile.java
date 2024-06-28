import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadNetworkfromFile 
{
   

    public List<Node> loadNetwork(String fileName)
    {
        List<Node> nodes = new ArrayList<>();
        try
        {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] nodeInfo = line.split(" ");
                int id = Integer.parseInt(nodeInfo[0]);
                int x = Integer.parseInt(nodeInfo[1]);
                int y = Integer.parseInt(nodeInfo[2]);
                int dataPackets = Integer.parseInt(nodeInfo[3]);
                if(dataPackets > 0)
                {
                    nodes.add(new Node(id, x, y, true, dataPackets));
                }
                else
                {
                    nodes.add(new Node(id, x, y, false, 0));
                }
            }
            scanner.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File not found");
        }
        return nodes;
    }
    

    
}
