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
}
