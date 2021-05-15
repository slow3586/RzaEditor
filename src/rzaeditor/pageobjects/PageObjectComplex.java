package rzaeditor.pageobjects;

import java.awt.Color;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.Cursor;
import rzaeditor.drawmodes.DrawModeObject;
import static rzaeditor.drawmodes.DrawModeObject.objectClass;
import rzaeditor.Drawing;
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
        DOWN
    }
    
    public String id = "?";
    
    public PageObjectComplex(Vector2i pos, Direction dir) {
        super(pos);
        direction=dir;
        if(!canSwitchDirection){
            if(dir!=LEFT && dir!=UP)
                direction=LEFT;
        }
        try {
            methodDrawPhantom = getClass().getMethod("drawPhantom", Vector2i.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            System.err.println("drawPhantom method not found in class "+getClass().getName()+"!!!");
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            methodDrawDefWILines = getClass().getMethod("drawDefaultWireIntersectLines", Class.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            System.err.println("drawPhantom method not found in class "+getClass().getName()+"!!!");
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            Field f = getClass().getField("defaultSize");
            size = Logic.swapIfTrue((Vector2i) f.get(Vector2i.class),dir==UP||dir==DOWN);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            System.err.println("defaultSize field not found in class "+getClass().getName()+"!!!");
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        addDefaultWireIntersects();
        setDefaultID();
    }
    
    public static boolean canPutHere(Class<PageObjectComplex> c, Vector2i p, Direction dir){
        try {
            Field f =  c.getField("defaultSize");
            Vector2i s = Logic.swapIfTrue((Vector2i) f.get(Vector2i.class),dir==UP||dir==DOWN);
            Rectanglei r = new Rectanglei(p, new Vector2i(p).add(s));
            return !Page.current.objects.stream().filter((t) -> {
                return !(t instanceof Wire); //To change body of generated lambdas, choose Tools | Templates.
            }).anyMatch((t) -> {
                System.out.println(t.isRectInside(r));
                return t.isRectInside(r);
            });
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchFieldException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public void setDefaultID(){
        String nid = "";
        try {
            nid = (String) this.getClass().getField("defaultIDru").get(null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        id = nid;
    }
    
    public void addDefaultWireIntersects(){
        try {
            int offset = this.getClass().getField("defaultWireIntersectOffset").getInt(null);
            String s0 = (String) this.getClass().getField("defaultContactId0").get(null);
            String s1 = (String) this.getClass().getField("defaultContactId1").get(null);
            leftWI = WireIntersection.getWI(0,offset,this); 
            rightWI = WireIntersection.getWI(size.x,offset,this); 
            leftWI.addWireless(rightWI);
            wireIntersections.add(leftWI);
            wireIntersections.add(rightWI);
            leftWI.textBelow = s0;
            rightWI.textBelow = s1;
            Wire.checkAllWires();
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void drawChildren(){
    }
    
    public static void callRotateCheck(Class c, Vector2i p, Direction dir){
        try {
            objectClass.getMethod("rotateCheck", Vector2i.class, Vector2i.class, Direction.class)
                    .invoke(null, p, objectClass.getField("defaultSize").get(Vector2i.class), dir);
            objectClass.getMethod("drawPhantom", Vector2i.class).invoke(null, Cursor.posGrid);
            objectClass.getMethod("drawDefaultWireIntersectLines", Class.class).invoke(null, c);
        } catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void rotateCheck(Vector2i pos, Vector2i size, Direction dir){
        if(dir==Direction.LEFT){
            Drawing.setTranslateGrid(pos.x, pos.y);
        }
        if(dir==Direction.UP){
            Drawing.setTranslateGrid(pos.x+size.x, pos.y);
            Drawing.setRot(90);
        }
        if(dir==Direction.RIGHT){
            Drawing.setTranslateGrid(pos.x+size.x, pos.y);
            Drawing.setRot(90);
            Drawing.setScale(0, -1);
        }
        if(dir==Direction.DOWN){
            Drawing.setTranslateGrid(pos.x+size.x, pos.y);
            Drawing.setRot(90);
            Drawing.setScale(0, -1);
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
    
    @Override
    public void delete() {
        super.delete();
        leftWI.wireless.remove(rightWI);
        rightWI.wireless.remove(leftWI);
        wireIntersections.forEach((t) -> {
            t.delete();
        });
    }

    @Override
    public String save() {
        return super.save()+werw(direction);
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
        try {
            methodDrawPhantom.invoke(null, pos);
            methodDrawDefWILines.invoke(null, this.getClass());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        drawLabels();
        if(selected)
            drawConnections();
        drawChildren();
    }
    
    public Vector2i getCenterPos(){
        return new Vector2i(Math.round(pos.x*Logic.zoomGridGap+size.x*Logic.zoomGridGap/2), Math.round(pos.y*Logic.zoomGridGap+size.y*Logic.zoomGridGap/2));
    }
    
    public static void drawDefaultWireIntersectLines(Class c){
        try {
            Vector2i s = (Vector2i) c.getField("defaultSize").get(null);
            int o = (int) c.getField("defaultWireIntersectOffset").get(null);
            Drawing.drawLineGrid(0,o,1,o);
            Drawing.drawLineGrid(s.x-1,o,s.x,o);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
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
