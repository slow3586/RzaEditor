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
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;
import static rzaeditor.pageobjects.Relay.size;

public abstract class PageObject {
    Vector2i pos = new Vector2i();
    String ID = "";
    String name = "";
    protected String type = "";
    public boolean selected = false;
    public PageObject parent = null;
    public boolean rotated = false;
    //public HashSet<PageObject> children = new HashSet<>();
    public HashSet<WireIntersection> wireIntersections = new HashSet<>();
    private Method methodDrawPhantom;
    private Vector2i __size;
    
    public PageObject(Vector2i p, boolean rot) {
        try {
            methodDrawPhantom = getClass().getMethod("drawPhantom", Vector2i.class, boolean.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(PageObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Field f = getClass().getField("size");
            //if(f.get)
            __size = (Vector2i) f.get(Vector2i.class);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PageObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pos = new Vector2i(p);
        rotated = rot;
        name = "Объект "+Page.current.wires.size();
        ID = "Объект "+Page.current.wires.size();
        type = "Объект";
    }
    
    public static Vector2i toGlobal(PageObject o, int x, int y){
        return new Vector2i(o.pos.x+x, o.pos.y+y);
    }
    
    public static Vector2i toGlobal(PageObject o, Vector2i loc){
        return new Vector2i(o.pos.x+loc.x, o.pos.y+loc.y);
    }
    
    final public Vector2i toGlobal(int x, int y){
        return new Vector2i(pos.x+x, pos.y+y);
    }
    
    public Vector2i toGlobal(Vector2i loc){
        return new Vector2i(pos.x+loc.x, pos.y+loc.y);
    }
    
    public void drawChildren(){
        //children.forEach((t) -> {
        //   t.draw();
        //});
        wireIntersections.forEach((t) -> {
            t.draw();
        });
    }
    
    public Vector2i getSize(){
        return __size;
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
        try {
            methodDrawPhantom.invoke(null, pos, rotated);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawChildren();
    }
    abstract public void updatePageInteractions();
    
    public String getType(){
        return type;
    }
}
