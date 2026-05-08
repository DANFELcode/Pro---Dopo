package domain;
import java.awt.Color;
import java.io.Serializable;

/**
 * Thing
 * Representa la interfaz base para todos los elementos que pueden existir 
 * dentro de la cuadrícula del bosque.
 */
public interface Thing extends Serializable{	
	
    public static final int SQUARE = 2;
    public static final int ROUND = 1;
    
    /**
     * Define la acción o comportamiento que el objeto ejecutará en cada 
     * ciclo de tiempo (pulso) de la simulación.
     */
    public void ticTac();
  
    /**
     * Obtiene la forma geométrica del objeto para su representación visual.
     * * @return un entero que representa la forma (por defecto SQUARE)
     */
    public default int shape() {
        return SQUARE;
    }
  
    /**
     * Obtiene el color del objeto para su representación visual.
     * * @return el objeto Color correspondiente (por defecto Color.black)
     */
    public default Color getColor() {
        return Color.black;
    }
  
    /**
     * Indica si el objeto es exclusivamente una cosa inanimada (no viva).
     * * @return true por defecto, indicando que es un objeto inanimado
     */
    public default boolean isOnlyThing() {
        return true;
    }
  
    /**
     * Indica si el objeto es una entidad viva (LivingThing).
     * * @return false por defecto, asumiendo que las cosas básicas no están vivas
     */
    public default boolean isLivingThing() {
        return false;
    }       
}