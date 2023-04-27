package Core;

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
        //setWalls();
        setFloor();
    }

    /////
    private void setBounds() {
        for (int x = P.getX(); x <= P.getX() + WIDTH; x++) {
            Position up = new Position(x, P.getY() + HEIGHT);
            Position low = new Position(x, P.getY());

            BOUNDS.add(up);
            BOUNDS.add(low);

            TOP_WALLS.add(up);
            BOTTOM_WALLS.add(low);
        }

        for (int y = P.getY(); y <= P.getY() + HEIGHT; y++) {
            Position right = new Position(P.getX() + WIDTH, y);
            Position left = new Position(P.getX(), y);

            BOUNDS.add(right);
            BOUNDS.add(left);

            RIGHT_WALLS.add(right);
            LEFT_WALLS.add(left);
        }

        CORNER_WALLS.add(new Position(P.getX(), P.getY()));
        CORNER_WALLS.add(new Position(P.getX(), P.getY() + HEIGHT));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY()));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY() + HEIGHT));
    }

    private void setFloor() {
        for (int x = P.getX(); x < P.getX() + WIDTH; x++) {
            for (int y = P.getY(); y < P.getY() + HEIGHT; y++) {
                FLOOR.add(new Position(x, y));
            }
        }
    }

    private void setWalls() {

        CORNER_WALLS.add(new Position(P.getX() - 1, P.getY() - 1));
        CORNER_WALLS.add(new Position(P.getX() - 1, P.getY() + HEIGHT));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY() - 1));
        CORNER_WALLS.add(new Position(P.getX() + WIDTH, P.getY() + HEIGHT));

        for (int x = P.getX(); x < P.getX() + WIDTH ; x++) {
            Position up = new Position(x, P.getY() + HEIGHT);
            Position low = new Position(x, P.getY() - 1);

            TOP_WALLS.add(up);
            BOTTOM_WALLS.add(low);
        }


        for (int y = P.getY(); y < P.getY() + HEIGHT; y++) {
            Position right = new Position(P.getX() + WIDTH, y);
            Position left = new Position(P.getX() - 1, y);

            RIGHT_WALLS.add(right);
            LEFT_WALLS.add(left);
        }

        ALL_WALLS.addAll(CORNER_WALLS);
        ALL_WALLS.addAll(TOP_WALLS);
        ALL_WALLS.addAll(BOTTOM_WALLS);
        ALL_WALLS.addAll(RIGHT_WALLS);
        ALL_WALLS.addAll(LEFT_WALLS);
    }

    public List<Position> allWalls() {
        return ALL_WALLS;
    }

    public List<Position> floor() {
        return FLOOR;
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
        return P;
    }

    public int width() {
        return WIDTH;
    }

    public int height() {
        return HEIGHT;
    }
}
