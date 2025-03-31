package project;

import java.util.ArrayList;
import java.util.List;

public class Board implements BoardInterface {
    private ArrayList<ArrayList<Entity>> grid; // The board
    private int iterations; // The amount of iterations we have had
    private int living; // The amount of current living cells
    private int stun = 100;   // The sleep-time before iterations (in tics) 

    public Board() { //int rows, int cols
        //if (!(25 <= rows && rows <= 100) || !(25 <= cols && cols <= 100)) {
        //    throw new IllegalArgumentException("The rows and collums have to be ")
        //}
        this.iterations = 0;
        this.living = 0;
        for (int i = 0; i < 100; i++) {
            ArrayList<Entity> col = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                col.add(new Entity(i, j));
            }
            this.grid.add(col);
        }
    }

    @Override
    public ArrayList<ArrayList<Entity>> nextIteration() {
        for (ArrayList<Entity> arrayList : grid) {
            for (Entity entity : arrayList) {
                if (checkNeighbors(entity.getCol(), entity.getRow())) {
                    if (!entity.check()) {this.living++;} // må øke living med 1 om denne ikke allerede var levende
                    entity.live();}
                else {
                    if (entity.check()) {this.living--;}  // må senke living med 1 om denne var levende
                    entity.die();}
            }
        }
        this.iterations++;
        return grid;

        
    }

    @Override
    public boolean checkNeighbors(int col, int row) {
        // kan skjekke de åtte rundt vha. abs(col1-col2) <= 1 && abs(row1-row2) <= 1

        List<Entity> neighbors = new ArrayList<>();
        
        
        return false;
    }

    @Override
    public void uploadBoard(ArrayList<ArrayList<Entity>> upload) {
        // MÅ GJØRE EN REKKE FEILTESTER?? ELLER GJØRES DET I DEN ANDRE?
        this.grid = upload;
    } 

    @Override
    public void editEntity(int col, int row) {
        grid.get(row).get(col).change();
        if (grid.get(row).get(col).check()) {this.living++;}
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
    }

  
    
}
