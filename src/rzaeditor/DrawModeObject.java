package rzaeditor;

import rzaeditor.pageobjects.PageLine;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import static rzaeditor.Logic.dragEnd;
import static rzaeditor.Logic.dragStart;
import rzaeditor.pageobjects.PageObjectComplex;

public class DrawModeObject extends DrawMode {

    public static DrawModeObject imp = new DrawModeObject();
    public static Class objectClass = null;
    public static boolean rotate = false;

    @Override
    public void keyboardEvent() {
        Keyboard.released.size();
        if(Keyboard.isReleased(KeyEvent.VK_SHIFT))
            rotate=!rotate;
    }
    
    @Override
    public void draw() {
       if(!Logic.isDragging) return;
        
        Drawing.setColor(Color.RED);
        
        try {
            objectClass.getMethod("drawPhantom", Vector2i.class, boolean.class).invoke(null, Cursor.posGrid, rotate);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DrawModeObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseReleased() {
        try {
            PageObjectComplex o = (PageObjectComplex) objectClass.getConstructor(Vector2i.class, boolean.class).newInstance(Cursor.posGrid, rotate);
            Page.current.objects.add(o);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(DrawModeObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
