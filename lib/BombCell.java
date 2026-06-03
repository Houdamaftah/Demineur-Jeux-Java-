package lib;
import lib.AbstractCell;
// Représente une case contenant une bombe dans le jeu du Démineur
public class BombCell extends AbstractCell {
    //Constructeur par défaut
    public BombCell(){
        super();
    }
    @Override 
    //Indique que cette case est une bombe
    public boolean isBomb(){
        return true;  // c'est une bombe
    }
    //Non applicable pour une bombe ; une case bombe ne compte pas de voisins
    @Override
    public int getAdjacentBombs(){
        return -1;      // non applicable pour une bombe 
    }

} 