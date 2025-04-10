package project;

import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class GOLController implements GridListener { // A little thick because of the need to draw the UI continuously. All functionality for the model resides in Board.java though. Who would have thought that a dynamic game would need some lines even in the controller.
    private Board board;
    
    @FXML private GridPane gridPane;
    @FXML private Text iterationsText;
    @FXML private Text livingCellsText;
    @FXML private Button clearButton;
    @FXML private Button playButton;
    @FXML private Button stopButton;
    @FXML private Button saveButton;
    @FXML private Button uploadButton;
    @FXML private AnchorPane savePane;
    @FXML private AnchorPane loadPane;
    @FXML private Button toMain;
    @FXML private Button toMain2;
    @FXML private Button saveTwo;
    @FXML private Button loadTwo;
    @FXML private TextField saveText;
    @FXML private TextField loadText;
    @FXML private Text errorText;
  
    public void initialize() throws IOException { // Basicly the constructor. Initializing the board object in order to bind the app to the board methods
        System.out.println("Initializing GOLController...");
        this.board = new Board();
        this.board.addGridListener(this); // Adding this as an observer of the Board object in line with the Observable-Observet technique
        System.out.println("Board initialized successfully.");
        System.out.println("FileHandling initialized successfully.");
        drawBoard(); // Draws an empty grid
        System.out.println("GOLController initialized successfully.");
    }

    // Methods that are called when the right button is pressed
    @FXML
    private void handlePlay() {
        if (board != null) { board.startGameLoop();}
    }
    @FXML
    private void handleStop() {
        board.stopGameLoop();
    }
    @FXML
    private void handleClear() {
        board.clear();
        drawBoard();
    }

    // The method from the GridListener interface. Avoids that the Controller and the model is intertwined.
    @Override 
    public void gridChanged() {
        drawBoard();
    }

    // The method for drawing the grid of the board as squares in a gridPane. Green for alive, black for dead.
    public void drawBoard() { // draws iteratively. Resembles nextIteration()
        gridPane.getChildren().clear(); // Clears the gridPane
        ArrayList<ArrayList<Entity>> grid = board.getGrid(); 

        for (int row = 0; row < grid.size(); row++) { // Making a rectangle for every entity in board's grid-matrix
            for (int col = 0; col < grid.get(row).size(); col++) { // Color is based on the corresponding entity 
                Entity entity = grid.get(row).get(col); 
                Rectangle cell = new Rectangle(15, 15, entity.isAlive() ? Color.GREEN : Color.BLACK);

                // If you click on a given rectangle object, editEntity will be called to that entity
                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(e -> { 
                    board.editEntity(finalCol, finalRow);
                    cell.setFill(entity.isAlive() ? Color.GREEN : Color.BLACK); // We refill the rectangle with the new color
                    livingCellsText.setText("Living Cells: " + board.getLiving());
                });
                // Adding the colored rectangles one by one
                gridPane.add(cell, col, row);
            }
        } // NB the info-text
        iterationsText.setText("Iterations: " + board.getIterations());
        livingCellsText.setText("Living Cells: " + board.getLiving());
    }

    // UI stuff to change pages (anchor panes stacking upon each other)
    @FXML
    private void toMain() {
        savePane.setDisable(true);
        savePane.setVisible(false);
        loadPane.setDisable(true);
        loadPane.setVisible(false);
    }
    @FXML
    private void toSavePane() {
        savePane.setDisable(false);
        savePane.setVisible(true);
        loadPane.setDisable(true);
        loadPane.setVisible(false);
        board.stopGameLoop();
    }
    @FXML
    private void toLoadPane() {
        savePane.setDisable(true);
        savePane.setVisible(false);
        loadPane.setDisable(false);
        loadPane.setVisible(true);
        board.stopGameLoop();
    }
    @FXML  // The text in the UI textfield has the argument for the Filehandler save method. 
    private void save() throws IOException{
        try { // The save function in the FIlehandler class throws exceptions if the state isnt right. We want to catch these errors and give the feedback with UI.
            board.getFiles().save(saveText.getText());
            toMain();
        } catch (IllegalArgumentException e) { // We check which exception we have (two possibilities) to give better feedback
            if (e.getMessage().equals("The name parameter cannot be null or empty.")) {
                showTemporaryError("Name cannot be null or empty.");
            }
            else {
                showTemporaryError("Name is already used");
            }
        }
    }
    @FXML // The text in the UI textfield has the argument for the Filehandler upload method. 
    private void load(){
        try { // The upload function in the FIlehandler class throws exceptions if the state isnt right. We want to catch this and give the feedback with UI. Also if foundSave returns false, nothing was wrong with the parameter but we didnt find a match.
            boolean foundSave = board.getFiles().upload(loadText.getText());
            if (!foundSave) {showTemporaryError("No save matching the name."); }
            else if (foundSave) {toMain();}
        } catch (IllegalArgumentException e) {
            showTemporaryError("Name cannot be null or empty.");
        }
    }

    // method to create a temporary error message (purely UI so it's okay?)
    private void showTemporaryError(String message) {
        errorText.setText(message);
        errorText.setVisible(true);
        // To make the errorText visible for 2s we create a new timeline with 1 cycle and 2s duration between cycles that sets it invisible.
        Timeline timeline = new Timeline(new KeyFrame(
            Duration.seconds(2),
            e -> errorText.setVisible(false)
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }
        
}
