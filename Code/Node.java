public class Node {
    private int id;
    private int x;
    private int y;
    private boolean isDataNode;
    private int dataPackets;
    

    public Node(int x, int y, boolean isDataNode, int dataPackets) {
        
        this.x = x;
        this.y = y;
        this.isDataNode = isDataNode;
        this.dataPackets = dataPackets;
    
    }

    public String toString(){
        String s  = String.format("Node id: %d || x: %4d || y: %4d || is data node: %b || no of data packets: %4d",
        getID(), getX(), getY(), isDataNode(), getDataPackets());
        return s;
    }

    public int getID(){
        return id;
    }
    
    public void setID(int id){
        this.id = id;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int[] getLocation() {
        return new int[]{x, y};
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDataNode() {
        return isDataNode;
    }

    public int getDataPackets() {
        return dataPackets;
    }

}
