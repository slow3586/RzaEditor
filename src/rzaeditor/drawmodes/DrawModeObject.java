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
import rzaeditor.Keyboard;
import rzaeditor.Logic;
import rzaeditor.Page;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.PageObjectComplex.Direction;

public class DrawModeObject extends DrawMode {

    public static DrawModeObject imp = new DrawModeObject();
    public static Class objectClass = null;
    public static Direction dir = Direction.LEFT;

    public static void initWithClass(Class c){
        DrawMode.setCurrent(imp);
        objectClass = c;
    }
    
    @Override
    public void keyboardEvent() {
        if(Keyboard.isReleased(KeyEvent.VK_SHIFT)){
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
       if(!Logic.isDragging) return;
        
        Drawing.setColor(Color.RED);
        
        PageObjectComplex.callRotateCheck(objectClass, Cursor.posGrid, dir);
    }

    @Override
    public void mouseReleased() {
        try {
            PageObjectComplex o = (PageObjectComplex) objectClass.getConstructor(Vector2i.class, Direction.class).newInstance(Cursor.posGrid, dir);
            Page.current.objects.add(o);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DrawModeObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
