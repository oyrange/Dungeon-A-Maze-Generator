package byow.Core;

import byow.TileEngine.TETile;

public class Avatar {
    public Position p;

    public boolean hasKey;

    public Avatar(Position p) {
        this.p = p;
        this.hasKey = false;
    }

    public Position getPosition() {
        return p;
    }

    public void pickUpKey() {
        this.hasKey = true;
    }

    public boolean hasKey() {
        return hasKey;
    }
}
