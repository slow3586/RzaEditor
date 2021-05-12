package rzaeditor.drawmodes;

import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.Wire;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.function.Consumer;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Keyboard;
import rzaeditor.Logic;
import rzaeditor.Page;
import static rzaeditor.Drawing.drawRect;
import static rzaeditor.Logic.*;
import rzaeditor.pageobjects.PageObjectBase;

public class DrawModeSelect extends DrawMode {

    public static DrawModeSelect imp = new DrawModeSelect();
    public static HashSet<PageObjectBase> selectedObjects = new HashSet<>();
    public static HashSet<PageObjectBase> hoveredObjects = new HashSet<>();

    @Override
    public void keyboardEvent() {
        if(Keyboard.isReleased(KeyEvent.VK_DELETE)){
            selectedObjects.forEach((t) -> {
                t.delete();
            });
        }
    }
    
    @Override
    public void mouseDrag() {        
        Page.current.objects.stream().forEach((t) -> {
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
        //if(!selectedObjects.isEmpty()) return;
        
        Page.current.objects.stream().forEach((t) -> {
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
        Drawing.setTranslateGrid(Logic.dragRect.minX, Logic.dragRect.minY);
        Drawing.drawRectGrid(0,0, Logic.dragRect.lengthX(), Logic.dragRect.lengthY());
    }

   @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() {  
        InfoTable.reset();
        
        Page.current.objects.stream().forEach((t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.selected = o.hovered;
            o.hovered = false;
            if(o.selected){
                selectedObjects.add(o);
                o.onSelect();
            }
        });
        
        if(hoveredObjects.isEmpty())
            cleanup();
        hoveredObjects.clear();
    }

    @Override
    public void cleanup() {
        Page.current.objects.stream().forEach((t) -> {
            PageObjectBase o = (PageObjectBase) t;
            o.selected = false;
            o.hovered = false;
        });
        selectedObjects.clear();
        hoveredObjects.clear();
    }

}
