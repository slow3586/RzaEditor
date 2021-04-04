package rzaeditor;

import java.util.HashSet;
import org.joml.Vector2i;
import org.joml.primitives.Rectanglei;

public class Page {

    public static Vector2i titleSize = new Vector2i(185, 55);
    public static Vector2i sizeNoBorder = new Vector2i(420, 297);
    public static HashSet<Wire> wires = new HashSet<>();
    public static HashSet<WireIntersection> wireIntersections = new HashSet<>();
    public static Vector2i pos = new Vector2i(0, 0);
    public static Vector2i size = new Vector2i(395, 287);
    public static Vector2i gridSize = new Vector2i(size.x / Logic.gridGap, size.y / Logic.gridGap);
    public static Rectanglei rect = new Rectanglei(-1, -1, gridSize.x, gridSize.y);

}
