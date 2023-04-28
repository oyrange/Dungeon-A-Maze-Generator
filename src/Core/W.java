package Core;

import TileEngine.*;
import java.util.*;

import static java.lang.Math.floorDiv;

/*
 * TODO: verify WIDTH / HEIGHT must be odd
 *  TODO: delete ROOM_BOUNDS  ?
 */

public class W {

    private static final int EDGE = 1;


    /** Debug tiles. */
    public static final List<TETile> debugROOMTiles = new ArrayList<>();
    public static final List<TETile> debugPATHTiles = new ArrayList<>();
    public static final TETile roomWALL = debugTile.BLACK; //debugTile.CYAN; //debugTile.DARK_GREY;
    public static final TETile roomCORNER = debugTile.OTHER_BLACK; //debugTile.CYAN;
    public static final TETile pathFLOOR = debugTile.LIGHT_GREY;
    public static final TETile roomFLOOR = debugTile.WHITE; //debugTile.LIGHT_GREY;
    public static final TETile pathWALL = debugTile.DARK_GREY;
    public static final TETile BLANK_WALL = Tileset.WALL;

    /** Constructor variables. */
    private final int WIDTH;
    private final int HEIGHT;
    private final TETile[][] MAP_ARR;
    private final Room MAP_ROOM;
    private static final int MARGIN = 4;   // border between canvas and room generation

    /** Seed variables. */
    private final int marginWIDTH;       // TODO: refactor addEdges() and remove marginW and marginH
    private final int marginHEIGHT;
    private Random RANDOM = new Random();
    private MazeGraph EMPTY;

    /** Room and Path collections. */
    private final List<Room> ROOMS = new ArrayList<>();
    private final Set<Position> PATHS = new HashSet<>();


    /** Player. */
    private Avatar PLAYER;

    /** World tiles. */
    public static final TETile BASETILE = Tileset.FLOOR;//debugTile.CYAN; //debugTile.BLACK; //Tileset.FLOOR;
    public static final TETile FLOOR = dungeonTile.mossyBrick;
    public static final TETile GRASS = dungeonTile.grass;
    public static final TETile DOOR = dungeonTile.redDoor;
    public static final TETile KEY = Tileset.FLOWER;
    public static final TETile AVATAR_TILE = dungeonTile.mummy;
    //private Set<TETile> WALL_TILE = new HashSet<>();
    public static final TETile CORNER_WALL = dungeonTile.flatBrick;
    public static final TETile RIGHT_WALL = dungeonTile.rightBrick;
    public static final TETile LEFT_WALL = dungeonTile.leftBrick;
    public static final TETile TOP_WALL = dungeonTile.topBrick;
    public static final TETile BOTTOM_WALL = dungeonTile.bottomBrick;
    public static final TETile FLAT_WALL = dungeonTile.flatBrick;


    /**
     * Construct a full WORLD from SEED.
     */
    public W(int w, int h, long seed) {
        if (w / 2 == 0 || h / 2 == 0) {
            throw new IllegalArgumentException("Height and width must be odd.");
        }

        WIDTH = w;
        HEIGHT = h;
        marginWIDTH = WIDTH - MARGIN - EDGE;
        marginHEIGHT = HEIGHT - MARGIN - EDGE;
        MAP_ROOM = new Room(new Position(MARGIN, MARGIN), marginWIDTH - MARGIN, marginHEIGHT - MARGIN);

        MAP_ARR = new TETile[WIDTH][HEIGHT];

        TETile[] roomTiles = new TETile[] {roomFLOOR, roomWALL, roomCORNER};
        TETile[] pathTiles = new TETile[] {pathFLOOR, pathWALL};
        debugROOMTiles.addAll(Arrays.asList(roomTiles));
        debugPATHTiles.addAll(Arrays.asList(pathTiles));

        buildFromSeed(seed);
    }

    /**
     * Return MAP.
     */
    public TETile[][] map() {
        return MAP_ARR;
    }

