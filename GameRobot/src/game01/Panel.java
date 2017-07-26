package game01;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel implements KeyListener, MouseListener{
    // true: Sin cursor, false: Con cursor
    private boolean blankCursor;
    // Array de State del keyboard
    private boolean[] keyState;
    // Array de State del mouse
    private static boolean[] mousePressedState;
    private static boolean[] mouseReleasedState;
    private static boolean[] mouseClickedState;
    
    // Punto inicial presionado
    public static Point start;
    
    private Game game;
    
    // game Time
    private long gameTime;
    // last Time
    private long lastTime;
    
    //-------------------------
    
    JButton calcula;
    JTextField textData;

    public Panel() {
        Thread panelThread = new Thread() {
            @Override
            public void run() {
                Initialize();
                initPanel();
                initState();
                initTime();
                gameLoop();
            }
        };
        panelThread.start();
            
        //---------------------------------------
        textData = new JTextField(20);
        calcula = new JButton("Cambiar");
        this.add(textData);
        this.add(calcula);
        calcula.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setBlankCursor(false);
            }
        });
        //---------------------------------------
    }
    private void Initialize() {
        game = new Game();
    }
    private void initPanel() {
        // Desaparece cursor del Mouse
        this.blankCursor = true;

        // Establecer el Focus al panel actual para los Listener
        this.setFocusable(true);
        
        // Establece el color de Fondo
        this.setBackground(Color.BLACK);
        
        // Establece doble buffer para pintar la Pantalla
        this.setDoubleBuffered(true);
        
        // Oculta el cursor del mouse
        this.setBlankCursor(this.blankCursor);     
        
        // Agrega el Key Listenes al JPanel
        this.addKeyListener(this);
        
        // Agrega el Mouse Listenes al JPanel
        this.addMouseListener(this);
    }
    private void initState() {
        // Array de 200 del key state
        this.keyState = new boolean[200];        
        // Array de 4 del Mouse state
        mousePressedState = new boolean[4];
        mouseReleasedState = new boolean[4];
        mouseClickedState = new boolean[4];
        
        start = null;
    }
    private void initTime() {
        // Game init in 0
        this.gameTime = 0;
        // lastTime es el time del JVM en nanosecond
        this.lastTime = System.nanoTime();
    }
    private void setBlankCursor(boolean blankCursor) {
        this.blankCursor = blankCursor;
        if(this.blankCursor) {
            // Crea una imagen transparente de 16 x 16
            BufferedImage blankImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            
            // Crea un cursor con la imagen transparente
            Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(blankImg, new Point(0, 0), "Blank Cursor");
            
            // Establece el cursor
            this.setCursor(cursor);            
        }
        else {
            // Establece el cursor por defecto
            this.setCursor(Cursor.getDefaultCursor());
        }
    }
    // Devuelve la Posición del mouse
    private Point mousePosition() {
        Point mp = this.getMousePosition();
        if(mp != null) 
            return mp;
        else
            return new Point(0, 0);
    }
    private void gameLoop() {
        // Usado para la visualización del estado del juego
        long visualTime = 0;
        long lastVisualTime = System.nanoTime();
        
        // para el sleep del thread
        long beginTime;
        long takenTime;
        long leftTime;
        
        // Point del mouse
        //Point mp;
        
        while(true) {
            beginTime = System.nanoTime();
            //----------------------
            // PLAYING
            //
            // Tiempo actual - último tiempo
            this.gameTime += System.nanoTime() - this.lastTime;
            
            //update GAME
            game.update(this.getMousePosition());
            
            
            this.lastTime = System.nanoTime();
            //----------------------
            // Dibuja la pantalla
            repaint();
            
            // Calculo para el sleep del Thread
            takenTime = System.nanoTime() - beginTime;
            leftTime = (GameTime.PAUSE_UPDATE - takenTime) / GameTime.oneMiliNano;
            
            if (leftTime < 10) 
                leftTime = 10; //set a minimum
            System.out.println(leftTime);
            try {
                Thread.sleep(leftTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(Panel.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
    }
    //private updateGame(Point)
    
    // Convierte Graphics a Graphics2D e
    // invoca al método Draw que dibuja todo en la pantalla
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d); 
        Draw(g2d);
    }
    public void Draw(Graphics2D g2d) {
        Point mp = this.mousePosition();
        game.draw(g2d, mp);
        //textData.setText(String.valueOf(mp.x) + "-" + String.valueOf(mp.y));
    }
    
    // Métodos del Keyboard Listener
    
    @Override
    public void keyTyped(KeyEvent e) {    }
    // Se ejecuta cuando se presiona una tecla
    @Override    
    public void keyPressed(KeyEvent e) {
        // Cambia a true el valor del elemento del array 
        this.keyState[e.getKeyCode()] = true;
        
        //textData.setText(textData.getText() + KeyEvent.getKeyText(e.getKeyCode()));
        //textData.setText(textData.getText() + String.valueOf(e.getKeyCode()));        
        keyPressedPanel(e);
    }
    // Se ejecuta cuando se suelta una tecla
    @Override
    public void keyReleased(KeyEvent e) {
        this.keyState[e.getKeyCode()] = false;
        //textData.setText(textData.getText() + "*");
        keyReleasedPanel(e);
    }
    // Se ejecuta cuando se presiona una tecla
    private void keyPressedPanel(KeyEvent e) {
        // Cuando se presiona la tecla CTRL + X
        if(e.getKeyCode() == KeyEvent.VK_X) {
            if(this.getKeyState(KeyEvent.VK_CONTROL)) {
                System.exit(0);
            }
        }
    }
    // Se ejecuta cuando se presiono y se solto una tecla
    private void keyReleasedPanel(KeyEvent e) {
        // Cuando se presiona la tecla ESCAPE
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
    private boolean getKeyState(int key) {
        return this.keyState[key];
    }
    
    // Métodos del Mouse Listener

    // Cuando se presiona una tecla del mouse
    @Override
    public void mousePressed(MouseEvent e) {        
        start = this.getMousePosition();
        mousePressedState[e.getButton()] = true;
        
        
        textData.setText(textData.getText() + String.valueOf(e.getButton()));        
    }
    // cuando se suelta una tecla del mouse
    @Override
    public void mouseReleased(MouseEvent e) {
        mouseReleasedState[e.getButton()] = true;
        mousePressedState[e.getButton()] = false;
        
        textData.setText(textData.getText() + "*");
    }
    //------------------------
    // Cuando se hace click en el mouse(presiona y suelta el mouse)
    @Override
    public void mouseClicked(MouseEvent e) {  
        mouseClickedState[e.getButton()] = true;
        textData.setText(textData.getText() + "C");  
    }
    @Override
    // Cuando el mouse entra en un componente
    public void mouseEntered(MouseEvent e) {    }
    // Cuando el mouse sale de un componente
    @Override
    public void mouseExited(MouseEvent e) {     }   
    
    public static boolean getMousePressedState(int button) {
        return mousePressedState[button];
    }
    public static boolean getMouseReleasedState(int button) {
        return mouseReleasedState[button];
    }
    public static boolean getMouseClickedState(int button) {
        return mouseClickedState[button];
    }
    public static void setMousePressedState(int button, boolean state) {
        mousePressedState[button] = state;
    }
    public static void setMouseReleasedState(int button, boolean state) {
        mouseReleasedState[button] = state;
    }
    public static void setMouseClickedState(int button, boolean state) {
        mouseClickedState[button] = state;
    }
    
}
