package project;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Filehandler {
    private Board board;
    protected File file; // Its protected because of the unit testing (we need to change the file there soo..)

    public Filehandler(Board board){
        this.board = board;
        this.file = new File("saves.json");
        try { // If the file doesnt exist, try to create a new one. 
            if (!file.exists()) { 
                file.createNewFile();
            }
        } catch (IOException e) { // If we get an IOException we just print it out. Shouldnt really ever happen.
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    // Method for saving the current grid of the board, with the argument name (identifier) in a Saves object.
    public void save(String name) {
        // Checks if the name is null or empty. Throws exception if so. The controller will pick up on that and sends and errorText for the user.        
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name parameter cannot be null or empty.");
        }
        if (name.contains("\"")) {
            throw new IllegalArgumentException("You cannot use \" in your name.");
        }
        // Unpacking the saves.json file into a list of Saves objects.
        List<Saves> savesList = loadSaves();
    
        // Checks if the name argument already is the name of some other save. 
        for (Saves saves : savesList) {
            if (saves.getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("The name is already used.");
            }
        }
        // Making a new Saves object. Name is the name-argument and the grid will always be the current grid of the board.
        Saves save = new Saves(name.toUpperCase(), board.getGrid());
        savesList.add(save); // if the JSON file was empty or non-existent -> savesList was an empty list. 
        writeSaves(savesList); // write to file
    }

    // Running through every Saves object in the json file, and uploads the one with the same name as the argument name
    public boolean upload(String name){
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The name parameter cannot be null or empty!");
        }
        // Unpacking the saves.json file into a list of Saves objects.
        List<Saves> savesList = loadSaves();

        // Just a little user thing here, to see what saves we have found. Isnt visible to a user. Just terminal stuff..
        System.out.println("Saves found: ");
        for (Saves save : savesList) {
            System.out.println(" - " + save.getName());
}
        // Going through every save in the list
        for (Saves save : savesList) { 
            // If we find the name-argument in the saves-file we upload the corresponding grid, but only if the size of the grid is 40x40. 
            if (save.getName().equals(name.toUpperCase()) && save.getGrid().size() == 40 && save.getGrid().get(0).size() == 40) {
                board.uploadBoard(save.getGrid()); 
                return true; // returns true if we have found the save
            }
        }
        System.out.println("No save matches the search name " + name.toUpperCase() + ".");
        return false; // returns false if we have not found the save
    }

    private void writeSaves(List<Saves> savesList) {
        try (FileWriter writer = new FileWriter(file)) {
            // Begins the list of the saves objects
            writer.write("[\n");

            // For every Saves object in the savesList
            for (int i = 0; i < savesList.size(); i++) {
                Saves s = savesList.get(i);
                // Write the saves object to JSON in proper JSON format (toJSON())
                writer.write(s.toJSON()); 
                // if it is not the final element in the list, add a comma to seperate the objects in the JSON file.
                if (i < savesList.size() - 1) {
                    writer.write(",");
                } // new line
                writer.write("\n");
            } // end the JSON array with a "]"
            writer.write("]");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing the list to the file: " + e.getMessage());
        }
        
        
    }

    private List<Saves> loadSaves() {
        List<Saves> savesList = new ArrayList<>();

        // File should exist considering we make it in the constructor if it doesnt already exists, but just in case..
        if (!file.exists()) return savesList;

        // try with the FileReader reader
        try (FileReader reader = new FileReader(file);){
            // First we take the info in the file and append it to a StringBuilder. (get the previous saves)
            StringBuilder JSON = new StringBuilder();
            int ch; // a character that we reconstruct to a char object

            // Reading each character in the file and appending it to the StringBuilder, and putting it in a contentstring      
            while ((ch = reader.read()) != -1) {JSON.append((char) ch);}
            String content = JSON.toString().trim();

            // If the file is empty or the array is empty in there, just return an empty list to the save and load methods.
            if (content.length() < 5) return savesList;

            // Removing the outer [ ]
            content = content.substring(1, content.length() - 1);
            // Splitting the different objects of the content string into different strings in a list (String[])
            String[] saves = content.split("(?<=\\})\\s*,\\s*\\{"); // wierd as fuck split to account for corrution, newline, etc..

            // Goes through every Saves object from the content
            for (String s : saves) {
                // Fixing the damage from the split above (adding {} to the object)
                if (!s.startsWith("{")) s = "{" + s;
                if (!s.endsWith("}")) s = s + "}";
                // parsing the string into a Saves object using the fromJSON method
                Saves save = Saves.fromJSON(s);
                savesList.add(save);
            }
        } catch (IOException e) {
            System.err.println("Error with reading the saves file.");
            e.printStackTrace();
        }
        return savesList;
    }

    public File getFile() {
        return file;
    }

    public static class Saves {
        private String name;
        private ArrayList<ArrayList<Entity>> grid;

        public Saves(String name, ArrayList<ArrayList<Entity>> grid){
            this.name = name;
            this.grid = grid;
        }

        public String toJSON() {
            StringBuilder sb = new StringBuilder();
            // Writing the name and the start of the grid of the Saves object using the StringBuilder object
            sb.append("{\"name\":\"").append(name).append("\",\"grid\":[");
        
            // Writing the rows of the grid using sb
            for (int i = 0; i < grid.size(); i++) {
                sb.append("[");
                for (int j = 0; j < grid.get(i).size(); j++) {
                    sb.append(grid.get(i).get(j).toString());  // Using the toString of the entity to make it readable
                    if (j < grid.get(i).size() - 1) sb.append(",");
                }
                sb.append("]");
                // If its not the final iteration of the for loop, we add a comma as a seperation betheen the values. ()
                if (i < grid.size() - 1) sb.append(",");
            }
            sb.append("]}");
            return sb.toString();
        }
        public static Saves fromJSON(String saveAsString) {
            // Getting the name from the JSON string
            String name = saveAsString.split("\"name\":\"")[1].split("\"")[0];

            // Getting the grid from the JSON string
            String gridPart = saveAsString.split("\"grid\":\\[")[1];
            gridPart = gridPart.substring(0, gridPart.lastIndexOf("]"));

            // Splitting the grid into rows
            String[] rows = gridPart.split("\\],\\[");

            // Making an empty grid 2D array that will hold the actual entities that the saved grid represents. 
            ArrayList<ArrayList<Entity>> grid = new ArrayList<>();

            int rowNumber = 0;
            for (String row : rows) {
                int colNumber = 0;
                // We are taking all the 0 and 1 values (dead or alive) in the row and simplifying it to just a big string of 0's and 1's representing the entities.
                row = row.replace("[", "").replace("]", "").replace("\"", "");
                String[] values = row.split(",");

                // Create a new row in the grid
                ArrayList<Entity> gridRow = new ArrayList<>();
                for (String v : values) {
                    // Create entities based on the value ("1" or "0")
                    Entity entity = new Entity(rowNumber, colNumber);
                    if ("1".equals(v)) {entity.live();} // Set the entity as alive. If not, is is dead.
                    gridRow.add(entity);
                    colNumber++;
                }
                // Add the row to the grid
                grid.add(gridRow);
                rowNumber++;
            }
            return new Saves(name, grid);
        }

        public String getName() {
            return name;
        }
        public ArrayList<ArrayList<Entity>> getGrid() {
            return grid;
        }
    }
}
