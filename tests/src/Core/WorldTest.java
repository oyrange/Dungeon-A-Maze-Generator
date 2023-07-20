package src.Core;

import Core.MazeGraph;
import Core.Position;
import Core.Room;
import TileEngine.TETile;
import TileEngine.debugTile;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

public class WorldTest {

    static Random RANDOM = new Random(4444);
    static TETile TILE = debugTile.BLACK;
    static TETile[][] MAP = new TETile[10][10];

    @Test
    public void aPositionShouldBeEqualToItself() {
        Position position = new Position(1, 1);
        assertEquals(position, position);
    }

    @Test
    public void aPositionShouldBeEqualToAPositionInTheSamePlace() {
        Position position = new Position(1, 1);
        Position anotherPosition = new Position(1, 1);

        assertEquals(position, anotherPosition);
    }

    @Test
    public void roomShouldContainStartingPosition() {
        Position position = new Position(1,1);
        Room room = new Room(position, 5, 5);
        assertTrue(room.containsPosition(position));
    }

    @Test
    public void roomShouldContainBoundPositions() {
        Position position = new Position(1,1);
        Room room = new Room(position, 5, 5);
        Position randomPosition = room.randomPosition(RANDOM);

        assertTrue(room.containsPosition(randomPosition));
    }

    @Test
    public void roomShouldNotContainPositionOutsideOfBound() {
        Position position1 = new Position(1,1);
        Position position2 = new Position(10,10);

        Room room1 = new Room(position1, 5, 5);
        Room room2 = new Room(position2, 5, 5);

        Position room1Position = room1.randomPosition(RANDOM);
        assertFalse(room2.containsPosition(room1Position));
    }

    @Test
    public void roomShouldKnowIfItOverlapsWithAnotherRoom() {
        Position position = new Position(1,1);

        Room room1 = new Room(position, 5, 5);
        Room room2 = new Room(position, 2, 2);

        assertTrue(room1.overlap(room2));
    }

    @Test
    public void roomShouldKnowIfItOverlapsWithARoomAtSeparateStartingPosition() {
        Position position1 = new Position(1,1);
        Position position2 = new Position(5,5);

        Room room1 = new Room(position1, 5, 5);
        Room room2 = new Room(position2, 5, 5);

        assertTrue(room1.overlap(room2));
    }

    @Test
    public void roomShouldKnowIfItDoesNotOverlapWithAnotherRoom() {
        Position position1 = new Position(1,1);
        Position position2 = new Position(10,10);

        Room room1 = new Room(position1, 5, 5);
        Room room2 = new Room(position2, 5, 5);

        assertFalse(room1.overlap(room2));
    }

    @Test
    public void positionShouldKnowIfAtOddXCoordinate() {
        Position oddPosition = new Position(1, 1);
        Position evenPosition = new Position(2, 2);

        assertFalse(oddPosition.xCoordinateIsEven());
        assertTrue(evenPosition.xCoordinateIsEven());
    }

    @Test
    public void positionShouldKnowIfAtOddYCoordinate() {
        Position oddPosition = new Position(1, 1);
        Position evenPosition = new Position(2, 2);

        assertFalse(oddPosition.yCoordinateIsEven());
        assertTrue(evenPosition.yCoordinateIsEven());
    }

    @Test
    public void positionShouldKnowIfXCoordIsGreaterThanAnotherPositionXCoord() {
        Position smallerPosition = new Position(1, 1);
        Position largerPosition = new Position(2, 2);

        assertFalse(smallerPosition.xCoordinateLargerThan(largerPosition));
    }

    @Test
    public void positionShouldKnowIfYCoordIsGreaterThanAnotherPositionYCoord() {
        Position smallerPosition = new Position(1, 1);
        Position largerPosition = new Position(2, 2);

        assertFalse(smallerPosition.yCoordinateLargerThan(largerPosition));
    }

