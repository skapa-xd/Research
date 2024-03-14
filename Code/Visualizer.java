import java.awt.BasicStroke;
import java.awt.Color;
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
    private List<Integer> route1;
    private List<Integer> route2;
    private List<Integer> route3;

    public Visualizer(List<Node> nodes, double width, double height, List<Integer> route1, List<Integer> route2, List<Integer> route3) {
        this.nodes = nodes;
        this.graphWidth = width;
        this.graphHeight = height;
        this.route1 = route1;
        this.route2 = route2;
        this.route3 = route3;
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
        for (int i = 0; i < graphPoints.size(); i++) {
            double x = graphPoints.get(i).x - (metrics.stringWidth("" + nodes.get(i).getID()) / 2.0);
            double y = graphPoints.get(i).y + ovalSize + textHeight + textPadding;
            g2.drawString("" + nodes.get(i).getID(), (int) x, (int) y);
        }
        
        Stroke stroke = new BasicStroke(2f);
        g2.setStroke(stroke);

        // Draw route 1 (dark yellow)
        g2.setColor(Color.ORANGE);
            for (int j = 0; j < route1.size() - 1; j++) {
            int nodeIndex1 = route1.get(j);
            int nodeIndex2 = route1.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
            }

        // Draw route 2 (dark green)
        g2.setColor(Color.green);
            for (int j = 0; j < route2.size() - 1; j++) {
            int nodeIndex1 = route2.get(j);
            int nodeIndex2 = route2.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
            }

        g2.setColor(Color.MAGENTA);
            for (int j = 0; j < route3.size() - 1; j++) {
            int nodeIndex1 = route3.get(j);
            int nodeIndex2 = route3.get(j + 1);
            Point point1 = graphPoints.get(nodeIndex1);
            Point point2 = graphPoints.get(nodeIndex2);
            g2.drawLine(point1.x, point1.y, point2.x, point2.y);
        }
        
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
