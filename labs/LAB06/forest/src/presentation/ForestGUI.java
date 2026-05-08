package presentation;
import domain.*;
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
 
public class ForestGUI extends JFrame{  
    public static final int SIDE=20;
 
    public final int SIZE;
    private JButton ticTacButton;
    private JPanel  controlPanel;
    private PhotoForest photo;
    private Forest theForest;
    
    private JMenuBar menuBar;
    private JMenu archivo;
    private JMenuItem nuevo, guardarComo, abrir, exportarComo, importar, salir;
   
    
    private ForestGUI() {
        theForest=new Forest();
        SIZE=theForest.getSize();
        prepareElements();
        prepareActions();
        prepareElementsMenu();
        prepareActionsMenu();
    }
    
    private void prepareElements() {
        setTitle("Forest");
        photo = new PhotoForest(this);
        ticTacButton = new JButton("Tic-tac");
        setLayout(new BorderLayout());
        add(photo, BorderLayout.CENTER); 
        add(ticTacButton, BorderLayout.SOUTH);

        setResizable(false);
        pack(); 
        setLocationRelativeTo(null);
    }
 
    private void prepareActions(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);       
        ticTacButton.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    ticTacButtonAction();
                }
            });
 
    }
    
    private void prepareElementsMenu() {
    	menuBar = new JMenuBar();
    	archivo = new JMenu("Archivo");
    	nuevo = new JMenuItem("Nuevo");
    	guardarComo = new JMenuItem("Guardar Como");
    	abrir = new JMenuItem("Abrir");
    	exportarComo = new JMenuItem("Exportar Como");
    	importar = new JMenuItem("Importar");
    	salir = new JMenuItem("Salir");
    	
    	archivo.add(nuevo);
    	archivo.addSeparator();
    	archivo.add(guardarComo);
    	archivo.add(abrir);
    	archivo.addSeparator();
    	archivo.add(exportarComo);
    	archivo.add(importar);
    	archivo.addSeparator();
    	archivo.add(salir);
    	
    	menuBar.add(archivo);
    	setJMenuBar(menuBar);
    }
    
    private void prepareActionsMenu() {
        nuevo.addActionListener(e -> optionNew());
        abrir.addActionListener(e -> optionOpen());
        guardarComo.addActionListener(e -> optionSaveAs());
        importar.addActionListener(e -> optionImport());
        exportarComo.addActionListener(e -> optionExportAs());
        salir.addActionListener(e -> optionExit());
    }
    
    private void optionExit() {
        System.exit(0);
    }

    private void optionNew() {
        theForest = new Forest();
        photo.repaint();
    }

    private void optionOpen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter("Archivos DAT (*.dat)", "dat"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                theForest.open(chooser.getSelectedFile());
                photo.repaint();                  
            } catch (ForestException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Abrir", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void optionSaveAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos DAT (*.dat)", "dat"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                theForest.saveAs(chooser.getSelectedFile());
            } catch (ForestException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Guardar Como", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void optionImport() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos TXT (*.txt)", "txt"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                theForest._import(chooser.getSelectedFile());
                photo.repaint();
            } catch (ForestException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Importar", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void optionExportAs() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos TXT (*.txt)", "txt"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                theForest.exportAs(chooser.getSelectedFile());
            } catch (ForestException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Exportar Como", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    private void ticTacButtonAction() {
        theForest.ticTac();
        photo.repaint();
    }
 
    public Forest gettheForest(){
        return theForest;
    }
    
    public static void main(String[] args) {
        ForestGUI cg=new ForestGUI();
        cg.setVisible(true);
    }  
}
 
class PhotoForest extends JPanel{
    private ForestGUI gui;
 
    public PhotoForest(ForestGUI gui) {
        this.gui=gui;
        setBackground(Color.white);
        setPreferredSize(new Dimension(gui.SIDE * gui.SIZE, gui.SIDE * gui.SIZE));         
    }
 
 
    public void paintComponent(Graphics g){
        Forest theForest=gui.gettheForest();
        super.paintComponent(g);
         
        for (int c=0;c<=theForest.getSize();c++){
            g.drawLine(c*gui.SIDE,0,c*gui.SIDE,theForest.getSize()*gui.SIDE);
        }
        for (int f=0;f<=theForest.getSize();f++){
            g.drawLine(0,f*gui.SIDE,theForest.getSize()*gui.SIDE,f*gui.SIDE);
        }       
        for (int f=0;f<theForest.getSize();f++){
            for(int c=0;c<theForest.getSize();c++){
                if (theForest.getThing(f,c)!=null){
                    g.setColor(theForest.getThing(f,c).getColor());
                    if (theForest.getThing(f,c).shape()==Thing.SQUARE){                  
                        g.fillRoundRect(gui.SIDE*c+1,gui.SIDE*f+1,gui.SIDE-2,gui.SIDE-2,2,2);   
                    }else {
                        g.fillOval(gui.SIDE*c+1,gui.SIDE*f+1,gui.SIDE-2,gui.SIDE-2);
                    }
                    if (theForest.getThing(f,c).isLivingThing()){
                        g.setColor(Color.red);
                        if (((LivingThing)theForest.getThing(f,c)).getEnergy()>=50){
                            g.drawString("+",gui.SIDE*c+6,gui.SIDE*f+15);
                        } else {
                            g.drawString("~",gui.SIDE*c+6,gui.SIDE*f+17);
                        }
                    }    
                }
            }
        }
    }
}