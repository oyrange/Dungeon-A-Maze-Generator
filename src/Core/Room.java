package Core;

import java.util.Random;
import java.util.*;

public class Room {
    private Position P; // a room's position is its bottom left corner
    private int WIDTH;
    private int HEIGHT;

    private List<Position> FLOOR = new ArrayList<>();
    private List<Position> ALL_WALLS = new ArrayList<>();
    private List<Position> CORNER_WALLS = new ArrayList<>();
    private List<Position> RIGHT_WALLS = new ArrayList<>();
    private List<Position> LEFT_WALLS = new ArrayList<>();
    private List<Position> TOP_WALLS = new ArrayList<>();
    private List<Position> BOTTOM_WALLS = new ArrayList<>();

    //
    private final List<Position> BOUNDS = new ArrayList<>();

    public Room(Position position, int w, int h) {
        this.P = position;
        this.WIDTH = w;
        this.HEIGHT = h;

        //TETile[] walls = new TETile[][] {RIGHT_WALLS, LEFT_WALLS, TOP_WALLS, BOTTOM_WALLS};

        setBounds();
        setWalls();
    }

    /////
    private void setWalls() {
        for (int x = P.getX(); x <= P.getX() + WIDTH; x++) {
            Position up = new Position(x, P.getY() + HEIGHT);
            Position low = new Position(x, P.getY());

            TOP_WALLS.add(up);
            BOTTOM_WALLS.add(low);
        }

        for (int y = P.getY(); y <= P.getY() + HEIGHT; y++) {
            Position right = new Position(P.getX() + WIDTH, y);
            Position left = new Position(P.getX(), y);

            RIGHT_WALLS.add(right);
            LEFT_WALLS.add(left);
        }
        ALL_WALLS.addAll(TOP_WALLS);
        ALL_WALLS.addAll(BOTTOM_WALLS);
        ALL_WALLS.addAll(RIGHT_WALLS);
        ALL_WALLS.addAll(LEFT_WALLS);

        CORNER_WALLS.add(new Position(P.getX(), P.getY()));
        CORNER_WALLS.add(new Position(P.getX(), P.getY() + HEIGHT));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY()));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY() + HEIGHT));
    }

    private void setBounds() {
        BOUNDS.addAll(P.twoDimensionalPositions(WIDTH, HEIGHT));
    }


    public List<Position> bounds() {
        return BOUNDS;
    }

    public List<Position> walls() {
        return ALL_WALLS;
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
        return P;
    }

    public int width() {
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }

    public boolean containsPosition(Position position) {
        if (BOUNDS.contains(position)) return true;
        return false;
    }

    public Position randomPosition() {
        Random random = new Random();
        return BOUNDS.get(random.nextInt(BOUNDS.size()));
    }

    public boolean overlap(Room room2) {
        for (Position bound : room2.bounds()) {
            if (BOUNDS.contains(bound)) {
                return true;
            }
        }
        return false;
    }
}
