package project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 

public class BoardTest {
    Board testBoard;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
    }

    @Test
    void testConstructor(){
        // Checks that the grid has the right dimentions.
        assertEquals(40, testBoard.getGrid().size());
        for (ArrayList<Entity> row : testBoard.getGrid()) {
            assertEquals(40, row.size());
        }
    
        // Checks that all cells are initially dead
        for (ArrayList<Entity> row : testBoard.getGrid()) {
            for (Entity entity : row) {
                assertFalse(entity.isAlive());
            }
        }

        // Checks that text information starts at zero
        assertEquals(0, testBoard.getIterations());
        assertEquals(0, testBoard.getLiving());
    }

    @Test
    void testEditEntity(){
        // Checks that when I edit an entity that is currenty dead, becomes alive
        testBoard.editEntity(1, 2);
        assertEquals(true, testBoard.getGrid().get(2).get(1).isAlive());;
        // .. and vice versa ^^
        testBoard.editEntity(1, 2);
        assertEquals(false, testBoard.getGrid().get(2).get(1).isAlive());;   
    }

    @Test
    void testSurvival(){
        // Creating a simple living line in the grid
        testBoard.editEntity(1, 2);
        testBoard.editEntity(1, 3);
        testBoard.editEntity(1, 4);

        // Tests that the following simple logic is correct. If this simple example works, the logic should hold for every structure in the grid
        assertEquals(true, testBoard.survival(testBoard.getGrid().get(3).get(1)));
        assertEquals(true, testBoard.survival(testBoard.getGrid().get(3).get(0)));
        assertEquals(true, testBoard.survival(testBoard.getGrid().get(3).get(2)));
        assertEquals(false, testBoard.survival(testBoard.getGrid().get(2).get(1)));
        assertEquals(false, testBoard.survival(testBoard.getGrid().get(4).get(1)));
        assertEquals(false, testBoard.survival(testBoard.getGrid().get(0).get(0)));
    }

    @Test
    void testNextIteration(){     
        // Begins with creating a simple line
        testBoard.editEntity(1, 2);
        testBoard.editEntity(1, 3);
        testBoard.editEntity(1, 4);

        // Checks that everything is in order in regards to what is alive and what isnt alive
        assertEquals(true, testBoard.getGrid().get(3).get(1).isAlive());
        assertEquals(false, testBoard.getGrid().get(3).get(0).isAlive());
        assertEquals(false, testBoard.getGrid().get(3).get(2).isAlive());
        assertEquals(true, testBoard.getGrid().get(2).get(1).isAlive());
        assertEquals(true, testBoard.getGrid().get(4).get(1).isAlive());
        assertEquals(false, testBoard.getGrid().get(0).get(0).isAlive());

        // Checks that after nextIteration is called, the correct entities are now alive and vice versa.
        testBoard.nextIteration(); 
        assertEquals(true, testBoard.getGrid().get(3).get(1).isAlive());
        assertEquals(true, testBoard.getGrid().get(3).get(0).isAlive());
        assertEquals(true, testBoard.getGrid().get(3).get(2).isAlive());
        assertEquals(false, testBoard.getGrid().get(2).get(1).isAlive());
        assertEquals(false, testBoard.getGrid().get(4).get(1).isAlive());
        assertEquals(false, testBoard.getGrid().get(0).get(0).isAlive());

        // Part where we check that the text gets updated correctly
        int iterationsBefore = testBoard.getIterations();
        testBoard.editEntity(1, 4);
        testBoard.nextIteration();

        assertEquals(7, testBoard.getLiving());
        assertEquals(iterationsBefore+1, testBoard.getIterations());
    }

    @Test
    void testClear(){
        // Creating a simple square near the middle of the grid. According to logic this structure should stay unchanged
        testBoard.editEntity(20, 20);
        testBoard.editEntity(21, 20);
        testBoard.editEntity(20, 21);
        testBoard.editEntity(21, 21);

        // Run an iteration so that we can see that clear will take the iterations back to zero
        testBoard.nextIteration();
        assertTrue(testBoard.getIterations() > 0);
        assertTrue(testBoard.getLiving() > 0);

        // Clearing and checking
        testBoard.clear();
        assertEquals(0, testBoard.getIterations());
        assertEquals(0, testBoard.getLiving());
    }

    @Test
    void testUploadBoard(){
        // Creating an empty grid that is supposed to be the grid we get from the Filehandler class to upload.
        ArrayList<ArrayList<Entity>> uploadGrid = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            ArrayList<Entity> row = new ArrayList<>();
            for (int j = 0; j < 40; j++) {
                row.add(new Entity(i, j));
            }
            uploadGrid.add(row);
        }
        // Adding some living entities to the grid to see that something happens when we upload the grid to the empty testBoard.
        uploadGrid.get(5).get(5).live();
        uploadGrid.get(6).get(5).change(); // just using different methonds for fun

        // I will refer to the constructor where we got pretty good confirmation that every entity in the grid is dead at the start. Just in case / as a double-down we will clear the grid aswell.
        testBoard.clear();
        testBoard.uploadBoard(uploadGrid);

        // Checks that the living entities have been transfered over to the grid of the board. (And just quality checking that the dead remain that way)
        assertEquals(true, testBoard.getGrid().get(5).get(5).isAlive());
        assertEquals(true, testBoard.getGrid().get(6).get(5).isAlive());
        assertEquals(false, testBoard.getGrid().get(7).get(7).isAlive());
    }

    
}