    @Test
    public void roomShouldGenerateRandomPositionInsideItself() {
        Position position = new Position(1,1);
        Room room = new Room(position, 5,5);
        Position randomPosition = room.randomPosition(RANDOM);

        assertTrue(room.containsPosition(randomPosition));
    }

    @Test
    public void positionShouldKnowAdjacentHorizontalPositions() {
        Position leftPosition = new Position(0, 1);
        Position centrePosition = new Position(1, 1);
        Position rightPosition = new Position(2, 1);

        List<Position> adjacentPositionsList = centrePosition.adjacentHorizontalPositions(1);

        assertTrue(adjacentPositionsList.size() == 2);
        assertTrue(adjacentPositionsList.contains(leftPosition));
        assertTrue(adjacentPositionsList.contains(rightPosition));
    }

    @Test
    public void positionShouldKnowAdjacentVerticalPositions() {
        Position lowerPosition = new Position(1, 0);
        Position centrePosition = new Position(1, 1);
        Position upperPosition = new Position(1, 2);

        List<Position> adjacentPositionsList = centrePosition.adjacentVerticalPositions(1);

        assertTrue(adjacentPositionsList.size() == 2);
        assertTrue(adjacentPositionsList.contains(lowerPosition));
        assertTrue(adjacentPositionsList.contains(upperPosition));
    }

    @Test
    public void roomShouldKnowIfTouchingAnotherRoom() {
        Position position1 = new Position(1,1);
        Room room1 = new Room(position1, 5,5);

        Position position2 = new Position(6,6);
        Room room2 = new Room(position1, 5,5);

        assertTrue(room1.adjacentToRoom(room2));
    }

    @Test
    public void positionShouldKnowIfAdjacentToAnother() {
        Position position1 = new Position(1,1);
        Position position2 = new Position(2, 1);

        assertTrue(position1.adjacentTo(position2));
    }

    @Test
    public void positionShouldKnowIfOtherPosIsToLeftOrRight() {
        Position leftPosition = new Position(0, 1);
        Position centrePosition = new Position(1, 1);
        Position rightPosition = new Position(2, 1);

        assertTrue(centrePosition.leftPosition().equals(leftPosition));
        assertTrue(centrePosition.rightPosition().equals(rightPosition));
    }

    @Test
    public void positionShouldKnowIfOtherPosIsUpOrDown() {
        Position lowerPosition = new Position(1, 0);
        Position centrePosition = new Position(1, 1);
        Position upperPosition = new Position(1, 2);

        assertTrue(centrePosition.lowerPosition().equals(lowerPosition));
        assertTrue(centrePosition.upperPosition().equals(upperPosition));
    }

    @Test
    public void roomShouldBeAbleToSetTiles() {
        Room newRoom = new Room(new Position(0, 0), 2, 2);
        newRoom.setTiles(MAP, TILE, TILE, TILE);
        assertTrue(newRoom.randomPosition(RANDOM).hasTile());
    }

    @Test
    public void positionShouldBeAbleToModify2DArray() {
        Position position = new Position(1, 1);
        position.setTile(MAP, debugTile.BLACK);

        assertTrue(MAP[1][1].equals(debugTile.BLACK));

        position.setTile(MAP, null);  // is reset necessary?
    }

    @Test
    public void roomShouldBeAbleToChooseConnectorInsideItself() {
        Room newRoom = new Room((new Position(0,0)), 2, 2);

        Position internalPosition = newRoom.randomPosition(RANDOM);
        Position otherExternalPosition = new Position(10,10);
        Position anotherExternalPosition = new Position(20,20);
        Position finalExternalPosition = new Position(30,30);

        List<Position> allPositions = new ArrayList<>();
        allPositions.add(internalPosition);
        allPositions.add(otherExternalPosition);
        allPositions.add(anotherExternalPosition);
        allPositions.add(finalExternalPosition);

        Position position = newRoom.selectOneInternalPosition(RANDOM, allPositions);

        assertTrue(newRoom.containsPosition(position));
    }
}
