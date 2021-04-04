package rzaeditor;

import java.awt.Color;
import org.joml.Vector2i;
import static rzaeditor.Drawing.g;

public class DrawModeWire extends DrawMode {

    public static DrawModeWire ins = new DrawModeWire();
    public static boolean dwGood = false;
    public static Wire drawWire = Wire.createDrawWire();
    public static boolean isDrawing = false;
    
    @Override
    public void mouseDrag() {
        drawWire.end = Cursor.posGrid;
        
        Vector2i dpvec = drawWire.getVec();
        if(Math.abs(dpvec.x) > Math.abs(dpvec.y))
            drawWire.end.y=drawWire.start.y;
        else
            drawWire.end.x=drawWire.start.x;

        dwGood=drawWire.getLen()!=0;
        for (Wire w : Page.wires) {
            if(w.containsWire(drawWire)){
                dwGood=false;
                break;
            }
        }
    }

    @Override
    public void mouseMove() {
    }

    @Override
    public void draw() {
        if(!isDrawing) return;
        
        g.setColor(Color.RED);
        if (dwGood) {
            g.setColor(Color.GREEN);
        }
        drawWire.draw(g);
    }

    @Override
    public void mousePressed() {
        drawWire.start = Cursor.posGrid;
        drawWire.end = Cursor.posGrid;
        isDrawing = true;
    }

    @Override
    public void mouseReleased() {
        if(dwGood){
            Wire nw = Wire.create(drawWire.start, drawWire.end);
        }
        
        drawWire.start.zero();
        drawWire.end.zero();
        isDrawing = false;
    }

}
