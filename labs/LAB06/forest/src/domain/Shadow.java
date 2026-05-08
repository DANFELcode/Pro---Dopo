package domain;

import java.awt.Color;

/**
 * Shadow
 * Representa una sombra en el bosque que se esparce por toda su fila inicial
 * y se desplaza de sur a norte de forma circular.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Shadow implements Thing {
	private static final long serialVersionUID = 1L;
    private Forest forest;
    private int row;
    private int column;

    /**
     * Crea una nueva sombra principal que se esparce por toda la fila.
     * * @param forest el bosque donde se proyectará la sombra
     * @param row la fila inicial
     * @param column la columna inicial
     */
    public Shadow(Forest forest, int row, int column) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        
        if (this.forest.isEmpty(row, column)) {
            this.forest.setThing(row, column, this);
        }
        
        for (int c = 0; c < this.forest.getSize(); c++) {
            if (c != column && this.forest.isEmpty(row, c)) {
                new Shadow(this.forest, row, c, true);
            }
        }
    }

    /**
     * Constructor privado para crear los fragmentos de sombra extendida
     * sin causar un ciclo infinito de creacion.
     * * @param forest el bosque
     * @param row la fila
     * @param column la columna
     * @param isClone indicador de clon
     */
    private Shadow(Forest forest, int row, int column, boolean isClone) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        
        if (this.forest.isEmpty(row, column)) {
            this.forest.setThing(row, column, this);
        }
    }

    @Override
    public int shape() {
        return Thing.ROUND;
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public void ticTac() {
        int nextRow = row - 1;

        if (nextRow < 0) {
            nextRow = forest.getSize() - 1;
        }

        if (forest.getThing(row, column) == this) {
            forest.setThing(row, column, null);
        }

        row = nextRow;

        if (forest.isEmpty(row, column)) {
            forest.setThing(row, column, this);
        }
    }
}