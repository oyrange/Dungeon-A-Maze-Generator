package byow.TileEngine;

import java.awt.*;

public class debugTile {
    public static final TETile BLACK = new TETile(' ', Color.black, Color.black, "you");
    public static final TETile OTHER_BLACK = new TETile(' ', new Color(0, 0, 0), new Color(0, 0, 0), "you");
    public static final TETile DARK_GREY = new TETile(' ', Color.darkGray, Color.darkGray,
            "wall");
    public static final TETile LIGHT_GREY = new TETile(' ', Color.LIGHT_GRAY, Color.LIGHT_GRAY, "floor");
    public static final TETile otherLIGHT_GREY = new TETile(' ', Color.LIGHT_GRAY, Color.LIGHT_GRAY, "floor");
    public static final TETile WHITE = new TETile(' ', Color.WHITE, Color.WHITE, "you");
    public static final TETile otherWHITE = new TETile(' ', Color.WHITE, Color.WHITE, "you");

    public static final TETile CYAN = new TETile(' ', new Color(21,76,121), new Color(21,76,121), "you");
}
