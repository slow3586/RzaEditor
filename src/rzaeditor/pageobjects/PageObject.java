package rzaeditor.pageobjects;

import java.util.HashSet;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglef;
import org.joml.primitives.Rectanglei;
import rzaeditor.Drawing;
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
    
    public void setPos(int x, int y){
        pos.x = x;
        pos.y = y;
    }
    abstract public void draw();
    abstract public void updatePageInteractions();
   // abstract boolean canBeMovedTo();
    
    public String getType(){
        return type;
    }
}
