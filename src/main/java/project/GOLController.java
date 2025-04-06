package project;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GOLController { // A little thick because of the need to draw the UI continuously. All functionality for the model resides in Board.java
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
  
    public void initialize() throws IOException { // Basicly the constructor. Initializing the board object in order to bind the app to the board methods
        System.out.println("Initializing GOLController...");
        this.board = new Board(this);
        System.out.println("Board initialized successfully.");
        System.out.println("FileHandling initialized successfully.");
        drawBoard(); // Draws an empty grid
        System.out.println("GOLController initialized successfully.");
    }

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

    public void drawBoard() { // Method for drawing the board iteratively. Resembles the nextIteration method.
        gridPane.getChildren().clear(); // Clears the gridpane
        ArrayList<ArrayList<Entity>> grid = board.getGrid(); 

        for (int row = 0; row < grid.size(); row++) { // for every entity in board's grid-matrix, make a rectangle to -
            for (int col = 0; col < grid.get(row).size(); col++) { // be positioned at the same spot, with colors resembling the entity's state
                Entity entity = grid.get(row).get(col);
                Rectangle cell = new Rectangle(15, 15, entity.isAlive() ? Color.GREEN : Color.BLACK);

                int finalRow = row;
                int finalCol = col;
                cell.setOnMouseClicked(e -> { // if you click on a given rectangle object, editEntity will be called (change the entity's state)
                    board.editEntity(finalCol, finalRow);
                    cell.setFill(entity.isAlive() ? Color.GREEN : Color.BLACK); // then we just refill the rectangle
                    livingCellsText.setText("Living Cells: " + board.getLiving());
                });

                gridPane.add(cell, col, row);
            }
        }
        iterationsText.setText("Iterations: " + board.getIterations());
        livingCellsText.setText("Living Cells: " + board.getLiving());
    }


    // Filehandling part
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
    @FXML
    private void save() throws StreamWriteException, DatabindException, IOException{
        board.getFiles().save(saveText.getText());
        toMain();
    }
    @FXML
    private void load(){
        board.getFiles().upload(loadText.getText());
        toMain();
    }


        
}
