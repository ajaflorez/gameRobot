package game01;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Game {
    private Random random;
    private ArrayList<Robot> robots;
    private ArrayList<Robot> robotSelects;
    private int robotSelect;
    private Rectangle2D rectangle;
    private boolean drawRectangle;
    
    // Imágenes de los robots
    private BufferedImage imgRobot1;
    private BufferedImage imgRobot2;
    private BufferedImage imgRobot3;
    
    // Imagen para el cursor del mouse
    private BufferedImage imgCursor;
    private int middleWidthCursor;
    private int middleHeightCursor;
    
    
    public Game() {
        Thread gameThread = new Thread() {
            @Override
            public void run() {
                initialize();
                loadContent();
            }
        };
        gameThread.start();
    }
    // Inicializa los atributos
    private void initialize() {
        random = new Random();
        robots = new ArrayList<>();
        robotSelects = new ArrayList<>();
        robotSelect = -1;
        rectangle = new Rectangle2D.Double(0, 0, 1, 1);
        drawRectangle = false;
    }
    private void loadContent() {
        // Cargando todas la imágenes del juego
        try {
            URL urlRobot1 = this.getClass().getResource("/image/robot1.png");
            imgRobot1 = ImageIO.read(urlRobot1);
            
            URL urlRobot2 = this.getClass().getResource("/image/robot2.png");
            imgRobot2 = ImageIO.read(urlRobot2);
            
            URL urlRobot3 = this.getClass().getResource("/image/robot3.png");
            imgRobot3 = ImageIO.read(urlRobot3);
            
            URL urlCursor = this.getClass().getResource("/image/sight.png");
            imgCursor = ImageIO.read(urlCursor);
            middleWidthCursor = imgCursor.getWidth() / 2;
            middleHeightCursor = imgCursor.getHeight() / 2;
            
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Creando los robots y trazando 
         // Agregando robots a la lista        
        robots.add(new Robot(900, 100, 2, 0, imgRobot1));
        robots.add(new Robot(100, 200, imgRobot2));
        robots.add(new Robot(100, 300, 3, 0, imgRobot3));
        
        // Indicandole el destino
        
        
    }
    public void reset() {
        
    }
    private boolean isDrag(Point mousePosition) {
        if(Panel.start.x != mousePosition.x || Panel.start.y != mousePosition.y) {
            return true;
        }
        return false;
    }
    private void clearRobotSelected() {
        // Deseleccionar todos los robot Seleccionados
        for(int i = 0; i < robotSelects.size(); i++) {
            robotSelects.get(i).setSelected(false);
        }
        // Limpiar lista de robots seleccionados
        robotSelects.clear();
    }
    private void collisionDetection(Point mousePosition) {
        // Si esta arrastrando el mouse
        if(isDrag(mousePosition)) {
            // Limpia robots seleccionados
            this.clearRobotSelected();
            // Loop para verificar si hizo click sobre un robot
            for(int i = 0; i < robots.size(); i++) {
                // Verifica colision con el rectangle
                if(robots.get(i).collisionDetection(new Rectangle((int)rectangle.getX(), (int)rectangle.getY(), 
                        (int)rectangle.getWidth(), (int)rectangle.getHeight()))) {
                    // Robot seleccionado
                    robots.get(i).setSelected(true);
                    // Agrega el robot seleccionado
                    robotSelects.add(robots.get(i));                  
                }
            }
        }
        else {
            for(int i = 0; i < robots.size(); i++) {
                if(robots.get(i).collisionDetection(new Rectangle(mousePosition.x, mousePosition.y, 1, 1))) {
                    // Limpia robots seleccionados
                    this.clearRobotSelected();
                    // Robot seleccionado
                    robots.get(i).setSelected(true);
                    // Agrega el robot seleccionado
                    robotSelects.add(robots.get(i));
                    break;
                }
            }
        }

    }
    // Establece el rectangle selector
    private void doRectangle(Point mousePosition) {
        if(isDrag(mousePosition)) {
            if(Panel.start.x < mousePosition.x) {
                if(Panel.start.y < mousePosition.y) {
                    rectangle.setRect(Panel.start.x, Panel.start.y, 
                            Math.abs(Panel.start.x - mousePosition.x), Math.abs(Panel.start.y - mousePosition.y));
                }
                else {
                    rectangle.setRect(Panel.start.x, mousePosition.y, 
                            Math.abs(Panel.start.x - mousePosition.x), Math.abs(Panel.start.y - mousePosition.y));
                }
            }
            else {
                if(Panel.start.y < mousePosition.y) {
                    rectangle.setRect(mousePosition.x, Panel.start.y, 
                            Math.abs(Panel.start.x - mousePosition.x), Math.abs(Panel.start.y - mousePosition.y));
                }
                else {
                    rectangle.setRect(mousePosition.x, mousePosition.y, 
                            Math.abs(Panel.start.x - mousePosition.x), Math.abs(Panel.start.y - mousePosition.y));
                }
            }                
            drawRectangle = true;
        }
    }
    // Verifica si coliciona con robot seleccionado para tracert
    private void tracert(Point mousePosition) {
        if(!robotSelects.isEmpty()) {
            for(int i = 0; i < robotSelects.size(); i++) {
                // Si hace click sobre robot seleccionado, no elabora ruta
                if(!robotSelects.get(i).collisionDetection(new Rectangle(mousePosition.x, mousePosition.y, 1, 1))) {
                    // Elabora ruta
                    robotSelects.get(i).tracert(mousePosition);
                }                    
            }
        }
    }
    // Aqui se verifica las condiciones y estrategias del juego
    public void update(Point mousePosition) {

        // Si hace click sobre un robot        
        if(Panel.getMousePressedState(MouseEvent.BUTTON1)) {  
            // Verifica si selecciono algun robot
            this.collisionDetection(mousePosition);
            // obtiene el rectangle si esta arrastrando
            this.doRectangle(mousePosition);            
        }
        // Si se deja de presionar se cambia a false y ya no dibuja el rectangle
        if(Panel.getMouseReleasedState(MouseEvent.BUTTON1)) {
            // Si suelta el boton del mouse, ya no dibuja el rectangle
            drawRectangle = false;
            Panel.setMouseReleasedState(MouseEvent.BUTTON1, false);
        }
        // Si presiona el boton izquierdo del mouse se deselecciona el robot
        if(Panel.getMousePressedState(MouseEvent.BUTTON3)) {
            this.clearRobotSelected();
            Panel.setMousePressedState(MouseEvent.BUTTON3, false);
        }
        // Si esta seleccionado un robot e hizo click fuera del robot()
        if(Panel.getMouseClickedState(MouseEvent.BUTTON1)) {
            
            this.tracert(mousePosition);
            Panel.setMouseClickedState(MouseEvent.BUTTON1, false);
        }
        
        // Actualizando todos los robots        
        for(int i = 0; i < robots.size(); i++) {
            robots.get(i).update(robots);
        }        
    }
    // Aqui se dibuja todo lo que aparece en la pantalla del juego
    public void draw(Graphics2D g2d, Point mp) {        
                
        for(int i = 0; i < robots.size(); i++) {
            robots.get(i).draw(g2d);
        }         
        if(drawRectangle) {
            g2d.setColor(Color.GRAY);
            g2d.draw(this.rectangle);
        }
        
        
        // Dibuja el cursor del mouse
        g2d.drawImage(imgCursor, mp.x - middleWidthCursor, mp.y - middleHeightCursor, null);
    }
}
