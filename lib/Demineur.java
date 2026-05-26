package lib;

import java.util.Random;
import exceptions.*;

public class Demineur {
    private final int rowNb , colNb , bombNb ;
    private int NbClick = 0;
    private final AbstractCell[][] grid ;
    private boolean bombsPlaced = false ;

    public Demineur (int rows , int cols , int bombs){
        this.rowNb = rows;
        this.colNb = cols;
        this.bombNb = bombs;
        this.grid = new AbstractCell[rows][cols];

        for (int r = 0; r < rowNb ; r++ ){
            for (int c = 0; c < colNb ; c++){
                grid[r][c] = new SafeCell();
            }
        }
    }


    public void revealCell ( int r , int c) throws GameLostException , GameWonException , InvalidMoveException {
        if ( r < 0 || r >= rowNb || c <0 || c >= colNb ) throw new InvalidMoveException ("Hors Limite !");
            // premier click ; On place les bombes aléatoirement 
            if (!bombsPlaced){
            placeBombs (r ,c);
            bombsPlaced = true;
        }

        AbstractCell cell = grid [r][c];
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

    public void placeBombs(int firstRow , int firstCol){
        Random rand = new Random();
        int placed = 0;
        while (placed < bombNb){
            int r = rand.nextInt(rowNb) ;
            int c = rand.nextInt(colNb);
            if (!( r == firstRow && c == firstCol) && !grid[r][c].isBomb()){
                grid[r][c] = new BombCell();
                placed ++;
            }
        }
        calculateAdjacentBombs();
    } 

private void calculateAdjacentBombs() {
    for (int r = 0; r < rowNb; r++) {
        for (int c = 0; c < colNb; c++) {
            if (!grid[r][c].isBomb()) {
                int count = 0;
                // Parcourt les 8 voisins
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
                ((SafeCell) grid[r][c]).setAdjacentBombs(count);
            }
        }
    }
}
     private boolean checkWin(){
        for (int r=0 ; r < rowNb ; r++){
            for (int c = 0 ; c < colNb ; c++){
                if (! grid[r][c].isBomb() && ! grid[r][c].isRevealed()) return false;;
            }
        }
        return true;
     }

    public int getRowCount() { return rowNb; }
    public int getColCount() { return colNb; }
    public int getBombCount() { return bombNb; }
    public int getClickCount() { return NbClick; }
    public AbstractCell getCell(int r, int c) { return grid[r][c]; }

    public int[] attemptRandomReveal() {
        for (int r = 0; r < rowNb ; r++) {
            for (int c = 0; c < colNb; c++) {
                if (!grid[r][c].isRevealed() && !grid[r][c].isBomb()) return new int[]{r, c};
            }
        }
        return null;
    }
}