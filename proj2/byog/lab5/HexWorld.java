package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final long SEED = 28;
    private static final Random RANDOM = new Random(SEED);
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    /**
     * Computes the width of row i for a size s hexagon.
     * s The size of the hex.
     * i The row number where i = 0 is the bottom row.
     */
    private static class Position {
        int x;
        int y;
        private Position(int a, int b) {
            this.x = a;
            this.y = b;
        }
    }

    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return s + 2 * effectiveI;
    }

    /**
     * Computesrelative x coordinate of the leftmost tile in the ith
     * row of a hexagon, assuming that the bottom row has an x-coordinate
     * of zero. For example, if s = 3, and i = 2, this function
     * returns -2, because the row 2 up from the bottom starts 2 to the left
     * of the start position, e.g.
     *   xxxx
     *  xxxxxx
     * xxxxxxxx
     * xxxxxxxx <-- i = 2, starts 2 spots to the left of the bottom of the hex
     *  xxxxxx
     *   xxxx
     *
     * @param s size of the hexagon
     * @param i row num of the hexagon, where i = 0 is the bottom
     * @return
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - effectiveI - 1;
        }
        return -effectiveI;
    }

    /** Adds a row of the same tile.
     * @param world the world to draw on
     * @param p the leftmost position of the row
     * @param width the number of tiles wide to draw
     * @param t the tile to draw
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.x + xi;
            int yCoord = p.y;
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }

    /**
     * Adds a hexagon to the world.
     * @param world the world to draw on
     * @param p the bottom left coordinate of the hexagon
     * @param s the size of the hexagon
     * @param t the tile to draw
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.y + yi;
            int xRowStart = p.x + hexRowOffset(s, yi);
            Position xRowStartP = new Position(xRowStart, thisRowY);
            int rowWidth = hexRowWidth(s, yi);
            addRow(world, xRowStartP, rowWidth, t);
        }
    }

    /**Compute the bottom-left Position of a current hexagon’s neighbors.
     * Computed the right thing to pass to addHexagon so that I could get my topRight neighbor.
     */
    public static Position topRightNeighbor(int n,  Position p) {
        int x = p.x + n + 2;
        int y = p.y + n;
        Position pt = new Position(x, y);
        return pt;
    }

    /** To figure out the starting position for the “top” hex of each column,
     * I used the topRightNeighbor or bottomRightNeighbor on the old top*/
    public static Position bottomRightNeighbor(int s, Position p) {
        int x = p.x + s + 2;
        int y = p.y - s;
        Position pb = new Position(x, y);
        return pb;
    }

    /**Draws a column of N hexes, each one with a random biome.
     *
     */
    public static void drawRandomVerticalHexes(TETile[][] world, int n, Position p) {
        for (int i = 0; i < n; i ++) {
            TETile R = randomTile();
            addHexagon(world, p, 3, R);
            p.y += 6;
        }
    }

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.GRASS;
            default: return Tileset.NOTHING;
        }
    }

    /**basically calls drawRandomVerticalHexes five times, one for each of the
     * five columns of the world, consisting of 3, 4, 5, 4, and 3 hexagons.
     */
    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexWorld[x][y] = Tileset.NOTHING;
            }
        }
        Position p = new Position(15, 15);
        Position temp = new Position(p.x, p.y);
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    temp.x = p.x;
                    temp.y = p.y;
                    drawRandomVerticalHexes(hexWorld, 3, temp);
                    p = bottomRightNeighbor(3, p);
                    break;
                case 1:
                    temp.x = p.x;
                    temp.y = p.y;
                    drawRandomVerticalHexes(hexWorld, 4, temp);
                    p = bottomRightNeighbor(3, p);
                    break;
                case 2:
                    temp.x = p.x;
                    temp.y = p.y;
                    drawRandomVerticalHexes(hexWorld, 5, temp);
                    p = topRightNeighbor(3, p);
                    break;
                case 3:
                    temp.x = p.x;
                    temp.y = p.y;
                    drawRandomVerticalHexes(hexWorld, 4, temp);
                    p = topRightNeighbor(3, p);
                    break;
                case 4:
                    drawRandomVerticalHexes(hexWorld, 3, p);
                    break;
            }
        }
        ter.renderFrame(hexWorld);
    }

    @Test
    public void testHexRowWidth() {
        assertEquals(3, hexRowWidth(3, 5));
        assertEquals(5, hexRowWidth(3, 4));
        assertEquals(7, hexRowWidth(3, 3));
        assertEquals(7, hexRowWidth(3, 2));
        assertEquals(5, hexRowWidth(3, 1));
        assertEquals(3, hexRowWidth(3, 0));
        assertEquals(2, hexRowWidth(2, 0));
        assertEquals(4, hexRowWidth(2, 1));
        assertEquals(4, hexRowWidth(2, 2));
        assertEquals(2, hexRowWidth(2, 3));
    }

    @Test
    public void testHexRowOffset() {
        assertEquals(0, hexRowOffset(3, 5));
        assertEquals(-1, hexRowOffset(3, 4));
        assertEquals(-2, hexRowOffset(3, 3));
        assertEquals(-2, hexRowOffset(3, 2));
        assertEquals(-1, hexRowOffset(3, 1));
        assertEquals(0, hexRowOffset(3, 0));
        assertEquals(0, hexRowOffset(2, 0));
        assertEquals(-1, hexRowOffset(2, 1));
        assertEquals(-1, hexRowOffset(2, 2));
        assertEquals(0, hexRowOffset(2, 3));
    }
}
