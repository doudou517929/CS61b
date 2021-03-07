package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.junit.Test;

import javax.naming.ldap.PagedResultsControl;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

class MapGenerator {

    // Static members

    private static final int MAXROOMW = 6;
    private static final int MAXROOMH = 6;
    private static final String NORTH = "N";
    private static final String EAST = "E";
    private static final String SOUTH = "S";
    private static final String WEST = "W";


    //Instance members
    private Position initialPosition;
    private Random RANDOM;
    private int WIDTH = Game.WIDTH;
    private int HEIGHT = Game.HEIGHT;
    public TETile[][] world;

    MapGenerator(long seed, int initialX, int initialY) {
        initialPosition = new Position(initialX, initialY);
        RANDOM = new Random(seed);
    }

    MapGenerator(int initialX, int initialY) {
        initialPosition = new Position(initialX, initialY);
        RANDOM = new Random();
    }

    private class Position {
        int x;
        int y;
        Position (int a, int b) {
            this.x = a;
            this.y = b;
        }
    }

    private void initialWorld() {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }



    private boolean checkAvailability(Position leftBottom, Position rightUpper) {
        int leftBottomX = leftBottom.x;
        int leftBottomY = leftBottom.y;
        int rightUpperX = rightUpper.x;
        int rightUpperY = rightUpper.y;
        if (leftBottomX < 0 || WIDTH <= leftBottomX
                || leftBottomY < 0 || HEIGHT <= leftBottomY
                || rightUpperX < 0 || WIDTH <= rightUpperX
                || rightUpperY < 0 || HEIGHT <= rightUpperY) {
            return false;
        }
        for (int x = leftBottomX; x <= rightUpperX; x++) {
            for (int y = leftBottomY; y <= rightUpperY; y++) {
                TETile currentTile = world[x][y];
                if (currentTile == Tileset.WALL || currentTile == Tileset.FLOOR) {
                    return false;
                }
            }
        }
        return true;
    }

