/* Mahdeen Ahmed Khan Sameer
    Project 08
    GraphDisplay Class: To display a graph.
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.util.Random;

/**
 * Displays a Landscape graphically using Swing. The Landscape
 * contains a grid which can be displayed at any scale factor.
 * 
 * @author bseastwo
 */
public class GraphDisplay {
    
    

    final class Coord {
        double x, y;

        Coord(double a, double b) {
            x = a;
            y = b;
        }

        double norm() {
            return Math.sqrt(x * x + y * y);
        }

        Coord diff(Coord c) {
            return new Coord(x - c.x, y - c.y);
        }

        Coord sum(Coord c) {
            return new Coord(x + c.x, y + c.y);
        }

        void addBy(Coord c) {
            x += c.x;
            y += c.y;
        }

        Coord scale(double d) {
            return new Coord(x * d, y * d);
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }

    JFrame win;
    protected Graph graph;
    private LandscapePanel canvas;
    private int gridScale; // width (and height) of each square in the grid
    AbstractPlayerAlgorithm pursuer, evader;
    HashMap<Vertex, Coord> coords;

    /**
     * Initializes a display window for a Landscape.
     * 
     * @param scape the Landscape to display
     * @param scale controls the relative size of the display
     * @throws InterruptedException
     */
    public GraphDisplay(Graph g, int scale) throws InterruptedException {
        this(g, null, null, scale);
    }

    public GraphDisplay(Graph g, AbstractPlayerAlgorithm pursuer, AbstractPlayerAlgorithm evader, int scale)
            throws InterruptedException {

        // setup the window
        this.win = new JFrame("Grid-Search");
        this.win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.pursuer = pursuer;
        this.evader = evader;

        this.graph = g;
        this.gridScale = scale;

        // create a panel in which to display the Landscape
        // put a buffer of two rows around the display grid
        this.canvas = new LandscapePanel((int) (this.graph.size()) * this.gridScale,
                (int) (this.graph.size()) * this.gridScale);

        // add the panel to the window, layout, and display
        this.win.add(this.canvas, BorderLayout.CENTER);
        this.win.pack();
        createCoordinateSystem();
        this.win.setVisible(true);
        repaint();
    }

    public void setGraph(Graph graph) throws InterruptedException {
        this.graph = graph;
        createCoordinateSystem();
    }

    /**
     * Saves an image of the display contents to a file. The supplied
     * filename should have an extension supported by javax.imageio, e.g.
     * "png" or "jpg".
     *
     * @param filename the name of the file to save
     */
    public void saveImage(String filename) {
        // get the file extension from the filename
        String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());

        // create an image buffer to save this component
        Component tosave = this.win.getRootPane();
        BufferedImage image = new BufferedImage(tosave.getWidth(), tosave.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // paint the component to the image buffer
        Graphics g = image.createGraphics();
        tosave.paint(g);
        g.dispose();

        // save the image
        try {
            ImageIO.write(image, ext, new File(filename));
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    public void createCoordinateSystem() throws InterruptedException {

        // draw the graph
        // see http://yifanhu.net/PUB/graph_draw_small.pdf for more details
        Random rand = new Random();
        HashMap<Vertex, HashMap<Vertex, Double>> distances = new HashMap<>();
        for (Vertex v : graph.getVertices())
            distances.put(v, graph.distanceFrom(v));

        coords = new HashMap<>();
        for (Vertex v : graph.getVertices())
            coords.put(v, new Coord(rand.nextInt(canvas.getWidth() / 2) - canvas.getWidth() / 2,
                    rand.nextInt(canvas.getHeight() / 2) - canvas.getHeight() / 2));

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
                        f.addBy(xu.diff(xv).scale((xu.diff(xv).norm()
                                - (distances.get(u).get(v) == Double.POSITIVE_INFINITY ? 1000
                                        : distances.get(u).get(v) * 100))
                                / (xu.diff(xv).norm())));
                    else
                        pickRandom = true;
                }
                if (!pickRandom)
                    newCoords.put(v,
                            f.x == 0 && f.y == 0 ? coords.get(v) : coords.get(v).sum(f.scale(step / f.norm())));
                else
                    newCoords.put(v, new Coord(rand.nextInt(canvas.getWidth() / 2) - canvas.getWidth() / 2,
                            rand.nextInt(canvas.getHeight() / 2) - canvas.getHeight() / 2));

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
            // Uncomment below to see how the coordinates are formed!
            // repaint();
            // Thread.sleep(50);
        }
        Coord average = new Coord(0, 0);
        for (Vertex v : graph.getVertices())
            average.addBy(coords.get(v));
        average = average.scale(1.0 / graph.size());
        double maxNorm = 0;
        for (Vertex v : graph.getVertices()) {
            Coord newCoord = (new Coord(coords.get(v).x - average.x, coords.get(v).y - average.y));
            coords.put(v, newCoord);
            maxNorm = Math.max(maxNorm, newCoord.norm());
        }
        for (Vertex v : graph.getVertices())
            coords.put(v, coords.get(v)
                    .scale((Math.min(canvas.getWidth() / 2, canvas.getHeight() / 2) - gridScale / 2) / maxNorm));

        int singletonCount = 0;
        for (Vertex v : graph.getVertices())
            if (!v.adjacentVertices().iterator().hasNext())
                coords.put(v, new Coord(-canvas.getWidth() / 2 + gridScale * ++singletonCount,
                        -canvas.getHeight() / 2 + gridScale));

    }

