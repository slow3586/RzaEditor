package rzaeditor;

import rzaeditor.drawmodes.DrawMode;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import org.joml.Vector2f;
import org.joml.Vector2i;
import rzaeditor.pageobjects.primitives.Primitive;

public class Drawing {
    
    public static Graphics2D g;
    private static Vector2i translate = new Vector2i();
    public static LineType lineType = LineType.SOLID;
    public static Line2D line2d = new Line2D.Float();
    public static Ellipse2D oval2d = new Ellipse2D.Float();
    public static Rectangle2D rect2d = new Rectangle2D.Float();
    public static Arc2D arc2d = new Arc2D.Float();
    
    public enum LineType{
        SOLID,
        DOT,
        DASH
    }
    
    public static void drawLine(Vector2i s, Vector2i e) {
        drawLine(s.x, s.y, e.x, e.y);
    }
    
    public static void drawLine(float x, float y, float x1, float y1) {
        line2d.setLine(x, y, x1, y1);
        g.draw(line2d);
    }
    
    public static void drawLineZoom(float x, float y, float x1, float y1) {
        drawLine(x*Logic.zoom, y*Logic.zoom, x1*Logic.zoom, y1*Logic.zoom);
    }
    
    public static void drawLineGrid(int x, int y, int x1, int y1) {
        drawLine(x*Logic.zoomGridGap, y*Logic.zoomGridGap, 
                x1*Logic.zoomGridGap, y1*Logic.zoomGridGap);
    }
    
    public static void setScale(float x, float y){
        g.scale(x, y);
    }
    
    public static void drawLineGrid(Vector2i s, Vector2i e) {
        drawLineGrid(s.x, s.y, e.x, e.y);
    }
    
    public static void drawOvalZoom(float x, float y, float x1, float y1) {
        drawOval(x*Logic.zoom, y*Logic.zoom, x1*Logic.zoom, y1*Logic.zoom);
    }

    public static void drawOval(float x, float y, float x1, float y1) {
        oval2d.setFrame(x, y, x1, y1);
        g.draw(oval2d);
    }
    
    public static void fillOvalZoom(float x, float y, float x1, float y1) {
        fillOval(x*Logic.zoom, y*Logic.zoom, x1*Logic.zoom, y1*Logic.zoom);
    }
    
    public static void fillDrawOvalZoom(float x, float y, float x1, float y1) {
        Color c = g.getColor();
        Drawing.setColor(Color.white);
        fillOvalZoom(x,y,x1,y1);
        Drawing.setColor(c);
        drawOvalZoom(x,y,x1,y1);
    }
    
    public static void fillOvalGridCenter(float x, float y, float x1, float y1) {
        fillOval(x*Logic.zoomGridGap-x1/2*Logic.zoom, y*Logic.zoomGridGap-y1/2*Logic.zoom, x1*Logic.zoom, y1*Logic.zoom);
    }

    public static void fillOval(float x, float y, float x1, float y1) {
        oval2d.setFrame(x, y, x1, y1);
        g.fill(oval2d);
    }

    public static void drawRect(Vector2i s, Vector2i e) {
        drawRect(s.x, s.y, e.x, e.y);
    }
    
    public static void drawRectGrid(int x, int y, int w, int h) {
        drawRect(x*Logic.zoomGridGap, y*Logic.zoomGridGap,w*Logic.zoomGridGap, h*Logic.zoomGridGap);
    }
    
    public static void drawRectZoom(float x, float y, float w, float h) {
        drawRect(x*Logic.zoom, y*Logic.zoom, w*Logic.zoom, h*Logic.zoom);
    }
    
    public static void drawRect(float x, float y, float w, float h) {
        rect2d.setFrame(x, y, w, h);
        g.draw(rect2d);
    }
    
    public static void drawArc(Vector2i p, Vector2i s, float startAngle, float endAngle) {
        drawArc(p.x, p.y, s.x, s.y, startAngle, endAngle);
    }
    
    public static void drawArcZoom(float x, float y, float w, float h, float startAngle, float endAngle) {
        drawArc(x*Logic.zoom, y*Logic.zoom,
                w*Logic.zoom, h*Logic.zoom, startAngle, endAngle);
    }
    
    public static void drawArcGrid(float x, float y, float w, float h, float startAngle, float endAngle) {
        drawArc(x*Logic.zoomGridGap, y*Logic.zoomGridGap,
                w*Logic.zoomGridGap, h*Logic.zoomGridGap, startAngle, endAngle);
    }
    
    public static void drawArc(float x, float y, float w, float h, float startAngle, float endAngle) {
        arc2d.setArc(x, y, w, h, startAngle, endAngle, 1);
        g.draw(arc2d);
    }
    
    public static void setFont(Font font){
        g.setFont(font);
    }
    
    public static void setFontSize(float s){
        g.setFont(g.getFont().deriveFont(s));
    }
    
