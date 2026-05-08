package test;

import domain.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;


public class ForestTest {

    private Forest forest;

    @Before
    public void setUp() {
        forest = new Forest();
    }

    // --- PARTE II.B.2: Pruebas de Exportación ---

    @Test
    public void deberiaExportarArchivoConFormatoCorrecto() {
        File file = new File("forestTest.txt");
        try {
            for(int r=0; r<forest.getSize(); r++) {
                for(int c=0; c<forest.getSize(); c++) {
                    forest.setThing(r, c, null);
                }
            }
            
            new Tree(forest, 10, 10);
            forest.exportAs01(file);
            
            BufferedReader in = new BufferedReader(new FileReader(file));
            String linea = in.readLine();
            in.close();
            
            assertEquals("La línea exportada debe tener el formato Clase Fila, Columna", "Tree 10, 10", linea);
        } catch (Exception e) {
            fail("No debería fallar la exportación básica: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void deberiaExportarBosqueVacioSinErrores() {
        File file = new File("vacio.txt");
        try {
            for(int r=0; r<forest.getSize(); r++) {
                for(int c=0; c<forest.getSize(); c++) {
                    forest.setThing(r, c, null);
                }
            }
            
            forest.exportAs01(file);
            assertTrue("El archivo debe existir aunque esté vacío", file.exists());
            assertEquals("El archivo debe pesar 0 bytes si no hay objetos", 0, file.length());
        } catch (Exception e) {
            fail("No debería fallar al exportar un bosque sin elementos");
        } finally {
            if (file.exists()) file.delete();
        }
    }

    // --- PARTE II.B.4: Pruebas de Importación Básica ---

    @Test
    public void deberiaImportarMultiplesElementos() {
        File file = new File("importTest.txt");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Tree 0, 0");
            out.println("Squirrel 24, 24");
            out.close();
            
            forest._import01(file);
            assertTrue("Debería haber un Tree en 0,0", forest.getThing(0, 0) instanceof Tree);
            assertTrue("Debería haber una Squirrel en 24,24", forest.getThing(24, 24) instanceof Squirrel);
        } catch (Exception e) {
            fail("No debería fallar la importación de datos válidos: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    // --- PARTE III.B.3: Pruebas de Aceptación (Mensajes Detallados) ---

    @Test
    public void testImportLanzaExcepcionPorClaseDesconocida() {
        File file = new File("errorClase.txt");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Tree 1, 1");
            out.println("Pokemon 10, 10"); // Error en línea 2
            out.close();
            
            forest._import02(file);
            fail("Debió lanzar ForestException por clase no reconocida");
        } catch (ForestException e) {
            assertTrue("El mensaje debe indicar el número de línea", e.getMessage().contains("Línea 2"));
            assertTrue("El mensaje debe mencionar que la clase no es reconocida", 
                e.getMessage().contains("no es reconocida") || e.getMessage().contains("no existe"));
        } catch (Exception e) {
            fail("Se esperaba ForestException, pero se obtuvo: " + e.getClass().getName());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void testImportLanzaExcepcionPorCoordenadasNoEnteras() {
        File file = new File("errorFormato.txt");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Squirrel 5.5, 10"); // Error de formato en línea 1
            out.close();
            
            forest._import02(file);
            fail("Debió lanzar ForestException por coordenadas inválidas");
        } catch (ForestException e) {
            assertTrue("Debe indicar que el error es en la Línea 1", e.getMessage().contains("Línea 1"));
            assertTrue("Debe mencionar que las coordenadas deben ser enteros", e.getMessage().contains("enteros"));
        } catch (Exception e) {
            fail("Se obtuvo una excepción inesperada: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void testImportLanzaExcepcionPorCoordenadasFueraDeRango() {
        File file = new File("errorRango.txt");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Mushroom 30, 30"); // Fuera de matriz 25x25
            out.close();
            
            forest._import02(file);
            fail("Debió lanzar ForestException por rango inválido");
        } catch (ForestException e) {
            assertTrue("Debe mencionar que está fuera de rango o límites", 
                e.getMessage().contains("rango") || e.getMessage().contains("límites"));
        } catch (Exception e) {
            fail("Error inesperado en la prueba");
        } finally {
            if (file.exists()) file.delete();
        }
    }
    
 // --- PARTE IV.B: Pruebas de Flexibilidad con Reflexión ---

    @Test
    public void deberiaImportarCualquierClaseDelDominioDinamicamente() {
        File file = new File("flexTest.txt");
        try {
            for(int r=0; r<forest.getSize(); r++) {
                for(int c=0; c<forest.getSize(); c++) forest.setThing(r, c, null);
            }

            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Mushroom 5, 5");
            out.println("Pine 10, 10");
            out.close();
            
            forest._import(file);
            
            assertNotNull("Debería haber creado el Mushroom mediante reflexión", forest.getThing(5, 5));
            assertEquals("Mushroom", forest.getThing(5, 5).getClass().getSimpleName());
            assertNotNull("Debería haber creado el Pine mediante reflexión", forest.getThing(10, 10));
            assertEquals("Pine", forest.getThing(10, 10).getClass().getSimpleName());
            
        } catch (Exception e) {
            fail("La reflexión falló al instanciar clases dinámicamente: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void testImportLanzaExcepcionSiLaClaseNoExisteEnDominio() {
        File file = new File("claseInexistente.txt");
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            out.println("Avion 1, 1");
            out.close();
            
            forest._import(file);
            fail("Debió lanzar ForestException porque la clase Avion no existe");
        } catch (ForestException e) {
            assertTrue("El mensaje debe mencionar que la clase no existe en el dominio", 
                e.getMessage().contains("no existe") || e.getMessage().contains("dominio"));
        } catch (Exception e) {
            fail("Se esperaba ForestException (por reflexión), pero se obtuvo: " + e.getClass().getName());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    @Test
    public void deberiaExportarCualquierObjetoSinLógicaManual() {
        File file = new File("exportFlex.txt");
        try {
            for(int r=0; r<forest.getSize(); r++) {
                for(int c=0; c<forest.getSize(); c++) forest.setThing(r, c, null);
            }
            
            new Squirrel(forest, 2, 2);
            forest.exportAs(file);
            
            BufferedReader in = new BufferedReader(new FileReader(file));
            String linea = in.readLine();
            in.close();
            
            assertEquals("Debe exportar el nombre de la clase obtenido dinámicamente", "Squirrel 2, 2", linea);
        } catch (Exception e) {
            fail("Error en la exportación flexible: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }
}