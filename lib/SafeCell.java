package lib;
import lib.AbstractCell;

//Représente une case sûre (sans bombe) dans le jeu du Démineur
// cette classe stocke le nombre de bombes présentes dans les 8 cases adjacentes
public class SafeCell extends AbstractCell{
    private int adjacentBombs;

    // CONSTRUCTEUR PAR DÉFAUT 
    public SafeCell(){
        super();
        this.adjacentBombs = 0 ;    // sera mis à jour après le placement des bombes
    }
    
    //Indique que cette case ne contient pas de bombe
    @Override
    public boolean isBomb(){
        return false ;  // ce n'est pas une bombe
    }
    @Override
    public int getAdjacentBombs(){
        return this.adjacentBombs;
    }
    //Définit le nombre de bombes adjacentes à cette case
    public void setAdjacentBombs(int count){
        this.adjacentBombs = count;
        
    }
} 