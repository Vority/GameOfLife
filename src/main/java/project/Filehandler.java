package project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
 


public class Filehandler {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Board board;
    private File file;
    //private FileWriter file = new FileWriter(null);

    public Filehandler(Board board) throws IOException {
        this.board = board;
        this.file = new File("saves.json");
    }


    public void save(String name) throws StreamWriteException, DatabindException, IOException {  // TA INN BOARD ELLER GRID? ØNSKER HELST GRID.
        // feilhåndtering på name (identifikasjon på stringen)
        
        Saves save = new Saves(name, board.getGrid());
        List<Saves> savesList = new ArrayList<>();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name parameter cannot be null or empty!");
        }
       
        try { // Checks if the file at all exists. We could do this with if file.exists() but we do it this way here instead because we needed to implements error-handling
            savesList = objectMapper.readValue(file, new TypeReference<ArrayList<Saves>>() {}) ;
            for (Saves oldSaves : savesList) {
                if (oldSaves.getName().equals(name)) { // Sjekker om navnet til den nye saven allerede er i bruk
                    throw new IllegalArgumentException("The name is already used!");
                }
            }
            savesList.add(save);    
        } catch (Exception e) {
            System.err.println("Couldnt read a previous list");
            e.printStackTrace();
            savesList.add(save);
        }
        objectMapper.writeValue(file, savesList);
    }

    public void upload(String name){
        List<Saves> savesList = new ArrayList<>();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name parameter cannot be null or empty!");
        }

        try { // Checks if the file at all exists. We could do this with if file.exists() but we do it this way here instead because we needed to implements error-handling
            savesList = objectMapper.readValue(file, new TypeReference<ArrayList<Saves>>() {});  
        } catch (Exception e) {
            System.err.println("Couldnt find Saves objects in the file.");
            e.printStackTrace();
        }

        if (!savesList.isEmpty()) {
            System.out.println("Saves found: ");
            for (Saves s : savesList) {
                System.out.println(" - " + s.getName());
}
            for (Saves save : savesList) {
                if (save.getName().equals(name)) {
                    board.uploadBoard(save.getGrid()); 
                    return;
                }
            }
            System.out.println("No save matches the search name " + name + ".");
        }
    }

    public static class Saves {
        private String name;
        private ArrayList<ArrayList<Entity>> grid;

        public Saves(String name, ArrayList<ArrayList<Entity>> grid){
            this.name = name;
            this.grid = grid;
        }

        // Jackson biblioteket trenger en tom konstruktør for å lese JSON filen
        public Saves(){}

        public String getName() {
            return name;
        }
        public ArrayList<ArrayList<Entity>> getGrid() {
            return grid;
        }
    }
}
