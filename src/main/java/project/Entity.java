package project;

public class Entity {
    private boolean alive;
    private int row;
    private int col;
    
    public Entity(int row, int col) {
        this.alive = false;
        this.row = row;
        this.col = col;
    }

    public boolean check(){
        return this.alive;
    }

    public void live(){
        this.alive = true;
    }

    public void die(){
        this.alive = false;
    }

    public void change(){
        this.alive = !this.alive;
    }

    public int getCol() {
        return col;
    }
    public int getRow() {
        return row;
    }
}
