package domain;

import java.awt.Color;

/**
 * Pine
 * Representa un pino en el bosque, un tipo de árbol que mantiene
 * su color verde oscuro constante sin importar el paso del tiempo.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Pine extends Tree {
	private static final long serialVersionUID = 1L;

    /**
     * Crea un nuevo pino en una posición específica del bosque.
     * * @param forest el bosque donde habitará el pino
     * @param row la fila inicial
     * @param column la columna inicial
     */
    public Pine(Forest forest, int row, int column) {
        super(forest, row, column);
        this.color = new Color(0, 100, 0);
    }

    @Override
    public void ticTac() {
        super.ticTac();
        this.color = new Color(0, 100, 0);
    }
}