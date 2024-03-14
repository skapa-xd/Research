public class QTable 
{
    private double[][] QTable;
    private double learningRate;
    private double discountFactor;


    public QTable(int totalNodes, double learningRate, double discountFactor)
    {
        this.QTable = new double[totalNodes][totalNodes];
        this.discountFactor = discountFactor;
        this.learningRate = learningRate;

        // intitalize to 0
        for(int state = 0; state<totalNodes; state++)
        {
            for(int action = 0; action<totalNodes; action++)
            {
                QTable[state][action] = 0.0;
            }
        }
    }

    public double getQvalue(int currentNode, int nextNode)
    {
        if(currentNode == nextNode)
        {
            return 0.0;
        }
        return QTable[currentNode][nextNode];
    }

    public void setQvalue(int currentNode, int nextNode, double value)
    {
        QTable[currentNode][nextNode] = value;
    }

    public int getQmaxAction(int state)
    {
        double max = Double.NEGATIVE_INFINITY;
        int action = 0;

        for(int i = 0; i<QTable.length; i++)
        {
            if(i != state)
            {
                double val = QTable[state][i];
                if(val > max)
                {
                    max = val;
                    action = i;
                }
            }
        }
        return action; 
    }

    public double getQmaxValue(int state)
    {
        double max = Double.NEGATIVE_INFINITY;
       

        for(int i = 0; i<QTable.length; i++)
        {
            if(i != state)
            {
                double val = QTable[state][i];
                if(val > max)
                {
                    max = val;
                }
            }
        }
        return max; 
    }

    
}
