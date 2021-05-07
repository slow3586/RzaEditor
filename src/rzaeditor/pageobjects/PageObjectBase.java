package rzaeditor.pageobjects;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import rzaeditor.Page;

public abstract class PageObjectBase {
    
    Vector2i pos = new Vector2i();
    public boolean selected = false;
    public boolean hovered = false;
    protected Method methodDrawPhantom;
    public String id = "?";
    public String name = "";
    private Vector2i size = new Vector2i(0,0);
    public boolean visible = true;
    
    public PageObjectBase(Vector2i p) {
        pos = new Vector2i(p);
        name = getType()+" №"+(getCountInPage()+1);
    }
    
    public String getType(){
        return "Объект";
    }
    
    public long getCountInPage(){
        return Page.current.objects.stream().filter((t) -> {
            return t.getClass().equals(this.getClass());
        }).count(); 
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
        return new Rectanglei(pos.x, pos.y, pos.x+getSize().x, pos.y+getSize().y);
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
    
    public void onSelect(){
        InfoTable.addLine("Имя", name, null);
        InfoTable.addLine("Тип", getType(), null);
        InfoTable.addLine("ID", id, null);
    }
}
