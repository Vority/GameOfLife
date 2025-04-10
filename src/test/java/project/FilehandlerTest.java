package project;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class FilehandlerTest {
    private Board board;
    private Filehandler filehandler;

    @BeforeEach
    void setUp() {
        board = new Board();
        filehandler = new Filehandler(board);
        // Wanna remove the random saves.json file that is created here.
        if (filehandler.getFile().exists()) {  
            filehandler.getFile().delete();
        }

        filehandler.file = new File("test_saves.json");
        board.clear();
    }
    @AfterEach
    void removeJSONfile(){ // nice to have here to remove the file after each test. Also prevents duplicate save errors where we dont want to test them.
        if (filehandler.getFile().exists()) {
            filehandler.getFile().delete();
        }
    }


    @Test
    void testSave() throws IOException{
        // Check that save() will work as intended
        // We change the grid from board slightly and then upload it
        board.editEntity(5,5);
        filehandler.save("save1");

        // We verify that the save was completed 
        assertTrue(filehandler.getFile().exists()); // It exists
        assertTrue(filehandler.upload("save1")); // It can find the save
        assertTrue(filehandler.upload("SAVE1")); // letterCase doesnt matter
        assertFalse(filehandler.upload("save2"));  // This also tests the upload method a little bit. 

        // Testing common errors
        // 1 - Duplicates
        assertThrows(IllegalArgumentException.class, () -> filehandler.save("save1"));
        assertThrows(IllegalArgumentException.class, () -> filehandler.save("SAVE1"));
        // 2 - Empty name
        assertThrows(IllegalArgumentException.class, () -> filehandler.save(""));
    }

    @Test
    void testUpload() throws IOException {
        // Checks normal functionality:
        //      First we edit two entities to be alive
        board.editEntity(4, 5);
        board.editEntity(3, 4);
        assertTrue(board.getGrid().get(5).get(4).isAlive());
        assertTrue(board.getGrid().get(4).get(3).isAlive());
        //      We save the grid in the Saves object with name "save3"
        filehandler.save("save3");
        board.clear(); 
        //      After clearing the board we can see that the entities are now dead
        assertFalse(board.getGrid().get(5).get(4).isAlive());
        assertFalse(board.getGrid().get(4).get(3).isAlive());
        //      Uploading save3 should make the entities alive again
        filehandler.upload("save3");
        assertTrue(board.getGrid().get(5).get(4).isAlive());
        assertTrue(board.getGrid().get(4).get(3).isAlive());

        // Testing common errors
        // 1 - Empty name 
        assertThrows(IllegalArgumentException.class, () -> filehandler.upload(""));
        // 2 - Nonexisting save
        assertFalse(filehandler.upload("save4(doesnt exist)"));
    }


}
