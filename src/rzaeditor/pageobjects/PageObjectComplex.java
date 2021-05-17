package rzaeditor.pageobjects;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.drawmodes.DrawModeObject;
import static rzaeditor.drawmodes.DrawModeObject.objectClass;
import rzaeditor.Drawing;
import rzaeditor.Help;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;
import static rzaeditor.pageobjects.PageObjectComplex.Direction.*;

public abstract class PageObjectComplex extends PageObjectBase {
    
    public static final boolean canSwitchDirection = false;
    public Direction direction = Direction.LEFT;
    protected Method methodDrawDefWILines;
    public HashSet<WireIntersection> wireIntersections = new HashSet<>();
    public static final int defaultWireIntersectOffset = 1;
    public static final String defaultIDru = "";
    public static final String defaultIDen = "";
    public static Vector2i defaultSize = new Vector2i(3,1);
    public static final String defaultType = "Объект сложный";
    public static final String defaultContactId0 = "";
    public static final String defaultContactId1 = "";
    public WireIntersection leftWI = null;
    public WireIntersection rightWI = null;
    
    public static enum Direction{
        LEFT,
        UP,
        RIGHT,
        DOWN;
        
        public boolean isHorizontal(){
            return !isVertical();
        }
        
        public boolean isVertical(){
            if(this==UP || this==DOWN)
                return true;
            
            return false;
        }
    }
    
    public String id = "?";
    
    public PageObjectComplex(Vector2i pos, Direction dir) {
        super(pos);
        direction=dir;
        if(!(boolean)getFieldValue("canSwitchDirection")){
            if(dir!=LEFT && dir!=UP)
                direction=LEFT;
        }
        
        methodDrawPhantom = getMethod("drawPhantom", Vector2i.class);
        methodDrawDefWILines = getMethod("drawDefaultWireIntersectLines", Class.class, Direction.class);
        size = rotateDefaultSize(getClass(), dir);
        
        addDefaultWireIntersects();
        setDefaultID();
    }
    
    public static Vector2i getDefaultSize(Class<PageObjectComplex> c){
        return (Vector2i) Help.getFieldValue(c, "defaultSize", null);
    }
    
    public static boolean canPutHere(Class<PageObjectComplex> c, Vector2i p, Direction dir){
        Vector2i s = rotateDefaultSize(c, dir);
        Rectanglei r = new Rectanglei(p, new Vector2i(p).add(s));
        return !Page.current.objects.stream().filter((t) -> {
            return !(t instanceof Wire); //To change body of generated lambdas, choose Tools | Templates.
        }).anyMatch((t) -> {
            return t.isRectInside(r);
        });
    }
    
    public static boolean canPutHereIgnore(Class<PageObjectComplex> c, Vector2i p, Direction dir, HashSet<PageObjectBase> ignoreList){
        Vector2i s = rotateDefaultSize(c, dir);
        Rectanglei r = new Rectanglei(p, new Vector2i(p).add(s));
        return !Page.current.objects.stream().filter((t) -> {
            return !ignoreList.contains(t);
        }).filter((t) -> {
            return !(t instanceof Wire);
        }).anyMatch((t) -> {
            return t.isRectInside(r);
        });
    }
    
    public void setDefaultID(){
        id = (String) getFieldValue("defaultIDru");
    }
    
    public void addDefaultWireIntersects(){
            int offset = (int) getFieldValue("defaultWireIntersectOffset");
            String s0 = (String) getFieldValue("defaultContactId0");
            String s1 = (String) getFieldValue("defaultContactId1");
            Vector2i s = rotateDefaultSize(this.getClass(), direction);
            Vector2i offsetv = new Vector2i();
            if(direction.isHorizontal()){
                offsetv.y = offset;
            }
            else if(direction == Direction.UP){
                offsetv.x = s.x - offset;
            }
            else if(direction == Direction.DOWN){
                offsetv.x = offset;
            }
            Vector2i offset2 = new Vector2i(s.x,0);
            if(direction.isVertical())
                offset2.set(0, s.y);
            leftWI = WireIntersection.getWI(offsetv.x,offsetv.y, this); 
            rightWI = WireIntersection.getWI(offsetv.x+offset2.x,offsetv.y+offset2.y, this); 
            leftWI.addWireless(rightWI);
            wireIntersections.add(leftWI);
            wireIntersections.add(rightWI);
            leftWI.textBelow = s0;
            rightWI.textBelow = s1;
            Wire.checkAllWires();
    }
    
    public static Vector2i rotateDefaultSize(Class c, Direction dir){
        return Logic.swapIfTrue(getDefaultSize(c), dir.isVertical());
    }
    
