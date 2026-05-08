package domain;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.File;
import domain.*;

public class ForestTest {

    private Forest forest;

    @Before
    public void setUp() {
        forest = new Forest();
    }


    // PUNTO II.A — Guardar y Abrir (excepciones generales)

    // -- saveAs (punto II.A.2)

    @Test
    public void testSaveAsCreaArchivo() throws Exception {
        File file = new File("testForest.dat");
        forest.saveAs(file);
        assertTrue("El archivo debe existir en disco", file.exists());
        file.delete();
    }

    @Test
    public void testSaveAsArchivoNoVacio() throws Exception {
        File file = new File("testForest.dat");
        forest.saveAs(file);
        assertTrue("El archivo no debe estar vacío", file.length() > 0);
        file.delete();
    }

    // -- open (punto II.A.4) 

    @Test
    public void testOpenRestauraEstado() throws Exception {
        File file = new File("testForest.dat");
        forest.saveAs(file);
        Forest otroForest = new Forest();
        otroForest.open(file);        
        assertNotNull("Debe haber un objeto en (10,10)", otroForest.getThing(10, 10));
        file.delete();
    }

    @Test
    public void testOpenCeldaVaciaSeMantiene() throws Exception {
        File file = new File("testForest.dat");
        forest.saveAs(file);
        Forest otroForest = new Forest();
        otroForest.open(file);        
        assertNull("La celda (0,0) debe estar vacía", otroForest.getThing(0, 0));
        file.delete();
    }


    // PUNTO III.A — Excepciones detalladas en open y saveAs


    // - saveAs detallado (punto III.A.2) 

    @Test
    public void testSaveAsRutaInvalidaLanzaExcepcion() {
        File file = new File("Z:/rutaQueNoExiste/forest.dat");
        try {
            forest.saveAs(file);
            fail("Debió lanzar ForestException");
        } catch (ForestException e) {
            assertTrue("Mensaje debe mencionar permisos o ruta",
                e.getMessage().contains("crear") || e.getMessage().contains("ruta"));
        }
    }

    // -- open detallado (punto III.A.2) 

    @Test
    public void testOpenArchivoInexistenteLanzaExcepcion() {
        File file = new File("noExiste.dat");
        try {
            forest.open(file);
            fail("Debió lanzar ForestException");
        } catch (ForestException e) {
            assertTrue("Mensaje debe mencionar que no existe",
                e.getMessage().contains("no existe"));
        }
    }

    @Test
    public void testOpenArchivoCorruptoLanzaExcepcion() throws Exception {
        File file = new File("corrupto.dat");
        java.io.PrintWriter pw = new java.io.PrintWriter(file);
        pw.println("esto no es un objeto serializado");
        pw.close();
        try {
            forest.open(file);
            fail("Debió lanzar ForestException");
        } catch (ForestException e) {
            assertTrue("Mensaje debe mencionar archivo corrupto",
                e.getMessage().contains("corrupto"));
        }
        file.delete();
    }
}