package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.*;
import java.awt.Font;

public class Engine {

    /* TODO, REQUIREMENT:  - fix interactWithInputString()
     *                       Door, key, game over, everything
     *
     * TODO, design:   -  HUD message "i need to find the key..." then "i can unlock the door now..."
     *
     * TODO, design:   -  HUD inventory with picture/graphic of the key in the corner
     *
     * TODO, design:  - draw rectangle to be overlaid by menu items.
     *                  curr implement uses WORLD_HEIGHT vs CANVAS_HEIGHT. ugly.
     * */

    TERenderer ter = new TERenderer();
    public static final int WIDTH = 60;
    public static final int WORLD_HEIGHT = 45;
    public static final int CANVAS_HEIGHT = 50;
    private World WORLD;
    private World SHADOW_WORLD;
    private boolean LOW_LIGHT = false;
    public static final String OLD_WORLD_FILE = "old_world_file.txt";


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.

        input = input.toLowerCase();
        InputSource inputSource = new StringInputDevice(input);

        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();

            if (c == ':') {
                if (inputSource.possibleNextInput()) {
                    if (inputSource.getNextKey() == 'q') {
                        return null;
                    }
                }
            } else if (c == 'l') {
                loadWorld();
                if (WORLD == null) {
                    return null;
                }
                break;
            } else if (c == 'n') {
                long seed = getSeed(inputSource, false);
                WORLD = new World(WIDTH, WORLD_HEIGHT, seed);
                break;
            }
        }

        processGameInputs(inputSource, false);

        if (!LOW_LIGHT) {
            return WORLD.map();
        } else {
            return SHADOW_WORLD.map();
        }
    }


    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        displayMainMenu();
        InputSource inputSource = new KeyboardInputSource();

        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == ':') {
                if (inputSource.possibleNextInput()) {
                    if (inputSource.getNextKey() == 'q') {
                        System.exit(0);
                    }
                }
            } else if (c == 'l') {
                loadWorld();
                // if WORLD does not exist, exit immediately
                if (WORLD == null) {
                    System.exit(0);
                }
                ter.initialize(WIDTH, CANVAS_HEIGHT);
                if (LOW_LIGHT) {
                    ter.renderFrame(SHADOW_WORLD.map());
                } else {
                    ter.renderFrame(WORLD.map());
                }
                break;
            } else if (c == 'n') {
                long seed = getSeed(inputSource, true);
                WORLD = new World(WIDTH, WORLD_HEIGHT, seed);
                ter.initialize(WIDTH, CANVAS_HEIGHT);
                ter.renderFrame(WORLD.map());
                break;
            }
        }
        processGameInputs(inputSource, true);
    }


    /**
     * Parses user inputted seed until receiving (S).
     */
    public long getSeed(InputSource inputSource, boolean display) {
        if (display) {
            displaySeedMenu();
        }

        StringBuilder seed = new StringBuilder();
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == 's') {
                // will otherwise error if only s is pressed
                if (!seed.isEmpty()) {
                    return Long.parseLong(seed.toString());
                }
            }

            if (Character.isDigit(c)) {
                seed.append(c);
            }

            if (display) {
                StdDraw.clear(Color.BLACK);
                displaySeedMenu();
                StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2, seed.toString());
                StdDraw.show();
            }
        }
        return 0L;
    }


    /**
     *  Move player around WORLD.
     */
    public void processGameInputs(InputSource inputSource, boolean display) {
        while (inputSource.possibleNextInput()) {
            if (display) {
                displayHUD();
            }

            char c = inputSource.getNextKey();

            Position newPos = null;
            // move avatar
            if (c == 'w') {
                // move up
                newPos = new Position (WORLD.playerX(), WORLD.playerY() + 1);
            } else if (c == 'a') {
                // move left
                newPos = new Position (WORLD.playerX() - 1, WORLD.playerY());
            } else if (c == 's') {
                // move down
                newPos = new Position (WORLD.playerX(), WORLD.playerY() - 1);
            } else if (c == 'd') {
                // move right
                newPos = new Position (WORLD.playerX() + 1, WORLD.playerY());
            }

            if (newPos != null) {
                if (isKey(newPos)) {
                    WORLD.player().pickUpKey();
                } else if (isDoor(newPos)) {
                    if (WORLD.player().hasKey()) {
                        winMenu(inputSource, display);
                        break;
                    }
                }
                WORLD.placeAvatar(newPos);
            }

            // quit within game
            if (c == ':') {
                if (inputSource.possibleNextInput()) {
                    if (inputSource.getNextKey() == 'q') {
                        saveWorld();
                        if (display) {
                            System.exit(0);
                        }
                        break;
                    }
                }
            }

            //toggle light
            if (c == 'l') {
                LOW_LIGHT = !LOW_LIGHT;
            }
            if (LOW_LIGHT) {
                SHADOW_WORLD = new World(WIDTH, WORLD_HEIGHT);
                SHADOW_WORLD.drawLight(WORLD);
            }

            // return to main menu
            if (c == 'm') {
                saveWorld();
                interactWithKeyboard();
            }

            if (display) {
                if (LOW_LIGHT) {
                    ter.renderFrame(SHADOW_WORLD.map());
                } else {
                    ter.renderFrame(WORLD.map());
                }
            }
        }
    }


    /**
     *  Saves the world by storing the toString of WORLD's 2D array.
     */
    public void saveWorld() {
        Out out = new Out(OLD_WORLD_FILE);
        out.println(TETile.toString(WORLD.map()));
        out.print(LOW_LIGHT);
    }


    /**
     *   Loads the previously saved world from OLD_WORLD_FILE.
     */
    public void loadWorld() {
        In in = new In(OLD_WORLD_FILE);
        if (!in.hasNextLine()) {
            // there is no world saved, so exit immediately
            return;
        }

        // converts old_world_file into a 2D array of character representations of tiles
        char[][] map = new char[WIDTH][WORLD_HEIGHT];

        int y = WORLD_HEIGHT - 1;
        while (in.hasNextLine() && y >= 0) {
            String line = in.readLine();

            for (int x = 0; x < WIDTH; x += 1) {
                map[x][y] = line.charAt(x);
            }

            y -= 1;
        }

        // creates a world using the 2D array (with a new constructor I added in the World class!)
        WORLD = new World(WIDTH, WORLD_HEIGHT, map);

        // loads in the last line, which stores the LOW_LIGHT status of the last game
        if (in.hasNextLine()) {
            in.readLine();               // dummy line
            String line = in.readLine();
            LOW_LIGHT = Boolean.parseBoolean(line);
        }
        if (LOW_LIGHT) {
            SHADOW_WORLD = new World(WIDTH, WORLD_HEIGHT);
            SHADOW_WORLD.drawLight(WORLD);
        }
    }


    /** Returns boolean if @param P is door tile. */
    public boolean isDoor(Position p) {
        return (WORLD.map()[p.getX()][p.getY()].equals(WORLD.DOOR));
    }


    public boolean isKey(Position p) {
        return (WORLD.map()[p.getX()][p.getY()].equals(WORLD.KEY));
    }


    /** Interact with and displayWinMenu(). */
    public void winMenu(InputSource inputSource, boolean display) {
        if (display) {
            displayWinMenu();
        }
        while (inputSource.possibleNextInput()) {
            char c = inputSource.getNextKey();
            if (c == ':') {
                if (inputSource.possibleNextInput()) {
                    if (inputSource.getNextKey() == 'q') {
                        if (display) {
                            System.exit(0);
                        }
                        break;
                    }
                }
            } else if (c == 'm') {
                if (display) {
                    interactWithKeyboard();
                }
            }
        }
    }


    /**
     * Display menu asking user to input a seed.
     */
    private void displaySeedMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2 + 4,"Input a seed (only numbers). Press (S) when you are done!");
        StdDraw.show();
    }


    /**
     * Displays Main Menu: (N) NEW GAME, (L) LOAD GAME, (:Q) QUIT;
     */
    public void displayMainMenu() {
        StdDraw.setCanvasSize(WIDTH * 16, CANVAS_HEIGHT * 16);
        StdDraw.clear(Color.BLACK);

        String title = "CALL OF DUTY 12";

        // how centering / scaling background images is formatted in StdDraw
        //StdDraw.picture(0.5, 0.5, "./byow/TileEngine/images/soldier.jpg", 1, 1);

        Font font = new Font("Comic Sans", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, CANVAS_HEIGHT);

        StdDraw.enableDoubleBuffering();
        StdDraw.setPenColor(Color.WHITE);

        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT - 15, title);

        font = new Font("Comic Sans", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.text(WIDTH / 2, 27, "(N) New Game");
        StdDraw.text(WIDTH / 2, 24, "(L) Load Game");
        StdDraw.text(WIDTH / 2, 21, "(:Q) Quit");

        StdDraw.show();
    }

    /**
     * Display in game menu bar with HUD, lights toggle,
     *  and return to main menu.
     */
    public void displayHUD() {
        /* Description of tile the mouse is hovering over */
        double mouseX = StdDraw.mouseX();
        double mouseY = StdDraw.mouseY();

        if (mouseX < WIDTH && mouseY < WORLD_HEIGHT) {
            int x = (int) mouseX;
            int y = (int) mouseY;
            String currTile;

            if (!LOW_LIGHT) {
                currTile = (WORLD.map()[x][y] == World.BASETILE) ?
                        "The Dark and Creaking Void" : WORLD.map()[x][y].description();
            } else {
                currTile = (SHADOW_WORLD.map()[x][y] == World.BASETILE) ?
                        "???" : SHADOW_WORLD.map()[x][y].description();
            }
            StdDraw.text(50, CANVAS_HEIGHT - 2.5, currTile);
        }

        /* Key status */
        if (!WORLD.player().hasKey()) {
            StdDraw.text(WIDTH / 2, CANVAS_HEIGHT - 2.5, "Get the key.");
        } else {
            StdDraw.text(WIDTH / 2, CANVAS_HEIGHT - 2.5, "You can leave now. Find the door.");
        }

        /* Light toggle */
        if (!LOW_LIGHT) {
            StdDraw.text(10, CANVAS_HEIGHT - 3.8, "(L) Lights off!");
        } else {
            StdDraw.text(10, CANVAS_HEIGHT - 3.8, "(L) Lights on!");
        }

        /* Main menu */
        StdDraw.text(10, CANVAS_HEIGHT - 1, "(M) Main Menu");

        /* Quit */
        StdDraw.text(10, CANVAS_HEIGHT - 2.4, "(:Q) Quit");

        StdDraw.show();
    }

    /**
     * Menu displaying absurd win message and instructions to quit or
     *  return to main menu.
     */
    public void displayWinMenu() {
        String msg1 = "You have become one with the void.";
        String msg2 = "Congrats!";

        StdDraw.clear(Color.black);
        Font font = new Font("Comic Sans", Font.BOLD, 30);
        StdDraw.setFont(font);

        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2 + 1.5, msg1);
        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2, msg2);

        font = new Font("Comic Sans", Font.PLAIN, 15);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2 - 4, "(M) Return to Main Menu");
        StdDraw.text(WIDTH / 2, CANVAS_HEIGHT / 2 - 5, "(:Q) Quit");

        StdDraw.show();
    }
}
