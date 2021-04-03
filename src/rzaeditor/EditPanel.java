package rzaeditor;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.event.MouseInputAdapter;

public class EditPanel extends javax.swing.JPanel {

    public static EditPanel ins = new EditPanel();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Logic.drawEditPanel(g);
    }
    
    public EditPanel() {
        initComponents();
        
        setBackground(Color.WHITE);
        addMouseListener(Mouse.ins);
        addMouseMotionListener(Mouse.ins);
        addMouseWheelListener(Mouse.ins);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 716, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}