    /**
     * This inner class provides the panel on which Landscape elements
     * are drawn.
     */
    private class LandscapePanel extends JPanel {
        /**
         * Creates the panel.
         * 
         * @param width  the width of the panel in pixels
         * @param height the height of the panel in pixels
         */
        public LandscapePanel(int width, int height) {
            super();
            this.setPreferredSize(new Dimension(width, height));
            this.setBackground(Color.lightGray);
        }

        /**
         * Method overridden from JComponent that is responsible for
         * drawing components on the screen. The supplied Graphics
         * object is used to draw.
         * 
         * @param g the Graphics object used for drawing
         */
        public void paintComponent(Graphics g) {
            // take care of housekeeping by calling parent paintComponent
            super.paintComponent(g);
            if (pursuer != null && pursuer.getCurrentVertex() == evader.getCurrentVertex())
                setBackground(new Color(0, 255, 0));
            g.translate(canvas.getWidth() / 2, canvas.getHeight() / 2);
            for (Edge e : graph.getEdges()) {
                g.setColor(Color.BLACK);
                g.drawLine((int) coords.get(e.vertices()[0]).x + gridScale / 4,
                        (int) coords.get(e.vertices()[0]).y + gridScale / 4,
                        (int) coords.get(e.vertices()[1]).x + gridScale / 4,
                        (int) coords.get(e.vertices()[1]).y + gridScale / 4);
            }
            for (Vertex v : graph.getVertices()) {
                if (pursuer != null && v == pursuer.getCurrentVertex() && v == evader.getCurrentVertex())
                    g.setColor(new Color(148, 0, 211));
                else if (pursuer != null && v == pursuer.getCurrentVertex())
                    g.setColor(Color.BLUE);
                else if (evader != null && v == evader.getCurrentVertex())
                    g.setColor(Color.RED);
                else
                    g.setColor(Color.BLACK);
                g.fillOval((int) coords.get(v).x, (int) coords.get(v).y, gridScale / 2, gridScale / 2);
            }
        } // end paintComponent

    } // end LandscapePanel

    public void repaint() {
        this.win.repaint();
    }

    public static void main(String[] args) throws InterruptedException {
        Graph g = new Graph();

        // This draws C5
        Vertex[] vertices = new Vertex[7];
        for (int i = 0; i < 7; i++) {
            vertices[i] = g.addVertex();
        }
        for (int i = 0; i < 7; i++) {
            g.addEdge(vertices[i], vertices[(i + 1) % 7], 1);
        }

        g.addEdge(vertices[0], vertices[2], 1);
        g.addEdge(vertices[0], vertices[3], 1);
        g.addEdge(vertices[3], vertices[6], 1);
        g.addEdge(vertices[4], vertices[6], 1);

        // new GraphDisplay(g, 100);

        // ArrayList<Vertex> ordered = new ArrayList<>();
        // for(int i = 0; i < 10; i++){
        // ordered.add(g.addVertex());
        // } for (int i = 0; i < 5; i++){
        // g.addEdge(ordered.get(i), ordered.get((i + 1) % 5), 3);
        // g.addEdge(ordered.get(i), ordered.get((i + 5)), 2);
        // g.addEdge(ordered.get(i + 5), ordered.get(((i + 2) % 5) + 5), 1);
        // }
        AbstractPlayerAlgorithm pursuer = new MoveTowardsPlayerAlgorithm(g);
        AbstractPlayerAlgorithm evader = new MoveAwayPlayerAlgorithm(g);
        pursuer.chooseStart();
        evader.chooseStart(pursuer.getCurrentVertex());
        GraphDisplay gd = new GraphDisplay(g, pursuer, evader, 80);
        while (pursuer.getCurrentVertex() != evader.getCurrentVertex()) {
            Thread.sleep(1000);
            pursuer.chooseNext(evader.getCurrentVertex());
            gd.repaint();
            if (pursuer.getCurrentVertex() == evader.getCurrentVertex())
                break;
            Thread.sleep(1000);
            evader.chooseNext(pursuer.getCurrentVertex());
            gd.repaint();
        }

    }
}