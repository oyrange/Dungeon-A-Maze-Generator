package Core;

import TileEngine.TETile;
import TileEngine.TETile;
import TileEngine.Tileset;
import TileEngine.dungeonTile;

import java.util.*;


public class World {
    /** Constructor variables. */
    private final int WIDTH;
    private final int HEIGHT;
    private final TETile[][] MAP;
    //private final int MARGIN = 4;

    /** Seed variables. */

    //private int marginWIDTH;
    //private int marginHEIGHT;
    private Random RANDOM = new Random();

    /** Player. */
    private Avatar PLAYER;

    /** World tiles. */
    public static final TETile BASETILE = Tileset.FLOOR;
    public static final TETile DOOR = dungeonTile.runed_door;
    public static final TETile KEY = dungeonTile.purplerod;
    public static final TETile AVATAR_TILE = dungeonTile.mummy;
    public static final List<TETile> ROOMTiles = new ArrayList<>();
    public static final List<TETile> WALLTiles = new ArrayList<>();
        public static final TETile FLOOR = dungeonTile.mossyBrick;
        public static final TETile CORNER_WALL = dungeonTile.flatBrick;
        public static final TETile RIGHT_WALL = dungeonTile.rightBrick;
        public static final TETile LEFT_WALL = dungeonTile.leftBrick;
        public static final TETile TOP_WALL = dungeonTile.topBrick;
        public static final TETile BOTTOM_WALL = dungeonTile.bottomBrick;


    /**
     * Construct a full WORLD from SEED.
     */
    public World(int w, int h, long seed) {
        WIDTH = w;
        HEIGHT = h;
        MAP = new TETile[WIDTH][HEIGHT];
        RANDOM = new Random(seed);

        TETile[] tiles = new TETile[] {FLOOR, CORNER_WALL, RIGHT_WALL, LEFT_WALL, TOP_WALL, BOTTOM_WALL};
        ROOMTiles.addAll(List.of(tiles));
        WALLTiles.addAll(List.of(tiles));
        WALLTiles.remove(FLOOR);

        fillWorld();
        buildFromSeed(seed);
    }


    /**
     * Construct a WORLD of only BASETILES.
     */
    public World(int w, int h) {
        WIDTH = w;
        HEIGHT = h;
        MAP = new TETile[WIDTH][HEIGHT];

        fillWorld();
    }


    /**
     * Construct WORLD from existing MAP.
     */
    public World(int w, int h, char[][] charMap) {
        WIDTH = w;
        HEIGHT = h;
        MAP = new TETile[WIDTH][HEIGHT];

        buildFromCharMap(charMap);
    }


