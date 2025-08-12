import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Random;

public class RoundPetalFlowerPattern extends JPanel {
    private Random rand = new Random();
    private int flowerSize = 60; // ขนาดดอกไม้
    private int petalCount = 12; // กลีบกลมรอบศูนย์กลาง

    // โทนสีพาสเทล
    private Color[] petalColors = {
            new Color(255, 182, 193), // ชมพูอ่อน
            new Color(221, 160, 221), // ม่วงอ่อน
            new Color(173, 216, 230), // ฟ้าอ่อน
            new Color(255, 228, 196), // ครีมพีช
            new Color(255, 250, 205)  // เหลืองนวล
    };

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(new Color(200, 250, 220)); // พื้นเขียวอ่อนมาก

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // จัดเรียงเป็นกริด
        int cols = panelWidth / flowerSize +1;
        int rows = panelHeight / flowerSize + 1;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int x = col * flowerSize + flowerSize / 2;
                int y = row * flowerSize + flowerSize / 2;
                Color petalColor = petalColors[rand.nextInt(petalColors.length)];
                drawFlower(g2d, x, y, flowerSize / 4, petalColor);
            }
        }

        //วาดโลงศพ
        int coffinWidth = 120;
        int coffinHeight = 220;
        int coffinX = (panelWidth - coffinWidth) / 2;
        int coffinY = (panelHeight - coffinHeight) / 2;
        drawCoffin(g2d, coffinX, coffinY, coffinWidth, coffinHeight);
    }

    private void drawFlower(Graphics2D g2d, int x, int y, int radius, Color petalColor) {
        g2d.translate(x, y);

        // วาดกลีบกลมรอบศูนย์กลาง
        double angleStep = 2 * Math.PI / petalCount;
        g2d.setColor(petalColor);
        for (int i = 0; i < petalCount; i++) {
            double angle = i * angleStep;
            double px = Math.cos(angle) * radius * 1;
            double py = Math.sin(angle) * radius * 1;
            g2d.fill(new Ellipse2D.Double(
                    px - radius / 8,
                    py - radius / 8,
                    radius * 0.7,
                    radius * 0.7
            ));
        }

        // ใจกลางดอก (สีเหลืองอ่อน)
        g2d.setColor(new Color(255, 239, 170));
        g2d.fill(new Ellipse2D.Double(
                -radius / 1.8,
                -radius / 1.8,
                radius * 1.6,
                radius * 1.6
        ));

        g2d.translate(-x, -y);
    }

    private void drawCoffin(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.translate(x, y);

        // ตัวโลง (สีน้ำตาลเข้ม)
        g2d.setColor(new Color(102, 51, 0));
        Polygon coffinShape = new Polygon();
        coffinShape.addPoint(width / 4, 0);
        coffinShape.addPoint(width * 3 / 4, 0);
        coffinShape.addPoint(width, height / 4);
        coffinShape.addPoint(width, height);
        coffinShape.addPoint(0, height);
        coffinShape.addPoint(0, height / 4);
        g2d.fill(coffinShape);

        // ขอบโลง (สีน้ำตาลอ่อน)
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(153, 102, 51));
        g2d.draw(coffinShape);

        // ไม้ตรงกลางโลง
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(width / 2, 0, width / 2, height);
        
        // สัญลักษณ์กางเขน
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(width / 2, height / 3, width / 2, height * 2 / 3);
        g2d.drawLine(width / 3, height / 2, width * 2 / 3, height / 2);
        g2d.translate(-x, -y);
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Round Petal Flower Pattern");
        RoundPetalFlowerPattern panel = new RoundPetalFlowerPattern();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
