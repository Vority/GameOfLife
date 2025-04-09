package project;

import java.util.ArrayList;
import java.lang.Math;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class Board implements BoardInterface {
    private ArrayList<ArrayList<Entity>> grid; // The board
    private int iterations; // The amount of iterations we have had
    private int living; // The amount of current living cells
    private int stun = 150; // Time delay between iterations in ms
    private Timeline gameLoop; // Timeline object -> continually executing stuff through iterations / cycles (as long as its running)
    private GOLController controller; // Middleman between the game logic (this) and the FXML. Relation is here because the drawBoard function must be called from here
    private Filehandler files; // Handling saves and uploads from a JSON file

    // initialize the board object
    public Board(GOLController controller) { 
        System.out.println("Initializing board..!");
        this.iterations = 0;
        this.living = 0;
        this.grid = new ArrayList<>(); 
        this.controller = controller;
        this.files = new Filehandler(this);

        // Adding entities to every position of the grid
        for (int i = 0; i < 40; i++) {
            ArrayList<Entity> col = new ArrayList<>();
            for (int j = 0; j < 40; j++) {
                col.add(new Entity(i, j));
            }
            this.grid.add(col);
        }

        // the timeline object should draw the board as well as call nextIteration every cycle (stun = 150ms)
        gameLoop = new Timeline(new KeyFrame(Duration.millis(stun), e -> {nextIteration(); controller.drawBoard();}));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void nextIteration() {
        // Making a newGrid placeholder grid that we build besides this.grid, so that we dont fuck up the logic as we go.
        ArrayList<ArrayList<Entity>> newGrid = new ArrayList<>();

        // Creating the grid of the nextIteration
        for (ArrayList<Entity> arrayList : grid) {
            ArrayList<Entity> newRow = new ArrayList<>();
            for (Entity entity : arrayList) {
                Entity newEntity = new Entity(entity.getRow(), entity.getCol()); // new entity with same row and col as the old one
                if (survival(entity)) {  // Using the survival method to choose its alive status for next iteration.
                    if (!entity.isAlive()) {this.living++;} // living should increase by one if a dead cell is now alive
                    newEntity.live();
                    newRow.add(newEntity);
                } else {
                    if (entity.isAlive()) {this.living--;}  // inverse of the thing above
                    newEntity.die();
                    newRow.add(newEntity);}
            }
            newGrid.add(newRow);
        }
        this.iterations++;
        this.grid = newGrid;
    }

    @Override
    public boolean survival(Entity entity) { // Most gamelogic heavy method of the bunch
        int livingNeighbors = 0;
        // Finding the neighbors: iterating through every entity
        for (ArrayList<Entity> arrayList : grid) {
            for (Entity otherEntity : arrayList) { // if otherEntity is a neighbour but not itself
                if (Math.abs(otherEntity.getCol()-entity.getCol()) <= 1 && Math.abs(otherEntity.getRow()-entity.getRow()) <= 1 && !(otherEntity.getCol() == entity.getCol() && otherEntity.getRow() == entity.getRow())) {
                    if (otherEntity.isAlive()) {livingNeighbors++;} // if the neighbour is alive, increase the count by one
                }
            }
        } // We follow the rules of the game given in the Javadoc under here. Two reasons for survival:
        if (!entity.isAlive() && livingNeighbors == 3) {return true;}
        else if (entity.isAlive() && (livingNeighbors == 2 || livingNeighbors == 3)) {return true;}
        return false; // false => dead, true => alive (next iteration)
    }

    @Override
    public void uploadBoard(ArrayList<ArrayList<Entity>> upload) {
        // delegated from Filehandler (to update the grid and draw it to the application)
        this.grid = upload;
        System.out.println(this.getGrid()); 
        controller.drawBoard();
    } 

    @Override
    public void editEntity(int col, int row) { // javadoc from interface
        grid.get(row).get(col).change(); // Changing the alive state to whatever it was not when clicked
        if (grid.get(row).get(col).isAlive()) {this.living++;} // Updating text
        else {this.living--;}
    }

    @Override
    public void clear() { // javadoc from interface
        for (ArrayList<Entity> arrayList : grid) {
            for (Entity entity : arrayList) {
                entity.die(); // We dont really want to CLEAR the grid. 
            }   // (clear is really a reset() function and might as well be called that)
        } // Everything is reset to zero, and the game is paused. 
        this.living = 0;
        this.iterations = 0;
        stopGameLoop(); 
    }
    @Override
    public void startGameLoop() { 
        // Error handling: only doing .play() if its not already running.
        if (gameLoop.getStatus() != Animation.Status.RUNNING) {
            gameLoop.play(); // play -> status is running and nextIteration will be called every 150ms.
        }
    }
    @Override
    public void stopGameLoop() {
        // Also error handling here. .stop() could throw an exception if the gameLoop is not currently running when stop() is called.
        try {
            gameLoop.stop();
        } catch (RuntimeException e) {
            System.err.println("No current gameloop");
        }
    }
    @Override
    public int getLiving() {
        return living;
    }
    @Override
    public int getIterations() {
        return iterations;
    }
    @Override
    public ArrayList<ArrayList<Entity>> getGrid() {
        return grid;
    }
    @Override
    public Filehandler getFiles() {
        return files;
    }

}
