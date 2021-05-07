package rzaeditor;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.event.MouseInputAdapter;

public class EditPanel extends javax.swing.JPanel {

    public static EditPanel imp = new EditPanel();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Drawing.drawEditPanel(g);
    }
    
    public EditPanel() {
        initComponents();
        
        setBackground(Color.WHITE);
        addMouseListener(Mouse.imp);
        addMouseMotionListener(Mouse.imp);
        addMouseWheelListener(Mouse.imp);
        //addKeyListener(Keyboard.imp);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 99, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 80, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
