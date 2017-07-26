package game01;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Robot {
    private Point coordinate;       
    private int speed;    
    private int poppet;    
    private int score;    
    private BufferedImage image;
    private int width;
    private int height;
    private boolean selected;
    private ArrayList<Point> route;

    public Robot(int x, int y, int speed, int score, BufferedImage image) {
        this.coordinate = new Point(x, y);        
        this.speed = speed;
        this.poppet = -1;
        this.score = score;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.selected = false;
        route = new ArrayList<>();
    }
    public Robot(Point coord, int speed, int score, BufferedImage image) {
        this.coordinate = coord;
        this.speed = speed;
        this.poppet = -1;
        this.score = score;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.selected = false;
        route = new ArrayList<>();
    }
    public Robot(int x, int y, BufferedImage image) {        
        this.coordinate = new Point(x, y);        
        this.speed = 1;
        this.poppet = -1;
        this.score = 0;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.selected = false;
        route = new ArrayList<>();
    }    
    public Robot(Point coord, BufferedImage image) {        
        this.coordinate = coord;
        this.speed = 1;
        this.poppet = -1;    // cabezal
        this.score = 0;
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.selected = false;
        route = new ArrayList<>();
    }    
    // Trazar la ruta o camino del robot
    public void tracert(Point end) {
        this.stop();
        if(this.coordinate.x != end.x || this.coordinate.y != end.y) {
            this.route = Tracert.doRoute(this.coordinate, new Point(end.x - (this.width / 2), end.y - (this.height / 2)));
            if(this.route.size() > 0) {
                this.poppet = 0;
            }
        }
    }
    public void update(ArrayList<Robot> robots) {
        // Si no esta parado, verifico que no colisione y se mueve
        if(!this.isStop()) {
            if(!this.collisionDetection(robots)) {
                this.move();
            }
        }
    }
    private boolean collisionDetection(ArrayList<Robot> robots) {
        if(!this.isStop()) {
            for(int i = 0; i < robots.size(); i++) {
                if(this != robots.get(i)) {
                    if(this.getNextBounds().intersects(robots.get(i).getBounds()))
                        return true;
                }
            }        
        }        
        return false;
    }
    public boolean collisionDetection(Rectangle rectangle) {
        if(this.getBounds().intersects(rectangle))
            return true;
        return false;
    }
    private boolean isStop() {
        if(this.poppet == -1) {
            return true;
        } 
        else
            return false;
    }
    public void stop() {
        this.poppet = -1;
        this.route.clear();
    }
    private int getNextPoppet() {
        if(this.poppet < this.route.size()) {
            if(this.poppet + this.speed < this.route.size()) {
                return this.poppet + this.speed;
            }
            else {
                this.route.clear();
                return -1;
            }
        }
        return -1;
    }
    // moverse por la ruta
    private void move() {
        //if(this.poppet + this.speed <= this.route.size()) {
        if(this.poppet < this.route.size()) {
            this.coordinate.x = this.route.get(poppet).x;
            this.coordinate.y = this.route.get(poppet).y;                    
            this.poppet = this.getNextPoppet();
        }
    }
    public void draw(Graphics2D g2d) {
        g2d.drawImage(image, null, this.coordinate.x, this.coordinate.y);
        if(this.selected) {
            g2d.setColor(Color.RED);
            g2d.draw(new Rectangle2D.Double(this.coordinate.x, this.coordinate.y - 6, this.width, 3));
            g2d.setColor(Color.GREEN);
            g2d.fill(new Rectangle2D.Double(this.coordinate.x + 1, this.coordinate.y - 5, this.width - 1, 2));
            
        }
            
    }
    public int getX() {
        return this.coordinate.x;
    }
    public int getY() {
        return this.coordinate.y;
    }    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    public Point getCoordinate() {
        return coordinate;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public boolean isSelected() {
        return selected;
    } 
    
    // Obtener el limite del robot
    private Rectangle getNextBounds() {
        return new Rectangle(this.route.get(this.poppet).x, 
                this.route.get(this.poppet).y, this.width, this.height);
    }
    private Rectangle getBounds() {
        return new Rectangle(this.coordinate.x, this.coordinate.y, this.width, this.height);
    }
}
