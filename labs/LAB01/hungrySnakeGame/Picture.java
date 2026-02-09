import java.awt.*;
import javax.swing.*;

/**
 * Clase Picture para mostrar imágenes en el canvas.
 */
public class Picture {
    private int x;
    private int y;
    private Image image;
    private boolean isVisible;
    
    /**
     * Constructor de Picture.
     * @param filename Ruta del archivo de imagen
     * @param x Posición horizontal
     * @param y Posición vertical
     */
    public Picture(String filename, int x, int y) {
        this.image = new ImageIcon(filename).getImage();
        this.x = x;
        this.y = y;
        this.isVisible = false;
    }
    
    /**
     * Hace visible la imagen.
     */
    public void makeVisible() {
        this.isVisible = true;
        draw();
    }
    
    /**
     * Hace invisible la imagen.
     */
    public void makeInvisible() {
        this.isVisible = false;
        Canvas.getCanvas().erase(this);
    }
    
    /**
     * Dibuja la imagen en el canvas.
     */
    private void draw() {
        if (isVisible) {
            Canvas canvas = Canvas.getCanvas();
            canvas.drawImage(this, image, x, y);
        }
    }
}