    private Position[] randomPositionNorth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int leftBottomX = entryPositionX - RANDOM.nextInt(w) - 1;
        int leftBottomY = entryPositionY;
        int rightUpperX = leftBottomX + w + 1;
        int rightUpperY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightUpper = new Position(rightUpperX, rightUpperY);
        if (!checkAvailability(leftBottom, rightUpper)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightUpper};
        }
    }

    private Position[] randomPositionEast(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int leftBottomX = entryPositionX;
        int leftBottomY = entryPositionY - RANDOM.nextInt(h) - 1;
        int rightUpperX = leftBottomX + w + 1;
        int rightUpperY = leftBottomY + h + 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightUpper = new Position(rightUpperX, rightUpperY);
        if (!checkAvailability(leftBottom, rightUpper)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightUpper};
        }
    }

    private Position[] randomPositionSouth(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int rightUpperX = entryPositionX + RANDOM.nextInt(w) + 1;
        int rightUpperY = entryPositionY;
        int leftBottomX = rightUpperX - w - 1;
        int leftBottomY = rightUpperY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightUpper = new Position(rightUpperX, rightUpperY);
        if (!checkAvailability(leftBottom, rightUpper)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightUpper};
        }
    }

    private Position[] randomPositionWest(int w, int h, Position entryPosition) {
        int entryPositionX = entryPosition.x;
        int entryPositionY = entryPosition.y;
        int rightUpperX = entryPositionX;
        int rightUpperY = entryPositionY + RANDOM.nextInt(h) + 1;
        int leftBottomX = rightUpperX - w - 1;
        int leftBottomY = rightUpperY - h - 1;
        Position leftBottom = new Position(leftBottomX, leftBottomY);
        Position rightUpper = new Position(rightUpperX, rightUpperY);
        if (!checkAvailability(leftBottom, rightUpper)) {
            return null;
        } else {
            return new Position[]{leftBottom, rightUpper};
        }
    }

    private void drawRoom(Position leftBottom, Position rightUpper) {
        int leftBottomX = leftBottom.x;
        int leftBottomY = leftBottom.y;
        int rightUpperX = rightUpper.x;
        int rightUpperY = rightUpper.y;

        for (int x = leftBottomX; x <= rightUpperX; x++) {
            for(int y = leftBottomY; y <= rightUpperY; y++) {
                if (x == leftBottomX || x == rightUpperX || y == leftBottomY || y == rightUpperY) {
                    world[x][y] = Tileset.WALL;
                } else {
                    world[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    private void makeInitialEntrance(Position initialEntryPosition) {
        world[initialEntryPosition.x][initialEntryPosition.y] = Tileset.LOCKED_DOOR;
    }

    private void triExit(Position leftBottom, Position rightUpper, String currentDirection) {
        Object[] exitAndDirection1 = randomExit(leftBottom, rightUpper, currentDirection);
        Position nextExitPosition1 = (Position) exitAndDirection1[0];
        String nextDirection1 = (String) exitAndDirection1[1];

        Object[] exitAndDirection2;
        Position nextExitPosition2 = nextExitPosition1;
        String nextDirection2 = nextDirection1;
        while (nextDirection2.equals(nextDirection1)) {
            exitAndDirection2 = randomExit(leftBottom, rightUpper, currentDirection);
            nextExitPosition2 = (Position) exitAndDirection2[0];
            nextDirection2 = (String) exitAndDirection2[1];
        }

        Object[] exitAndDirection3;
        Position nextExitPosition3 = nextExitPosition1;
        String nextDirection3 = nextDirection1;
        while (nextDirection3.equals(nextDirection1) || nextDirection3.equals(nextDirection2)) {
            exitAndDirection3 = randomExit(leftBottom, rightUpper, currentDirection);
            nextExitPosition3 = (Position) exitAndDirection3[0];
            nextDirection3 = (String) exitAndDirection3[1];
        }

        recursiveAddRandomRoom(nextExitPosition1, nextDirection1);
        recursiveAddRandomRoom(nextExitPosition2, nextDirection2);
        recursiveAddRandomRoom(nextExitPosition3, nextDirection3);
    }

    private Object[] randomExit(Position leftBottom, Position rightUpper, String currentDirection) {
        int leftBottomX = leftBottom.x;
        int leftBottomY = leftBottom.y;
        int rightUpperX = rightUpper.x;
        int rightUpperY = rightUpper.y;
        int w = rightUpperX - leftBottomX - 1;
        int h = rightUpperY - leftBottomY - 1;
        Object[] exitAndDirection = new Object[2];

        // decide nextDirection
        List<String> directions = new LinkedList<>();
        directions.add(NORTH);
        directions.add(EAST);
        directions.add(SOUTH);
        directions.add(WEST);
        directions.remove(getReverseDirection(currentDirection));
        String nextDirection = directions.get(RANDOM.nextInt(directions.size()));

        // decide next exitPosition
        Position nextExitPosition;
        switch (nextDirection) {
            case NORTH:
                nextExitPosition = new Position(rightUpperX - RANDOM.nextInt(w) - 1, rightUpperY);
                break;
            case EAST:
                nextExitPosition = new Position(rightUpperX, rightUpperY - RANDOM.nextInt(h) - 1);
                break;
            case SOUTH:
                nextExitPosition = new Position(leftBottomX + RANDOM.nextInt(w) + 1, leftBottomY);
                break;
            default:
                nextExitPosition = new Position(leftBottomX, leftBottomY + RANDOM.nextInt(h) + 1);
                break;
        }
        exitAndDirection[0] = nextExitPosition;
        exitAndDirection[1] = nextDirection;
        return exitAndDirection;
    }

    private String getReverseDirection(String direction) {
        switch (direction) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            default:
                return EAST;
        }
    }

    private void recursiveAddRandomRoom(Position exitPosition, String currentDirection) {
        int exitX = exitPosition.x;
        int exitY = exitPosition.y;
        int w = RANDOM.nextInt(MAXROOMW) + 1;
        int h = RANDOM.nextInt(MAXROOMH) + 1;

        Position entryPosition;
        Position[] lrPositions;
        switch (currentDirection) {
            case NORTH:
                entryPosition = new Position(exitX, exitY + 1);
                lrPositions = randomPositionNorth(w, h, entryPosition);
                break;
            case EAST:
                entryPosition = new Position(exitX + 1, exitY);
                lrPositions = randomPositionEast(w, h, entryPosition);
                break;
            case SOUTH:
                entryPosition = new Position(exitX, exitY - 1);
                lrPositions = randomPositionSouth(w, h, entryPosition);
                break;
            default:
                entryPosition = new Position(exitX - 1, exitY);
                lrPositions = randomPositionWest(w, h, entryPosition);
                break;
        }

        if (lrPositions != null) {
            makeExit(exitPosition);
            Position leftBottom = lrPositions[0];
            Position rightUpper = lrPositions[1];
            drawRoom(leftBottom, rightUpper);
            makeEntrance(entryPosition);

            switch (RANDOM.nextInt(3)) {
                case 0:
                    monoExit(leftBottom, rightUpper, currentDirection);
                    break;
                case 1:
                    biExit(leftBottom, rightUpper, currentDirection);
                    break;
                default:
                    triExit(leftBottom, rightUpper, currentDirection);
                    break;
            }
        }
    }

    private void monoExit(Position leftBottom, Position rightUpper, String currentDirection) {
        Object[] exitAndDirection = randomExit(leftBottom, rightUpper, currentDirection);
        Position nextExitPosition = (Position) exitAndDirection[0];
        String nextDirection = (String) exitAndDirection[1];
        recursiveAddRandomRoom(nextExitPosition, nextDirection);
    }

    private void biExit(Position leftBottom, Position rightUpper, String currentDirection) {
        Object[] exitAndDirection1 = randomExit(leftBottom, rightUpper, currentDirection);
        Position nextExitPosition1 = (Position) exitAndDirection1[0];
        String nextDirection1 = (String) exitAndDirection1[1];

        Object[] exitAndDirection2;
        Position nextExitPosition2 = nextExitPosition1;
        String nextDirection2 = nextDirection1;
        while (nextDirection2.equals(nextDirection1)) {
            exitAndDirection2 = randomExit(leftBottom, rightUpper, currentDirection);
            nextExitPosition2 = (Position) exitAndDirection2[0];
            nextDirection2 = (String) exitAndDirection2[1];
        }
        recursiveAddRandomRoom(nextExitPosition1, nextDirection1);
        recursiveAddRandomRoom(nextExitPosition2, nextDirection2);
    }

    private void makeExit(Position exitPoint) {
        world[exitPoint.x][exitPoint.y] = Tileset.FLOOR;
    }

    private void makeEntrance(Position initialEntryPosition) {
        world[initialEntryPosition.x][initialEntryPosition.y] = Tileset.FLOOR;
    }

    TETile[][] generateWorld() {
        initialWorld();

        // Make the first room
        Position[] lrPositions = randomPositionNorth(MAXROOMW, MAXROOMH, initialPosition);
        Position leftBottom = lrPositions[0];
        Position rightUpper = lrPositions[1];
        drawRoom(leftBottom, rightUpper);
        makeInitialEntrance(initialPosition);
        triExit(leftBottom, rightUpper, NORTH);
        return world;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 50);
        MapGenerator wg = new MapGenerator(42, 40, 5);
        wg.generateWorld();
        ter.renderFrame(wg.world);
    }
}
