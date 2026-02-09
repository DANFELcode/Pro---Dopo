import java.util.ArrayList;
import java.util.Random;

public class Snake {    

    private ArrayList<int[]> body;
    private Circle headVisual;
    private ArrayList<Rectangle> bodyParts;
    private Picture fondo;
    private String color;
    private boolean isVisible;
    private boolean isOK;
    private int length;
    
    private Rectangle cuadrado1;
    private Rectangle cuadrado2;
    private int[] posCuadrado1 = new int[2];
    private int[] posCuadrado2 = new int[2];

    public Snake(int row, int column) {
        this.fondo = new Picture("snake(1).png", 0, 0); 
        
        this.body = new ArrayList<>();
        this.bodyParts = new ArrayList<>();
        this.color = "green";
        this.length = 1;
        this.isVisible = false;
        this.isOK = true;
        
        this.body.add(new int[]{row, column - 50});
        
        this.headVisual = new Circle();
        this.headVisual.changeSize(40);
        this.headVisual.changeColor("green");
        this.headVisual.moveHorizontal(column);
        this.headVisual.moveVertical(row);
        
        Random rand = new Random();
        
        int col1 = (rand.nextInt(15) * 46);
        int fila1 = (rand.nextInt(15) * 46);
        this.posCuadrado1 = new int[]{fila1, col1 - 50};
        this.cuadrado1 = new Rectangle();
        this.cuadrado1.changeColor("red");
        this.cuadrado1.changeSize(38, 38);
        this.cuadrado1.moveHorizontal(col1 - 50);
        this.cuadrado1.moveVertical(fila1);
        
        int col2 = (rand.nextInt(15) * 46);
        int fila2 = (rand.nextInt(15) * 46);
        this.posCuadrado2 = new int[]{fila2, col2 - 50};
        this.cuadrado2 = new Rectangle();
        this.cuadrado2.changeColor("yellow");
        this.cuadrado2.changeSize(38, 38);
        this.cuadrado2.moveHorizontal(col2 - 50);
        this.cuadrado2.moveVertical(fila2);
    }

    public void move(String direction) {
        int[] cabeza = body.get(0);
        int fAct = cabeza[0];
        int cAct = cabeza[1];
        int dx = 0, dy = 0;
        if (direction.equals("N")) dy = -46;
        else if (direction.equals("S")) dy = 46;
        else if (direction.equals("E")) dx = 46;
        else if (direction.equals("W")) dx = -46;

        int nF = fAct + dy;
        int nC = cAct + dx;
        // Validar giro de 180 grados
        if (body.size() > 1) {
            int[] cuello = body.get(1);
            if (Math.abs(nF - cuello[0]) < 46 && Math.abs(nC - cuello[1]) < 46) {
                this.isOK = false;
                return; 
            }
        }

        // Validar límites del tablero
        if (nF < 0 || nC < -50 || nF >= 703 || nC >= 749) {
            this.isOK = false;
            return;
        }

        // Validar colisión con obstáculo amarillo (cuadrado2)
        if (Math.abs(nF - posCuadrado2[0]) < 46 && Math.abs(nC - posCuadrado2[1]) < 46) {
            this.isOK = false;
            return;
        }

        for (int i = 1; i < body.size(); i++) {
            int[] segmento = body.get(i);
            // Verificar si la nueva posición colisiona con este segmento
            if (Math.abs(nF - segmento[0]) < 46 && Math.abs(nC - segmento[1]) < 46) {
                this.isOK = false;
                return;
            }
        }
        // Movimiento válido
        this.isOK = true;
        // Agregar nueva cabeza al inicio
        body.add(0, new int[]{nF, nC});
        body.remove(body.size() - 1);

        if (isVisible) {
            headVisual.slowMoveHorizontal(dx);
            headVisual.slowMoveVertical(dy);

            if (length > 1) {
                Rectangle nuevoSegmento = new Rectangle();
                nuevoSegmento.changeColor(color);
                nuevoSegmento.changeSize(38, 38);
                nuevoSegmento.moveHorizontal(cAct); 
                nuevoSegmento.moveVertical(fAct);
                bodyParts.add(0, nuevoSegmento);
                nuevoSegmento.makeVisible();

                if (bodyParts.size() > (length - 1)) {
                    bodyParts.remove(bodyParts.size() - 1).makeInvisible();
                }
            }
        }
    }
    
