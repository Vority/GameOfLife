package project;

public class Entity {
    private boolean alive;
    private int row;
    private int col;
    
    // Every entity starts as dead.
    public Entity(int row, int col) {
        this.alive = false;
        this.row = row;
        this.col = col;
    }

    // Checking if the entity is alive or not
    public boolean isAlive(){
        return this.alive;
    }

    // Now three methods for changing states. They all have their utility in the Board object.
    // 1 -Changing the alive state to true
    public void live(){this.alive = true;}
    // 2 - Chancing the alive state to false
    public void die(){this.alive = false;}
    // 3 - Changing the alive state to what it isn't currenty
    public void change(){this.alive = !this.alive;}

    // Getters..
    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }

    // Quite important actually: makes it easier to read from and write to the JSON file (we save 0's and 1's and attribute it to an Entity object with a certain alive state)
    @Override
    public String toString() {
        if (alive) {
            return "1";
        }
        return "0";
         
    }
}
