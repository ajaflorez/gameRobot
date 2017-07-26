package game01;

import java.awt.Point;
import java.util.ArrayList;

public class Tracert {
    private static Point start = null;
    private static Point end = null;
    private static float slope = 0F;    
    private static TipoRecta tipoRecta = TipoRecta.ASCENDENTE;
    
    public static ArrayList<Point> doRoute(Point _start, Point _end) {
        start = _start;
        end = _end;        
        getSlope();
        
        ArrayList<Point> route  = new ArrayList<>();
        
        if(Math.abs(start.x - end.x) > Math.abs(start.y - end.y)) {
            if(start.x < end.x) {
                for(int x = start.x + 1; x <= end.x; x++) {
                    addX(route, x);
                }
            }
            else if(start.x > end.x) {
                for(int x = start.x - 1; x >= end.x; x--) {
                    addX(route, x);
                }            
            }
        }
        else if(Math.abs(start.x - end.x) < Math.abs(start.y - end.y)) {
            if(start.y < end.y) {
                for(int y = start.y + 1; y <= end.y; y++) {   
                    addY(route, y);
                }
            }
            else if(start.y > end.y) {
                for(int y = start.y - 1; y >= end.y; y--) {
                    addY(route, y);
                }            
            }
        }        
        return route;
    }
    private static int getY(int x) {
        // y = mx - mx1 + y1
        int y = (int)((slope * x) - (slope * start.x) + start.y); 
        return y;
    }
    private static int getX(int y) {
        // x = (y - y1 + mx1) / m
        int x = (int)((y - start.y + (slope * start.x)) / slope); 
        return x;
    }
    private static void addX(ArrayList<Point> route, int x) {
        if(tipoRecta == TipoRecta.ASCENDENTE || tipoRecta == TipoRecta.DESCENDENTE)
            route.add(new Point(x, getY(x)));
        else if(tipoRecta == TipoRecta.HORIZONTAL)
            route.add(new Point(x, start.y));
    }
    private static void addY(ArrayList<Point> route, int y) {
        if(tipoRecta == TipoRecta.ASCENDENTE || tipoRecta == TipoRecta.DESCENDENTE)
            route.add(new Point(getX(y), y));
        else if(tipoRecta == TipoRecta.VERTICAL)
            route.add(new Point(start.x, y));
    }
    private static void getSlope() {
        if(end.x - start.x == 0) {
            slope = 0F;
            tipoRecta = TipoRecta.VERTICAL;
        }
        else if(end.y - start.y == 0) {
            slope = 0F;
            tipoRecta = TipoRecta.HORIZONTAL;
        }
        else {
            slope = (float)(end.y - start.y) / (float)(end.x - start.x);
            if(slope > 0) {
                tipoRecta = TipoRecta.ASCENDENTE;
            }
            else {
                tipoRecta = TipoRecta.DESCENDENTE;
            }
        }        
    }
}
enum TipoRecta {
    ASCENDENTE, DESCENDENTE, HORIZONTAL, VERTICAL
}
