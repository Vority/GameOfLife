package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {
    // Hvorfor gjør ikke denne noe?
    @BeforeEach
    public void setup() {
        Entity test = new Entity(0, 0);
    }
    

    @Test
    public void testConstructor() {
        Entity test = new Entity(0, 0);
        assertEquals(false, test.check());
    }

    @Test
    public void testLiveAndDie () {
        Entity test = new Entity(0, 0);
        test.live();
        assertEquals(true, test.check());
        test.live();
        assertEquals(true, test.check());
        test.die();
        assertEquals(false, test.check());
        test.die();
        assertEquals(false, test.check());
    }
}