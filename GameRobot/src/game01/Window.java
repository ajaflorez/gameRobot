package game01;

import java.awt.Dimension;
import javax.swing.JFrame;


public class Window extends JFrame {
    // Variable para espcificar si es pantalla completa
    private boolean fullScreen;
    // Ancho y alto del panel
    public static int windowWidth;
    public static int windowHeight;   
    
    public Window(boolean fullScreen) {
        this.fullScreen = fullScreen;
        // Colocar el Titulo de Ventana
        this.setTitle("My Window");
        
        if(this.fullScreen) {
            // Dehabilitar las decoraciones del frame
            this.setUndecorated(true);
                        
            // Permite Maximizar el Frame            
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        else {
            // Establecer el tamaño de la ventana
            this.setSize(800, 600);
            
            // Establecer la localización del frame -> Centrar
            this.setLocationRelativeTo(null);
            
            // Establecer que el Frame no cambia de Tamaño
            this.setResizable(false);
        }        
        // termina la áplicación cuando se cierra el Frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
        // Establece el panel
        this.setContentPane(new Panel());
        
        // Establce Visible la ventana
        this.setVisible(true);
        
        // capturando el ancho y alto del Frame
        Dimension dimen = this.getSize();
        windowWidth = dimen.width;
        windowHeight = dimen.height;        
        
    }
}
