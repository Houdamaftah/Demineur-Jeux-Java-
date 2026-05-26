package lib ;


public abstract class AbstractCell {
    private boolean revealed ;
    public AbstractCell (){
        this.revealed = false; // par défaut c'est faux
    }

    public boolean isRevealed(){
        return this.revealed;
    }

    public void setRevealed(boolean revealed){
        this.revealed = revealed ;
    }

    public abstract boolean isBomb();
    public abstract int getAdjacentBombs(); 

}