package rzaeditor.pageobjects;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    
    public Vector2i pos = new Vector2i();
    public boolean selected = false;
    public boolean hovered = false;
    protected Method methodDrawPhantom;
    public String name = "";
    public Vector2i size = new Vector2i(0,0);
    public boolean visible = true;
    public String type = "";
    
    public PageObjectBase(Vector2i p) {
        pos = new Vector2i(p);
        
        try {
            type = (String) getClass().getField("defaultType").get(null);
        } catch (SecurityException | IllegalArgumentException | NoSuchFieldException | IllegalAccessException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        name = type+" №"+(getCountInPage()+1);
    }
    
    public static boolean canPutHere(Class<PageObjectBase> c, Vector2i p, Vector2i s){
        Rectanglei r = new Rectanglei(p, new Vector2i(p).add(s));
        return Page.current.objects.stream().anyMatch((t) -> {
            return t.isRectInside(r);
        });
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
    
    public void dataUpdated(){
        InfoTable.reset();
    }
    
    public void draw(){
        Drawing.setLineType(Drawing.LineType.SOLID);
        selectedCheck();
    }
    
    public static String werw(Object... a){
        StringBuilder s = new StringBuilder();
        for (Object o : a) {
            s.append(o.toString());
            s.append("\t");
        }
        return s.toString();
    }
    
    public String save(){
        return werw(getClass().getName(), pos.x, pos.y);
    }
    
    public static PageObjectBase read(Class c, String[] args){
        PageObjectBase i =null;
        try {
            Constructor o = c.getConstructor(Vector2i.class);
            i = (PageObjectBase) o.newInstance(new Vector2i(Integer.valueOf(args[0]), Integer.valueOf(args[1])));
            Page.current.objects.add( i);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return i;
    }
    
    public void delete(){
        Page.current.objects.remove(this);
    }
    
    public Vector2i getCenterScreenCoords(){
        return new Vector2i(new Vector2i(pos).add(size).sub(new Vector2i(size).div(2)));
    }
    
    public Rectanglei getRect(){
        return new Rectanglei(pos.x, pos.y, pos.x+size.x, pos.y+size.y);
    }
    
    public boolean isRectInside(Rectanglei r1){
        Rectanglei r0 = getRect();
        return r0.minX < r1.maxX && r0.maxX > r1.minX &&
               r0.maxY > r1.minY && r0.minY < r1.maxY;
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
    
    final public Vector2i toGlobal(int x, int y){
        return new Vector2i(pos.x+x, pos.y+y);
    }
    
    public Vector2i toGlobal(Vector2i loc){
        return new Vector2i(pos.x+loc.x, pos.y+loc.y);
    }
    
    public void onSelect(){
        InfoTable.addLineText("Имя", this, "name");
        InfoTable.addLineText("Тип", this, "type");
    }
}