    public static void setFontSizeZoom(float s){
        setFontSize(s * Logic.zoom);
    }
    
    public static int getStringWidth(String s){
        return g.getFontMetrics(g.getFont()).stringWidth(s);
    }
    
    public static Vector2i getStringHeight(String s){
        FontRenderContext frc = g.getFontRenderContext();
        int textwidth = getStringWidth(s);
        int textheight = (int)(g.getFont().getStringBounds(s, frc).getHeight());
        return new Vector2i(textwidth, textheight);
    }
    
    public static void drawString(String str, float x, float y) {
        g.drawString(str, x, y);
    }
    
    public static void drawStringZoom(String str, float x, float y) {
        drawString(str, x*Logic.zoom, y*Logic.zoom);
    }
    
    public static void drawStringZoomCentered(String str, float x, float y) {
        Vector2i s = getStringHeight(str);
        drawString(str, x-s.x/2, y*Logic.zoom+s.y/3);
    }
    
    public static void setStrokeSize(float s){
        BasicStroke os = (BasicStroke) g.getStroke();
        g.setStroke(new BasicStroke(s, os.getEndCap(), os.getLineJoin(), os.getMiterLimit(), os.getDashArray(), os.getDashPhase()));
    }
    
    public static void setLineType(LineType t){
        lineType=t;
        BasicStroke os = (BasicStroke) g.getStroke();
        if(t==LineType.SOLID){
            g.setStroke(new BasicStroke(os.getLineWidth(), os.getEndCap(), os.getLineJoin(), os.getMiterLimit(), new float[]{1}, 1f));
        }
        if(t==LineType.DASH){
            g.setStroke(new BasicStroke(os.getLineWidth(), os.getEndCap(), os.getLineJoin(), os.getMiterLimit(), new float[]{15,15}, 2f));
        }
        if(t==LineType.DOT){
            g.setStroke(new BasicStroke(os.getLineWidth(), os.getEndCap(), os.getLineJoin(), os.getMiterLimit(), new float[]{1,0,1}, 0.5f));
        }
    }
    
    public static void setColor(Color c){
        g.setColor(c);
    }
    
    public static void resetTransform(){
        g.setTransform(new AffineTransform());
    }
    
    public static void setTranslatePagePos(){
        setTranslate(Page.current.pos);
    }
    
    public static void setTranslate(Vector2i v){
        setTranslate(v.x, v.y);
    }
    
    public static void setTranslate(int x, int y){
        g.setTransform(AffineTransform.getTranslateInstance(x, y));
    }
    
    public static void setTranslateGrid(Vector2i p){
        setTranslateGrid(p.x, p.y);
    }
    
    public static void setTranslateGrid(int x, int y){
        g.setTransform(AffineTransform.getTranslateInstance(Page.current.pos.x+x*Logic.zoomGridGap, Page.current.pos.y+y*Logic.zoomGridGap));
    }
    
    public static void setRot(float angle){
        g.rotate(Math.toRadians(angle));
    }
    
    public static float getStrokeWidth(){
        return ((BasicStroke)Drawing.g.getStroke()).getLineWidth();
    }
    
    public static void drawWire(){
        Vector2i off0 = new Vector2i(Page.current.pos);
        
        resetTransform();
        
        int every = 6;
        Color c0 = new Color(0.8f,0.8f,0.8f);
        Color c1 = new Color(0.6f,0.6f,0.6f);
        Color cur = c1;
        float off1 = 0;
        for (int i = 1; i<=Math.max(Page.current.gridSize.x, Page.current.gridSize.y); i++){
            off1 = i * Page.gridGap * Logic.zoom;
            cur = c0;
            if(i%every==0){
                cur = c1;
            }
            setColor(cur);
            if(i<=Page.current.gridSize.y)
                drawLine(off0.x, off0.y + off1, off0.x + (Page.current.size.x * Logic.zoom), off0.y + off1);
            if(i<=Page.current.gridSize.x)
                drawLine(off0.x + off1, off0.y, off0.x + off1, off0.y + (Page.current.size.y * Logic.zoom));
        }
        
        setColor(new Color(1,1,1));
        setStrokeSize(1);
        drawRect(off0.x, off0.y, Page.current.size.x * Logic.zoom, Page.current.size.y * Logic.zoom);
    }

    public static void drawEditPanel(Graphics gr) {
        g = (Graphics2D) gr;
        EditPanel ep = EditPanel.imp;
        
        drawWire();
        
        Cursor.draw();
        
        Drawing.setColor(Color.BLACK);
        
        DrawMode.getCurrent().draw();
        
        Drawing.setColor(Color.BLACK);
        
        Drawing.setStrokeSize(1 * Logic.zoom);
        
        Page.current.objects.stream().sorted((o1, o2) -> {
            if(o2 instanceof WireIntersection){
                return -100;
            }else{
                return 100;
            }
        }).forEach((t) -> {
            t.draw();
        });
        
        
        Drawing.resetTransform();
    }

}
