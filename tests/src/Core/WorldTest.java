package src.Core;

import Core.Position;
import Core.Room;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
}
