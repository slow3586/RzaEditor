package rzaeditor;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.joml.Vector2i;

public class Mouse extends MouseInputAdapter {

    public static Mouse imp = new Mouse();
    
    public static Vector2i pos = new Vector2i();
    public static Vector2i rel = new Vector2i();
    public static List<Integer> pressed = new ArrayList<>();
    public static List<Integer> released = new ArrayList<>();
    public static List<Integer> down = new ArrayList<>();
    public static List<Integer> consumed = new ArrayList<>();
    public static Boolean consumedMouseOver = false;
    public static Boolean moved = false;
    public static Float wheel = 0f;
    public static Boolean ignoreOnce = true;
    
    public static boolean isDown(Integer key){
        return down.contains(key);
    }
    
    public static boolean isPressed(Integer key){
        return pressed.contains(key);
    }
    
    public static boolean isReleased(Integer key){
        return released.contains(key);
    }
    
    private Mouse(){}
    
    public static void reset() {
        moved = false;
        rel.zero();
        //pressed.clear();
        //released.clear();
        //consumed.clear();
        consumedMouseOver = false;
        wheel = 0f;
    }
    
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        wheel +=e.getWheelRotation();
        Logic.mouseEvent();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        pressed.add((Integer) e.getButton());
        down.add((Integer) e.getButton());
        Logic.mouseEvent();
        pressed.clear();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }
    
    @Override
    public void mouseMoved(MouseEvent e){
        if(ignoreOnce){
            ignoreOnce=false;
        }else{
            rel.x = (int) (pos.x - e.getX());
            rel.y = (int) (pos.y - e.getY());
        }
        pos.x = (int) e.getX();
        pos.y = (int) e.getY();
        moved=true;
        
        Logic.mouseEvent();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released.add((Integer) e.getButton());
        down.remove((Integer) e.getButton());
        Logic.mouseEvent();
        released.clear();
    }

    
}
