package rzaeditor.pageobjects;

import java.awt.Color;
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
import rzaeditor.DrawModeObject;
import static rzaeditor.DrawModeObject.objectClass;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.Logic;
import static rzaeditor.Logic.zoomGridGap;
import rzaeditor.Page;

public abstract class PageObjectComplex extends PageObjectBase {
    
    public static final boolean canSwitchDirection = false;
    public Direction direction = Direction.LEFT;
    public HashSet<WireIntersection> wireIntersections = new HashSet<>();
    
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
            if(dir!=Direction.LEFT && dir!=Direction.RIGHT)
                direction=Direction.LEFT;
        }
        try {
            methodDrawPhantom = getClass().getMethod("drawPhantom", Vector2i.class);
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
    
    @Override
    public String getType() {
        return "Сложный объект";
    }

    public void drawChildren(){
        //children.forEach((t) -> {
        //   t.draw();
        //});
        wireIntersections.forEach((t) -> {
            t.draw();
        });
    }
    
    public static void callRotateCheck(Class c, Vector2i p, Direction dir){
        try {
            objectClass.getMethod("rotateCheck", Vector2i.class, Vector2i.class, Direction.class)
                    .invoke(null, p, objectClass.getField("defaultSize").get(Vector2i.class), dir);
            objectClass.getMethod("drawPhantom", Vector2i.class).invoke(null, Cursor.posGrid);
        } catch (NoSuchMethodException | SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void rotateCheck(Vector2i pos, Vector2i size, Direction dir){
        if(dir==Direction.LEFT){
            Drawing.setTranslateGrid(pos.x, pos.y);
        }
        if(dir==Direction.UP){
            Drawing.setTranslateGrid(pos.x+size.y, pos.y);
            Drawing.setRot(90);
        }
        if(dir==Direction.RIGHT){
            Drawing.setTranslateGrid(pos.x+size.y, pos.y);
            Drawing.setRot(90);
            Drawing.setScale(0, -1);
        }
        if(dir==Direction.DOWN){
            Drawing.setTranslateGrid(pos.x+size.y, pos.y);
            Drawing.setRot(90);
            Drawing.setScale(0, -1);
        }
    }
    
    public void setPos(int x, int y){
        pos.x = x;
        pos.y = y;
    }
    
    public void drawLabels(){
        Drawing.setFontSize(7 * Logic.zoom);
        drawIDLabel();
        Drawing.setFontSize(5 * Logic.zoom);
        drawContactLabels();
    }
    
    public void drawIDLabel(){
        Drawing.drawString(id, Logic.gridToScreen(getSize().x)/2-Drawing.getStringWidth(String.valueOf(id))/2, Logic.posToScreen(-3));
    }
    
    public void drawContactLabels(){
        
    }
    
    public void draw(){
        selectedCheck();
        PageObjectComplex.rotateCheck(pos, getSize(), direction);
        try {
            methodDrawPhantom.invoke(null, pos);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(PageObjectComplex.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        drawLabels();
        if(selected)
            drawConnections();
        drawChildren();
    }
    
    public Vector2i getCenterPos(){
        return new Vector2i(Math.round(pos.x*Logic.zoomGridGap+getSize().x*Logic.zoomGridGap/2), Math.round(pos.y*Logic.zoomGridGap+getSize().y*Logic.zoomGridGap/2));
    }
    
    public void drawConnection(PageObjectComplex c){
        if(c==null)return;
        Drawing.drawLine(getCenterPos(),c.getCenterPos());
    }
    
    public void drawConnections(){
        Drawing.setTranslate(Page.current.pos.x, Page.current.pos.y);
        Drawing.setStroke(1);
        Drawing.setColor(Color.red);
    }

    @Override
    public void onSelect() {
        super.onSelect();
        InfoTable.addLine("ID", id, null);
    }
}
