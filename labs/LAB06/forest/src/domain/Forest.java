package domain;

import java.io.*;
import java.lang.reflect.*;

/**
 * Forest
 * Representa el bosque o tablero principal de la simulación. 
 * Gestiona una matriz bidimensional espacial donde interactúan los diferentes 
 * elementos (Thing) y controla el paso del tiempo global mediante el método ticTac.
 *
 * @author Daniel Felipe Sua y Juan David Munar
 */
public class Forest implements Serializable{
    private static final long serialVersionUID = 1L;
    static private int SIZE = 25;
    private Thing[][] places;
    
    /**
     * Crea un nuevo bosque vacío de tamaño SIZE x SIZE y lo inicializa
     * con los elementos predeterminados de la simulación.
     */
    public Forest() {
        places = new Thing[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                places[r][c] = null;
            }
        }
        someThings();
    }

    /**
     * Obtiene el tamaño de un lado de la cuadrícula del bosque.
     * @return el número de filas o columnas (SIZE)
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Obtiene el elemento ubicado en una posición específica del bosque.
     * @param r la fila a consultar
     * @param c la columna a consultar
     * @return el objeto Thing en esa posición, o null si está vacía
     */
    public Thing getThing(int r, int c) {
        return places[r][c];
    }

    /**
     * Coloca un elemento en una posición específica del bosque.
     * @param r la fila destino
     * @param c la columna destino
     * @param e el objeto Thing a ubicar en la matriz
     */
    public void setThing(int r, int c, Thing e) {
        places[r][c] = e;
    }

    /**
     * Inicializa el bosque con los elementos base 
     * para las pruebas de aceptación
     */
    public void someThings() {   
        Tree beard = new Tree(this, 10, 10);
        Tree soul = new Tree(this, 15, 15);
        
        Squirrel scrat = new Squirrel(this, 5, 5);
        Squirrel sandy = new Squirrel(this, 5, 3); 
        
        Shadow thief = new Shadow(this, 20, 10);
        Shadow lass = new Shadow(this, 15, 20);
        
        Pine munar = new Pine(this, 5, 19);
        Pine sua = new Pine(this, 3, 1);
        
        for (int i = 0; i < 400; i++) {
            munar.ticTac();
        }
        
        Mushroom juan = new Mushroom(this, 18, 20);
        Mushroom daniel = new Mushroom(this, 2, 1);
        
        for (int i = 0; i < 7; i++) {
            daniel.ticTac();
        }
    }
    
    /**
     * Cuenta cuántos vecinos inmediatos (adyacentes o diagonales) son
     * exactamente de la misma clase que el elemento en la posición dada.
     * @param r la fila del elemento central
     * @param c la columna del elemento central
     * @return el número de vecinos idénticos
     */
    public int neighborsEquals(int r, int c) {
        int num = 0;
        if (inForest(r, c) && places[r][c] != null) {
            for (int dr = -1; dr < 2; dr++) {
                for (int dc = -1; dc < 2; dc++) {
                    if ((dr != 0 || dc != 0) && inForest(r + dr, c + dc) && 
                       (places[r + dr][c + dc] != null) &&  
                       (places[r][c].getClass() == places[r + dr][c + dc].getClass())) {
                        num++;
                    }
                }
            }
        }
        return num;
    }

    /**
     * Verifica si una celda específica está completamente vacía (null).
     * @param r la fila a verificar
     * @param c la columna a verificar
     * @return true si la celda está dentro del bosque y no contiene ningún objeto
     */
    public boolean isEmpty(int r, int c) {
        return (inForest(r, c) && places[r][c] == null);
    }    
        
    /**
     * Verifica si unas coordenadas dadas se encuentran dentro de los límites de la matriz.
     * @param r la fila a verificar
     * @param c la columna a verificar
     * @return true si las coordenadas son válidas dentro del tamaño del bosque
     */
    private boolean inForest(int r, int c) {
        return ((0 <= r) && (r < SIZE) && (0 <= c) && (c < SIZE));
    }
    
    /**
     * Avanza el tiempo global de la simulación un paso, delegando la acción
     * a cada uno de los elementos presentes en el tablero.
     */
    public void ticTac() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (places[r][c] != null) {
                    places[r][c].ticTac();
                }
            }
        }
    }

    public void open00(File file) throws ForestException {
        throw new ForestException(ForestException.ABRIR + " Archivo " + file.getName());
    }

    public void saveAs00(File file) throws ForestException {
        throw new ForestException(ForestException.GUARDARCOMO + " Archivo " + file.getName());
    }

    public void _import00(File file) throws ForestException{
        throw new ForestException(ForestException.IMPORTAR + " Archivo " + file.getName());
    }

    public void exportAs00(File file) throws ForestException{
        throw new ForestException(ForestException.EXPORTARCOMO + " Archivo " + file.getName());
    }
    
    /**
     * Guarda el estado completo del forest en un archivo binario (.dat)
     * usando serialización de objetos Java.
     * @param file archivo destino (extensión .dat)
     * @throws ForestException si ocurre cualquier error de E/S
     */
    public void saveAs01(File file) throws ForestException {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (IOException e) {
            throw new ForestException("Error al guardar el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga el estado de un forest desde un archivo binario (.dat),
     * reemplazando la cuadrícula actual.
     * @param file archivo origen (extensión .dat)
     * @throws ForestException si ocurre cualquier error de E/S o de formato
     */
    public void open01(File file) throws ForestException {
        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(file))) {
            Forest loaded = (Forest) ois.readObject();            
            this.places = loaded.places;
        } catch (IOException | ClassNotFoundException e) {
            throw new ForestException("Error al abrir el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Exporta el estado actual a un archivo de texto con un mensaje de error general.
     * @param file archivo destino
     * @throws ForestException si ocurre un error en la exportación
     */
    public void exportAs01 (File file) throws ForestException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (places[r][c] != null) {
                        String nombreClase = places[r][c].getClass().getSimpleName();
                        out.println(nombreClase + " " + r + ", " + c);
                    }
                }
            }
        } catch (IOException e) {
            throw new ForestException("Error al exportar: " + e.getMessage());
        }
    }
    
    /**
     * Importa elementos desde un archivo de texto con un mensaje de error general.
     * @param file archivo origen
     * @throws ForestException si ocurre un error en la importación
     */
    public void _import01 (File file) throws ForestException {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                places[r][c] = null;
            }
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] datos = line.split("[,\\s]+");
                
                if (datos.length >= 3) {
                    String tipo = datos[0];
                    int f = Integer.parseInt(datos[1]);
                    int col = Integer.parseInt(datos[2]);

                    if (tipo.equals("Tree")) new Tree(this, f, col);
                    else if (tipo.equals("Squirrel")) new Squirrel(this, f, col);
                    else if (tipo.equals("Shadow")) new Shadow(this, f, col);
                    else if (tipo.equals("Pine")) new Pine(this, f, col);
                    else if (tipo.equals("Mushroom")) new Mushroom(this, f, col);
                }
            }
        } catch (Exception e) {
            throw new ForestException("Error al importar: " + e.getMessage());
        }
    }
    
    
    /**
     * Guarda el estado del forest en un archivo .dat con manejo
     * detallado de excepciones.
     * @param file archivo destino
     * @throws ForestException si el archivo no existe, no hay permisos o hay error de E/S
     */
    public void saveAs(File file) throws ForestException {
        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            throw new ForestException(
                "No se puede crear el archivo '" + file.getName() +
                "'. Verifique que la ruta exista y tenga permisos de escritura.");
        } catch (SecurityException e) {
            throw new ForestException(
                "Permiso denegado al intentar guardar '" + file.getName() + "'.");
        } catch (IOException e) {
            throw new ForestException(
                "Error inesperado al guardar '" + file.getName() +
                "': " + e.getMessage());
        }
    }

    /**
     * Carga el estado del forest desde un archivo .dat con manejo
     * detallado de excepciones.
     * @param file archivo origen
     * @throws ForestException si el archivo no existe, está corrupto o es incompatible
     */
    public void open(File file) throws ForestException {
        try (ObjectInputStream ois =
                 new ObjectInputStream(new FileInputStream(file))) {
            Forest loaded = (Forest) ois.readObject();
            this.places = loaded.places;
        } catch (FileNotFoundException e) {
            throw new ForestException(
                "El archivo '" + file.getName() + "' no existe o no se encontró.");
        } catch (InvalidClassException e) {
            throw new ForestException(
                "El archivo '" + file.getName() +
                "' no es compatible con la versión actual del forest " +
                "(serialVersionUID no coincide).");
        } catch (StreamCorruptedException e) {
            throw new ForestException(
                "El archivo '" + file.getName() +
                "' está corrupto o no es un archivo .dat válido.");
        } catch (ClassNotFoundException e) {
            throw new ForestException(
                "El archivo '" + file.getName() +
                "' contiene clases que no existen en el proyecto.");
        } catch (IOException e) {
            throw new ForestException(
                "Error inesperado al abrir '" + file.getName() +
                "': " + e.getMessage());
        }
    }
    
    
    
    /**
     * Exporta el estado actual a un archivo de texto con manejo detallado de excepciones.
     * @param file archivo destino
     * @throws ForestException si hay problemas de escritura o permisos
     */
    public void exportAs02(File file) throws ForestException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (places[r][c] != null) {
                        String className = places[r][c].getClass().getSimpleName();
                        out.println(className + " " + r + ", " + c);
                    }
                }
            }
        } catch (IOException e) {
            throw new ForestException("Error al escribir en '" + file.getName() + "'. " +
                "Verifique correctamente el archivo.");
        } catch (Exception e) {
            throw new ForestException("Ocurrió un error inesperado al exportar el archivo: " + e.getMessage());
        }
    }
    
    /**
     * Importa elementos desde un archivo de texto con validación línea por línea.
     * @param file archivo origen
     * @throws ForestException si el formato es inválido, coordenadas fuera de rango o clase desconocida
     */
    public void _import02(File file) throws ForestException {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) places[r][c] = null;
        }

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            int lineNum = 0;
            while ((line = in.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,\\s]+");
                
                if (parts.length < 3) {
                    throw new ForestException("Línea " + lineNum + ": Formato incompleto. Se esperaba 'Tipo Fila, Columna'.");
                }

                try {
                    String type = parts[0];
                    int r;
                    int c;
                    try {
                        r = Integer.parseInt(parts[1]);
                        c = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        throw new ForestException("Línea " + lineNum + ": Las coordenadas deben ser enteros (se encontró '" + parts[1] + "' o '" + parts[2] + "').");
                    }
                    
                    if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                        throw new ForestException("Línea " + lineNum + ": Coordenadas fuera de rango [0-24].");
                    }

                    switch (type) {
                        case "Tree": new Tree(this, r, c); break;
                        case "Squirrel": new Squirrel(this, r, c); break;
                        case "Shadow": new Shadow(this, r, c); break;
                        case "Pine": new Pine(this, r, c); break;
                        case "Mushroom": new Mushroom(this, r, c); break;
                        default: 
                            throw new ForestException("Línea " + lineNum + ": La clase '" + type + "' no es reconocida.");
                    }
                } catch (ForestException e) {
                    throw e; 
                }
            }
        } catch (FileNotFoundException e) {
            throw new ForestException("No se encontró el archivo: " + file.getName());
        } catch (IOException e) {
            throw new ForestException("Error leyendo el archivo: " + e.getMessage());
        }
    }
    
   //BONO
   //A 
  
    /**
     * 
     */
    public void _import03() {
        
    }
    
    
    
   //B
    
    /**
     * Exporta dinámicamente el estado del bosque a un archivo de texto usando reflexión.
     * @param file archivo destino
     * @throws ForestException si ocurre un error de exportación
     */
    public void exportAs(File file) throws ForestException {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    Thing thing = places[r][c];
                    if (thing != null) {
                        String className = thing.getClass().getSimpleName();
                        out.println(className + " " + r + ", " + c);
                    }
                }
            }
        } catch (IOException e) {
            throw new ForestException("Error al exportar a '" + file.getName() + "': " + e.getMessage());
        }
    }
    
    /**
     * Importa elementos dinámicamente desde un archivo de texto usando reflexión.
     * @param file archivo origen
     * @throws ForestException si el formato es inválido, clase no existe en dominio o error de instanciación
     */
    public void _import(File file) throws ForestException {
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) places[r][c] = null;
            }

            String line;
            int lineNum = 0;
            while ((line = in.readLine()) != null) {
                lineNum++;
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("[,\\s]+");
                if (parts.length < 3) throw new ForestException("Línea " + lineNum + ": Formato incompleto.");

                try {
                    String type = parts[0];
                    int r = Integer.parseInt(parts[1]);
                    int c = Integer.parseInt(parts[2]);

                    if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
                        throw new ForestException("Línea " + lineNum + ": Coordenadas fuera de rango.");
                    }

                    Class<?> cls = Class.forName("domain." + type);                 
                    Constructor<?> constructor = cls.getConstructor(Forest.class, int.class, int.class);
                    constructor.newInstance(this, r, c);

                } catch (ClassNotFoundException e) {
                    throw new ForestException("Línea " + lineNum + ": La clase '" + parts[0] + "' no existe en el dominio.");
                } catch (NoSuchMethodException e) {
                    throw new ForestException("Línea " + lineNum + ": La clase '" + parts[0] + "' no tiene el constructor requerido (Forest, int, int).");
                } catch (Exception e) {
                    throw new ForestException("Línea " + lineNum + ": Error al crear el objeto: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new ForestException("Error de lectura: " + e.getMessage());
        }
    }
    
}
