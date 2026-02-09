import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.awt.event.KeyListener;

/**
 * Canvas is a class to allow for simple graphical drawing on a canvas.
 * Modificado para soportar imágenes y eventos de teclado en el proyecto HungrySnakeGame.
 */
public class Canvas {
    private static Canvas canvasSingleton;

    public static Canvas getCanvas() {
        if (canvasSingleton == null) {
            canvasSingleton = new Canvas("BlueJ Hungry Snake", 799, 703, Color.white);
        }
        canvasSingleton.setVisible(true);
        return canvasSingleton;
    }

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List<Object> objects;
    private HashMap<Object, ShapeDescription> shapes;

    private Canvas(String title, int width, int height, Color bgColour) {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList<Object>();
        shapes = new HashMap<Object, ShapeDescription>();
    }

    public void setVisible(boolean visible) {
        if (graphic == null) {
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Agrega un KeyListener al canvas para capturar eventos de teclado.
     */
    public void addKeyListener(KeyListener listener) {
        canvas.addKeyListener(listener);
    }
    
    /**
     * Permite que el canvas reciba el foco para eventos de teclado.
     */
    public void setFocusable(boolean focusable) {
        canvas.setFocusable(focusable);
    }
    
    /**
     * Solicita el foco para el canvas.
     */
    public void requestFocus() {
        canvas.requestFocus();
    }

    /**
     * Dibuja una imagen en el canvas y la registra para que no se borre.
     */
    public void drawImage(Object referenceObject, Image image, int x, int y) {
        objects.remove(referenceObject);
        objects.add(0, referenceObject); // La añadimos al inicio (índice 0) para que sea el FONDO
        shapes.put(referenceObject, new ShapeDescription(image, x, y));
        redraw();
    }

    public void draw(Object referenceObject, String color, Shape shape) {
        objects.remove(referenceObject);
        objects.add(referenceObject); // Las formas van al final para estar sobre el fondo
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    public void erase(Object referenceObject) {
        objects.remove(referenceObject);
        shapes.remove(referenceObject);
        redraw();
    }

    public void setForegroundColor(String colorString) {
        if (colorString.equals("red")) graphic.setColor(Color.red);
        else if (colorString.equals("black")) graphic.setColor(Color.black);
        else if (colorString.equals("blue")) graphic.setColor(Color.blue);
        else if (colorString.equals("yellow")) graphic.setColor(Color.yellow);
        else if (colorString.equals("green")) graphic.setColor(Color.green);
        else if (colorString.equals("magenta")) graphic.setColor(Color.magenta);
        else if (colorString.equals("white")) graphic.setColor(Color.white);
        else graphic.setColor(Color.black);
    }

    public void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception e) {}
    }

    private void redraw() {
        erase();
        for (Object obj : objects) {
            shapes.get(obj).draw(graphic);
        }
        canvas.repaint();
    }

    private void erase() {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new java.awt.Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    private class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /**
     * Esta clase ahora es capaz de manejar tanto Formas (Shape) como Imágenes (Image).
     */
    private class ShapeDescription {
        private Shape shape;
        private String colorString;
        private Image image;
        private int x, y;
        private boolean isImage;

        // Constructor para Formas
        public ShapeDescription(Shape shape, String color) {
            this.shape = shape;
            this.colorString = color;
            this.isImage = false;
        }

        // Constructor para Imágenes
        public ShapeDescription(Image image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.isImage = true;
        }

        public void draw(Graphics2D graphic) {
            if (isImage) {
                graphic.drawImage(image, x, y, null);
            } else {
                setForegroundColor(colorString);
                graphic.draw(shape);
                graphic.fill(shape);
            }
        }
    }
}