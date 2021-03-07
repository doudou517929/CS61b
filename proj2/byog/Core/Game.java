package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;

public class Game {

    /* Instance members */
    TERenderer ter = new TERenderer();
    private boolean setupMode = true;
    private boolean newGameMode = false; //check whether a new game is gonna be generated
    private boolean quitMode = false;
    private String seedString = "";
    private TETile[][] world;
    private int playerX;
    private int playerY;

    /* Static members. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 50;
    private static final int ENTRYX = 40;
    private static final int ENTRYY = 5;
    private static final String PATH = "saved.txt";
    private static final String NORTH = "w";
    private static final String EAST = "d";
    private static final String SOUTH = "s";
    private static final String WEST = "a";
    private static final int WELCOMEWIDTH = 600;
    private static final int WELCOMEHEIGHT = 800;

    private void switchNewGameMode() {
        newGameMode = !newGameMode;
    }

    private void switchSetupMode() {
        setupMode = !setupMode;
    }

    private void processInput(String input) {
        if (input == null) {
            System.out.println("No input given");
            System.exit(0);
        }
        String first = Character.toString(input.charAt(0));
        first = first.toLowerCase();
        processInputString(first);
        if (input.length() > 1) {
            String rest = input.substring(1);
            processInput(rest);
        }
    }

    private void processInputString(String first) {
        if (setupMode) {
            switch (first) {
                case "n":
                    switchNewGameMode();
                    break;
                case "s":
                    setupNewGame();
                    break;
                case "l":    //load the previously saved game
                    load();
                    break;
                case "q":
                    System.exit(0);
                    break;
                default:
                    try {
                        Long.parseLong(first);
                        seedString += first;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input given: " + first);
                        System.exit(0);
                    }
                    break;
            }
        } else {
            switch (first) {
                case NORTH:
                case "o":
                case EAST:
                case "l":
                case SOUTH:
                case "n":
                case WEST:
                case "k":
                    move(first);
                    break;
                case ":":
                    switchQuitMode();
                    break;
                case "q":
                    saveAndQuit();
                    System.exit(0);
                    break;
                default:
            }
        }
    }

    private void setupNewGame() {
        if (!newGameMode) {
            String error = "Input string " + "\"S\" given, but no game has been initialized.\n"
                    + "Please initialize game first by input string \"N\" and following random seed" +
                    "numbers";
            System.out.println(error);
            System.exit(0);
        }
        switchNewGameMode();

        //setup a random seed and generate a world according to it
        MapGenerator wg;
        if (seedString.equals("")) {
            wg = new MapGenerator(ENTRYX, ENTRYY);
        } else {
            long seed = Long.parseLong(seedString);
            wg = new MapGenerator(seed, ENTRYX, ENTRYY);
        }
        world = wg.generateWorld();

        //setup a player
        world[ENTRYX][ENTRYY + 1] = Tileset.PLAYER;
        playerX = ENTRYX;
        playerY = ENTRYY + 1;

        //switch off setupMode
        switchSetupMode();
    }

    private void load() {
        File f = new File(PATH);
        try {
            FileInputStream fis = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fis);
            world = (TETile[][]) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            System.out.println("No previously saved world found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println("Class TETile[][] not found");
            System.exit(1);
        }
        switchSetupMode();
        rewritePlayerLocation();
    }

    private void rewritePlayerLocation() {
        for (int w = 0; w < WIDTH; w++) {
            for (int h = 0; h < HEIGHT; h++) {
                if (world[w][h].equals(Tileset.PLAYER)) {
                    playerX = w;
                    playerY = h;
                }
            }
        }
    }

    private void move(String input) {
        switch (input) {
            case NORTH:
            case "o":
                if (world[playerX][playerY + 1].equals(Tileset.FLOOR)) {
                    world[playerX][playerY + 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY += 1;
                }
                return;
            case EAST:
            case "l":
                if (world[playerX + 1][playerY].equals(Tileset.FLOOR)) {
                    world[playerX + 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX += 1;
                }
                return;
            case SOUTH:
            case "n":
                if (world[playerX][playerY - 1].equals(Tileset.FLOOR)) {
                    world[playerX][playerY - 1] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerY -= 1;
                }
                return;
            case WEST:
            case "k":
                if (world[playerX - 1][playerY].equals(Tileset.FLOOR)) {
                    world[playerX - 1][playerY] = Tileset.PLAYER;
                    world[playerX][playerY] = Tileset.FLOOR;
                    playerX -= 1;
                }
                return;
            default:
        }
    }

    private void switchQuitMode() {
        quitMode = !quitMode;
    }

    private void saveAndQuit() {
        if (!quitMode) {
            return;
        }
        switchQuitMode();
        File f = new File(PATH);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(world);
            oos.close();
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }
    }

    private void processWelcome() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(WELCOMEWIDTH, WELCOMEHEIGHT);
        StdDraw.clear(StdDraw.BLACK);

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String typed = Character.toString(StdDraw.nextKeyTyped());
                processInput(typed);
            }
            renderWelcomeBoard();
            if (!setupMode) {
                break;
            }
        }
        processGame();
    }

    private void renderWelcomeBoard() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        // title
        StdDraw.setFont(new Font("Arial", Font.BOLD, 40));
        StdDraw.text(0.5, 0.8, "CS61B: BYoG");
        // menu
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 20));
        StdDraw.text(0.5, 0.5, "New Game: N");
        StdDraw.text(0.5, 0.475, "Load Game: L");
        StdDraw.text(0.5, 0.45, "Quit: Q");
        // seed
        if (newGameMode) {
            StdDraw.text(0.5, 0.25, "Seed: " + seedString);
            StdDraw.text(0.5, 0.225, "(Press S to start the game)");
        }
        StdDraw.show();
        StdDraw.pause(100);
    }

    private void processGame() {
        ter.initialize(WIDTH, HEIGHT);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                String typed = Character.toString(StdDraw.nextKeyTyped());
                processInput(typed);
            }
            renderGame();
        }
    }

    private void renderGame() {
        renderWorld();
        showTileOnHover();
        StdDraw.pause(10);
    }

    private void renderWorld() {
        StdDraw.setFont();
        StdDraw.setPenColor();
        ter.renderFrame(world);
    }

    private void showTileOnHover() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        TETile mouseTile = world[mouseX][mouseY];
        // draw as text
        StdDraw.setFont(new Font("Arial", Font.PLAIN, 15));
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1, mouseTile.description());
        StdDraw.show();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        processWelcome();
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().
        processInput(input);
        return world;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.playWithKeyboard();
    }
}
