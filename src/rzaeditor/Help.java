package rzaeditor;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joml.Vector2i;
import rzaeditor.pageobjects.PageObjectComplex;

public class Help {
    public static Field getField(Class c, String f){
        try {
            return c.getField(f);
        } catch (NoSuchFieldException | SecurityException ex) {
            throw new IllegalArgumentException(f+" field not found");
        }
    }
    
    public static Class forName(String c){
        try {
            return Class.forName(c);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Method getMethod(Class c, String m, Class<?>... params){
        try {
            return c.getMethod(m, params);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new IllegalArgumentException(m+" method not found");
        }
    }
    
    public static Vector2i fromStr(String s){
        String[] coords = s.split(",");
        return new Vector2i(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]));
    }
    
    public static void invokeMethodF(Class c, String m, Object... args){
        ArrayList<Class> a = new ArrayList<>();
        for (Object arg : args) {
            a.add(arg.getClass());
        }
        Class[] ca = new Class[a.size()];
        for (int i = 0; i < a.size(); i++) {
            Class get = a.get(i);
            ca[i] = get;
        }
        try {
            Help.getMethod(c, m, ca).invoke(null, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void invokeMethod(Class c, String m, Object o, Object... args){
        ArrayList<Class> a = new ArrayList<>();
        for (Object arg : args) {
            a.add(arg.getClass());
        }
        invokeMethod(Help.getMethod(c, m, (Class<?>[]) a.toArray()), o, args);
    }
    
     public static void invokeMethodF(Method m, Object... args){
        try {
            m.invoke(null, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void invokeMethod(Method m, Object o, Object... args){
        try {
            m.invoke(o, args);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean hasField(Class c, String f){
        Field[] fields = c.getFields();
        for (Field field : fields) {
            if(field.getName().equals(f))
                return true;
        }
        return false;
    }
    
    public static Object getFieldValue(Class c, String f){
        try {
            return getField(c,f).get(null);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object getFieldValue(Class c, String f, Object o){
        try {
            return getField(c,f).get(o);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object getFieldValue(Field f, Object o){
        try {
            return f.get(o);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static void setFieldValue(Class c, String f, Object fieldOf, Object fieldVal){
        try {
            Field field = getField(c, f);
            field.set(fieldOf, fieldVal);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Help.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String findfirst(Pattern p, String s, int i) {
        Matcher m = p.matcher(s);
        String o = null;
        if (m.find()) {
            o = m.group(i);
        }
        return o;
    }

    public static ArrayList<String> findall(Pattern p, String s, int i) {
        Matcher m = p.matcher(s);
        ArrayList<String> o = new ArrayList<>();
        while (m.find()) {
            o.add(m.group(i));
        }
        return o;
    }
    
    public static ArrayList<ArrayList<String>> findall(Pattern p, String s, int[] i) {
        Matcher m = p.matcher(s);
        ArrayList<ArrayList<String>> o = new ArrayList<>();
        while (m.find()) {
            ArrayList<String> a = new ArrayList<>();
            for (int j = 0; j < i.length; j++) {
                a.add(m.group(i[j]));
            }
            o.add(a);
        }
        return o;
    }
    
    public static String listtostr(List<String> l) {
        StringBuilder r = new StringBuilder();
        for (String line : l) {
            r.append(line);
            r.append("\n");
        }
        return r.toString();
    }

    public static List<String> readfile(Path f) {
        List<String> l = new ArrayList<>();
        try {
            l = Files.readAllLines(f);
        } catch (IOException ex) {
            
        }
        return l;
    }
}