    @Override
    public void moveTo(Vector2i p){
        super.moveTo(p);
        /*
        HashSet s = new HashSet<PageObjectBase>();
        s.add(this);
        if(canPutHereIgnore((Class<PageObjectComplex>) getClass(), p, direction, s)){
            this.pos = p;
        }
        */
        leftWI.moveTo(new Vector2i(p).add(new Vector2i(leftWI.pos).sub(pos)));
        rightWI.moveTo(new Vector2i(p).add(new Vector2i(rightWI.pos).sub(pos)));
        //deleteDefaultWireIntersects();
        //addDefaultWireIntersects();
    }
    
    public void drawChildren(){
    }
    
    public static void callRotateCheck(Class c, Vector2i p, Direction dir){
        Help.invokeMethodF(c, "rotateCheck", p, rotateDefaultSize(c, dir), dir);
        Help.invokeMethodF(c, "drawPhantom", p);
        Help.invokeMethodF(c, "drawDefaultWireIntersectLines", c, dir);
    }
    
    public static void rotateCheck(Vector2i pos, Vector2i size, Direction dir){
        //System.out.println(pos.x+" "+pos.y+" "+dir+" "+size.x+" "+size.y);
        if(dir==Direction.LEFT){
            Drawing.setTranslateGrid(pos.x, pos.y);
        }
        else if(dir==Direction.UP){
            Drawing.setTranslateGrid(pos.x+size.x, pos.y);
            Drawing.setRot(90);
        }
        else if(dir==Direction.RIGHT){
            Drawing.setTranslateGrid(pos.x+size.x, pos.y+size.y);
            Drawing.setRot(180);
        }
        else if(dir==Direction.DOWN){
            Drawing.setTranslateGrid(pos.x, pos.y+size.y);
            Drawing.setRot(270);
        }
    }
    
    public void drawLabels(){
        Drawing.setFontSizeZoom(7);
        drawIDLabel();
    }
    
    public void drawIDLabel(){
        Drawing.drawStringZoomCentered(id, Logic.gridToScreen(size.x)/2, -6);
        //Drawing.drawString(id, Logic.gridToScreen(size.x)/2-Drawing.getStringWidth(String.valueOf(id))/2, Logic.posToScreen(-3));
    }
    
    public void setContactId0(String s){
        if(leftWI!=null)
            leftWI.textBelow = s;
    }
    
    public void setContactId1(String s){
        if(rightWI!=null)
            rightWI.textBelow = s;
    }
    
    public void deleteDefaultWireIntersects(){
        leftWI.wireless.remove(rightWI);
        rightWI.wireless.remove(leftWI);
        wireIntersections.forEach((t) -> {
            t.delete();
        });
    }
    
    @Override
    public void delete() {
        super.delete();
        deleteDefaultWireIntersects();
    }
    
    public static PageObjectComplex read(Class c, String[] args){
        if(args.length!=3){
            throw new IllegalArgumentException();
        }
        PageObjectComplex obj =null;
        try {
            Constructor constr = c.getConstructor(Vector2i.class, Direction.class);
            obj = (PageObjectComplex) constr.newInstance(new Vector2i(Integer.valueOf(args[0]), Integer.valueOf(args[1])), Direction.valueOf(args[2]));
            Page.current.objects.add( obj);
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        return obj;
    }
    
    public void draw(){
        super.draw();
        PageObjectComplex.rotateCheck(pos, size, direction);
        Help.invokeMethodF(methodDrawPhantom, pos);
        Help.invokeMethodF(methodDrawDefWILines, this.getClass(), direction);
        drawLabels();
        if(selected)
            drawConnections();
        drawChildren();
    }
    
    public Vector2i getCenterPos(){
        return new Vector2i(Math.round(pos.x*Logic.zoomGridGap+size.x*Logic.zoomGridGap/2), Math.round(pos.y*Logic.zoomGridGap+size.y*Logic.zoomGridGap/2));
    }
    
    public static void drawDefaultWireIntersectLines(Class c, Direction dir){
        Vector2i s = rotateDefaultSize(c, Direction.LEFT);
        int o = (int) Help.getFieldValue(c, "defaultWireIntersectOffset");
        Drawing.drawLineGrid(0,o,1,o);
        Drawing.drawLineGrid(s.x-1,o,s.x,o);
    }
    
    public void drawConnection(PageObjectComplex c){
        if(c==null)return;
        Drawing.drawLine(getCenterPos(),c.getCenterPos());
    }
    
    public void drawConnections(){
        Drawing.setTranslate(Page.current.pos.x, Page.current.pos.y);
        Drawing.setStrokeSize(1);
        Drawing.setColor(Color.red);
    }

    @Override
    public void onSelect() {
        super.onSelect();
        InfoTable.addLineText("ID", this, "id");
    }
}
