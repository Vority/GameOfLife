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
     * gets the (up to) 8 neighbors of a spesific entity and adds them to a list. Then checks if the amount of living neighbors coheres to the rules:
     * <ol>
     *    <li> Living cells with less than two living neighbors die. </li>
     *    <li> Living cells with more than three living neighbors die. </li>
     *    <li> Dead cells with excactly three living neighbors live. </li>
     * </ol>
     * @param entity
     * The entity in question
     * @return
     * true or false
     */
    public boolean survival(Entity entity);

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

    /**
     * If the Timeline object gameLoop isn't currently running : start running the loop.
     * <p> 
     * In the constructor of the Board object, its Timeline object is specified to call nextIteration every 100ms. 
     */
    public void startGameLoop();

    /**
     * Try to stop the gameloop. <p>
     * In the constructor of the Board object, its Timeline object is specified to call nextIteration every 100ms. 
     * @throws
     * throws and exception E if there is no running gameLoop. 
     */
    public void stopGameLoop();

    /**
     * Getter for the (int) living state of the Board object
     */
    public int getLiving();

    /**
     * Getter for the (int) iterations state of the Board object
     */
    public int getIterations();

    /**
     * Getter for the grid state of the Board object
     */
    public ArrayList<ArrayList<Entity>> getGrid();

    /**
     * Getter for the (Filehandler) files state of the Board object
     * @see {@code Filehandler.java}
     */
    public Filehandler getFiles();
}
