package rzaeditor;

import rzaeditor.pageobjects.PageObject;
import rzaeditor.pageobjects.Wire;
import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import static rzaeditor.Drawing.drawRect;
import static rzaeditor.Logic.*;

public class DrawModeSelect extends DrawMode {

    public static DrawModeSelect imp = new DrawModeSelect();
    public static PageObject selectedObject = null;
    
    @Override
    public void mouseDrag() {
        if(selectedObject!=null && Cursor.gridMoved){
            //todo
        }

        for (Wire w : Page.current.wires) {
            if(dragRect.containsPoint(w.getCenter())){
                w.selected=true;
                break;
            }
        }
    }

    @Override
    public void mouseMove() {
        for (Wire w : Page.current.wires) {
            w.selected=false;
            if(w.pointInside(Cursor.posGrid, 1)){
                w.selected=true;
                selectedObject =w;
            }
        }
    }

    @Override
    public void draw() {
        if(!isDragging) return;
        
        Drawing.setColor(Color.CYAN);
        Vector2i t0 = Logic.gridToScreen(dragRect.minX, dragRect.minY);
        Vector2i t1 = Logic.gridToScreen(dragRect.lengthX(), dragRect.lengthY());
        drawRect(t0.x, t0.y, t1.x, t1.y);
    }

   @Override
    public void mousePressed() {
        
        if(selectedObject!=null)
            isDragging = true;
        
    }

    @Override
    public void mouseReleased() {   
        
    }

}
