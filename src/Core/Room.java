package Core;

import TileEngine.TETile;

import java.util.Random;
import java.util.*;

    // TODO: refactor area()-- getter and setter used in W.java generateRooms()

    //TODO: remove width(), height() getters and setters.

/**
 * Understands the bounds of a Room.
 */

public class Room {
    private Position STARTING_POSITION; // a room's position is its bottom left corner
    private int WIDTH;
    private int HEIGHT;

    private final List<Position> FLOOR = new ArrayList<>();
    private final List<Position> BOUNDS = new ArrayList<>();
    private final List<Position> CORNER_WALLS = new ArrayList<>();
    private final List<Position> RIGHT_WALLS = new ArrayList<>();
    private final List<Position> LEFT_WALLS = new ArrayList<>();
    private final List<Position> TOP_WALLS = new ArrayList<>();
    private final List<Position> BOTTOM_WALLS = new ArrayList<>();

    //
    private final List<Position> AREA = new ArrayList<>();

    public Room(Position position, int w, int h) {
        this.STARTING_POSITION = position;
        this.WIDTH = w;
        this.HEIGHT = h;

        //TETile[] walls = new TETile[][] {RIGHT_WALLS, LEFT_WALLS, TOP_WALLS, BOTTOM_WALLS};

        setArea();
        setBounds();
    }

    /////
    private void setBounds() {
        for (int x = STARTING_POSITION.getX(); x <= STARTING_POSITION.getX() + WIDTH; x++) {
            Position up = new Position(x, STARTING_POSITION.getY() + HEIGHT);
            Position low = new Position(x, STARTING_POSITION.getY());

            TOP_WALLS.add(up);
            BOTTOM_WALLS.add(low);
        }

        for (int y = STARTING_POSITION.getY(); y <= STARTING_POSITION.getY() + HEIGHT; y++) {
            Position right = new Position(STARTING_POSITION.getX() + WIDTH, y);
            Position left = new Position(STARTING_POSITION.getX(), y);

            RIGHT_WALLS.add(right);
            LEFT_WALLS.add(left);
        }
        BOUNDS.addAll(TOP_WALLS);
        BOUNDS.addAll(BOTTOM_WALLS);
        BOUNDS.addAll(RIGHT_WALLS);
        BOUNDS.addAll(LEFT_WALLS);

        CORNER_WALLS.add(new Position(STARTING_POSITION.getX(), STARTING_POSITION.getY()));
        CORNER_WALLS.add(new Position(STARTING_POSITION.getX(), STARTING_POSITION.getY() + HEIGHT));
        CORNER_WALLS.add(new Position(STARTING_POSITION.getX() + WIDTH, STARTING_POSITION.getY()));
        CORNER_WALLS.add(new Position(STARTING_POSITION.getX() + WIDTH, STARTING_POSITION.getY() + HEIGHT));
    }

    private void setArea() {
        AREA.addAll(STARTING_POSITION.twoDimensionalPositions(WIDTH, HEIGHT));
    }

    // TODO: remove area()
    public List<Position> area() {
        return AREA;
    }

    public List<Position> bounds() {
        return BOUNDS;
    }

    public List<Position> cornerWalls() {
        return CORNER_WALLS;
    }

    public List<Position> rightWalls() {
        return RIGHT_WALLS;
    }

    public List<Position> leftWalls() {
        return LEFT_WALLS;
    }

    public List<Position> topWalls() {
        return TOP_WALLS;
    }

    public List<Position> bottomWalls() {
        return BOTTOM_WALLS;
    }

    public Position position() {
        return STARTING_POSITION;
    }

    public int width() {
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }

    public boolean containsPosition(Position position) {
        return AREA.contains(position);
    }

    public Position randomPosition(Random random) { //TODO: random arg makes sure samecode world generates
                                                    // same every time, but it's ugly
        //Random random = new Random();
        return AREA.get(random.nextInt(AREA.size()));
    }

    public boolean overlap(Room otherRoom) {
        for (Position bound : otherRoom.area()) {
            if (AREA.contains(bound)) {
                return true;
            }
        }
        return false;
    }

    public boolean totalOverlap(Room otherRoom) {
        return new HashSet<>(AREA).containsAll(otherRoom.AREA);
    }

    public boolean adjacentToRoom(Room otherRoom) {
        for (Position position : BOUNDS) {
            for (Position otherPosition : otherRoom.BOUNDS) {
                if (position.adjacentTo(otherPosition)) return true;
            }
        }
        return false;
    }

    public void setTiles(TETile[][] map, TETile floorTile, TETile wallTile, TETile cornerTile) {
        for (Position floor : AREA) {
            floor.setTile(map, floorTile);
        }
        for (Position wall : BOUNDS) {
            wall.setTile(map, wallTile);
        }
        for (Position corner : CORNER_WALLS) {
            corner.setTile(map, cornerTile);
        }
    }
}
