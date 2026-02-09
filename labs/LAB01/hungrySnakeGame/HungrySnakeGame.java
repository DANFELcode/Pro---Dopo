import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class HungrySnakeGame {
    private Snake serpiente;
    private String direccionActual;
    private Timer gameLoop;
    private int velocidad;
    private boolean juegoEnCurso;
    private int comidasConsumidas;
    
    public HungrySnakeGame() {
        velocidad = 300;
        createInitialState();
        setupKeyboardControls();
        
        gameLoop = new Timer(velocidad, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serpiente.isOK() && juegoEnCurso) {
                    realizarMovimiento(direccionActual);
                } else if (!serpiente.isOK()) {
                    informarPerdida();
                }
            }
        });
    }
    
    private void createInitialState() {
        if (serpiente != null) {
            serpiente.makeInvisible();
        }
        
        serpiente = new Snake(46 * 5, 46 * 10);
        serpiente.makeVisible();
        direccionActual = "E";
        juegoEnCurso = false;
        comidasConsumidas = 0;
    }
    
    private void setupKeyboardControls() {
        Canvas canvas = Canvas.getCanvas();
        
        canvas.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                char tecla = Character.toLowerCase(e.getKeyChar());
                
                switch(tecla) {
                    case 'w': north(); break;
                    case 'a': west(); break;
                    case 's': south(); break;
                    case 'd': east(); break;
                    case ' ': 
                        if (!juegoEnCurso) {
                            iniciarMovimiento();
                        }
                        break;
                }
            }
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyTyped(KeyEvent e) {}
        });
        
        canvas.setFocusable(true);
        canvas.requestFocus();
    }
    
    public void north() {
        if (!juegoEnCurso) {
            JOptionPane.showMessageDialog(null, 
                "El juego no está activo.\nUsa iniciarMovimiento() primero.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (direccionActual.equals("S")) {
            return;
        }
        
        direccionActual = "N";
    }
    
    public void south() {
        if (!juegoEnCurso) {
            JOptionPane.showMessageDialog(null, 
                "El juego no está activo.\nUsa iniciarMovimiento() primero.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (direccionActual.equals("N")) {
            return;
        }
        
        direccionActual = "S";
    }
    
    public void east() {
        if (!juegoEnCurso) {
            JOptionPane.showMessageDialog(null, 
                "El juego no está activo.\nUsa iniciarMovimiento() primero.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (direccionActual.equals("W")) {
            return;
        }
        
        direccionActual = "E";
    }
    
    public void west() {
        if (!juegoEnCurso) {
            JOptionPane.showMessageDialog(null, 
                "El juego no está activo.\nUsa iniciarMovimiento() primero.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (direccionActual.equals("E")) {
            return;
        }
        
        direccionActual = "W";
    }
    
    public void iniciarMovimiento() {
        if (!serpiente.isOK()) {
            JOptionPane.showMessageDialog(null, 
                "La serpiente murió.\nUsa reiniciar() para empezar de nuevo.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        juegoEnCurso = true;
        gameLoop.start();
        Canvas.getCanvas().requestFocus();
    }
    
    public void detenerMovimiento() {
        juegoEnCurso = false;
        gameLoop.stop();
    }
    
    private void realizarMovimiento(String direccion) {
        int comida = serpiente.haComido();
        
        if (comida > 0) {
            serpiente.grow(direccion);
            comidasConsumidas++;
            serpiente.reposicionarComida(comida);
        } else {
            serpiente.move(direccion);
        }
        
        if (!serpiente.isOK()) {
            informarPerdida();
        }
    }
    
    public void reiniciar() {
        juegoEnCurso = false;
        gameLoop.stop();
        createInitialState();
        
        JOptionPane.showMessageDialog(null, 
            "Juego reiniciado.\n\nUsa iniciarMovimiento() para empezar.");
    }
    
    public void consultarEstado() {
        String estado = juegoEnCurso ? "ACTIVO" : "PAUSADO";
        String vida = serpiente.isOK() ? "VIVA" : "MUERTA";
        
        JOptionPane.showMessageDialog(null, 
            "Estado: " + estado + "\n" +
            "Serpiente: " + vida + "\n" +
            "Longitud: " + serpiente.length() + "\n" +
            "Comidas: " + comidasConsumidas + "\n" +
            "Dirección: " + getDireccion(direccionActual));
    }
    
    private void informarPerdida() {
        juegoEnCurso = false;
        gameLoop.stop();
        
        int longitud = serpiente.length();
        
        JOptionPane.showMessageDialog(null, 
            "Game Over\n\n" +
            "Longitud: " + longitud + "\n" +
            "Comidas: " + comidasConsumidas + "\n\n" +
            "Usa reiniciar() para volver a jugar.",
            "Fin del juego",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getDireccion(String dir) {
        switch(dir) {
            case "N": return "Arriba";
            case "S": return "Abajo";
            case "E": return "Derecha";
            case "W": return "Izquierda";
            default: return "?";
        }
    }
}