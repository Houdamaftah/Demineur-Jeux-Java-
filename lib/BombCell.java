package lib;
import lib.AbstractCell;

public class BombCell extends AbstractCell {
    public BombCell(){
        super();
    }
    @Override 
    public boolean isBomb(){
        return true;  // c'est une bombe
    }
    @Override
    public int getAdjacentBombs(){
        return -1;      // non applicable pour une bombe 
    }

} 