package lib ;

//Classe abstraite représentant une case du plateau de jeu du Démineur
//Chaque case possède un état "révélé" et doit implémenter les comportements
public abstract class AbstractCell {
    //Indique si la case a été révélée (cliquée) par le joueur
    private boolean revealed ;
    //Constructeur par défaut
    public AbstractCell (){
        this.revealed = false; // par défaut c'est faux, aucune case n'est visible au démarrage
    }
    //Indique si la case a été révélée
    public boolean isRevealed(){
        return this.revealed;
    }
    //Définit l'état de révélation de la case
    public void setRevealed(boolean revealed){
        this.revealed = revealed ;
    }
    //Indique si cette case contient une bombe
    public abstract boolean isBomb();
    public abstract int getAdjacentBombs(); 

}