    public int haComido() {
        int[] cabeza = head();
        int fCabeza = cabeza[0];
        int cCabeza = cabeza[1];
        
        if (Math.abs(fCabeza - posCuadrado1[0]) < 46 && 
            Math.abs(cCabeza - posCuadrado1[1]) < 46) {
            return 1;
        }
        
        return 0;
    }
    
    public void reposicionarComida(int numeroCuadrado) {
        Random rand = new Random();
        
        if (numeroCuadrado == 1) {
            cuadrado1.makeInvisible();
            
            int col = (rand.nextInt(15) * 46);
            int fila = (rand.nextInt(15) * 46);
            this.posCuadrado1 = new int[]{fila, col - 50};
            
            this.cuadrado1 = new Rectangle();
            this.cuadrado1.changeColor("red");
            this.cuadrado1.changeSize(38, 38);
            this.cuadrado1.moveHorizontal(col - 50);
            this.cuadrado1.moveVertical(fila);
            
            if (isVisible) {
                this.cuadrado1.makeVisible();
            }
        }
    }
    
    public int[] head() { 
        return body.get(0); 
    }
    
    public int[] tail() { 
        return body.get(body.size() - 1); 
    }
    
    /**
     * La serpiente crece manteniendo la cola en su lugar
     */
    public void grow(String dir) {
        int[] colaAnterior = new int[]{tail()[0], tail()[1]};
        length++;
        
        int[] cabeza = body.get(0);
        int fAct = cabeza[0];
        int cAct = cabeza[1];

        int dx = 0, dy = 0;
        if (dir.equals("N")) dy = -46;
        else if (dir.equals("S")) dy = 46;
        else if (dir.equals("E")) dx = 46;
        else if (dir.equals("W")) dx = -46;

        int nF = fAct + dy;
        int nC = cAct + dx;

        // Validar giro de 180 grados
        if (body.size() > 1) {
            int[] cuello = body.get(1);
            if (Math.abs(nF - cuello[0]) < 46 && Math.abs(nC - cuello[1]) < 46) {
                this.isOK = false;
                length--;
                return; 
            }
        }

        // Validar límites
        if (nF < 0 || nC < -50 || nF >= 703 || nC >= 749) {
            this.isOK = false;
            length--;
            return;
        }

        // Validar colisión con obstáculo amarillo
        if (Math.abs(nF - posCuadrado2[0]) < 46 && Math.abs(nC - posCuadrado2[1]) < 46) {
            this.isOK = false;
            length--;
            return;
        }

        // Validar auto-colisión
        for (int i = 1; i < body.size(); i++) {
            int[] segmento = body.get(i);
            if (Math.abs(nF - segmento[0]) < 46 && Math.abs(nC - segmento[1]) < 46) {
                this.isOK = false;
                length--;
                return;
            }
        }

        this.isOK = true;
        body.add(0, new int[]{nF, nC});
        
        if (isVisible) {
            headVisual.slowMoveHorizontal(dx);
            headVisual.slowMoveVertical(dy);

            // Crear nuevo segmento visual donde estaba la cabeza
            Rectangle nuevoSegmento = new Rectangle();
            nuevoSegmento.changeColor(color);
            nuevoSegmento.changeSize(38, 38);
            nuevoSegmento.moveHorizontal(cAct); 
            nuevoSegmento.moveVertical(fAct);
            bodyParts.add(0, nuevoSegmento);
            nuevoSegmento.makeVisible();
        }
    }
    
    public boolean isOK() { 
        return isOK; 
    }
    
    public int length() { 
        return this.length; 
    }
    
    public void makeVisible() {
        this.isVisible = true;
        if (fondo != null) fondo.makeVisible();
        if (headVisual != null) headVisual.makeVisible();
        if (cuadrado1 != null) cuadrado1.makeVisible();
        if (cuadrado2 != null) cuadrado2.makeVisible();
        for (Rectangle segment : bodyParts) segment.makeVisible();
    }
    
    public void makeInvisible() {
        this.isVisible = false;
        if (fondo != null) fondo.makeInvisible();
        if (headVisual != null) headVisual.makeInvisible();
        if (cuadrado1 != null) cuadrado1.makeInvisible();
        if (cuadrado2 != null) cuadrado2.makeInvisible();
        for (Rectangle segment : bodyParts) {
            segment.makeInvisible();
        }
    }
}