package domain;

import java.awt.Color;
import java.util.Random;

/**
 * Squirrel
 * Representa una ardilla en el bosque que envejece, cambia de color, se mueve
 * aleatoriamente, se reproduce únicamente si hay exactamente una celda vacía 
 * en medio de ella y otra ardilla (en línea recta o diagonal), y muere tras 
 * 10 años o al quedarse sin energía.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Squirrel extends LivingThing implements Thing {
	private static final long serialVersionUID = 1L;
    private Forest forest;
    private int row;
    private int column;
    private Color color;
    private int tictac;

    /**
     * Crea una nueva ardilla en una posición específica del bosque.
     *
     * @param forest el bosque donde habitará la ardilla
     * @param row la fila inicial
     * @param column la columna inicial
     */
    public Squirrel(Forest forest, int row, int column) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.color = new Color(139, 69, 19);
        this.tictac = 0;
        this.forest.setThing(row, column, this);
    }

    /**
     * Define la forma visual de la ardilla en la interfaz gráfica.
     * * @return una constante entera que representa la forma redonda (Thing.ROUND)
     */
    @Override
    public int shape() {
        return Thing.ROUND;
    }

    /**
     * Obtiene el color actual de la ardilla.
     * * @return un objeto Color que representa el tono de la ardilla
     */
    @Override
    public Color getColor() {
        return color;
    }

    /**
     * Ejecuta las acciones de la ardilla en un ciclo de tiempo: 
     * envejece, cambia de color, consume energía, se mueve, se reproduce o muere.
     */
    @Override
    public void ticTac() {
        tictac++;
        
        if (tictac % 4 == 1) {
            years += 1;
            int r = Math.min(255, color.getRed() + 10);
            int g = Math.min(255, color.getGreen() + 15);
            color = new Color(r, g, 0);
        }
        
        if (tictac % 4 == 3) {
            if (!step()) {
                die();
                return;
            }
        }

        if (years >= 10) {
            die();
            return;
        }

        moveRandomly();
        reproduce();
    }

    /**
     * Mueve la ardilla aleatoriamente a una de las celdas adyacentes vacías
     * disponibles en el bosque.
     */
    private void moveRandomly() {
        Random rand = new Random();
        int dr = rand.nextInt(3) - 1; 
        int dc = rand.nextInt(3) - 1; 
        
        int newRow = row + dr;
        int newCol = column + dc;

        if (newRow >= 0 && newRow < forest.getSize() && newCol >= 0 && newCol < forest.getSize()) {
            if (forest.isEmpty(newRow, newCol)) {
                forest.setThing(row, column, null);
                row = newRow;
                column = newCol;
                forest.setThing(row, column, this);
            }
        }
    }

    /**
     * Gestiona la reproducción de la ardilla. Si encuentra otra ardilla a una 
     * distancia de dos celdas y la celda intermedia está vacía, genera una nueva cría.
     */
    private void reproduce() {
        int[][] directions = {
            {-2, 0}, {2, 0}, {0, -2}, {0, 2}, 
            {-2, -2}, {-2, 2}, {2, -2}, {2, 2}
        };

        for (int[] dir : directions) {
            int dr = dir[0];
            int dc = dir[1];
            
            int mateRow = row + dr;
            int mateCol = column + dc;

            if (mateRow >= 0 && mateRow < forest.getSize() && mateCol >= 0 && mateCol < forest.getSize()) {
                if (forest.getThing(mateRow, mateCol) instanceof Squirrel) {
                    
                    int midRow = row + (dr / 2);
                    int midCol = column + (dc / 2);
                    
                    if (forest.isEmpty(midRow, midCol)) {
                        new Squirrel(forest, midRow, midCol);
                        return; 
                    }
                }
            }
        }
    }

    /**
     * Elimina a la ardilla del bosque, liberando su celda en la matriz para que 
     * otros objetos puedan ocuparla.
     */
    private void die() {
        if (forest.getThing(row, column) == this) {
            forest.setThing(row, column, null);
        }
    }
}