package rzaeditor.pageobjects;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.Drawing;
import rzaeditor.Logic;
import rzaeditor.Page;

public abstract class PageObjectBase {
    Vector2i pos = new Vector2i();
    public boolean selected = false;
    public boolean hovered = false;
    protected Method methodDrawPhantom;
    String ID = "";
    String name = "";
    protected String type = "";
    private Vector2i size = new Vector2i(1,1);
    
    public PageObjectBase(Vector2i p) {
        pos = new Vector2i(p);
        
        name = "Объект "+Page.current.wires.size();
        ID = "Объект "+Page.current.wires.size();
        type = "Объект";
    }
    
    public void selectedCheck(){
        if(selected)
            Drawing.setColor(Color.red);
        else if (hovered)
            Drawing.setColor(Color.blue);
        else
            Drawing.setColor(Color.black);
    }
    
    public void draw(){
        
    }
    
    public Rectanglei getRect(){
        return new Rectanglei(pos.x, pos.y, pos.x+size.x, pos.y+size.y);
    }
    
    public boolean isRectTouching(Rectanglei r){
        return getRect().intersectsRectangle(r);
    }
    
    public boolean isVecTouching(Vector2i vec){
        Rectanglei r = getRect();
        r.minX-=1;
        r.minY-=1;
        r.maxX+=1;
        r.maxY+=1;
        return r.containsPoint(vec);
    }
    
    public void setSize(Vector2i s){
        size = s;
    }
    
    public Vector2i getSize(){
        return size;
    }
    
    final public Vector2i toGlobal(int x, int y){
        return new Vector2i(pos.x+x, pos.y+y);
    }
    
    public Vector2i toGlobal(Vector2i loc){
        return new Vector2i(pos.x+loc.x, pos.y+loc.y);
    }
}
