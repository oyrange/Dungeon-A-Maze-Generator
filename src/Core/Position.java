package Core;

import java.util.ArrayList;
import java.util.List;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Understands (width, height) 2D array of positions starting from this. position.
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
}
