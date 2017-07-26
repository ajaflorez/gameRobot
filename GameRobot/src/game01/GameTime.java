package game01;

public class GameTime {
    // Ancho del Panel
    public static int panelWidth;
    // Alto del Panel
    public static int panelHeight;
    
    // Un segundo equilave a 1 000 000 000 nanosegundos
    public static final long oneSecNano = 1000000000L;
    // Un mili-segundo equilave a 1 000 000 nanosegundos
    public static final long oneMiliNano = 1000000L;
    // Frames per second
    public static final int FRAME_SECOND = 60;
    // pause between update 
    public static final long PAUSE_UPDATE = oneSecNano / FRAME_SECOND;
    
    
    
}
