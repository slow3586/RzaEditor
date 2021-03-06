package rzaeditor.drawmodes;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import rzaeditor.Cursor;
import rzaeditor.Page;
import static rzaeditor.drawmodes.DrawModeSelect.hoveredObjects;
import static rzaeditor.drawmodes.DrawModeSelect.selectedObjects;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.PageObjectComplex;

public class DrawModeAssign extends DrawMode {

    public static DrawModeAssign imp = new DrawModeAssign();
    public static Class objectType = null;
    public static PageObjectBase editedObject = null;
    public static PageObjectBase hoveredObject = null;
    public static Consumer onSelect = null;
    
    public static void doAssign(PageObjectBase editObj, Class type, Consumer r){
        imp.cleanup();
        
        editedObject = editObj;
        editedObject.selected = true;
        objectType = type;
        onSelect = r;
        imp.infoText = "Выбор "+type.getSimpleName()+" для "+editObj.name;
        DrawMode.setCurrent(imp);
    }
    
    @Override
    public void mouseMove() {
        if(!selectedObjects.isEmpty()) return;
        
        Page.current.objects.stream().filter((t) -> {
            return t.getClass().equals(objectType) && t.isVecTouching(Cursor.posGrid);
        }).findFirst().ifPresent((t) -> {
            t.hovered = true;
            hoveredObject = t;
        });
    }
    
    @Override
    public void mouseReleased() {  
        if(hoveredObject==null) return;
        
        onSelect.accept(hoveredObject);
        
        editedObject.hovered = true;
        DrawMode.setCurrent(DrawModeSelect.imp);
    }

    @Override
    public void cleanup() {
        onSelect = null;
        objectType = null;
        if(hoveredObject!=null)
            hoveredObject.hovered = false;
        hoveredObject = null;
        if(editedObject!=null)
            editedObject.selected=false;
        editedObject=null;
    }
}
