package rzaeditor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.intersections.WireIntersection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.primitives.Primitive;

public class Page {
    
    //size of one cell in a diagram, related to relay size
    public static final int gridGap = 6;
    //public static float gridGapFine = 0.5f;
    public static Page current = newCircuitA3Page();
    
    public File file = new File("unnamed.txt");
    public Vector2i titleSize;
    public Vector2i sizeNoBorder;
    public HashSet<PageObjectBase> objects = new HashSet<>();
    public Vector2i pos= new Vector2i(0, 0);
    public Vector2i size;
    public Vector2i gridSize;
    public Rectanglei rect;
    public boolean hasUnsavedProgress = true;
    public float cmPerCell;
    
    
    public HashSet<PageObjectBase> getObjectsClass(Class c){
        HashSet<PageObjectBase> h = new HashSet<>();
        objects.parallelStream().filter((t) -> {
            return t.getClass().equals(c);
        }).forEach((t) -> {
            h.add(t);
        });
        return h;
    }
    
    public HashSet<Wire> getWires(){
        HashSet<Wire> h = new HashSet<>();
        objects.parallelStream().filter((t) -> {
            return t.getClass().equals(Wire.class);
        }).forEach((t) -> {
            h.add((Wire)t);
        });
        return h;
    }
    
    public HashSet<WireIntersection> getWireIntersections(){
        HashSet<WireIntersection> h = new HashSet<>();
        objects.parallelStream().filter((t) -> {
            return t.getClass().equals(WireIntersection.class);
        }).forEach((t) -> {
            h.add((WireIntersection)t);
        });
        return h;
    }
    
    private Page(){
        
    }
    
    public static void open(File f){
        try {
            List<String> readAllLines = Files.readAllLines(f.toPath());
            for (String l : readAllLines) {
                String[] sp = l.split("\t");
                String claz = sp[0];
                String[] args = new String[sp.length-1];
                for (int i = 1; i < sp.length; i++) {
                    args[i-1]=sp[i];
                }
                Class c = Class.forName(claz);
                Method r = c.getMethod("read", Class.class, String[].class);
                r.invoke(null, c, (String[]) args);
            }
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void save(){
        ArrayList<String> s = new ArrayList<>();
        objects.forEach((t) -> {
            String ss = t.save();
            if(ss!=null)
                s.add(ss);
        });
        System.out.println(s.toString());
        try {
            Files.write(file.toPath(), s);
        } catch (IOException ex) {
            Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
        }
        hasUnsavedProgress = false;
    }
    
    public static Page newCircuitA3Page(){
        Page p = new Page();
        p.titleSize = new Vector2i(185, 55);
        p.sizeNoBorder = new Vector2i(420, 297);
        p.size = new Vector2i(395, 287);
        p.gridSize = new Vector2i(p.size.x / p.gridGap, p.size.y / p.gridGap);
        p.rect = new Rectanglei(-1, -1, p.gridSize.x, p.gridSize.y);
        p.cmPerCell = 6;
        return p;
    }
}
