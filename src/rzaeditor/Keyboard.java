package rzaeditor;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Keyboard extends KeyAdapter{

    public static Keyboard imp = new Keyboard();
    
    public static List<Integer> pressed = new ArrayList<>();
    public static List<Integer> released = new ArrayList<>();
    public static List<Integer> down = new ArrayList<>();
    public static String typed = "";

    private Keyboard(){}
    
    public static void reset() {
        released.clear();
        pressed.clear();
        typed = "";
    }
    
    public static boolean isDown(Integer key){
        return down.contains(key);
    }
    
    public static boolean isPressed(Integer key){
        return pressed.contains(key);
    }
    
    public static boolean isReleased(Integer key){
        return released.contains(key);
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        if(down.contains(e.getKeyCode())) return;
        
        pressed.add(e.getKeyCode());
        down.add(e.getKeyCode());
        
        Logic.keyboardEvent();
    }
    @Override
    public void keyReleased(KeyEvent e){
        released.add(e.getKeyCode());
        pressed.remove((Integer)e.getKeyCode());
        down.remove((Integer)e.getKeyCode());
        Logic.keyboardEvent();
    }
    @Override
    public void keyTyped(KeyEvent e){
        
    }
}