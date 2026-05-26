package lib;
import lib.AbstractCell;

public class SafeCell extends AbstractCell{
    private int adjacentBombs;
    public SafeCell(){
        super();
        this.adjacentBombs = 0 ;
    }
    @Override
    public boolean isBomb(){
        return false ;  // ce n'est pas une bombe
    }
    @Override
    public int getAdjacentBombs(){
        return this.adjacentBombs;
    }
    public void setAdjacentBombs(int count){
        this.adjacentBombs = count;
        
    }
} 