package rzaeditor;

import java.awt.Color;
import org.joml.Vector2f;
import org.joml.Vector2i;
import static rzaeditor.Drawing.setColor;

public class Cursor {

    public static Vector2i posPage = new Vector2i();
    public static Vector2i posPageGridSnap = new Vector2i();
    public static Vector2i relGridMove = new Vector2i();
    public static Vector2i posGrid = new Vector2i();
    public static boolean gridMoved = false;

    public static void updateCursor() {
        posPage = new Vector2i(Mouse.pos).sub(Page.current.pos);
        gridMoved = false;
        Vector2i newCurPos = new Vector2i(posPage).add(Page.gridGap, Page.gridGap).div(Logic.zoomGridGap);
        if (!posGrid.equals(newCurPos)) {
            relGridMove = new Vector2i(newCurPos).sub(posGrid);
            posGrid = new Vector2i(newCurPos);
            gridMoved = true;
            posPageGridSnap = Logic.gridToScreenCenter(newCurPos);
        }
    }
    
    public static void draw(){
        setColor(Color.red);
        Drawing.setTranslate(new Vector2i(posPageGridSnap).add(Page.current.pos));
        Drawing.setStroke(1);
        Drawing.drawOval(0,0, Logic.zoomGridGap, Logic.zoomGridGap);
    }

}
