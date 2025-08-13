import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
public class frame2 extends JPanel implements ActionListener{
    

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
     @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // ตั้งค่าพื้นหลัง
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawHair(g2d);
        drawFace(g2d);
        drawShirt(g2d);
        drawArms(g2d);
        drawDeskAndLaptop(g2d);
    }
    private void drawHair(Graphics2D g2d) {
        g2d.setColor(new Color(102, 51, 0)); // สีน้ำตาลผม
        g2d.fillOval(150, 50, 200, 250); // ทรงผมพื้นฐาน
    }

    private void drawFace(Graphics2D g2d) {
        g2d.setColor(new Color(255, 224, 189)); // สีผิว
        g2d.fillOval(190, 100, 120, 150); // ใบหน้า
        g2d.setColor(Color.BLACK);
        g2d.fillOval(220, 150, 10, 10); // ตาซ้าย
        g2d.fillOval(270, 150, 10, 10); // ตาขวา
        g2d.drawArc(230, 180, 40, 20, 0, -180); // ปาก
    }

    private void drawShirt(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(180, 240, 140, 100); // ตัวเสื้อ
    }

    private void drawArms(Graphics2D g2d) {
        g2d.setColor(new Color(255, 224, 189));
        g2d.fillRect(150, 260, 40, 100); // แขนซ้าย
        g2d.fillRect(300, 260, 40, 100); // แขนขวา
    }

    private void drawDeskAndLaptop(Graphics2D g2d) {
    g2d.setColor(new Color(153, 102, 51)); // สีน้ำตาลโต๊ะ

    // กำหนดจุดสี่เหลี่ยมคางหมูเพื่อทำให้โต๊ะเอียง
     int[] xPoints = {0, 550, 600, 0};
    int[] yPoints = {300, 600, 600, 600};
    

    Polygon desk = new Polygon(xPoints, yPoints, 4);
    g2d.fillPolygon(desk);

// ======= วาดฐานคีย์บอร์ด =======
g2d.setColor(new Color(30, 30, 30)); // สีเทาเข้ม
int[] baseX = {190, 360, 340, 170}; // ขยับซ้ายเข้าหาตัวผู้หญิง
int[] baseY = {420, 450, 480, 450}; // ขยับขึ้นให้อยู่ใกล้ตัวผู้หญิงมากขึ้น
g2d.fillPolygon(baseX, baseY, 4);
g2d.setColor(Color.RED);
g2d.fillRect(190, 420, 10, 10);
// เส้นขอบฐาน
g2d.setColor(Color.BLACK);
g2d.drawPolygon(baseX, baseY, 4);

// ======= วาดฝาจอ =======
g2d.setColor(new Color(20, 20, 20)); // ดำสนิท
int[] screenX = {170, 340, 335, 165}; // ขยับขวาอีก
int[] screenY = {470, 500, 400, 375};
g2d.fillPolygon(screenX, screenY, 4);

// เส้นขอบจอ
g2d.setColor(Color.BLACK);
g2d.drawPolygon(screenX, screenY, 4);

// ======= วาดหน้าจอภายใน =======
g2d.setColor(new Color(70, 70, 70)); // เทาเข้ม (จอด้านใน)
int[] innerX = {175, 335, 330, 170}; // ขยับขวาอีก
int[] innerY = {468, 498, 405, 380};
g2d.fillPolygon(innerX, innerY, 4);

// ======= เพิ่มเงาใต้ Laptop =======
g2d.setColor(new Color(0, 0, 0, 50)); // โปร่งใส
g2d.fillOval(160, 520, 170, 25);
}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Girl Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new frame2());
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
