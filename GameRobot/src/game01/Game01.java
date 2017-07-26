/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game01;

import javax.swing.SwingUtilities;

/**
 *
 * @author ADMIN
 */
public class Game01 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Método usado para invocar aplicaciones que se actualizan constantemente con hilos
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Window(true);
            }
        });
        
    }
    
}
