package rzaeditor.pageobjects.primitives;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import org.joml.Vector2i;
import rzaeditor.Drawing;
import rzaeditor.InfoTable;
import rzaeditor.pageobjects.PageObjectBase;

public abstract class Primitive extends PageObjectBase {
            
    public Drawing.LineType lineType = Drawing.LineType.SOLID;
    public static final String[] fieldsToSave = new String[]{"lineType"};
    
    public Primitive(Vector2i p) {
        super(p);
    }
    
    public void draw(){
        super.draw();
        Drawing.setTranslateGrid(pos);
        Drawing.setLineType(lineType);
    }
    
    @Override
    public void onSelect() {
        super.onSelect();
        InfoTable.addLineOptions("Тип линии", this, "lineType", new String[]{"Простая", "Прерывистая"}, new Drawing.LineType[]{Drawing.LineType.SOLID,Drawing.LineType.DASH});
    }
}
