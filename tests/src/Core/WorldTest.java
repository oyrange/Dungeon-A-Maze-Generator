package src.Core;

import Core.Position;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
