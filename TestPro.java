import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class TestPro extends JPanel implements ActionListener {
    private Timer timer;
    private long startTime;

    public TestPro() {
        startTime = System.currentTimeMillis();
        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;

        long elapsed = System.currentTimeMillis() - startTime;
        double seconds = elapsed / 1000.0;

        // ใช้ sine wave เพื่อให้ scale แกว่งขึ้นลง
        // scale ดอกตอนขยาย & ตอนหด
        float scale = (float)(0.45 + 0.25 * Math.sin(seconds * 1.5 * Math.PI / 2)); // รอบละ 2 วิ

        int flowerSpacing = 100;

        for (int y = flowerSpacing / 2; y <= getHeight(); y += flowerSpacing) {
            for (int x = flowerSpacing / 2; x <= getWidth(); x += flowerSpacing) {
                AffineTransform old = g2.getTransform();
                g2.translate(x, y);
                g2.scale(scale, scale);
                drawFlower(g2);
                g2.setTransform(old);
            }
        }

        //drawHexCoffin(g2, elapsed, getWidth(), getHeight());
        drawFullCoffin(g2, elapsed, getWidth(), getHeight());

    }

    private void drawFlower(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        for (int i = 0; i < 8; i++) {
            g2.fillOval(-30, -80, 65, 65);
            g2.rotate(Math.PI / 4);
        }
        g2.setColor(Color.BLACK);
        g2.fillOval(-20, -20, 40, 40);
    }
    private void drawFullCoffin(Graphics2D g2, long elapsed, int panelWidth, int panelHeight) {
        int cx = 300;
        int cy = 300;

        // ====== วาดโลงฐาน ======
        Polygon base = new Polygon();
        base.addPoint(cx - 100, cy - 200);   // บนซ้าย(200,100)
        base.addPoint(cx + 100, cy - 200);   // บนขวา(400,100)
        base.addPoint(cx + 200, cy);         // กลางขวา(500,300)
        base.addPoint(cx + 120 , cy * 2);    // ล่างขวา(420,600)
        base.addPoint(cx - 120, cy * 2);    // ล่างซ้าย(180,600)
        base.addPoint(cx - 200 , cy);       // กลางซ้าย(100,300)

        g2.setColor(new Color(102, 51, 0));  // น้ำตาลเข้ม
        g2.fillPolygon(base);

        // ====== คนที่นอนในโลง (เห็นแค่ครึ่งตัวบน) ======
        drawLyingPerson(g2, cx, cy, 500);

        // ====== ฝาโลงเคลื่อนออกด้านขวา ======
        float progress = Math.min(1.0f, elapsed / 3000f); // 3 วินาทีเปิดฝา
        int xOffset = (int)(progress * (panelWidth * 0.5)); // เคลื่อนฝาไปทางขวา

        AffineTransform old = g2.getTransform();
        g2.translate(xOffset, 0); // เคลื่อนฝาออก

        // ฝาโลงเหมือนฐาน
        Polygon lid = new Polygon();
        lid.addPoint(cx - 100, cy - 200);       // บนซ้าย(200,100)
        lid.addPoint(cx + 100, cy - 200);       // บนขวา(400,100)
        lid.addPoint(cx + 200, cy);             // กลางขวา(500,300)
        lid.addPoint(cx + 120 , cy * 2);    // ล่างขวา(420,600)
        lid.addPoint(cx - 120, cy * 2);    // ล่างซ้าย(180,600)
        lid.addPoint(cx - 200 , cy);        // กลางซ้าย(100,300)


        g2.setColor(new Color(153, 102, 0)); // ฝาสีน้ำตาลอ่อน
        g2.fillPolygon(lid);

        // ไม้กางเขน
        int crossCenterX = cx;
        int crossCenterY = cy - 500/4; // ขยับขึ้นเล็กน้อย
        int crossWidth = (int)(panelWidth * 0.05);
        int crossHeight = (int)(450 * 0.35);
        int crossBarWidth = (int)(panelWidth * 0.16);
        int crossBarHeight = (int)(300 * 0.08);

        g2.setColor(new Color(240, 220, 150)); // สีเหลืองทอง
        // แกนยาว (vertical)
        g2.fillRect(crossCenterX - crossWidth/2, crossCenterY - crossHeight/8+25, crossWidth, crossHeight);
        // แกนขวาง (horizontal)
        g2.fillRect(crossCenterX - crossBarWidth/2, crossCenterY - crossBarHeight+60, crossBarWidth, crossBarHeight);

        g2.setTransform(old); // รีเซ็ต transform
    }

    private void drawLyingPerson(Graphics2D g2, int cx, int cy, int height) {
        
        // วาดผมยาวลอน (wavy hair)
        int headRadius = (int)(height * 0.175);
        int headY = cy - height / 2 + (int)(height * 0.2);
        int headX = cx - headRadius;
        int bodyY = headY + (int)(height * 0.13);

        g2.setColor(new Color(0xDEAA7F)); // สีผิวคน
        // หัว
        g2.fillOval(cx - (int)(height * 0.17), headY, (int)(height * 0.35), (int)(height * 0.35));
        
        
        g2.setColor(new Color(0xDEAA7F)); // สีผิวคน


        // ลำตัว (ครึ่งบน)
        g2.fillRect(cx - (int)(height * 0.035), bodyY, (int)(height * 0.07), (int)(height * 0.22));

        // แขน
        g2.fillRect(cx - (int)(height * 0.11), bodyY + (int)(height * 0.03), (int)(height * 0.08), (int)(height * 0.04)); // ซ้าย
        g2.fillRect(cx + (int)(height * 0.03), bodyY + (int)(height * 0.03), (int)(height * 0.08), (int)(height * 0.04)); // ขวา
        
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Breathing Flowers");
        TestPro panel = new TestPro();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