    private void buildFromSeed(long seed) {
        RANDOM = new Random(seed);
        EMPTY = new MazeGraph(RANDOM, WIDTH, HEIGHT);   // TODO: check for remnant crossovers btw W and MazeGraph, tidy code
        //WQU = new WeightedQuickUnionUF(WIDTH * HEIGHT);

        fillWorld();
        generateRooms();
        generatePaths();
        //openConnectors();
        //removeDeadEnds();removeDeadEnds();removeDeadEnds();

        //System.out.println(EMPTY.getVertexCount());
        //System.out.println(EMPTY.getEdgesCount());
        //System.out.println(EMPTY.Stringify());

        /*

        removeDeadEnds()

        place avatar
        place door
        place key
         */
    }

    /**
     * Fill empty MAP with BASETILE.
     */
    private void fillWorld() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                MAP_ARR[x][y] = BASETILE;
            }
        }
    }


    /**
     * Generates rooms. Only adds rooms that do not overlap FLOOR tiles or
     *  that are not out of margin bounds.
     *
     * @var attempts limits amount of attempted room generation.
     */

    private void generateRooms() {
        // could also set limit on successfully created rooms

        // room gen rules:
        int attempts = 100;
        int width_min = 4;
        int width_max = 9;
        int height_min = 4;
        int height_max = 9;

        for (int i = 0; i < attempts; i++) {
            boolean overlap = false;
            Position pos = MAP_ROOM.randomPosition();
            int width = RANDOM.nextInt(width_min, width_max);
            int height = RANDOM.nextInt(height_min, height_max);
            Room newRoom = new Room(pos, width, height);

            for (Position bound : MAP_ROOM.bounds()) {
                if (newRoom.containsPosition(bound)) {
                    overlap = true;
                    break;
                }
            }
            for (Room room : ROOMS) {
                if (room.overlap(newRoom)) {
                    overlap = true;
                }
            }
            if (!overlap) {
                ROOMS.add(newRoom);
                for (Position floor : newRoom.area()) {
                    setTile(floor, roomFLOOR);
                }
                for (Position bound : newRoom.bounds()) {
                    setTile(bound, roomWALL);
                }
                for (Position corner : newRoom.cornerWalls()) {
                    setTile(corner, roomCORNER);
                }
            }
        }
    }


    /**
     * Traverse EMPTY MazeGraph to generate random pathways.
     * @var start generates random non-room position.
     */
    private void generatePaths() {
        addEdges();

        // use for debugging without generateRooms() on:
//        Position start = MAP_ROOM.randomPosition();
//        while (debugROOMTiles.contains(currentTile(start)) ||
//                !(start.xCoordinateIsEven() && start.yCoordinateIsEven())) {
//            start = MAP_ROOM.randomPosition();
//        }

        Position start = ROOMS.get(RANDOM.nextInt(ROOMS.size())).randomPosition();
        while (!(start.xCoordinateIsEven() && start.yCoordinateIsEven())){
            start = MAP_ROOM.randomPosition();
        }

        List<Position> path = EMPTY.traverse(start);

        for (int i = 0; i < path.size() - 1; i++) {
            Position tile = path.get(i);
            Position next = path.get(i + 1);
            //PATHS.add(tile);

            int x = tile.getX();
            int y = tile.getY();
            boolean vertical = false;

            if (tile.getX() < next.getX()) {         // path goes right
                x++;
                //vertical = false;
            } else if (tile.getX() > next.getX()) {  // path goes left
                x--;
                //vertical = false;
            } else if (tile.getY() < next.getY()) {  // path goes up
                y++;
                vertical = true;
            } else if (tile.getY() > next.getY()) {  // path goes down
                y--;
                vertical = true;
            }

//            x = (tile.getX() < next.getX()) ? x+1: x-1;
//            y = (tile.getY() < next.getY()) ? y+1 : y-1;

            Position between = new Position(x, y);

            if (!debugROOMTiles.contains(currentTile(tile))) {
                setTile(tile, pathFLOOR);
            }                                                   // TODO: these if conditions removeable when maze gen around rooms
            if (!debugROOMTiles.contains(currentTile(between))) {
                setTile(between, pathFLOOR);
            }
            PATHS.add(tile);
            PATHS.add(between);

            /* TODO: walls curr overwrite floor tiles due to loop */

            /* draw walls
            for (Position wall : adjacentPos(vertical, tile) ) {
                if (MAP[wall.getX()][wall.getY()] == BASETILE) {

                    setTile(wall, pathWALL);
                }
            }
            for (Position wall : adjacentPos(vertical, btw)) {
                if (MAP[wall.getX()][wall.getY()] == BASETILE) {

                    setTile(wall, pathWALL);
                }
            }
            //*/
        }

        /*
        for (Position tile : PATHS) {
            List<Position> adj = adjacentPos(true, tile, 1);

            if (Collections.frequency(adj, BASETILE) > 1) {

                setTile(wall, pathWALL);
            }
        }
        for (Position wall : adjacentPos(vertical, btw)) {
            if (MAP[wall.getX()][wall.getY()] == BASETILE) {

                setTile(wall, pathWALL);
            }
        }
         */
    }

    private void openConnectors() {
        for (Position c : findConnectors()) {
            setTile(c, roomFLOOR);
        }
    }

    private List<Position> findConnectors() {
        List<Position> connectors = new ArrayList<>();

        for (Room room : ROOMS) {
            List<Position> c = new ArrayList<>();
            List<Position> r = new ArrayList<>();
            for (Position bound : room.area()) {
                for (TETile p : adjacentTiles(bound, 1)) {
                    if (debugPATHTiles.contains(p) && !currentTile(bound).equals(roomCORNER)) {
                        c.add(bound);
                    }
                }

                Position vert1 = adjacentPos(true, bound, 1).get(0);
                Position vert2 = adjacentPos(true, bound, 1).get(1);
                Position horiz1 = adjacentPos(false, bound, 1).get(0);
                Position horiz2 = adjacentPos(false, bound, 1).get(1);

                // if touching another room
                if ((currentTile(vert1).equals(roomFLOOR) && currentTile(vert2).equals(roomWALL) ||
                        currentTile(vert2).equals(roomFLOOR) && currentTile(vert1).equals(roomWALL))) {
                    //setTile(bound, roomFLOOR);
                    //setTile(vert1, roomFLOOR);
                    //setTile(vert2, roomFLOOR);
                    r.add(bound);
                }
                if ((currentTile(horiz1).equals(roomFLOOR) && currentTile(horiz2).equals(roomWALL) ||
                        currentTile(horiz2).equals(roomFLOOR) && currentTile(horiz1).equals(roomWALL))) {
                    //setTile(bound, roomFLOOR);
                    //setTile(horiz1, roomFLOOR);
                    //setTile(horiz2, roomFLOOR);
                    r.add(bound);
                }

            }
            if (!c.isEmpty()) {
                int ind = RANDOM.nextInt(0, c.size());
                connectors.add(c.get(ind));
            }
            if (!r.isEmpty()) {
                connectors.addAll(r);               // TODO: laaazy. this opens all adjacent room bounds
            }
        }

        return connectors;

    }

    private void removeDeadEnds() {

        for (Position tile : PATHS) {               /* TODO: optional, concat PATHS and ROOMS ? */
            List<TETile> adjacent = adjacentTiles(tile, 1);
            if (Collections.frequency(adjacent, BASETILE) > 2) {        /* TODO: this causes checkerboard effect */
                setTile(tile, BASETILE);
                //PATHS.remove(tile);
            }
        }

        /*
        for (each FLOOR tile)
            if (tile touches three walls)
                one wall == FLOOR
         */
    }

    private TETile currentTile(Position pos) {
        return MAP_ARR[pos.getX()][pos.getY()];
    }

    private List<TETile> adjacentTiles(Position pos, int distance) {        // TODO: optional, a more efficient way to write this
        List<TETile> adjacent = new ArrayList<>();
        //int i = 0;

        for (Position p : adjacentPos(true, pos, distance)) {
            adjacent.add(currentTile(p));
        }
        for (Position p : adjacentPos(false, pos, distance)) {
            adjacent.add(currentTile(p));
        }

        /*
        TETile up = MAP[pos.getX()][pos.getY() + 1];
        TETile low = MAP[pos.getX()][pos.getY() - 1];
        TETile right = MAP[pos.getX() + 1][pos.getY()];
        TETile left = MAP[pos.getX() - 1][pos.getY()];

        adjacent.add(up);
        adjacent.add(low);
        adjacent.add(right);
        adjacent.add(left);

        /*
        for (TETile tile : adjacent) {
            if (tile == roomWALL) {
                i++;
            }
        }
        */

        return adjacent;
    }

    private List<Position> adjacentPos(boolean vertical, Position pos, int distance) {
        List<Position> positions = new ArrayList<>();

        if (!vertical) {
            Position upper = new Position(pos.getX(), pos.getY() + distance);
            Position lower = new Position(pos.getX(), pos.getY() - distance);

            positions.add(upper);
            positions.add(lower);

        } else {
            Position left = new Position(pos.getX() + distance, pos.getY());
            Position right = new Position(pos.getX() - distance, pos.getY());

            positions.add(left);
            positions.add(right);
        }

        return positions;
    }

    /**
     * Generate random position between width and height bounds.
     *
     * @param widthMin lower x bound
     * @param widthMax upper x bound
     * @param heightMin lower y bound
     * @param heightMax upper y bound
     *
     * @return Position(x, y)
     */
    private Position randomPosition(int widthMin, int widthMax, int heightMin, int heightMax) {
        int x = RANDOM.nextInt(widthMin, widthMax);
        int y = RANDOM.nextInt(heightMin, heightMax);

        return new Position(x, y);
    }

    /**
     * Set position to tile.
     * @param pos position
     * @param tile TETile to assign into MAP
     */
    private void setTile(Position pos, TETile tile) {
        MAP_ARR[pos.getX()][pos.getY()] = tile;
    }


    public void addEdges() {
        for (int x = MARGIN; x <= marginWIDTH; x += 2) {
            for (int y = MARGIN; y <= marginHEIGHT; y += 2) {
                Position curr = new Position(x, y);
                if (!debugROOMTiles.contains(currentTile(curr))) {

                    Position left = new Position(Math.max(MARGIN, x - 2), y);
                    Position right = new Position((Math.min(marginWIDTH, x + 2)), y);
                    Position up = new Position(x, (Math.min(marginHEIGHT, y + 2)));
                    Position down = new Position(x, Math.max(MARGIN, y - 2));

                    if (!debugROOMTiles.contains(currentTile(left))) {
                        EMPTY.addEdge(curr, left);
                    }
                    if (!debugROOMTiles.contains(currentTile(right))) {
                        EMPTY.addEdge(curr, right);
                    }
                    if (!debugROOMTiles.contains(currentTile(up))) {
                        EMPTY.addEdge(curr, up);
                    }
                    if (!debugROOMTiles.contains(currentTile(down))) {
                        EMPTY.addEdge(curr, down);
                    }
                }
            }
        }
    }













    /*

    marked.add(node)
    List<Integer> neighbours = MAP.get(pos);

    if (!(neighbours == null)) {
        for (int i = 0; i < neighbours.size(); i++) {
            int ind = RANDOM...
            Integer n = neighbours.get(ind);

            if (!marked.contains(n)) {
                helpTraverse(node, path, marked)
            }
            Position pos = assign2D(node);
            MAP[pos.getX()][pos.getY()] = FLOOR;
        }
    }


    Add edges
    traverse map

    for each node
        FLOOR

        assign 2D
        direction: x >< y<>
            btw
                assign walls
                assign floor
        btw floor to pos

     */




    public static void main(String[] args) {
        int width = 45;
        int height = 51;
        TERenderer ter = new TERenderer();

        W world = new W(width, height, 1234);
        TETile[][] map = world.map();

        ter.initialize(width, height);
        ter.renderFrame(map);

    }
}