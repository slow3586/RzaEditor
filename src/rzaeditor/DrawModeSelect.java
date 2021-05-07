package rzaeditor;

import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.Wire;
import java.awt.Color;
import java.util.HashSet;
import java.util.function.Consumer;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import static rzaeditor.Drawing.drawRect;
import static rzaeditor.Logic.*;
import rzaeditor.pageobjects.PageObjectBase;

public class DrawModeSelect extends DrawMode {

    public static DrawModeSelect imp = new DrawModeSelect();
    public static HashSet<PageObjectBase> selectedObjects = new HashSet<>();
    public static HashSet<PageObjectBase> hoveredObjects = new HashSet<>();
    
    @Override
    public void mouseDrag() {        
        Logic.forAllObjects((Consumer) (Object t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.hovered=false;
            if(o.isRectTouching(dragRect)){
                o.hovered=true;
                hoveredObjects.add(o);
            }
        });
    }

    @Override
    public void mouseMove() {
        if(!selectedObjects.isEmpty()) return;
        
        Logic.forAllObjects((Consumer) (Object t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.hovered=false;
            if(o.isVecTouching(Cursor.posGrid)){
                o.hovered=true;
                hoveredObjects.add(o);
            }
        });
    }

    @Override
    public void draw() {
        if(!isDragging) return;
        
        Drawing.setColor(Color.CYAN);
        Drawing.drawRect(Logic.gridToScreen(new Vector2i(Logic.dragRect.minX, Logic.dragRect.minY)),
                new Vector2i(Math.round(Logic.dragRect.lengthX()*zoomGridGap), Math.round(Logic.dragRect.lengthY()*zoomGridGap)));
    }

   @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {  
        InfoTable.reset();
        
        Logic.forAllObjects((Consumer) (Object t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.selected = o.hovered;
            o.hovered = false;
            if(o.selected){
                o.onSelect();
            }
        });
        
        selectedObjects.clear();
        hoveredObjects.clear();
    }

    @Override
    public void cleanup() {
        Logic.forAllObjects((Consumer) (Object t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.selected = false;
            o.hovered = false;
        });
        selectedObjects.clear();
        hoveredObjects.clear();
    }

}
