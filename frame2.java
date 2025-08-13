import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

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

        drawDeskAndLaptop(g2d);
        drawhair(g2d);
        drawFace(g2d);
        drawbang(g2d);
        //drawShirt(g2d);
    }
   
    private void drawFace(Graphics2D g2d) {
        int dx = -50; // ลดแกน X ลง 50
        int dy = 70;  // เพิ่มแกน Y ขึ้น 70

       GeneralPath face = new GeneralPath();
       face.moveTo(314+dx,174+dy);
       face.curveTo(314+dx,174+dy,261+dx,257+dy,367+dx,239+dy);
        face.curveTo(473+dx,220+dy,355+dx,86+dy,314+dx,174+dy);
        g2d.setColor(Color.WHITE); // สีชมพู
        g2d.fill(face);
        g2d.setColor(Color.BLACK);
        g2d.draw(face);

    }
    
    
    public void drawhair(Graphics2D g2d){
    int dx = -50; // ลดแกน X ลง 50
    int dy = 70;  // เพิ่มแกน Y ขึ้น 70

    GeneralPath hairPath = new GeneralPath();
    hairPath.moveTo(293 + dx, 224 + dy);
    hairPath.curveTo(285 + dx,171 + dy, 291 + dx,156 + dy, 323 + dx,130 + dy);
    hairPath.curveTo(355 + dx,105 + dy, 411 + dx,118 + dy, 430 + dx,163 + dy);
    hairPath.curveTo(449 + dx,209 + dy, 411 + dx,253 + dy, 448 + dx,287 + dy);
    hairPath.curveTo(484 + dx,322 + dy, 434 + dx,323 + dy, 455 + dx,334 + dy);
    hairPath.curveTo(474 + dx,346 + dy, 309 + dx,343 + dy, 286 + dx,334 + dy);
    hairPath.curveTo(263 + dx,323 + dy, 293 + dx,325 + dy, 283 + dx,313 + dy);
    hairPath.curveTo(269 + dx,295 + dy, 305 + dx,287 + dy, 293 + dx,224 + dy);
    hairPath.closePath();
    
    //
   
    g2d.setColor(new Color(238, 157, 236)); // สีชมพู
    g2d.fill(hairPath);
    g2d.setColor(Color.BLACK);
    g2d.draw(hairPath);

    
}
public void drawbang(Graphics2D g2d){
    int dx = -50;
    int dy =70;
   GeneralPath bang  = new GeneralPath();
   bang.moveTo(341+dx, 144+dy);
   bang.curveTo(341+dx,144+dy,298+dx,191+dy,385+dx,191+dy);
   bang.curveTo(471+dx,191+dy,374+dx,111+dy,341+dx,144+dy);
   g2d.setColor(new Color(238, 157, 236)); // สีชมพู
   g2d.fill(bang);
//    g2d.setColor(Color.BLACK);
//    bezierCurve(g2d,341+dx,144+dy,298+dx,191+dy,385+dx,191+dy,385+dx,191+dy);
//วาดตา
    g2d.setColor(Color.BLACK); 
    g2d.setStroke(new BasicStroke(5)); // ความหนาหลอด
    g2d.drawLine(325 + dx, 260 , 325 + dx, 267); // ตาซ้าย
    g2d.drawLine(350 + dx, 260, 350 + dx, 267 ); // ตาขวา
    g2d.setStroke(new BasicStroke(1)); // คืนความหนาเดิม
//วาดปาก
    //g2d.drawLine(327 + dx, 225 + dy, 353 + dx, 219 + dy);
    g2d.drawArc(340-65, 220+65, 20, 5, 0, -180);
    //วาดหู

}

   public void drawShirt(Graphics2D g2d){
       int dx = -50; // ลดแกน X ลง 50
       int dy = 70;  // เพิ่มแกน Y ขึ้น 70

       GeneralPath shirt = new GeneralPath();
       shirt.moveTo(314 + dx, 174 + dy);
       shirt.lineTo(314 + dx, 239 + dy);
       shirt.lineTo(367 + dx, 239 + dy);
       shirt.lineTo(367 + dx, 174 + dy);
       shirt.closePath();

       g2d.setColor(new Color(0, 102, 204)); // สีน้ำเงิน
       g2d.fill(shirt);
       g2d.setColor(Color.BLACK);
       g2d.draw(shirt);
   }
    public BufferedImage floodFill(BufferedImage m,int x,int y,int target_color,int replacement_color){
        // Check for invalid input or if the starting pixel is already the replacement color
        if (m == null || x < 0 || x >= m.getWidth() || y < 0 || y >= m.getHeight()) {
            return m;
        }
        // Get the RGB integer value of the target and replacement colors
        int targetRGB = target_color;
        int replacementRGB = replacement_color;

        // If the target color is the same as the replacement color, no action needed
        if (targetRGB == replacementRGB) {
            return m;
        }
        int currentPixelColor = m.getRGB(x, y);
        if (currentPixelColor != targetRGB) {
            return m;
        }
         Queue<int[]> Q = new LinkedList<int[]>();
         

        
        m.setRGB(x, y, replacementRGB); // Paint the starting pixel
        Q.add(new int[]{x,y});  // Add the starting pixel's coordinates to the queue

        while(!Q.isEmpty()){
            //current_coords = Q.dequeue();
            int[] currentpoint = Q.remove(); //Get the (x,y) array from the queue
            int currx =currentpoint[0];
            int curry =currentpoint[1];

            // --- Check South of current pixel (x, y+1) ---
            // if (south of current_node = target_colour)
            if(curry+1< m.getHeight() && m.getRGB(currx,curry+1)==targetRGB){
                // paint(south of current_node, replacement_colour);
                // Q.enqueue(south of current_node);
                m.setRGB(currx,curry+1,replacementRGB);
                Q.add(new int[]{currx,curry+1}); // Add the south neighbor's coordinates
            }
            //Check the North
            if(curry-1 >=0 && m.getRGB(currx,curry-1)==targetRGB){
                m.setRGB(currx, curry-1, replacementRGB);
                Q.add(new int[]{currx,curry-1}); // Add the north neighbor's coordinates
            }
            //Check the East
            if(currx+1<m.getWidth()&& m.getRGB(currx+1, curry)==targetRGB){
                m.setRGB(currx+1, curry, replacementRGB);
                Q.add(new int[]{currx+1,curry}); // Add the east neighbor's coordinates
            }
            //Check the West
            if(currx-1 >= 0 && m.getRGB(currx-1, curry)==targetRGB){
                m.setRGB(currx-1, curry, replacementRGB);
                Q.add(new int[]{currx-1,curry}); // Add the west neighbor's coordinates
            }
        }
        return m;
    }

    private void drawDeskAndLaptop(Graphics2D g2d) {
    g2d.setColor(new Color(153, 102, 51)); // สีน้ำตาลโต๊ะ

    // กำหนดจุดสี่เหลี่ยมคางหมูเพื่อทำให้โต๊ะเอียง
     int[] xPoints = {0, 550, 600, 0};
    int[] yPoints = {300, 600, 600, 600};
    

    Polygon desk = new Polygon(xPoints, yPoints, 4);
    g2d.fillPolygon(desk);
    // ======= เพิ่มเงาใต้ Laptop =======
    g2d.setColor(new Color(0, 0, 0, 50)); // โปร่งใส
    g2d.fillOval(140, 500, 190, 30); // ขยับขึ้นและขยายให้เงาอยู่ใกล้และใต้ Laptop มากขึ้น
    // ======= วาดฐานคีย์บอร์ด =======
    g2d.setColor(new Color(30, 30, 30)); // สีเทาเข้ม
    int[] baseX = {145 + 40, 81 + 40, 226 + 40, 303 + 40}; // ขยับขวาอีก 40
    int[] baseY = {414 + 70, 437 + 70, 452 + 70, 431 + 70};
    g2d.fillPolygon(baseX, baseY, 4);

    // ======= วาดฝาจอ =======
    g2d.setColor(new Color(20, 20, 20)); // ดำสนิท
    int[] screenX = {50+40, 81+40, 227+40, 201+40}; // ขยับขวาอีก 40
    int[] screenY = {324+70, 437+70, 451+70, 338+70};
    g2d.fillPolygon(screenX, screenY, 4);

    // วาดแอปเปิ้ล (ใช้วงรีสองอันซ้อนกัน)
    g2d.setColor(new Color(180, 180, 180)); // สีเทาเงิน
    g2d.fillOval(110+40, 370+70, 30, 35); // วงรีหลัก
    g2d.fillOval(125+40, 370+70, 30, 35); // วงรีซ้อนด้านขวา
    g2d.setColor(new Color(20, 20, 20)); // สีพื้นหลัง
    g2d.fillOval(140+40, 377+70, 25, 25);

    // เส้นขอบจอ
    g2d.setColor(Color.BLACK);
    g2d.drawPolygon(screenX, screenY, 4);
    
    // ;วาดเเก้วน้ำ
    g2d.setColor(new Color(180, 138, 96)); // ดำสนิท
    int[] glassX = {25, 72, 68, 34}; // ขยับขวาอีก 40
    int[] glassY = {403, 404, 454, 454};
    g2d.fillPolygon(glassX, glassY, 4);
    // วาดฐานแก้ว (วงรีล่าง)
    g2d.setColor(new Color(180, 138, 96));
    g2d.fillOval(34, 445, 35, 12); // ฐานแก้ว

    // วาดฝาแก้ว (วงรีบน) ให้กว้างเท่าปากแก้ว
    g2d.setColor(new Color(200, 170, 120));
    g2d.fillOval(25, 397, 47, 12); // ฝาแก้ว (กว้างเท่าปากแก้ว polygon)
    g2d.setColor(Color.BLACK);
    midpointElipse(g2d, 25+47/2, 397+12/2, 47/2, 12/2);
    //วาดหลอด
    g2d.setColor(new Color(0,0,0)); // สีเทาอ่อน
    g2d.setStroke(new BasicStroke(5)); // ความหนาหลอด
    g2d.drawLine(34, 373, 49, 402); // จุดเริ่มต้นที่ปากแก้วไปด้านบนซ้าย
    g2d.drawLine(24, 387, 33, 374);
    g2d.setStroke(new BasicStroke(1)); // คืนความหนาเดิม
    // ======= วาดหนังสือเปิดสีขาวตรงมุมซ้ายล่าง (ขนาดเล็กลงและขอบโค้ง) =======
    g2d.setColor(Color.WHITE);
    // วาดหน้าซ้าย (ขนาดเล็กลงและขยับขึ้น)
    int[] bookLeftX = {50, 105, 90, 30};
    int[] bookLeftY = {510, 525, 545, 530};
    g2d.fillPolygon(bookLeftX, bookLeftY, 4);
    // วาดหน้าขวา (ขนาดเล็กลงและขยับขึ้น)
    int[] bookRightX = {105, 160, 145, 90};
    int[] bookRightY = {525, 510, 540, 545};
    g2d.fillPolygon(bookRightX, bookRightY, 4);

    // เส้นขอบหนังสือ
    g2d.setColor(Color.BLACK);
    // ขอบหน้าซ้าย
    g2d.drawPolygon(bookLeftX, bookLeftY, 4);
    // ขอบหน้าขวา
    g2d.drawPolygon(bookRightX, bookRightY, 4);
    // วาดรอยพับกลางหนังสือ
    g2d.drawLine(90, 545, 105, 525);
}
    
    public void midpointCircle(Graphics g,int xc,int yc,int r){
        int x =0;
        int y =r;
        int d = 1-r;

        while(x<=y){
            plot (g,x+xc,y+yc);
            plot(g, -x+xc, y+yc);
            plot(g, x+xc, -y+yc);
            plot(g, -x+xc, -y+yc);
            plot(g, y+xc, x+yc);
            plot(g, -y+xc, x+yc);
            plot(g, y+xc, -x+yc);
            plot(g, -y+xc, -x+yc);

            x++;

            if(d >= 0){
                y--; //increase y value (move up )
                d = d + 2 * x-2*y + 1;
            }else{
                d = d + 2 * x + 1;
            }
            
        }
    };
    
    public void midpointElipse(Graphics g,int xc,int yc,int a,int b){
        int x,y,d;
        //region 1
        x=0;
        y=b;
        d=Math.round(b*b-a*a*b+a*a/4);
        
        while(b*b*x <= a*a*y){
            plot(g,x+xc,y+yc);
            plot(g,-x+xc,y+yc);
            plot(g,x+xc,-y+yc);
            plot(g,-x+xc,-y+yc);

            x++;

            if(d>=0){
                y--;
                d= d-2*a*a*y;
            }
            d=d+2*b*b*x+ b*b;
        }
        //region 2
        x=a;
        y=0;
        d=Math.round(a*a-b*b*a+b*b/4);

        while(b*b*x >= a*a*y){
            plot(g,x+xc,y+yc);
            plot(g,-x+xc,y+yc);
            plot(g,x+xc,-y+yc);
            plot(g,-x+xc,-y+yc);

            y++;
            if(d>=0){
                x--;
                d=d-2*b*b*x;
            }
            d=d+2*a*a*y+a*a;
        }

    }
    
     public static void bezierCurve(Graphics g,int x1,int y1,int x2,int y2,int x3 ,int y3,int x4,int y4){
        for(int i =0;i<1000;i++){
            double t = i/(double)(999); //find the value of t each round
            double xt = Math.pow(1-t, 3)*x1 + 
                        3*t*Math.pow(1-t,2)*x2+
                        3*Math.pow(t, 2)*(1-t)*x3+
                        Math.pow(t,3)*x4;

            double yt = Math.pow(1-t, 3)*y1 + 
                        3*t*Math.pow(1-t,2)*y2+
                        3*Math.pow(t, 2)*(1-t)*y3+
                        Math.pow(t,3)*y4;
            plot(g,xt,yt);
            
        }
    }
    private static void plot(Graphics g,double x,double y){
      
        g.fillRect((int)x,(int)y,2,2); //ใช้การวาดสี่เหลี่ยมเเทนการวาดจุด
       
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
