package domain;
 
public class ForestException extends Exception {
    public static final String ABRIR = "Error al abrir";
    public static final String GUARDARCOMO = "Error al guardar como";
    public static final String IMPORTAR = "Error al importar"; 
    public static final String EXPORTARCOMO = "Error al exportar como"; 

    public ForestException(String message) {
        super(message);
    }
}
 
