package Core;

import java.util.*;

public class MazeGraph {

    /**
     * HyponymsGraph modelled after geeksforgeeks.org generic graph class.
     */
    private Map<Integer, List<Integer>> MAP;
    private int VERTEX_COUNT;
    private List<Integer> VERTICES = new ArrayList<>();
    private Random RANDOM;
    private int EDGES;
    private int marginWIDTH;
    private int marginHEIGHT;
    //private int MARGIN;
    private int WIDTH;
    private int HEIGHT;

    public MazeGraph(Random random, int margin, int mw, int mh, int width, int height) {
        MAP = new HashMap<>();

        //MARGIN = margin;
        //marginWIDTH = mw;
        //marginHEIGHT = mh;
        WIDTH = width;
        HEIGHT = height;
        RANDOM = random;

        VERTEX_COUNT = 0;
        EDGES = 0;

        //addEdges();
    }

    /**
     * Adds a new vertex to graph.
     */
    public void addVertex(Integer pos) {
        MAP.put(pos, new LinkedList<>());

        VERTICES.add(pos);
        VERTEX_COUNT++;
    }

    /**
     * Adds a two-way edge between SOURCE and DESTINATION.
     */
    public void addEdge(Position src, Position dest) {
        Integer source = assign1D(src);
        Integer destination = assign1D(dest);

        if (!MAP.containsKey(source)) {
            addVertex(source);
        }
        if (!MAP.containsKey(destination)) {
            addVertex(destination);
        }
        MAP.get(source).add(destination);
        MAP.get(destination).add(source);

        EDGES++;
    }
/*
    public void addEdges() {                // TODO: optional, missing vertices on the top row
        for (int x = MARGIN; x <= marginWIDTH; x += 2) {
            for (int y = MARGIN; y <= marginHEIGHT; y += 2) {
                Position curr = new Position(x, y);                 // this constructs...
                int curr1D = assign1D(curr);                        // ...then deconstructs. wasteful.

                Position right = new Position((Math.min(marginWIDTH, x + 2)), y);
                int right1D = assign1D(right);

                Position up = new Position(x, (Math.min(marginHEIGHT, y + 2)));
                int up1D = assign1D(up);

                addEdge(curr1D, right1D);
                addEdge(curr1D, up1D);
            }
        }
    }
*/

    private int assign1D(Position p) {
        return p.getY() * WIDTH + p.getX();
    }

    private Position assign2D(int num) {
        int x = num % WIDTH;
        int y = (num - x) / WIDTH;
        // OR // int y = floorDiv(num, WIDTH);

        return new Position(x, y);
    }

    /**
     * Returns list of connected Positions..
     */
    public List<Integer> getNeighbours(Integer pos) {
        return MAP.get(pos);
    }

    /**
     * Gives the total vertex count.
     */
    public int getVertexCount() {
        return VERTEX_COUNT;
    }

    /**
     * Gives the total edge count.
     */
    public int getEdgesCount() {
        return EDGES;
    }

    /**
     * Returns if vertex is present or not.
     */
    public boolean hasVertex(Integer s) {
        return MAP.containsKey(s);
    }

    /**
     * Returns boolean if edge is present or not.
     */
    public boolean hasEdge(Integer s, Integer d) {
        return MAP.get(s).contains(d);
    }

    /**
     * Prints the adjacency list of each vertex.
     */
    public String Stringify() {
        StringBuilder builder = new StringBuilder();

        for (Integer pos : MAP.keySet()) {
            builder.append(pos.toString()).append(": ");

            for (Integer p : MAP.get(pos)) {
                builder.append(p.toString()).append(" ");
            }

            builder.append("\n");
        }

        return builder.toString();
    }


    /**
     * Set of functions to DFS traverse the graph.

    public List<Integer> traverse(Integer pos) {
        List<Integer> marked = new ArrayList<>();
        List<Integer> path = new ArrayList<>();

        helpTraverse(pos, path, marked);
        //System.out.println(path);
        return path;
    }
    */
    public List<Position> traverse(Position pos) {
        int start = assign1D(pos);

        List<Integer> marked = new ArrayList<>();
        List<Position> path = new ArrayList<>();
        List<Integer> unvisited = new ArrayList<>(MAP.keySet());

        //Queue<Integer> fringe = new PriorityQueue<>();
        //Stack<Integer> fringe = new Stack<>();//

        helpTraverse(start, path, marked, unvisited);

        while (!unvisited.isEmpty()) {
            int ind = RANDOM.nextInt(0, unvisited.size());
            start = unvisited.get(ind);

            helpTraverse(start, path, marked, unvisited);
        }

        return path;
    }

/* FRINGE implementation, only fills corner.
    public void helpTraverse(Integer pos, List<Position> path, List<Integer> marked, Stack<Integer> fringe) {
        fringe.add(pos); //
        marked.add(pos);
        path.add(assign2D(pos));

        List<Integer> neighbours = MAP.get(pos);

        if (!(neighbours == null)) {
            while (!fringe.isEmpty()) {
                int v = fringe.pop();
                path.add(assign2D(v));
                for (int i = 0; i < MAP.get(v).size(); i++) {
                //for (int w : MAP.get(v)) {
                    int ind = RANDOM.nextInt(0, MAP.get(v).size()); //
                    Integer w = MAP.get(v).get(ind); //
                    if (!marked.contains(w)) {
                        fringe.add(w);
                        marked.add(w);
                        //path.add(assign2D(w));
                    }
                }
                //path.add(assign2D(v));
            }
        }
    }
*/


/* DFS basic implementation. */

    public void helpTraverse(Integer pos, List<Position> path, List<Integer> marked, List<Integer> unvisited) {
        marked.add(pos);            // TODO: optional, concat these lines into one
        unvisited.remove(pos);
        path.add(assign2D(pos));

        List<Integer> neighbours = MAP.get(pos);

        if (!(neighbours == null)) {
            for (int i = 0; i < neighbours.size(); i++) {
                int ind = RANDOM.nextInt(0, neighbours.size());
                Integer node = neighbours.get(ind);

                if (!marked.contains(node)) {
                    helpTraverse(node, path, marked, unvisited);
                }
                path.add(assign2D(pos));

                //curr.remove(ind);
            }
            //path.add(assign2D(pos));
        }
    }
}

