package rzaeditor;

import rzaeditor.drawmodes.DrawModeAssign;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import rzaeditor.pageobjects.PageObjectBase;
import rzaeditor.pageobjects.PageObjectComplex;

public class InfoTable extends JTable {

    public static InfoTable imp = new InfoTable();
    
    private InfoTable() {
        setModel(InfoTableModel.imp);
        setGridColor(new java.awt.Color(226, 226, 226));
        setRowSelectionAllowed(false);
        addMouseListener(InfoTableMouseAdapter.imp);
    }
    
    public static void reset(){
        InfoTableModel.col0.clear();
        InfoTableModel.col1.clear();
        InfoTableModel.col2.clear();
        InfoTableModel.rows=0;
        InfoTableModel.imp.fireTableDataChanged();
    }
    
    public static void addLine(String n, String v, Runnable f){
        InfoTableModel.col0.add(n);
        InfoTableModel.col1.add(v);
        InfoTableModel.col2.add(f);
        InfoTableModel.rows++;
        InfoTableModel.imp.fireTableDataChanged();
    }
    
    public static void addLineID(String n, PageObjectComplex o, Runnable f){
        if(o!=null)
            addLine(n, o.id, f);
        else
            addLine(n, "", f);
    }
    
    public static void showEditTextField(PageObjectBase editedObject, Field field){
        InfoTableEditMenu.imp.ep.removeAll();
        JTextField tf = new JTextField();
        InfoTableEditMenu.imp.ep.add(tf);
        tf.setLocation(0, 0);
        tf.setSize(350, 20);
        InfoTableEditMenu.imp.ep.revalidate();
        InfoTableEditMenu.butOK.addActionListener((ActionEvent e) -> {
            try {
                field.set(editedObject, tf.getText());
                editedObject.dataUpdated();
                InfoTableEditMenu.imp.setVisible(false);
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(InfoTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        InfoTableEditMenu.imp.setVisible(true);
    }
    
    public static void addLineText(String rowName, PageObjectBase editedObject, String fieldName){
        try {
            Field field = editedObject.getClass().getField(fieldName);
            final String fieldValue = (String)field.get(editedObject);

            String v = "";
            if(fieldValue!=null)
                v=fieldValue;
            
            addLine(rowName, v, () -> {
                showEditTextField(editedObject, field);        
            });
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(InfoTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void addLineNameAssign(String rowName, PageObjectBase editedObject, String fieldName){
        try {
            Field field = editedObject.getClass().getField(fieldName);
            final PageObjectBase fieldValue = (PageObjectBase) field.get(editedObject);
            
            Consumer c = (Consumer) (Object foundAssignObject) -> {
                try {
                    field.set(editedObject, foundAssignObject);
                    editedObject.dataUpdated();
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    Logger.getLogger(InfoTable.class.getName()).log(Level.SEVERE, null, ex);
                }
            };
            String name = "";
            if(fieldValue!=null)
                name=fieldValue.name;
            addLine(rowName, name, () -> {
                DrawModeAssign.doAssign(editedObject, field.getType(), c);
            });
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(InfoTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public static class InfoTableModel extends AbstractTableModel {
        
        public static InfoTableModel imp = new InfoTableModel();
        
        private static final long serialVersionUID = 1L;
        private static final String[] COLUMN_NAMES = new String[] {"Переменная", "Значение"};
        private static final Class<?>[] COLUMN_TYPES = new Class<?>[] {String.class, String.class,  JButton.class};
        public static int rows = 0;
        public static ArrayList<String> col0 = new ArrayList<>();
        public static ArrayList<String> col1 = new ArrayList<>();
        public static ArrayList<Runnable> col2 = new ArrayList<>();

        @Override public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        @Override public int getRowCount() {
            return rows;
        }

        @Override public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        @Override public Class<?> getColumnClass(int columnIndex) {
            return COLUMN_TYPES[columnIndex];
        }

        @Override public Object getValueAt(final int rowIndex, final int columnIndex) {
            if(columnIndex==0){
                if(rowIndex>=col0.size()) return null;
                
                return col0.get(rowIndex);
            }else if(columnIndex==1){
                if(rowIndex>=col1.size()) return null;
                
                return col1.get(rowIndex);
            }
            return null;
        }
    }
    
    private static class InfoTableMouseAdapter extends MouseAdapter {
        
        public static InfoTableMouseAdapter imp = new InfoTableMouseAdapter();

        @Override
        public void mouseClicked(MouseEvent e) {
            int column = InfoTable.imp.getColumnModel().getColumnIndexAtX(e.getX()); 
            if(column!=1)return;
            int row = e.getY()/InfoTable.imp.getRowHeight(); 
            if (row < InfoTable.imp.getRowCount() && row >= 0 && InfoTableModel.col2.get(row)!=null) {
                Runnable r = InfoTableModel.col2.get(row);
                r.run();
            }
        }
    }

}
