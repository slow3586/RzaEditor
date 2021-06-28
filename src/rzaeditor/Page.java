package rzaeditor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import rzaeditor.pageobjects.PageObjectComplex;
import rzaeditor.pageobjects.Wire;
import rzaeditor.pageobjects.WireIntersection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.PageObjectComplex.Direction;
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
    
    public static void openold(File f){
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
    
    public static void open(File f){
        current = newCircuitA3Page();
        
        try {
            String s = Help.listtostr(Help.readfile(f.toPath()));
            Pattern p0 = Pattern.compile("(class=.*?)(?=\\nclass|\\z)", Pattern.DOTALL);
            ArrayList<String> s0 = Help.findall(p0, s, 1);
            HashMap<Integer, HashMap<String, String>> paramList = new HashMap<>();
            for (String s1 : s0) {
                HashMap<String, String> params = new HashMap<>();
                String[] s2 = s1.split("\n");
                for (String s3 : s2) {
                    String[] s4 = s3.split("=");
                    if(s4.length==2)
                        params.put(s4[0], s4[1]);
                    else{
                        for (String s5 : s4) {
                            System.out.println(s5);
                        }
                        throw new IllegalArgumentException("s4 length not 2, is "+s4.length);
                    }
                }
                paramList.put(Integer.valueOf(params.get("internalId")), params);

                Class c = Class.forName(params.get("class"));
                Constructor con = null;
                PageObjectBase o = null;
                Vector2i pos = Help.fromStr(params.get("pos"));
                if(c==WireIntersection.class){
                    o = WireIntersection.getWI(pos);
                }
                else if(c==Wire.class){
                    o = Wire.create(pos, new Vector2i(pos).add(Help.fromStr(params.get("size"))));
                }
                else if(PageObjectComplex.class.isAssignableFrom(c)){
                    con = c.getConstructor(Vector2i.class, PageObjectComplex.Direction.class);
                    o = (PageObjectBase) con.newInstance(pos, Direction.valueOf(params.get("direction")));
                }
                else if(PageObjectBase.class.isAssignableFrom(c)){
                    con = c.getConstructor(Vector2i.class);
                    o = (PageObjectBase) con.newInstance(pos);
                }else{
                    throw new IllegalArgumentException("Unknown class "+c.toString());
                }
                if(o==null) continue;
                o.internalId =Integer.parseInt(params.get("internalId"));
            }
            
            Page.current.objects.forEach((o) -> {
                HashMap<String, String> p = paramList.get(o.internalId);
                Class c = o.getClass();
                if(p==null){
                    System.out.println("id:"+o.internalId+" class:"+c.toString());
                    throw new IllegalArgumentException();
                }
                Class c0 = Help.forName(p.get("class"));
                if(!c.equals(c0)){
                    System.out.println(c.toString()+" vs "+c0.toString());
                    System.out.println("id:"+o.internalId+" id:"+Page.current.objects.stream().filter((t1) -> {
                        return t1.internalId==o.internalId;
                    }).findFirst());
                    throw new IllegalArgumentException();
                }
                p.forEach((t, u) -> {
                    if(t.equals("class"))return;
                    Field fd = Help.getField(c, t);
                    Class<?> fc = fd.getType();
                    if(fc==String.class)
                        Help.setFieldValue(c, t, o, u);
                    else if(fc==int.class)
                        Help.setFieldValue(c, t, o, Integer.valueOf(u));
                    else if(fc==int.class){
                        Help.setFieldValue(c, t, o, Help.fromStr(u));
                    }
                    else if(fc.isAssignableFrom(PageObjectComplex.class)){

                    }
                    else if(fc.isAssignableFrom(PageObjectBase.class)){

                    }
                });
            });
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
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
        try {
            Files.deleteIfExists(file.toPath());
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
