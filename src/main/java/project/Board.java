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
    private Timeline gameLoop; // Timeline objekt håndterer at vi kontinuerlig utfører iterasjoner, så lenge status er RUNNING (stop() / play())
    private GOLController controller; // Relasjon fra kontroller for at vår gameLoop også kaller tegnefunksjonen til kontrolleren
    private Filehandler files;

    // initialize the board object
    public Board(GOLController controller) { 
        System.out.println("Initializing board..!");
        this.controller = controller;
        this.iterations = 0;
        this.living = 0;
        this.grid = new ArrayList<>(); // initializing the grid
        this.files = new Filehandler(this);

        // Adding entities to every position of the grid
        for (int i = 0; i < 40; i++) {
            ArrayList<Entity> col = new ArrayList<>();
            for (int j = 0; j < 40; j++) {
                col.add(new Entity(i, j));
            }
            this.grid.add(col);
        }

        // Making a game loop witht the timeline class. Every this.stun = 150ms nextIteration and then drawBoard will happen, as long as gameLoop.status is RUNNING
        gameLoop = new Timeline(new KeyFrame(Duration.millis(stun), e -> {nextIteration(); controller.drawBoard();}));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        System.out.println(gameLoop);
    }

    @Override
    public ArrayList<ArrayList<Entity>> nextIteration() {
        // Vi lager en "newGrid" placeholder variabel for grid, slik at vi ikke tuller med logikken til spillet mens vi kjører igjennom én iterasjon
        ArrayList<ArrayList<Entity>> newGrid = new ArrayList<>();

        for (ArrayList<Entity> arrayList : grid) {
            ArrayList<Entity> newCol = new ArrayList<>();
            for (Entity entity : arrayList) {
                Entity newEntity = new Entity(entity.getRow(), entity.getCol());
                if (survival(entity)) {
                    if (!entity.isAlive()) {this.living++;} // må øke living med 1 om denne ikke allerede var levende
                    newEntity.live();
                    newCol.add(newEntity);
                }
                else {
                    if (entity.isAlive()) {this.living--;}  // må senke living med 1 om denne var levende
                    newEntity.die();
                    newCol.add(newEntity);}
            }
            newGrid.add(newCol);
        }
        this.iterations++;
        this.grid = newGrid;
        return newGrid;
    }

    @Override
    public boolean survival(Entity entity) {
        int livingNeighbors = 0;
        // Finding the neighbors
        for (ArrayList<Entity> arrayList : grid) {
            for (Entity otherEntity : arrayList) {
                if (Math.abs(otherEntity.getCol()-entity.getCol()) <= 1 && Math.abs(otherEntity.getRow()-entity.getRow()) <= 1 && !(otherEntity.getCol() == entity.getCol() && otherEntity.getRow() == entity.getRow())) {
                    if (otherEntity.isAlive()) {livingNeighbors++;} // adding directly to the amount of living neighbors
                }
            }
        }
        if (!entity.isAlive() && livingNeighbors == 3) { // If entity is dead and has excactly three living neighbors it should live next iteration.
            return true;
        }
        else if (entity.isAlive() && (livingNeighbors == 2 || livingNeighbors == 3)) { // If entity is alive and has at between two and three living neighbors should live next iteration.
            return true;
        }
        return false;
    }

    @Override
    public void uploadBoard(ArrayList<ArrayList<Entity>> upload) {
        // delegated to this one
        this.grid = upload;
        System.out.println(this.getGrid()); 
        controller.drawBoard();
    } 

    @Override
    public void editEntity(int col, int row) {
        grid.get(row).get(col).change();
        if (grid.get(row).get(col).isAlive()) {this.living++;}
        else {this.living--;}
    }

    @Override
    public void clear() {
        for (ArrayList<Entity> arrayList : grid) {
            for (Entity entity : arrayList) {
                entity.die();
            }
        }
        this.living = 0;
        this.iterations = 0;
        stopGameLoop();
    }
    @Override
    public void startGameLoop() {
        if (gameLoop.getStatus() != Animation.Status.RUNNING) {
            gameLoop.play();
        }
    }
    @Override
    public void stopGameLoop() {
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
