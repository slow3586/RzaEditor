package rzaeditor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import rzaeditor.pageobjects.PageObjectComplex;

public class Help {
    public static Field getField(Class c, String f){
        try {
            return c.getField(f);
        } catch (NoSuchFieldException | SecurityException ex) {
            
        }
        return null;
    }
    
    public static Method getMethod(Class c, String m, Class<?>... params){
        try {
            return c.getMethod(m, params);
        } catch (NoSuchMethodException | SecurityException ex) {
        }

        return null;
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
}
