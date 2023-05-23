import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;


public class GraphDisplay {
    private JFrame frame;
    private LandscapePanel canvas;
    private Graph graph;
    private AbstractPlayerAlgorithm pursuer;
    private AbstractPlayerAlgorithm evader;
    private int gridScale;
    private HashMap<Vertex, Coord> coords;
    private Color pursuerColor;
    private Color evaderColor;
    private Color edgeColor;
    private JLabel statusBar;
    private Timer animationTimer;
    private boolean isAnimating;

    public GraphDisplay(Graph graph, AbstractPlayerAlgorithm pursuer, AbstractPlayerAlgorithm evader, int scale) {
        this.graph = graph;
        this.pursuer = pursuer;
        this.evader = evader;
        this.gridScale = scale;
        this.pursuerColor = Color.BLUE;
        this.evaderColor = Color.YELLOW;
        this.edgeColor = Color.BLACK;

        frame = new JFrame("Graph Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        canvas = new LandscapePanel(graph.size() * scale, graph.size() * scale);
        frame.add(canvas, BorderLayout.CENTER);

        statusBar = new JLabel("Vanish it if you want");
        statusBar.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(statusBar,BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        createCoordinateSystem();
        animationTimer = new Timer(1000, new AnimationListener());
        isAnimating = false;
    
        createButtons();
        repaint(); // Move repaint() here
    }
    
    public LandscapePanel getCanvas() {
        return this.canvas;
    }
    
    
    private void createButtons() {
        JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);
    
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new StartButtonListener());
        buttonPanel.add(startButton);
    
        JButton stopButton = new JButton("Stop");
        stopButton.addActionListener(new StopButtonListener());
        buttonPanel.add(stopButton);
    
        JButton vanishButton = new JButton("Vanish");
        vanishButton.addActionListener(new ResetButtonListener());
        buttonPanel.add(vanishButton);
    }
    
    public void setPursuerColor(Color color) {
        this.pursuerColor = color;
    }
    
    public void setEvaderColor(Color color) {
        this.evaderColor = color;
    }
    
    public void setEdgeColor(Color color) {
        this.edgeColor = color;
    }
    
    public void createCoordinateSystem() {
        Random random = new Random();
        HashMap<Vertex, HashMap<Vertex, Double>> distances = new HashMap<>();
        for (Vertex v : graph.getVertices())
            distances.put(v, graph.distanceFrom(v));
    
        coords = new HashMap<>();
        for (Vertex v : graph.getVertices())
            coords.put(v, new Coord(random.nextInt(canvas.getWidth() / 2) - canvas.getWidth() / 2,
                    random.nextInt(canvas.getHeight() / 2) - canvas.getHeight() / 2));
    
        double step = 1000;
        for (int i = 0; i < 100; i++) {
            HashMap<Vertex, Coord> newCoords = new HashMap<>();
            for (Vertex v : graph.getVertices()) {
                Coord f = new Coord(0, 0);
                boolean pickRandom = false;
                for (Vertex u : graph.getVertices()) {
                    if (u == v)
                        continue;
                    Coord xv = coords.get(v);
                    Coord xu = coords.get(u);
                    if ((Math.abs(xv.x - xu.x) > .1 / i) && (Math.abs(xv.y - xu.y) > .1 / i))
                        f.addBy(xu.diff(xv).scale(
                                (xu.diff(xv).norm() - (distances.get(u).get(v) == Double.POSITIVE_INFINITY ? 1000
                                : distances.get(u).get(v) * 100)) / (xu.diff(xv).norm())));
                    else
                        pickRandom = true;
                }
                if (!pickRandom)
                    newCoords.put(v, f.x == 0 && f.y == 0 ? coords.get(v) : coords.get(v).sum(f.scale(step / f.norm())));
                else
                    newCoords.put(v,
                            new Coord(random.nextInt(canvas.getWidth() / 2) - canvas.getWidth() / 2,
                                    random.nextInt(canvas.getHeight() / 2) - canvas.getHeight() / 2));
            }
            step *= .9;
            Coord average = new Coord(0, 0);
            for (Vertex v : graph.getVertices())
                average.addBy(coords.get(v));
            average = average.scale(1.0 / graph.size());
            for (Coord c : newCoords.values()) {
                c.x -= average.x;
                c.x = Math.min(Math.max(c.x, -canvas.getWidth() / 2), canvas.getWidth() / 2);
                c.y -= average.y;
                c.y = Math.min(Math.max(c.y, -canvas.getHeight() / 2), canvas.getHeight() / 2);
            }
            coords = newCoords;
        }
        Coord average = new Coord(0, 0);
        for (Vertex v : graph.getVertices())
        average.addBy(coords.get(v));
        average = average.scale(1.0 / graph.size());
        double maxNorm = 0;
        for (Vertex v : graph.getVertices()) {
        Coord newCoord = new Coord(coords.get(v).x - average.x, coords.get(v).y - average.y);
        coords.put(v, newCoord);
        maxNorm = Math.max(maxNorm, newCoord.norm());
        }
        for (Vertex v : graph.getVertices())
        coords.put(v, coords.get(v).scale(
        (Math.min(canvas.getWidth() / 2, canvas.getHeight() / 2) - gridScale / 2) / maxNorm));
        int singletonCount = 0;
        for (Vertex v : graph.getVertices()) {
            if (!v.adjacentVertices().iterator().hasNext())
                coords.put(v, new Coord(-canvas.getWidth() / 2 + gridScale * ++singletonCount,
                        -canvas.getHeight() / 2 + gridScale));
        }
    }
    
