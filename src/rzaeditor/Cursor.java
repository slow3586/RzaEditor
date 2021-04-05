package rzaeditor;

import org.joml.Vector2f;
import org.joml.Vector2i;

public class Cursor {

    public static Vector2i posPage = new Vector2i();
    public static Vector2f posPageGridSnap = new Vector2f();
    public static Vector2i relGridMove = new Vector2i();
    public static Vector2i posGrid = new Vector2i();
    public static boolean gridMoved = false;

    public static void updateCursor() {
        posPage = new Vector2i(Mouse.pos).sub(Page.current.pos);
        gridMoved = false;
        Vector2i newCurPos = new Vector2i(posPage).div(Logic.zoomGridGap);
        if (!posGrid.equals(newCurPos)) {
            relGridMove = new Vector2i(newCurPos).sub(posGrid);
            posGrid = newCurPos;
            gridMoved = true;
            posPageGridSnap = new Vector2f(posGrid.x * Logic.zoomGridGap, posGrid.y * Logic.zoomGridGap).add(Page.current.pos.x, Page.current.pos.y);
        }
    }

}
