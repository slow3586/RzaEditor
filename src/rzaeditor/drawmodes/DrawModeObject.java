package rzaeditor.drawmodes;

import rzaeditor.pageobjects.primitives.PageLine;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import rzaeditor.Cursor;
import rzaeditor.Drawing;
import rzaeditor.Help;
import rzaeditor.Keyboard;
import rzaeditor.Logic;
import rzaeditor.Page;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.PageObjectComplex.Direction;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;

public class DrawModeObject extends DrawMode {

    public static DrawModeObject imp = new DrawModeObject();
    public static Class objectClass = null;
    public static Direction dir = Direction.LEFT;
    public static boolean canPutHere = false;

    public static void initWithClass(Class c){
        DrawMode.setCurrent(imp);
        objectClass = c;
    }
    
    @Override
    public void keyboardEvent() {
        if(Keyboard.isReleased(KeyEvent.VK_ESCAPE)){
            DrawMode.setCurrent(DrawModeSelect.imp);
            return;
        }
        else if(Keyboard.isReleased(KeyEvent.VK_SHIFT)){
            boolean canSwitchDir = false;
            try {
               canSwitchDir = (boolean) objectClass.getField("canSwitchDirection").get(boolean.class);
            } catch (NoSuchFieldException ex) {
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(DrawModeObject.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if(dir==Direction.LEFT){
                dir=Direction.UP;
            }
            else if(dir==Direction.UP){
                if(canSwitchDir)
                    dir=Direction.RIGHT;
                else
                    dir=Direction.LEFT;
            }
            else if(dir==Direction.RIGHT){
                dir=Direction.DOWN;
            }
            else if(dir==Direction.DOWN){
                dir=Direction.LEFT;
            }
        }
    }
    
    @Override
    public void draw() {
        
        if(canPutHere)
            Drawing.setColor(Color.GREEN);
        else
            Drawing.setColor(Color.RED);
            
        
        PageObjectComplex.callRotateCheck(objectClass, Cursor.posGrid, dir);
    }

    @Override
    public void mouseMove() {
        canPutHere = PageObjectComplex.canPutHere(objectClass, Cursor.posGrid, dir);
    }

    @Override
    public void mouseReleased() {
        if(!canPutHere) return;
        try {
            PageObjectComplex o = (PageObjectComplex) objectClass.getConstructor(Vector2i.class, Direction.class).newInstance(Cursor.posGrid, dir);
            Page.current.objects.add(o);
            if(dir.isHorizontal()){
                for (int i = Cursor.posGrid.x-1; i > 0; i--) {
                    int offset = (int) Help.getFieldValue(objectClass, "defaultWireIntersectOffset");
                    Vector2i p = new Vector2i(i,Cursor.posGrid.y+offset);
                    if(WireIntersection.WIexists(p)){
                        Wire.create(p, new Vector2i(Cursor.posGrid).add(0, offset));
                        break;
                    }
                }
            }else{
                for (int i = Cursor.posGrid.y-1; i > 0; i--) {
                    int offset = (int) Help.getFieldValue(objectClass, "defaultWireIntersectOffset");
                    Vector2i p = new Vector2i(Cursor.posGrid.x+offset, i);
                    if(WireIntersection.WIexists(p)){
                        Wire.create(p, new Vector2i(Cursor.posGrid).add(offset, 0));
                        break;
                    }
                }
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DrawModeObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
