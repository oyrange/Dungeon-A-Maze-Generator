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

    public List<Position> adjacentPositions(Position startingPosition, int width, int height) {
        List<Position> positions = new ArrayList<>();
        for (int x = startingPosition.x; x <= startingPosition.x + width; x++) {
            for (int y = startingPosition.y; y <= startingPosition.y + height; y++) {
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
}
