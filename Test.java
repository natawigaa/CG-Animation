import java.awt.*;
import java.awt.geom.GeneralPath;
import javax.swing.*;

public class Test extends JPanel {

    // ตำแหน่ง offset สำหรับการเลื่อน
    private int baseCoffinOffsetX = -400; // เริ่มอยู่นอกจอซ้าย
    private int lidCoffinOffsetX = 800;   // เริ่มอยู่นอกจอขวา

    // state ของแอนิเมชัน
    private boolean showBase = false;
    private boolean showLid = false;

    public Test() {
        // หลัง 500ms → เริ่มเลื่อนฐานโลง
        Timer startBaseTimer = new Timer(500, e -> {
            showBase = true;
            Timer moveBase = new Timer(16, ev -> {
                baseCoffinOffsetX += 10;
                if (baseCoffinOffsetX >= 0) { // มาถึงตำแหน่ง
                    baseCoffinOffsetX = 0;
                    ((Timer) ev.getSource()).stop();

                    // หลังฐานโลงหยุด → เริ่มเลื่อนฝาโลง
                    showLid = true;
                    Timer moveLid = new Timer(16, ev2 -> {
                        lidCoffinOffsetX -= 10;
                        if (lidCoffinOffsetX <= 0) {
                            lidCoffinOffsetX = 0;
                            ((Timer) ev2.getSource()).stop();
                        }
                        repaint();
                    });
                    moveLid.start();
                }
                repaint();
            });
            moveBase.start();
        });
        startBaseTimer.setRepeats(false);
        startBaseTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // พื้นหลัง
        g2d.setColor(new Color(220, 220, 220));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 1. วาดฐานโลง (ด้านหลัง)
        if (showBase) {
            g2d.translate(baseCoffinOffsetX, 0);
            drawBaseCoffin(g2d);
            g2d.translate(-baseCoffinOffsetX, 0);
        }

        // 2. วาดกางเกง (อยู่หน้าโลงฐาน)
        drawPantsStand5(g2d);

        // 3. วาดฝาโลง (อยู่หน้ากางเกง)
        if (showLid) {
            g2d.translate(lidCoffinOffsetX, 0);
            drawLidCoffin(g2d);
            g2d.translate(-lidCoffinOffsetX, 0);
        }
    }

    private void drawBaseCoffin(Graphics2D g2d) {
        int cx = 300;
        int cy = 300;
        Polygon base = new Polygon();
        base.addPoint(cx - 100, cy - 200);
        base.addPoint(cx + 100, cy - 200);
        base.addPoint(cx + 200, cy);
        base.addPoint(cx + 120, cy * 2);
        base.addPoint(cx - 120, cy * 2);
        base.addPoint(cx - 200, cy);
        g2d.setColor(new Color(102, 51, 0));
        g2d.fillPolygon(base);
    }

    private void drawLidCoffin(Graphics2D g2d) {
       // ฝาโลงเหมือนฐาน
        int cx = 300;
        int cy = 300;
        Polygon lid = new Polygon();
        lid.addPoint(cx - 100, cy - 200);       // บนซ้าย(200,100)
        lid.addPoint(cx + 100, cy - 200);       // บนขวา(400,100)
        lid.addPoint(cx + 200, cy);             // กลางขวา(500,300)
        lid.addPoint(cx + 120 , cy * 2);    // ล่างขวา(420,600)
        lid.addPoint(cx - 120, cy * 2);    // ล่างซ้าย(180,600)
        lid.addPoint(cx - 200 , cy);        // กลางซ้าย(100,300)


        g2d.setColor(new Color(153, 102, 0)); // ฝาสีน้ำตาลอ่อน
        g2d.fillPolygon(lid);

        // ไม้กางเขน
        int crossCenterX = cx;
        int crossCenterY = cy - 500/4; // ขยับขึ้นเล็กน้อย
        int crossWidth = (int)(600 * 0.05);
        int crossHeight = (int)(450 * 0.35);
        int crossBarWidth = (int)(600 * 0.16);
        int crossBarHeight = (int)(300 * 0.08);

        g2d.setColor(new Color(240, 220, 150)); // สีเหลืองทอง
        // แกนยาว (vertical)
        g2d.fillRect(crossCenterX - crossWidth/2, crossCenterY - crossHeight/8+25, crossWidth, crossHeight);
        // แกนขวาง (horizontal)
        g2d.fillRect(crossCenterX - crossBarWidth/2, crossCenterY - crossBarHeight+60, crossBarWidth, crossBarHeight);

        
    }

    private void drawPantsStand5(Graphics2D g2d) {
        int dx = 20;
        int dy = 150;
        GeneralPath pant = new GeneralPath();
        pant.moveTo(297 + dx, 260 + dy);
        pant.curveTo(297 + dx, 260 + dy, 299 + dx, 427 + dy, 287 + dx, 455 + dy);
        pant.curveTo(287 + dx, 455 + dy, 467 + dx, 472 + dy, 240 + dx, 457 + dy);
        pant.curveTo(240 + dx, 457 + dy, 215 + dx, 472 + dy, 199 + dx, 456 + dy);
        pant.curveTo(199 + dx, 456 + dy, 179 + dx, 288 + dy, 188 + dx, 261 + dy);
        pant.lineTo(297 + dx, 260 + dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);

        GeneralPath p2 = new GeneralPath();
        p2.moveTo(233 + dx, 342 + dy);
        p2.curveTo(233 + dx, 342 + dy, 232 + dx, 419 + dy, 240 + dx, 457 + dy);
        g2d.setColor(new Color(68, 71, 73));
        g2d.draw(p2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Test());
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