    public void repaint() {
        canvas.repaint();
    }
    
    private class LandscapePanel extends JPanel {
        public LandscapePanel(int width, int height) {
            super();
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.RED);
        }
    
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
    
            if (pursuer != null && pursuer.getCurrentVertex() == evader.getCurrentVertex())
                setBackground(new Color(0, 255, 0));
    
            g2d.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
    
            // Draw edges
            for (Edge e : graph.getEdges()) {
                g2d.setColor(edgeColor);
                Vertex[] vertices = e.vertices();
                Coord coord1 = coords.get(vertices[0]);
                Coord coord2 = coords.get(vertices[1]);
                g2d.drawLine((int) coord1.x + gridScale / 4, (int) coord1.y + gridScale / 4,
                        (int) coord2.x + gridScale / 4, (int) coord2.y + gridScale / 4);
            }
    
            // Draw vertices
            for (Vertex v : graph.getVertices()) {
                if (pursuer != null && v == pursuer.getCurrentVertex() && v == evader.getCurrentVertex())
                    g2d.setColor(new Color(148, 0, 211));
                else if (pursuer != null && v == pursuer.getCurrentVertex())
                    g2d.setColor(pursuerColor);
                else if (evader != null && v == evader.getCurrentVertex())
                    g2d.setColor(evaderColor);
                else
                    g2d.setColor(Color.BLACK);
                int vertexSize = gridScale / 2;
                int x = (int) coords.get(v).x;
                int y = (int) coords.get(v).y;
    
                // Draw vertex circle
                g2d.fillOval(x, y, vertexSize, vertexSize);
    
                // Draw vertex label
                String label = "Vertex " + v.getId();
                FontMetrics metrics = g2d.getFontMetrics();
                int labelWidth = metrics.stringWidth(label);
                int labelHeight = metrics.getHeight();
                int labelX = x + (vertexSize - labelWidth) / 2;
                int labelY = y + (vertexSize + labelHeight) / 2;
                g2d.setColor(Color.WHITE);
                g2d.drawString(label, labelX, labelY);
            }
        }
    }
    private void stopAnimation() {
        animationTimer.stop();
        isAnimating = false;
        }
        private void animate() {
            // Get the current positions of the pursuer and evader
            Vertex pursuerVertex = pursuer.getCurrentVertex();
            Vertex evaderVertex = evader.getCurrentVertex();
            Coord pursuerCoord = coords.get(pursuerVertex);
            Coord evaderCoord = coords.get(evaderVertex);
        
            // Calculate the movement direction for pursuer and evader
            Coord pursuerMovement = calculateMovement(pursuerVertex, evaderVertex);
            Coord evaderMovement = calculateMovement(evaderVertex, pursuerVertex);
        
            // Update the positions of the pursuer and evader
            pursuerCoord = pursuerCoord.sum(pursuerMovement);
            evaderCoord = evaderCoord.sum(evaderMovement);
        
            // Update the coordinates map
            coords.put(pursuerVertex, pursuerCoord);
            coords.put(evaderVertex, evaderCoord);
        
            // Check if pursuer and evader have collided
            if (pursuerVertex == evaderVertex) {
                stopAnimation();
                statusBar.setText("Collision occurred!");
            }
        
            // Check if pursuer caught the evader
            if (graph.getEdge(pursuerVertex, evaderVertex) != null) {
                stopAnimation();
                statusBar.setText("Pursuer caught the evader!");
            }
        }
        
        private Coord calculateMovement(Vertex source, Vertex target) {
            Coord sourceCoord = coords.get(source);
            Coord targetCoord = coords.get(target);
            double dx = targetCoord.x - sourceCoord.x;
            double dy = targetCoord.y - sourceCoord.y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            double speed = 1.0;
            double moveX = speed * dx / distance;
            double moveY = speed * dy / distance;
            return new Coord(moveX, moveY);
        }
        
        private class AnimationListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                animate();
                repaint();
            }
        }
        
        private class StartButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                if (!isAnimating) {
                    animationTimer.start();
                    isAnimating = true;
                }
            }
        }
        
        private class StopButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                stopAnimation();
            }
        }
        
        private class ResetButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                stopAnimation();
                resetAnimation();
                repaint();
            }
        }
        
        private void resetAnimation() {
            for (Vertex v : graph.getVertices()) {
                coords.put(v, new Coord(0, 0));
            }
            statusBar.setText("Vanish it if you want");
        }
    }        
    

