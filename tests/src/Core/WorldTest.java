package src.Core;

import Core.Position;
import Core.Room;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorldTest {

    @Test
    public void oneShouldEqualOne() {
        assertEquals(1, 1);
    }

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
        Position randomPosition = room.randomPosition();

        assertTrue(room.containsPosition(randomPosition));
    }

    @Test
    public void roomShouldNotContainPositionOutsideOfBound() {
        Position position1 = new Position(1,1);
        Position position2 = new Position(10,10);

        Room room1 = new Room(position1, 5, 5);
        Room room2 = new Room(position2, 5, 5);

        Position room1Position = room1.randomPosition();
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
}
