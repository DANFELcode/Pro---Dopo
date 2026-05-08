package domain;

import java.awt.Color;

/**
 * Mushroom
 * Representa un hongo en el bosque que es de color morado, tiene forma redonda
 * y se propaga lentamente creando clones en celdas adyacentes vacías.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Mushroom implements Thing {
	private static final long serialVersionUID = 1L;
    private Forest forest;
    private int row;
    private int column;
    private int tictac;

    /**
     * Crea un nuevo hongo en una posición específica del bosque.
     * * @param forest el bosque donde habitará el hongo
     * @param row la fila inicial
     * @param column la columna inicial
     */
    public Mushroom(Forest forest, int row, int column) {
        this.forest = forest;
        this.row = row;
        this.column = column;
        this.tictac = 0;
        this.forest.setThing(row, column, this);
    }

    /**
     * Define la forma visual del hongo en la interfaz gráfica.
     * * @return una constante entera que representa la forma redonda (Thing.ROUND)
     */
    @Override
    public int shape() {
        return Thing.ROUND;
    }

    /**
     * Obtiene el color del hongo.
     * * @return un objeto Color que representa el tono morado característico
     */
    @Override
    public Color getColor() {
        return new Color(128, 0, 128);
    }

    /**
     * Ejecuta las acciones del hongo en cada ciclo de tiempo.
     * Incrementa su contador interno y, cada 8 unidades de tiempo, intenta 
     * propagarse mediante la liberación de esporas.
     */
    @Override
    public void ticTac() {
        tictac++;

        if (tictac % 8 == 0) {
            spread();
        }
    }

    /**
     * Mecánica de clonación y expansión del hongo. Busca en sus celdas adyacentes
     * (horizontales, verticales y diagonales) un espacio vacío; si lo encuentra, 
     * genera un nuevo hongo en esa posición y termina el proceso.
     */
    private void spread() {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;

                int nr = row + dr;
                int nc = column + dc;

                if (nr >= 0 && nr < forest.getSize() && nc >= 0 && nc < forest.getSize()) {
                    if (forest.isEmpty(nr, nc)) {
                        new Mushroom(forest, nr, nc);
                        return;
                    }
                }
            }
        }
    }
}