    // converts a map of character tiles into a map of actual TETiles.
    // Check out dungeonTile.java, I changed the character representations of the tiles, so they aren't all '#' like before.
    public void buildFromCharMap(char[][] charMap) {
        boolean keyPickedUp = true;
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                char tile = charMap[x][y];
                if (tile == AVATAR_TILE.character()) {
                    MAP[x][y] = AVATAR_TILE;
                    // init the player
                    PLAYER = new Avatar(new Position(x, y));
                } else if (tile == BASETILE.character()) {
                    MAP[x][y] = BASETILE;
                } else if (tile == FLOOR.character()) {
                    MAP[x][y] = FLOOR;
                } else if (tile == CORNER_WALL.character()) {
                    MAP[x][y] = CORNER_WALL;
                } else if (tile == RIGHT_WALL.character()) {
                    MAP[x][y] = RIGHT_WALL;
                } else if (tile == LEFT_WALL.character()) {
                    MAP[x][y] = LEFT_WALL;
                } else if (tile == TOP_WALL.character()) {
                    MAP[x][y] = TOP_WALL;
                } else if (tile == BOTTOM_WALL.character()) {
                    MAP[x][y] = BOTTOM_WALL;
                } else if (tile == DOOR.character()) {
                    MAP[x][y] = DOOR;
                } else if (tile == KEY.character()) {
                    MAP[x][y] = KEY;
                    keyPickedUp = false;
                }
            }
        }
        if (keyPickedUp) {
            PLAYER.pickUpKey();
        }
    }


    /**
     * Return MAP.
     */
    public TETile[][] map() {
        return MAP;
    }


    /**
     * Return PLAYER.
     */
    public Avatar player() {
        return PLAYER;
    }


    /**
     * Return PLAYER position.
     */
    public Position playerPos() {
        return PLAYER.getPosition();
    }


    /**
     * Return PLAYER x coordinate.
     */
    public int playerX() {
        return playerPos().getX();
    }


    /**
     * Return PLAYER y coordinate.
     */
    public int playerY() {
        return playerPos().getY();
    }


    /**
     * Place PLAYER at (parameter) P and render new placement.
     */
    public void placeAvatar(Position p) {
        if (p.getX() < 0 || p.getX() > WIDTH - 1 || p.getY() < 0 || p.getY() > HEIGHT - 1) {
            return;
        } else if (!(MAP[p.getX()][p.getY()].equals(FLOOR)) && !(MAP[p.getX()][p.getY()].equals(KEY))) {
            return;
        } else {
            MAP[PLAYER.p.getX()][PLAYER.p.getY()] = FLOOR;    // could change to default tile
            PLAYER.p = p;
            MAP[PLAYER.p.getX()][PLAYER.p.getY()] = AVATAR_TILE;
        }
    }


    /**
     * Fill empty TETile[][] with BASETILE, can be overwritten by room tiles.
     */
    private void fillWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                MAP[x][y] = BASETILE;
            }
        }
    }


    /**
     * Create a diamond-shaped visibility light area out of a BASETILE world around the player.
     */
    public void drawLight(World world) {
        int radius = 5;
        int px = world.playerX();
        int py = world.playerY();

        for (int i = 0; i < radius; i += 1) {
            for (int j = 0; j < radius - i; j += 1) {
                int x1 = Math.min(WIDTH, px + j);
                int x2 = Math.max(0, px - j);
                int y1 = Math.min(HEIGHT, py + i);
                int y2 = Math.max(0, py - i);

                MAP[x1][y1] = world.map()[x1][y1];
                MAP[x1][y2] = world.map()[x1][y2];
                MAP[x2][y1] = world.map()[x2][y1];
                MAP[x2][y2] = world.map()[x2][y2];
            }
        }
    }


    /**
     * Adds rooms and hallways to MAP.
     */
    private void buildFromSeed(long seed) {
        Random random = new Random(seed);
        //List<Room> rooms = generateRooms(random);
        List<Room> rooms = generateRooms(random);

        //drawRooms(rooms);

        List<Room> hallways = generateHallways(rooms, random);
        //drawRooms(hallways);

        // generate and place AVATAR
        Position randomPos = getRandomFloor(random, rooms);
        PLAYER = new Avatar(randomPos);
        placeAvatar(randomPos);

        placeDoor(random);
        placeKey(random, rooms);
    }

    /**
     * Generates rooms. Only adds rooms that do not overlap FLOOR tiles or
     *  that are not out of margin bounds.
     *
     * @var attempts limits amount of attempted room generation.
     */

    /* TODO: inefficient and repetitive check for room.bounds() etc.
     *       Can take too long to generate all floor bounds then
     *       discard and try again, attempts x. */
    private List<Room> generateRooms(Random random) {
        int width_min = 4;
        int width_max = 9;
        int height_min = 4;
        int height_max = 9;

        int attempts = 100;
        // could also set limit on successfully created rooms

        List<Room> rooms = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            boolean overlap = false;

            //Position pos = randomPosition(MARGIN, marginWIDTH, MARGIN, marginHEIGHT);
            Position pos = randomPosition(new Room(new Position(0, 0), WIDTH, HEIGHT));
            int width = RANDOM.nextInt(width_min, width_max);
            int height = RANDOM.nextInt(height_min, height_max);
            Room r = new Room(pos, width, height);

            for (Position bound : r.bounds()) {             // how to condense init room and check bounds?
                if (bound.getX() < 0 || bound.getX() >= WIDTH ||
                //if (bound.getX() < 0 || bound.getX() >= marginWIDTH ||
                        //bound.getY() < 0 || bound.getY() >= marginHEIGHT ||
                        bound.getY() < 0 || bound.getY() >= HEIGHT ||
                        (ROOMTiles.contains(currentTile(bound)))) {
                    overlap = true;                         // get rid of break; continue; ?
                    break;
                }
            }

            if (!overlap) {
                rooms.add(r);
                for (Position floor : r.bounds()) {
                    setTile(floor, FLOOR);
                }
                for (Position upper : r.topWalls()) {
                    setTile(upper, TOP_WALL);
                }
                for (Position lower : r.bottomWalls()) {
                    setTile(lower, BOTTOM_WALL);
                }
                for (Position left : r.leftWalls()) {
                    setTile(left, LEFT_WALL);
                }
                for (Position right : r.rightWalls()) {
                    setTile(right, RIGHT_WALL);
                }
                for (Position corner : r.cornerWalls()) {
                    setTile(corner, CORNER_WALL);
                }
            }
        }
        return rooms;
    }

    /**
     * Generate random position in given ROOM.
     *
     * @return Position(x, y)
     */
    private Position randomPosition(Room room) {
    //private Position randomPosition(int widthMin, int widthMax, int heightMin, int heightMax) {
        int x = RANDOM.nextInt(room.position().getX(), room.position().getX() + room.width());
        int y = RANDOM.nextInt(room.position().getY(), room.position().getY() + room.height());

        return new Position(x, y);
    }


    /**
     * Places TETiles in WORLD[][].
     */
    private void drawRooms(List<Room> rooms) {
        for (Room room : rooms) {

            for (Position floor : room.bounds()) {
                //if (!ROOMTiles.contains(currentTile(floor))) {
                    setTile(floor, FLOOR);
                //}
            }

            /*
            if it's a basetile
                set to etc
            else if adjacent to wall tile && wall tile adjacent to floor tile
                set adjacent tile to FLOOR
            else if adjacent to floor tile and floor tile
                set self to floor

            wall:wall:floor
            floor:wall:floor
             */

            for (Position upper : room.topWalls()) {
                if (currentTile(upper).equals(BASETILE)) {
                    setTile(upper, TOP_WALL);
                } else if (Collections.frequency(adjacentTiles(true, upper), FLOOR) == 2 ||
                        Collections.frequency(adjacentTiles(false, upper), FLOOR) == 2) {
                    setTile(upper, FLOOR);
                }
            }
            for (Position lower : room.bottomWalls()) {
                /*if (!ROOMTiles.contains(currentTile(lower))) {
                    setTile(lower, BOTTOM_WALL);
                }*/
                if (currentTile(lower).equals(BASETILE)) {
                    setTile(lower, BOTTOM_WALL);
                } else if (Collections.frequency(adjacentTiles(true, lower), FLOOR) == 2 ||
                        Collections.frequency(adjacentTiles(false, lower), FLOOR) == 2) {
                    setTile(lower, FLOOR);
                }
            }
            for (Position left : room.leftWalls()) {
                /*if (!ROOMTiles.contains(currentTile(left))) {
                    setTile(left, LEFT_WALL);
                }*/
                if (currentTile(left).equals(BASETILE)) {
                    setTile(left, LEFT_WALL);
                } else if (Collections.frequency(adjacentTiles(true, left), FLOOR) == 2 ||
                        Collections.frequency(adjacentTiles(false, left), FLOOR) == 2) {
                    setTile(left, FLOOR);
                }
            }
            for (Position right : room.rightWalls()) {
                /*if (!ROOMTiles.contains(currentTile(right))) {
                    setTile(right, RIGHT_WALL);
                }*/
                if (currentTile(right).equals(BASETILE)) {
                    setTile(right, RIGHT_WALL);
                } else if (Collections.frequency(adjacentTiles(true, right), FLOOR) == 2 ||
                        Collections.frequency(adjacentTiles(false, right), FLOOR) == 2) {
                    setTile(right, FLOOR);
                }
            }
            for (Position corner : room.cornerWalls()) {
                /*if (!ROOMTiles.contains(currentTile(corner))) {
                    setTile(corner, CORNER_WALL);
                }*/
                if (currentTile(corner).equals(BASETILE)) {
                    setTile(corner, CORNER_WALL);
                } else if (Collections.frequency(adjacentTiles(true, corner), FLOOR) == 2 ||
                        Collections.frequency(adjacentTiles(false, corner), FLOOR) == 2) {
                    setTile(corner, FLOOR);
                }
            }

            /*
            for (Position upper : room.topWalls()) {
                if (!ROOMTiles.contains(currentTile(upper))) {
                    setTile(upper, TOP_WALL);
                }
            }
            for (Position lower : room.bottomWalls()) {
                if (!ROOMTiles.contains(currentTile(lower))) {
                    setTile(lower, BOTTOM_WALL);
                }
            }
            for (Position left : room.leftWalls()) {
                if (!ROOMTiles.contains(currentTile(left))) {
                    setTile(left, LEFT_WALL);
                }
            }
            for (Position right : room.rightWalls()) {
                if (!ROOMTiles.contains(currentTile(right))) {
                    setTile(right, RIGHT_WALL);
                }
            }
            for (Position corner : room.cornerWalls()) {
                if (!ROOMTiles.contains(currentTile(corner))) {
                    setTile(corner, CORNER_WALL);
                }
            }*/


            /*
            // draws the floor
            for (Position tile : room.floor()) {
                setTile(tile, FLOOR);
            }

            // draws the walls
            for (Position tile : room.cornerWalls()) {
                if (currentTile(tile).equals(BASETILE)) {
                    setTile(tile, debugTile.WHITE);
                }
            }

            for (Position tile : room.rightWalls()) {
                if (!ROOMTiles.contains(currentTile(tile))) {
                    setTile(tile, debugTile.WHITE);
                }
            }

            for (Position tile : room.leftWalls()) {
                if (!ROOMTiles.contains(currentTile(tile))) {
                    setTile(tile, debugTile.LIGHT_GREY);
                }
            }

            for (Position tile : room.topWalls()) {
                if (!ROOMTiles.contains(currentTile(tile))) {
                    setTile(tile, debugTile.WHITE);
                }
            }

            for (Position tile : room.bottomWalls()) {
                if (!ROOMTiles.contains(currentTile(tile))) {
                    setTile(tile, debugTile.WHITE);
                }
            }
             */
        }
    }

    /**
     * Calls connectRooms() on all rooms in WORLD.
     */
    private List<Room> generateHallways(List<Room> rooms, Random random) {
        List<Room> hallways = new ArrayList<>();

        for (int i = 0; i < rooms.size() - 1; i++) {
            hallways.addAll(connectRooms(rooms.get(i), rooms.get(i+1), random));
        }

        for (Room hallway : hallways) {
            for (Position floor : hallway.bounds()) {
                //if (!ROOMTiles.contains(currentTile(floor))) {
                    setTile(floor, FLOOR);
                //}
            }
            for (Position upper : hallway.topWalls()) {
                if (!ROOMTiles.contains(currentTile(upper))) {
                    setTile(upper, TOP_WALL);
                }
            }
            for (Position lower : hallway.bottomWalls()) {
                if (!ROOMTiles.contains(currentTile(lower))) {
                    setTile(lower, BOTTOM_WALL);
                }
            }
            for (Position left : hallway.leftWalls()) {
                if (!ROOMTiles.contains(currentTile(left))) {
                    setTile(left, LEFT_WALL);
                }
            }
            for (Position wall : hallway.rightWalls()) {
                if (!ROOMTiles.contains(currentTile(wall))) {
                    setTile(wall, RIGHT_WALL);
                }
            }
            for (Position corner : hallway.cornerWalls()) {
                if (!ROOMTiles.contains(currentTile(corner))) {
                    setTile(corner, CORNER_WALL);
                }
            }
        }

        return hallways;
    }


    /**
     * Draws an L hallway between two rooms, either vertical then horizontal
     * OR horizontal then vertical.
     */
    private Set<Room> connectRooms(Room r1, Room r2, Random random) {
        HashSet<Room> connected = new HashSet<>();

        // Random position in each room
        Position p1 = randomPosition(r1);
        Position p2 = randomPosition(r2);

        // assign x, y values to position
        int x1 = p1.getX();
        int y1 = p1.getY();

        int x2 = p2.getX();
        int y2 = p2.getY();

        // distance between two positions
        int xSpan = Math.abs(x1 - x2) + 1;
        int ySpan = Math.abs(y1 - y2) + 1;

        int coinFlip = random.nextInt(0, 3);
        Room hallway1;
        Room hallway2;

        // if coinFlip is 1, horizontal hallway first, then vertical...
        // else vertical then horizontal.
        hallway1 = (coinFlip == 1) ? new Room(new Position(Math.min(x1, x2), y1), xSpan, 1) :
                                     new Room(new Position(x1, Math.min(y1, y2)), 1, ySpan);
        hallway2 = (coinFlip == 1) ? new Room(new Position(x1, Math.min(y1, y2)), 1, ySpan) :
                                     new Room(new Position(Math.min(x1, x2), y2), xSpan, 1);

        connected.add(hallway1);
        connected.add(hallway2);

        return connected;
    }


    /**
     * Returns an ArrayList of random Positions within WORLD bounds.
     * To be used to generate Rooms.
     */
    private List<Position> generatePositions(int numPositions, Random random) {
        List<Position> positions = new ArrayList<>();

        for (int i = 0; i < numPositions; i++) {
            int x = random.nextInt(1, WIDTH - 1);
            int y = random.nextInt(1, HEIGHT - 1);
            positions.add(new Position(x, y));
        }
        return positions;
    }


    public void placeDoor(Random random) {
        Position randomWall = getRandomWall(random);
        MAP[randomWall.getX()][randomWall.getY()] = DOOR;
    }


    private Position getRandomWall(Random random) {
        List<Position> walls = new ArrayList<>();
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (MAP[x][y].equals(TOP_WALL) || MAP[x][y].equals(BOTTOM_WALL)
                        || MAP[x][y].equals(LEFT_WALL) || MAP[x][y].equals(RIGHT_WALL)) {
                    walls.add(new Position(x, y));
                }
            }
        }
        return walls.get(random.nextInt(walls.size()));
    }


    public void placeKey(Random random, List<Room> rooms) {
        Position randomFloor = getRandomFloor(random, rooms);
        while (randomFloor.getX() != playerX() && randomFloor.getY() != playerY()) {
            randomFloor = getRandomFloor(random, rooms);
        }
        MAP[randomFloor.getX()][randomFloor.getY()] = KEY;
    }


    private Position getRandomFloor(Random random, List<Room> rooms) {
        Room randomRoom = rooms.get(random.nextInt(rooms.size()));
        List<Position> floor = randomRoom.bounds();
        Position randomFloor = floor.get(random.nextInt(floor.size()));
        return randomFloor;
    }

    private List<TETile> adjacentTiles(boolean vertical, Position pos) {        // TODO: optional, a more efficient way to write this
        List<TETile> adjacent = new ArrayList<>();

        for (Position p : adjacentPos(vertical, pos)) {
            adjacent.add(currentTile(p));
        }
        //for (Position p : adjacentPos(pos)) {
        //    adjacent.add(currentTile(p));
        //}
        return adjacent;
    }

    private List<Position> adjacentPos(boolean vertical, Position pos) {
        List<Position> positions = new ArrayList<>();

        if (!vertical) {
            Position upper = new Position(pos.getX(), pos.getY() + 1);
            Position lower = new Position(pos.getX(), pos.getY() - 1);

            positions.add(upper);
            positions.add(lower);

        } else {
            Position left = new Position(pos.getX() + 1, pos.getY());
            Position right = new Position(pos.getX() - 1, pos.getY());

            positions.add(left);
            positions.add(right);
        }

        return positions;
    }

    private TETile currentTile(Position pos) {
        return MAP[pos.getX()][pos.getY()];
    }

    private void setTile(Position pos, TETile tile) {
        MAP[pos.getX()][pos.getY()] = tile;
    }
}
