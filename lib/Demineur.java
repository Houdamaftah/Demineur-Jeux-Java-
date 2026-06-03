package lib;

import java.util.Random;
import exceptions.*;

public class Demineur {
    /** Nombre de lignes,  colonnes, bombes de la grille */
    private final int rowNb , colNb , bombNb ;
    /*Compteur du nbe de cases révélées par le joueur */
    private int NbClick = 0;
    /*la grille de jeu, composée de abstractCell soit safeCell ou bombCell*/
    private final AbstractCell[][] grid ;
    /*Indique si les bombes ont déja été placées */
    private boolean bombsPlaced = false ;

    /*Constructeur du Démineur*/
    /*Initialise la grille avec uniquement des cases sures */
    public Demineur (int rows , int cols , int bombs){
        this.rowNb = rows;      /*nbe de lignes de la grille */
        this.colNb = cols;      /*cols nbe de colonnes de la grille */
        this.bombNb = bombs;    /*bombs nbe de bombes à placer */
        this.grid = new AbstractCell[rows][cols];

        // remplit toute la grille avec des cases sures par defaut
        for (int r = 0; r < rowNb ; r++ ){
            for (int c = 0; c < colNb ; c++){
                grid[r][c] = new SafeCell();
            }
        }
    }

    // révèle la case à la position(r,c)
    //Si c'est le premier clic, les bombes sont d'abord placées aléatoirement 
    //(en évitant la case cliquée). Si la case révélée est une bombe, une
    //GameLostException est lancée. Si toutes les cases sures sont
    //révélées, une  GameWonException est lancée. Si la case révélée

    // r ligne : de la case à révéler.
    // c colonne : de la case à révéler.
    // GameLostException : si la case révélée contient une bombe.
    // si toutes les cases sûres sont maintenant révélées.
    // si la position est hors limites ou la case est déjà révélée.

    public void revealCell ( int r , int c) throws GameLostException , GameWonException , InvalidMoveException {
        if ( r < 0 || r >= rowNb || c <0 || c >= colNb ) throw new InvalidMoveException ("Hors Limite !");
            // premier click ; On place les bombes aléatoirement 
            if (!bombsPlaced){
            placeBombs (r ,c);
            bombsPlaced = true;
        }

        AbstractCell cell = grid [r][c];
        // Empêche de cliquer sur une case déjà ouverte
        if ( cell.isRevealed()) throw new InvalidMoveException("Case déja ouverte") ;

        cell.setRevealed(true);
        NbClick ++;
        // si la cell est une bombe , donc fin de partie
        if (cell.isBomb()) throw new GameLostException();

        // Propagation : si la case est vide, on ouvre les voisins recursivement
        if (cell.getAdjacentBombs() == 0){
            propagateReveal (r,c);
        }
        // Victoire 
        if (checkWin()) throw new GameWonException();
    }

// Ouvre recursivement les cases voisins quand on tombe sur une case 0
     // Propage récursivement la révélation aux cases voisines non révélées
     // Appelée lorsqu'une case sûre avec 0 bombe adjacente est révélée
     // Parcourt les 8 voisins (diagonales incluses) et appelle revealCell
     // sur chacun d'eux s'ils ne sont pas encore révélés

     // ligne de la case source.
     // colonne de la case source.
     // GameLostException    propagée depuis evealCell
     // GameWonException     propagée depuis revealCell
     // InvalidMoveException propagée depuis revealCell

    private void propagateReveal(int r , int c) throws GameLostException, GameWonException , InvalidMoveException{
        for (int dr = -1 ; dr <= 1; dr++){
            for (int dc = -1 ; dc <= 1 ; dc++){
                int nr = r + dr , nc = c +dc ;
                if (nr >= 0 && nr < rowNb && nc >=0 && nc <colNb && !grid[nr][nc].isRevealed()){
                    revealCell(nr , nc);
                }
            }
        }    
    } 



     // Place aléatoirement les bombes sur la grille.
     // La case du premier clic (firstRow, firstCol) est exclue pour garantir
     // que le joueur ne perde pas dès son premier coup. Après le placement,
     // calculateAdjacentBombs() est appelé pour mettre à jour les compteurs
     // firstRow ligne du premier clic (exclue du placement des bombes)
     // firstCol colonne du premier clic (exclue du placement des bombes)
 
    public void placeBombs(int firstRow , int firstCol){
        Random rand = new Random();
        int placed = 0;
        while (placed < bombNb){
            int r = rand.nextInt(rowNb) ;
            int c = rand.nextInt(colNb);
            // Évite la case du premier clic et les cases déjà minées
            if (!( r == firstRow && c == firstCol) && !grid[r][c].isBomb()){
                grid[r][c] = new BombCell();
                placed ++;
            }
        }
        //Calcule le nombre de bombes voisines pour chaque case sûre
        calculateAdjacentBombs();
    } 



     // Calcule et assigne à chaque {@link SafeCell} le nombre de bombes dans ses 8 voisins.
     // Doit être appelée après le placement de toutes les bombes
private void calculateAdjacentBombs() {
    for (int r = 0; r < rowNb; r++) {
        for (int c = 0; c < colNb; c++) {
            if (!grid[r][c].isBomb()) {
                int count = 0;
                // Parcourt les 8 voisins pour compter les bombes
                for (int dr = -1; dr <= 1; dr++) {
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = r + dr;
                        int nc = c + dc;
                        // Vérifie les limites et si la case est une bombe
                        if (nr >= 0 && nr < rowNb && nc >= 0 && nc < colNb && grid[nr][nc].isBomb()) {
                            count++;
                        }
                    }
                }
                // Affecte le résultat à la SafeCell
                ((SafeCell) grid[r][c]).setAdjacentBombs(count);
            }
        }
    }
}


     // Vérifie si le joueur a gagné la partie.
     // La victoire est atteinte lorsque toutes les cases non-bombes ont été révélées.
     // si toutes les cases sûres sont révélées, {@code false} sinon.
     private boolean checkWin(){
        for (int r=0 ; r < rowNb ; r++){
            for (int c = 0 ; c < colNb ; c++){
                if (! grid[r][c].isBomb() && ! grid[r][c].isRevealed()) return false;;
            }
        }
        return true;
     }

    //return le nombre de lignes de la grille
    public int getRowCount() { return rowNb; }
    //return le nombre de colonnes de la grille
    public int getColCount() { return colNb; }
    //return le nombre total de bombes sur la grille
    public int getBombCount() { return bombNb; }
    //return le nombre de cases révélées par le joueur depuis le début
    public int getClickCount() { return NbClick; }
    //return la case à la position donnée 
    // r ligne , c colonne
    public AbstractCell getCell(int r, int c) { return grid[r][c]; }

    //Tente de trouver une case sûre et non révélée pour un reveal automatique
    public int[] attemptRandomReveal() {
        for (int r = 0; r < rowNb ; r++) {
            for (int c = 0; c < colNb; c++) {
                if (!grid[r][c].isRevealed() && !grid[r][c].isBomb()) return new int[]{r, c};
            }
        }
        return null;
    }
}