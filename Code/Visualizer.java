import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visualizer extends JPanel implements Runnable {

    private static final long serialVersionUID = 1L;

    private List<Node> nodes;
    private double graphWidth;
    private double graphHeight;
    private int scaling = 25;
    private int ovalSize = 6;
    private List<Integer> tsp1;
    private List<Integer> tsp2;
    private List<Integer> yang;
    private List<Integer> marl;
    private List<Integer> csp1;
    private List<Integer> csp2;

    public Visualizer(List<Node> nodes, double width, double height, List<Integer> tsp1, List<Integer> tsp2, List<Integer> yang, List<Integer> marl, List<Integer> csp1, List<Integer> csp2) {
        this.nodes = nodes;
        this.graphWidth = width;
        this.graphHeight = height;
        this.tsp1 = tsp1;
        this.tsp2 = tsp2;
        this.yang = yang;
        this.marl = marl;
        this.csp1 = csp1;
        this.csp2 = csp2;
        invalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((getWidth() - 3 * scaling) / (graphWidth));
        double yScale = ((getHeight() - 3 * scaling) / (graphHeight));

        List<Point> graphPoints = new ArrayList<Point>();
        for (Node node : nodes) {
            double x1 = (node.getX() * (xScale) + (2 * scaling));
            double y1 = ((graphHeight - node.getY()) * yScale + scaling);
            Point point = new Point();
            point.setLocation(x1, y1);
            graphPoints.add(point);
        }

        g2.setColor(Color.white);
        g2.fillRect(2 * scaling, scaling, getWidth() - (3 * scaling), getHeight() - 3 * scaling);

        // Draw ovals in blue
        g2.setColor(Color.blue);
        for (int i = 0; i < graphPoints.size(); i++) {
            double x = graphPoints.get(i).x - ovalSize / 2;
            double y = graphPoints.get(i).y - ovalSize / 2;
            double ovalW = ovalSize;
            double ovalH = ovalSize;
            Ellipse2D.Double shape = new Ellipse2D.Double(x, y, ovalW, ovalH);
            g2.fill(shape);
        }

        // Draw node IDs in red with increased spacing
        g2.setColor(Color.red);
        FontMetrics metrics = g2.getFontMetrics();
        
        int textHeight = metrics.getHeight();
        int textPadding = 3;  // Adjust padding between oval and text

        // Set a larger font size
        Font originalFont = g2.getFont();
        Font largerFont = originalFont.deriveFont(originalFont.getSize() + 6f);
        g2.setFont(largerFont);

        for (int i = 0; i < graphPoints.size(); i++) {
            double x = graphPoints.get(i).x - (metrics.stringWidth("" + nodes.get(i).getID()) / 2.0);
            double y = graphPoints.get(i).y + ovalSize + textHeight + textPadding;
            g2.drawString("" + nodes.get(i).getID(), (int) x, (int) y);
        }

        Stroke stroke = new BasicStroke(2f);
        g2.setStroke(stroke);

        // Draw route 1 (dark yellow)
        /* g2.setColor(Color.green); // gTSP1
            for (int j = 0; j < tsp1.size() - 1; j++) {
            int nodeIndex1 = tsp1.get(j);
            int nodeIndex2 = tsp1.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        }
 */
        // Draw route 2 (dark green)
        /* g2.setColor(Color.orange); // gTSP2
            for (int j = 0; j < tsp2.size() - 1; j++) {
            int nodeIndex1 = tsp2.get(j);
            int nodeIndex2 = tsp2.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        } */

        /* g2.setColor(Color.RED); // yangs
            for (int j = 0; j < yang.size() - 1; j++) {
            int nodeIndex1 = yang.get(j);
            int nodeIndex2 = yang.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        } */

        g2.setColor(Color.BLUE); // marl
            for (int j = 0; j < marl.size() - 1; j++) {
            int nodeIndex1 = marl.get(j);
            int nodeIndex2 = marl.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        }

        /* g2.setColor(Color.MAGENTA); // csp1
            for (int j = 0; j < csp1.size() - 1; j++) 
            {
                int nodeIndex1 = csp1.get(j);
                int nodeIndex2 = csp1.get(j + 1);
                Point point1 = graphPoints.get(nodeIndex1);
                Point point2 = graphPoints.get(nodeIndex2);
                g2.drawLine(point1.x, point1.y, point2.x, point2.y);
            }

            // Draw circles around points in csp1 route
            g2.setColor(Color.gray);
            double averageScale = (xScale + yScale) / 2;
            double circleRadius = 700 * averageScale;
            
            for (int j = 0; j < csp1.size(); j++) {
                int nodeIndex = csp1.get(j);
                Point point = graphPoints.get(nodeIndex);
                double x = point.x - circleRadius / 2;
                double y = point.y - circleRadius / 2;
                double circleW = circleRadius;
                double circleH = circleRadius;
                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, circleW, circleH);
                g2.draw(shape);
            } */

       /*  g2.setColor(Color.CYAN); // csp2
            for (int j = 0; j < csp2.size() - 1; j++) 
            {
                int nodeIndex1 = csp2.get(j);
                int nodeIndex2 = csp2.get(j + 1);
                Point point1 = graphPoints.get(nodeIndex1);
                Point point2 = graphPoints.get(nodeIndex2);
                g2.drawLine(point1.x, point1.y, point2.x, point2.y);
            }
        
            g2.setColor(Color.gray);
            for (int j = 0; j < csp2.size(); j++) {
                int nodeIndex = csp2.get(j);
                Point point = graphPoints.get(nodeIndex);
                double x = point.x - circleRadius;
                double y = point.y - circleRadius;
                double circleW = circleRadius;
                double circleH = circleRadius;
                Ellipse2D.Double shape = new Ellipse2D.Double(x, y, circleW, circleH);
                g2.draw(shape);
            }
 */

            // Helper method to calculate the radius in pixels based on the given radius in meters
          
        
    }

    public void run() {
        String graphName = "Sensor Network Graph";
        JFrame frame = new JFrame(graphName);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
