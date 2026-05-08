package domain;
 
public class ForestException extends Exception {
	
	
    public static final String ABRIR    = "Opción Abrir en construcción.";
    public static final String GUARDARCOMO    = "Opción Guardar Como en construcción.";
    public static final String IMPORTAR    = "Opción Importar en construcción.";
    public static final String EXPORTARCOMO    = "Opción Exportar Como en construcción.";
 
    public ForestException(String message) {
        super(message);
    }
}
 