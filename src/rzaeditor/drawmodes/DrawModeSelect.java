package rzaeditor.drawmodes;

import rzaeditor.pageobjects.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.function.Consumer;
import org.joml.*;
import org.joml.primitives.Rectanglei;
import rzaeditor.*;
import static rzaeditor.Drawing.drawRect;
import static rzaeditor.Logic.*;
import static rzaeditor.drawmodes.DrawModeSelect.EditMode.*;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.PageObjectComplex.Direction;

public class DrawModeSelect extends DrawMode {

    public static DrawModeSelect imp = new DrawModeSelect();
    public static HashSet<PageObjectBase> selectedObjects = new HashSet<>();
    public static HashSet<PageObjectBase> hoveredObjects = new HashSet<>();
    public static EditMode editMode = NONE;
    public static boolean canDoEdit = false;
    
    public enum EditMode{
        NONE,
        DRAG,
        WIRES
    }

    @Override
    public void keyboardEvent() {
        if(Keyboard.isReleased(KeyEvent.VK_DELETE)){
            selectedObjects.forEach((t) -> {
                t.delete();
            });
        }
        else if(Keyboard.isReleased(KeyEvent.VK_G)){
            editMode=DRAG;
        }
        else if(Keyboard.isReleased(KeyEvent.VK_B)){
            editMode=NONE;
        }
        else if(Keyboard.isReleased(KeyEvent.VK_W)){
            editMode=WIRES;
        }
        infoText = editMode.toString();
    }
    
    @Override
    public void mouseDrag() {   
        if(editMode == EditMode.DRAG){
            
        }
        else if(editMode == EditMode.NONE){
            Page.current.objects.stream().forEach((t) -> {
                //if(t instanceof WireIntersection) return;
                PageObjectBase o = (PageObjectBase) t;
                o.hovered=false;
                if(o.isRectTouching(dragRect)){
                    o.hovered=true;
                    hoveredObjects.add(o);
                }
            });
        }
    }

    @Override
    public void mouseMove() {
        //if(!selectedObjects.isEmpty()) return;
        if(editMode == NONE){
            Page.current.objects.stream().forEach((t) -> {
                PageObjectBase o = (PageObjectBase) t;
                o.hovered=false;
                if(o.isVecTouching(Cursor.posGrid)){
                    o.hovered=true;
                    hoveredObjects.add(o);
                }
            });
        }
    }

    @Override
    public void draw() {
        if(!isDragging) return;
        
        if(editMode == NONE){
            Drawing.setColor(Color.CYAN);
            Drawing.setTranslateGrid(Logic.dragRect.minX, Logic.dragRect.minY);
            Drawing.drawRectGrid(0,0, Logic.dragRect.lengthX(), Logic.dragRect.lengthY());
        }
        else if(editMode == DRAG && dragVecFixed.lengthSquared()>0){
            canDoEdit = true;
            Drawing.setTranslateGrid(0, 0);
            selectedObjects.stream().forEach((t) -> {
                Class c = t.getClass();
                Vector2i p = new Vector2i(t.pos).add(Logic.dragVec);
                if(t instanceof Wire){
                    System.out.println(((Wire) t).pos+" "+p+" "+Logic.dragVec);
                    Drawing.drawLineGrid(p, new Vector2i(p).add(((Wire) t).size));
                }
                else if (t instanceof WireIntersection){
                    //Drawing.fillOvalGridCenter(p.x, p.y, 4, 4);
                }
                else{
                    Direction d = ((PageObjectComplex)t).direction;
                    boolean canPut = PageObjectComplex.canPutHereIgnore(c, p, d, selectedObjects);
                    Color cl = Color.green;
                    if(!canPut){
                        cl = Color.red;
                        canDoEdit = false;
                    }
                    Drawing.setColor(cl);
                    PageObjectComplex.callRotateCheck(c,p,d);
                }
            });
        }
    }

   @Override
    public void mousePressed() {
    }

    @Override
    public void mouseReleased() { 
        if(editMode == NONE){
            boolean shiftDown = Keyboard.isDown(KeyEvent.VK_SHIFT); 
            if(!shiftDown)
                selectedObjects.clear();

            Page.current.objects.stream().forEach((t) -> {
                PageObjectBase o = (PageObjectBase) t;
                if(shiftDown)
                    o.selected = o.hovered || o.selected;
                else
                    o.selected = o.hovered;
                o.hovered = false;
                if(o.selected){
                    selectedObjects.add(o);
                }
            });

            InfoTable.reset();

            if(hoveredObjects.isEmpty())
                cleanup();
            hoveredObjects.clear();
        }else if(editMode == DRAG && canDoEdit && dragVecFixed.lengthSquared()>0){
            selectedObjects.stream().forEach((t) -> {
                t.moveTo(new Vector2i(t.pos).add(dragVec));
            });
            Wire.checkAllWires();
        }
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
