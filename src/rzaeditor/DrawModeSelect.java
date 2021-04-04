package rzaeditor;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import static rzaeditor.Drawing.drawRect;
import static rzaeditor.Drawing.g;

public class DrawModeSelect extends DrawMode {

    public static DrawModeSelect ins = new DrawModeSelect();
    public static Rectanglei selectRect = new Rectanglei();
    public static Wire selectedWire = null;
    public static Vector2i start = new Vector2i();
    public static Vector2i end = new Vector2i();
    public static boolean isDragging = false;
    
    @Override
    public void mouseDrag() {
        end = Cursor.posGrid;
        if(!isDragging){
            selectRect.setMin(start);
            selectRect.setMax(end);

            Vector2i adjEnd = new Vector2i();
            Vector2i adjStart = new Vector2i();
            if(DrawModeWire.drawWire.start.x>DrawModeWire.drawWire.end.x) 
                adjStart.x=0;
            else
                adjEnd.x=1;
            if(DrawModeWire.drawWire.start.y>DrawModeWire.drawWire.end.y) 
                adjStart.y=0;
            else
                adjEnd.y=1;
            Vector2i s = new Vector2i(Math.min(start.x, end.x)-adjStart.x,Math.min(start.y, end.y)-adjStart.y);
            Vector2i e = new Vector2i(Math.max(start.x, end.x)+adjEnd.x,Math.max(start.y, end.y)+adjEnd.y);
            selectRect.setMin(s);
            selectRect.setMax(e);
        }
        
        if(selectedWire!=null && Cursor.gridMoved){
            selectedWire.start.add(Cursor.relGridMove);
            selectedWire.end.add(Cursor.relGridMove);
            selectedWire.checkIntersections();
        }

        for (Wire w : Page.wires) {
            if(selectRect.containsPoint(w.getCenter())){
                w.selected=true;
                break;
            }
        }
    }

    @Override
    public void mouseMove() {
        for (Wire w : Page.wires) {
            w.selected=false;
            if(w.pointInside(Cursor.posGrid, 1)){
                w.selected=true;
                selectedWire =w;
            }
        }
    }

    @Override
    public void draw() {
        if(isDragging) return;
        
        g.setColor(Color.CYAN);
        Vector2f t0 = Logic.gridToScreen(selectRect.minX, selectRect.minY);
        Vector2f t1 = Logic.gridToScreen(selectRect.lengthX(), selectRect.lengthY());
        drawRect(t0.x, t0.y, t1.x, t1.y);
    }

   @Override
    public void mousePressed() {
        start = Cursor.posGrid;
        end = Cursor.posGrid;
        if(selectedWire!=null)
            isDragging = true;
        selectRect.setMin(start);
        selectRect.setMax(end);
    }

    @Override
    public void mouseReleased() {   
        start.zero();
        end.zero();
        selectRect.setMin(0,0);
        selectRect.setMax(0,0);
    }

}
