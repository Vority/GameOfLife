package project;

import java.util.ArrayList;

public interface BoardInterface {
    /**
     * Updates the board according to the rules of the Game of Life.
     *  
     * More spesifically it checks the (up to) eight neighbours of every entity on the board to determine if the status of the entity should change in the next iteration of the game. 
     *  
     * @return 
     * The new version of the board.
      */
    public ArrayList<ArrayList<Entity>> nextIteration();

    /**
     * gets the (up to) 8 neighbors of a spesific entity and adds them to a list. Then checks if at least 3 of these neighbors are alive. If so, return true. Else return false.
     * 
     * @param rol
     * The second index in the matrix
     * @param row
     * The first index in the matrix
     * @return
     * true or false
     */
    public boolean checkNeighbors(int col, int row);

    /**
     * Takes an uploaded board and changes the current board to this new one.
     * 
     * @param upload
     * The matrix that was sent from the file-handler class. A saved matrix from the storage file.
     * @return
     * The new version of the board
     */
    public void uploadBoard(ArrayList<ArrayList<Entity>> upload);

    /**
     * .gets the entity with the given coordinates and changes its status. Also updates the amount of living cells
     * 
     * @param rol
     * The second index in the matrix
     * @param row
     * The first index in the matrix
     */
    public void editEntity(int col, int row);

    /**
     * "Clears" the entire board by killing every living entity
     */
    public void clear();


}
