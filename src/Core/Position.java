package Core;

import TileEngine.TETile;

import java.util.ArrayList;
import java.util.List;

public class Position {

    /**
     * Understands the coordinates of a Position.
     */

    // TODO: delete Position.(getX(),getY()). It's tangled in so much messy code.

    private int x;
    private int y;
    private TETile tile;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.tile = null;
    }

    /**
     * @return x coordinate -- needs to be deleted.
     */
    public int getX() {
        return x;
    }

    /**
     * @return y coordinate -- needs to be deleted.
     */
    public int getY() {
        return y;
    }

    /**
     * Understands (width, height) 2D array of positions starting from this.position.
     */
    public List<Position> twoDimensionalPositions(int width, int height) {
        List<Position> positions = new ArrayList<>();
        for (int x = this.x; x <= this.x + width; x++) {
            for (int y = this.y; y <= this.y + height; y++) {
                positions.add(new Position(x, y));
            }
        }
        return positions;
    }

    @Override
    public boolean equals(Object other) {
        //return super.equals(other);
        if (other == this) {
            return true;
        }
        if (!(other instanceof Position otherPosition)) {
            return false;
        }
        return otherPosition.x == x && otherPosition.y == y;
    }

    public boolean xCoordinateIsEven() {
        return x % 2 == 0;
    }

    public boolean yCoordinateIsEven() {
        return y % 2 == 0;
    }

    public boolean xCoordinateLargerThan(Position otherPosition) {
        return x > otherPosition.x;
    }

    public boolean yCoordinateLargerThan(Position otherPosition) {
        return y > otherPosition.y;
    }


    // TODO: concat adjacent methods into one ... DRY

    public List<Position> adjacentHorizontalPositions(int distance) {
        List<Position> positions = new ArrayList<>();

        Position left = new Position(x + distance, y);
        Position right = new Position(x - distance, y);

        positions.add(left);
        positions.add(right);

        return positions;
    }

    public List<Position> adjacentVerticalPositions(int distance) {
        List<Position> positions = new ArrayList<>();

        Position upper = new Position(x, y + distance);
        Position lower = new Position(x, y - distance);

        positions.add(upper);
        positions.add(lower);

        return positions;
    }


    public boolean adjacentTo(Position position2) {
        if (adjacentVerticalPositions(1).contains(position2)) {
            return true;
        }
        return adjacentHorizontalPositions(1).contains(position2);
    }

    public Position leftPosition() {
        return new Position(x - 1, y);
    }

    public Position rightPosition() {
        return new Position(x + 1, y);
    }

    public Position lowerPosition() {
        return new Position(x, y - 1);
    }

    public Position upperPosition() {
        return new Position(x, y + 1);
    }

    public void setTile(TETile[][] map, TETile tile) {
        this.tile = tile;
        map[this.x][this.y] = tile;
    }

    //TODO: please for the love of god delete this when you have a better solution in W.generatePaths().
    public TETile getTile() {
        return tile;
    }

    // TODO: remove when no longer needed for WorldTest.roomShouldBeAbleToSetTiles().
    public boolean hasTile() {
        return (this.tile != null);
    }
}
