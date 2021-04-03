package rzaeditor;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Optional;
import org.joml.Vector2f;
import org.joml.Vector2i;

public class WireIntersection {

    Vector2i pos = new Vector2i();
    HashSet<Wire> wireIntersects = new HashSet<>();

    private WireIntersection(Vector2i p) {
        pos = p;
        for (Wire wire : Logic.pageWires) {
            if (wire.start.equals(pos)) {
                wire.startWI = this;
                wireIntersects.add(wire);
            }
            if (wire.end.equals(pos)) {
                wire.endWI = this;
                wireIntersects.add(wire);
            }
            if (wire.pointInside(pos, 1)) {
                wireIntersects.add(wire);
            }
        }
        if (!wireIntersects.isEmpty()) {
            Logic.pageWireIntersections.add(this);
        }
    }

    public static WireIntersection getWI(Vector2i p) {
        WireIntersection wi = getWIAt(p);
        if (wi == null) {
            wi = new WireIntersection(p);
        }
        return wi;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke());
        Vector2f t0 = Logic.gridToScreen(pos);
        Logic.fillOval(g, t0.x + Logic.zoomGridGap / 4, t0.y + Logic.zoomGridGap / 4, Logic.zoomGridGap / 2, Logic.zoomGridGap / 2);
    }

    public static WireIntersection getWIAt(Vector2i p) {
        Optional<WireIntersection> m = Logic.pageWireIntersections.stream().filter((t) -> {
            return t.pos.equals(p);
        }).findFirst();
        return m.orElse(null);
    }

    public void checkIsEmpty() {
        if (wireIntersects.isEmpty()) {
            delete();
        }
    }

    public void delete() {
        Logic.pageWireIntersections.remove(this);
    }

}
