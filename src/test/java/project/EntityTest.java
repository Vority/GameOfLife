package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EntityTest {
    // Hvorfor gjør ikke denne noe?
    Entity test;
    @BeforeEach
    void setup() {
        test = new Entity(0, 0);
    }
    

    @Test
    void testConstructor() {
        // Just testing that the entity is dead upon creation (word?)
        assertEquals(false, test.isAlive());
    }

    @Test
    void testLiveAndLetDie () {
        // Testing all three methods to change the alive-state of an Entity
        test.live();
        assertEquals(true, test.isAlive());
        test.live();
        assertEquals(true, test.isAlive());
        test.die();
        assertEquals(false, test.isAlive());
        test.die();
        assertEquals(false, test.isAlive());
        test.change();
        assertEquals(true, test.isAlive());
        test.change();
        assertEquals(false, test.isAlive());
    }
}