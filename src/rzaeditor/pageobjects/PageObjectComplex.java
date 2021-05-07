package rzaeditor.pageobjects;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public abstract class PageObjectComplex extends PageObjectBase {
    
    public boolean rotated = false;
    public HashSet<WireIntersection> wireIntersections = new HashSet<>();

    public PageObjectComplex(Vector2i pos, boolean rot) {
        super(pos);
        rotated = rot;
        try {
            methodDrawPhantom = getClass().getMethod("drawPhantom", Vector2i.class, boolean.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            System.err.println("drawPhantom method not found in class "+getClass().getName()+"!!!");
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Field f = getClass().getField("defaultSize");
            setSize((Vector2i) f.get(Vector2i.class));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            System.err.println("defaultSize field not found in class "+getClass().getName()+"!!!");
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drawChildren(){
        //children.forEach((t) -> {
        //   t.draw();
        //});
        wireIntersections.forEach((t) -> {
            t.draw();
        });
    }
    
    
    
    public static void rotateCheck(Vector2i pos, Vector2i size, boolean rot){
        Drawing.setTranslateGrid(pos);
        if(rot){
            Drawing.setTranslateGrid(pos.x+size.y, pos.y);
            Drawing.setRot(90);
        }
    }
    
    public void setPos(int x, int y){
        pos.x = x;
        pos.y = y;
    }
    public void draw(){
        selectedCheck();
        try {
            methodDrawPhantom.invoke(null, pos, rotated);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawChildren();
    }
    
    abstract public void updatePageInteractions();
    
    public String getType(){
        return type;
    }
}
