import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.*;
import java.awt.*;
public class frame2 extends JPanel  {
    private boolean showBone = false;
    private boolean showbrown = true;
    
    // ตัวแปร offset สำหรับเลื่อนโต๊ะและของบนโต๊ะ
    private int deskOffsetX = 0;
    private Timer deskTimer;
    private int chairOffsetX = 0;
    private Timer chairTimer;

    // สำหรับ animation ลุกขึ้นยืน
    private int pantsFrame = 0; // 0 = นั่ง, 1-3 = กำลังลุก, 4 = ยืนเต็ม
    private Timer standUpTimer;

    private boolean useSideShirt = false; 
    private int bodyoffsetY = 0;
    private int bodyoffsetX = 0;

    private int headOffsetX = 0;
    private int headOffsetY = 0;

    // ตำแหน่ง offset สำหรับการเลื่อน
    private int baseCoffinOffsetX = -400; // เริ่มอยู่นอกจอซ้าย
    private int lidCoffinOffsetX = 800;   // เริ่มอยู่นอกจอขวา

    // state ของแอนิเมชัน
    private boolean showBase = false;
    private boolean showLid = false;
    
    private int backgroundStep = -1; // -1 = สีปกติ, 0-3 = สี transition
    private final Color[] bgColors = {
    new Color(255, 240, 196),
    new Color(140, 16, 7),
    new Color(102, 11, 5),
    new Color(62, 7, 3),
    //new Color(0,0,0)
};
    public frame2() {
        // Timer แสดงสีเปลี่ยน 0.5 วิ ต่อสี
    Timer bgChangeTimer = new Timer(500, e -> {
        backgroundStep++;
        repaint();
        if (backgroundStep >= bgColors.length) {
            backgroundStep = bgColors.length - 1; // หยุดที่สีสุดท้าย
            ((Timer)e.getSource()).stop();
        }
        repaint();
    });
        bgChangeTimer.start();
        // Timer เปลี่ยนหน้าเป็นหัวกะโหลกหลัง 2 วินาที
        Timer boneTimer = new Timer(2000, e -> {
            showBone = true;
            repaint();

            // หลังจากเปลี่ยนหน้าเป็นหัวกะโหลกแล้ว 1 วินาที → เริ่มเลื่อนโต๊ะ
            Timer startDeskMove = new Timer(1000, ev -> {
                deskTimer = new Timer(16, ev2 -> {
                    deskOffsetX -=10; // เลื่อนไปทางขวา ถ้าอยากไปซ้ายเปลี่ยนเป็น -= 5
                    if (deskOffsetX < -600) { // เมื่อโต๊ะเลื่อนไปจนถึงตำแหน่งที่กำหนด
                        deskTimer.stop(); // หยุดการเลื่อนของโต๊ะ
                    }
                    repaint();
                });
                deskTimer.start();

        // ซ่อนชิ้นส่วนโต๊ะสีน้ำตาล
            Timer hidepeicedeskTimer = new Timer(40, ev3 -> {
                showbrown = false;
                repaint();
            });
            hidepeicedeskTimer.setRepeats(false);
            hidepeicedeskTimer.start();

            // ✅ เริ่มยืนขึ้นหลังโต๊ะเลื่อน 1 วินาที
            standUpTimer = new Timer(100, ev4 -> {
                pantsFrame++;
                bodyoffsetY = -pantsFrame * 6; // สูงขึ้น
                bodyoffsetX = -pantsFrame * 2; // ขยับซ้าย
                headOffsetY = -pantsFrame * 7; // ให้สูงขึ้นเท่ากับเสื้อ
                headOffsetX = -pantsFrame * 5; // ให้ขยับซ้ายเท่ากับเสื้อ
                if (pantsFrame > 6) {
                    standUpTimer.stop();
                    pantsFrame = 6;
                }
                repaint();
            });
            standUpTimer.start();

            // ✅ 300ms หลังจากเริ่มยืน → เปลี่ยนเสื้อเป็น SideShirt
            Timer changeShirtTimer = new Timer(300, ev5 -> {
                useSideShirt = true;
                repaint();
            });
            changeShirtTimer.setRepeats(false);
            changeShirtTimer.start();
            });
            startDeskMove.setRepeats(false);
            startDeskMove.start();

             
        });
        boneTimer.setRepeats(false);
        boneTimer.start();

         // Timer ใหม่สำหรับเลื่อนเก้าอี้
        Timer startChairMove = new Timer(3500, e -> {
            chairTimer = new Timer(16, ev -> {
                chairOffsetX += 30; // เลื่อนเก้าอี้ไปทางขวา
                repaint();
            });
            chairTimer.start();
        });
        startChairMove.setRepeats(false);
        startChairMove.start();

        // หลัง 3500ms → เริ่มเลื่อนฐานโลง
        Timer startBaseTimer = new Timer(3800, e -> {
            showBase = true;
            Timer moveBase = new Timer(16, ev -> {
                baseCoffinOffsetX += 20;
                if (baseCoffinOffsetX >= 0) { // มาถึงตำแหน่ง
                    baseCoffinOffsetX = 0;
                    ((Timer) ev.getSource()).stop();

                    // หลังฐานโลงหยุด → เริ่มเลื่อนฝาโลง
                    showLid = true;
                    Timer moveLid = new Timer(16, ev2 -> {
                        lidCoffinOffsetX -= 20;
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

        // พื้นหลัง
        if (backgroundStep >= 0 && backgroundStep < bgColors.length) {
            g2d.setColor(bgColors[backgroundStep]);
        } else {
            g2d.setColor(new Color(220, 220, 220)); // สีปกติ
        }
        g2d.fillRect(0, 0, getWidth(), getHeight());
         // 1. วาดฐานโลง (ด้านหลัง)
        if (showBase) {
            g2d.translate(baseCoffinOffsetX, 0);
            drawBaseCoffin(g2d);
            g2d.translate(-baseCoffinOffsetX, 0);
        }

          // วาดเก้าอี้พร้อมกับ offset
        g2d.translate(chairOffsetX, 0);
        drawShair(g2d); 
        g2d.translate(-chairOffsetX, 0); 

        
          // วาดกางเกงตามเฟรม
        switch (pantsFrame) {
            case 0 -> drawPants(g2d);         // นั่ง
            case 1 -> drawPantsStanding1(g2d);
            case 2 -> drawPantsStanding2(g2d);
            case 3 -> drawPantsStanding3(g2d);
            case 4 -> drawPantsStanding3(g2d);
            case 5 -> drawPantsStanding4(g2d);
            case 6 -> drawPantsStand5(g2d);
             // ยืนเต็ม
        }
        
        g2d.translate(bodyoffsetX, bodyoffsetY);
        g2d.translate(headOffsetX, headOffsetY);
        if (showBone) {
            drawBone(g2d);
        } else {
            drawhair(g2d);
            drawFace(g2d);
            drawbang(g2d);
        }
        g2d.translate(-headOffsetX, -headOffsetY);
         if (useSideShirt) {
        drawSidehand(g2d);
        drawSideShirt(g2d);
        } else {
        drawShirt(g2d);
        drawHand(g2d);
    }
        g2d.translate(-bodyoffsetX, -bodyoffsetY);

        if(showbrown){
            drawPieceDesk(g2d);
        }

       
    
        // เลื่อนเฉพาะโต๊ะและของบนโต๊ะ
        g2d.translate(deskOffsetX, 0);
        drawDesk(g2d);
        drawLaptop(g2d);
        g2d.translate(-deskOffsetX, 0);
        // เพิ่มวัตถุอื่นๆ บนโต๊ะที่ต้องการเลื่อนในนี้

         // 3. วาดฝาโลง (อยู่หน้ากางเกง)
        if (showLid) {
            g2d.translate(lidCoffinOffsetX, 0);
            drawLidCoffin(g2d);
            g2d.translate(-lidCoffinOffsetX, 0);
        }
    }
   
    private void drawSideShirt(Graphics2D g2d){
        int dx=175;
        int dy=130;
        GeneralPath sh = new GeneralPath();
        sh.moveTo(83+dx,180+dy);
        sh.curveTo(83+dx,180+dy,95+dx,192+dy,122+dx,181+dy);
        sh.curveTo(122+dx,181+dy,148+dx,164+dy,174+dx,236+dy);
        sh.curveTo(199+dx,305+dy,209+dx,328+dy,204+dx,338+dy);
        sh.curveTo(199+dx,347+dy,168+dx,352+dy,160+dx,344+dy);
        sh.curveTo(160+dx,344+dy,151+dx,332+dy,151+dx,323+dy);
        sh.curveTo(151+dx,323+dy,90+dx,340+dy,53+dx,328+dy);
        sh.curveTo(53+dx,328+dy,50+dx,343+dy,42+dx,345+dy);
        sh.curveTo(34+dx,345+dy,25+dx,351+dy,7+dx,343+dy);
        sh.curveTo(-10+dx,338+dy,25+dx,251+dy,31+dx,233+dy);
        sh.curveTo(37+dx,215+dy,57+dx,177+dy,83+dx,180+dy);
        g2d.setColor(new Color(131,192,231));
        g2d.fill(sh);
        g2d.setColor(Color.BLACK);
        g2d.draw(sh);
        
        bezierCurve(g2d, 136+dx,227+dy,142+dx, 305+dy,153+dx,325+dy, 153+dx, 325+dy);
        bezierCurve(g2d,63+dx,231+dy,59+dx,304+dy,53+dx,329+dy,53+dx,329+dy);

    }
    private void drawSidehand(Graphics2D g2d){
        int dx=180;
        int dy=125;
        int x =173;
        int y = 118;
        GeneralPath hand = new GeneralPath();
        hand.moveTo(197+dx,348+dy);
        hand.curveTo(197+dx,328+dy,212+dx,385+dy,195+dx,389+dy);
        hand.curveTo(181+dx,393+dy,192+dx,369+dy,181+dx,364+dy);
        hand.curveTo(181+dx,364+dy,178+dx,382+dy,169+dx,352+dy);
        hand.lineTo(197+dx,348+dy);
        g2d.setColor(new Color(255,255,255));
        g2d.fill(hand);
        g2d.setColor(Color.BLACK);
        g2d.draw(hand);
        GeneralPath handl = new GeneralPath();

        handl.moveTo(16+x,352+y);
        handl.curveTo(16+x,352+y,3+x,398+y,15+x,395+y);
        handl.curveTo(28+x,392+y,21+x,377+y,32+x,372+y);
        handl.curveTo(32+x,372+y,31+x,397+y,44+x,358+y);
        handl.lineTo(16+x,352+y);
        g2d.setColor(new Color(255,255,255));
        g2d.fill(handl);
        g2d.setColor(Color.BLACK);
        g2d.draw(handl);
    }
   
    private void drawFace(Graphics2D g2d) {
        int dx = -40; // ลดแกน X ลง 50
        int dy = 100;  // เพิ่มแกน Y ขึ้น 70
         //วาดคอ
        g2d.setColor(Color.WHITE);
        //g2d.setStroke(new BasicStroke(2));
        g2d.fillRect(298+10, 235+dy, 35, 25);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(298+10, 235+dy, 35, 25);
        
       GeneralPath face = new GeneralPath();
       face.moveTo(314+dx,174+dy);
       face.curveTo(314+dx,174+dy,261+dx,257+dy,367+dx,239+dy);
        face.curveTo(473+dx,220+dy,355+dx,86+dy,314+dx,174+dy);
        g2d.setColor(Color.WHITE); // สีชมพู
        g2d.fill(face);
        g2d.setColor(Color.BLACK);
        g2d.draw(face);
       // g2d.setStroke(new BasicStroke(1));
       //วาดตา
        g2d.setColor(Color.BLACK); 
        g2d.setStroke(new BasicStroke(5)); // ความหนาหลอด
        g2d.drawLine(325 + dx, 190+dy , 325 + dx, 197+dy); // ตาซ้าย
        g2d.drawLine(350 + dx, 190+dy, 350 + dx, 197+dy ); // ตาขวา
        g2d.setStroke(new BasicStroke(1)); // คืนความหนาเดิม
    //วาดปาก
        //g2d.drawLine(327 + dx, 225 + dy, 353 + dx, 219 + dy);
        g2d.drawArc(325+dx, 215+dy, 20, 5, 0, -180);
        //วาดหู
       

    }
    private void drawHand(Graphics2D g2d) {
        int dx = 18; // ลดแกน X ลง 50
        int dy = 169;  // เพิ่มแกน Y ขึ้น 70
        int x = 14;
        GeneralPath handr = new GeneralPath();
        handr.moveTo(289+dx, 333+dy);
        handr.curveTo(289+dx, 330+dy, 294+dx, 306+dy, 269+dx, 304+dy);
        handr.curveTo(242+dx, 300+dy, 224+dx, 328+dy, 231+dx, 327+dy);
        handr.curveTo(231+dx, 327+dy, 225+dx, 351+dy, 247+dx, 337+dy);
        handr.curveTo(247+dx, 337+dy,277+dx,330+dy,289+dx, 333+dy);
        g2d.setColor(Color.WHITE); // สีชมพู
        g2d.fill(handr);
        g2d.setColor(Color.BLACK);
        g2d.draw(handr);
        bezierCurve(g2d, 267+dx,320+ dy,255+dx, 316+dy, 235+dx, 340+dy,  235+dx, 340+dy);
        bezierCurve(g2d, 262+dx,313+ dy, 246+dx, 311+dy, 230+dx, 338+dy,  230+dx, 338+dy);
        bezierCurve(g2d, 261+dx, 305+dy, 246+dx, 303+dy, 231+dx, 328+dy,  231+dx, 328+dy);
        
        GeneralPath handl = new GeneralPath();  
        handl.moveTo(250+x,306+dy);
        handl.curveTo(250+x, 306+dy, 244+x,285+dy, 219+x, 301+dy);
        handl.curveTo(195+x,316+dy,199+x,354+dy,231+x,339+dy);
        handl.curveTo(231+x,339+dy,229+x,324+dy,250+x,306+dy);
        g2d.setColor(Color.WHITE); 
        g2d.fill(handl);
        g2d.setColor(Color.BLACK);
        g2d.draw(handl);
        g2d.setColor(Color.BLACK);
        g2d.draw(handr);
        bezierCurve(g2d, 213+x,341+ dy,204+x, 326+dy, 218+x, 309+dy, 218+x, 309+dy);
        bezierCurve(g2d, 221+x,341+ dy, 214+x, 324+dy, 227+x, 310+dy,  227+x, 310+dy);
        bezierCurve(g2d, 231+x,341+ dy, 224+x, 324+dy, 237+x, 310+dy,  237+x, 310+dy);
        //bezierCurve(g2d, 261+dx, 305+dy, 246+dx, 303+dy, 231+dx, 328+dy,  231+dx, 328+dy);
        
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
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(base);
        //red carpet
            Polygon smallBase = new Polygon();
            smallBase.addPoint(cx - 90, cy - 180);   // บนซ้าย
            smallBase.addPoint(cx + 90, cy - 180);   // บนขวา
            smallBase.addPoint(cx + 180, cy);        // กลางขวา
            smallBase.addPoint(cx + 100, cy + 280);   // ล่างขวา
            smallBase.addPoint(cx - 100, cy + 280);   // ล่างซ้าย
            smallBase.addPoint(cx - 180, cy);        // กลางซ้าย
        // สร้าง Gradient จากบนลงล่าง (แดงเข้ม → แดงสด)
           GradientPaint redCarpetGradient = new GradientPaint(
        cx-50, cy -100, new Color(50,0,0),   // ด้านบนแดงสด
        cx+50, cy +280, new Color(170, 0, 0)     // ด้านล่างแดงเข้มเกือบดำ
    );
            g2d.setPaint(redCarpetGradient);
            g2d.fillPolygon(smallBase);

            // วาดเส้นขอบ
            g2d.setColor(new Color(0,0,0));
            g2d.drawPolygon(smallBase);

        

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


       
        // Gradient น้ำตาลเข้ม → น้ำตาลอ่อน
        GradientPaint lidGradient = new GradientPaint(
            cx, cy - 200, new Color(60, 30, 10),   // ด้านบนเข้มมาก
            cx, cy * 2, new Color(153, 102, 0)     // ด้านล่างน้ำตาลอ่อน
        );
        g2d.setPaint(lidGradient);
        g2d.fillPolygon(lid);

        // ขอบฝาโลง
        g2d.setStroke(new BasicStroke(3)); // ความหนาเส้นขอบ
        g2d.setColor(new Color(50, 25, 5));
        g2d.drawPolygon(lid);
        g2d.setStroke(new BasicStroke(1)); // คืนความหนาเส้นขอบ

        // ไม้กางเขน
        int crossCenterX = cx;
        int crossCenterY = cy - 500/4; // ขยับขึ้นเล็กน้อย
        int crossWidth = (int)(600 * 0.05);
        int crossHeight = (int)(450 * 0.35);
        int crossBarWidth = (int)(600 * 0.16);
        int crossBarHeight = (int)(300 * 0.08);

        // สีทอง
        g2d.setColor(new Color(240, 220, 150));
        // แกนยาว (vertical)
        g2d.fillRect(crossCenterX - crossWidth / 2, crossCenterY - crossHeight / 8 + 25, crossWidth, crossHeight);
        // แกนขวาง (horizontal)
        g2d.fillRect(crossCenterX - crossBarWidth / 2, crossCenterY - crossBarHeight + 60, crossBarWidth, crossBarHeight);
        
    }

    private void drawShair(Graphics2D g2d){
        int dx = 30;
        int dy = 160;

        GeneralPath sh = new GeneralPath();
        sh.moveTo(248+dx, 200+dy);
        sh.lineTo(423+dx, 215+dy);
        sh.curveTo(423+dx, 215+dy, 440+dx, 214+dy, 440+dx, 245+dy);
        sh.lineTo(433+dx, 457+dy);
        sh.lineTo(355+dx, 485+dy);
        sh.lineTo(146+dx, 451+dy);
        sh.lineTo(139+dx, 354+dy);
        sh.curveTo(139+dx, 354+dy, 135+dx, 330+dy, 207+dx, 306+dy);
        sh.curveTo(207+dx, 306+dy, 209+dx, 200+dy, 248+dx, 200+dy);
        g2d.setColor(new Color(210,210,210));
        g2d.fill(sh);
        g2d.setColor(Color.BLACK);
        g2d.draw(sh);

        g2d.drawLine(139+dx, 354+dy, 351+dx, 388+dy);
        g2d.drawLine(351+dx, 388+dy, 355+dx, 485+dy);

        // g2d.drawLine(396,372,351,388);
        bezierCurve(g2d, 351+dx, 388+dy, 340+dx, 368+dy, 394+dx, 333+dy, 394+dx, 333+dy);
        g2d.drawLine(207+dx, 306+dy, 393+dx, 330+dy);
        bezierCurve(g2d, 423+dx, 215+dy, 385+dx, 197+dy, 393+dx, 325+dy, 393+dx, 325+dy);
        g2d.drawLine(393+dx, 325+dy, 402+dx, 468+dy);

    }
    
    public void drawhair(Graphics2D g2d){
    int dx = -40; // ลดแกน X ลง 50
    int dy = 100;  // เพิ่มแกน Y ขึ้น 70

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
   
    g2d.setColor(new Color(238, 157, 236)); // สีชมพู
    g2d.fill(hairPath);
    g2d.setColor(Color.BLACK);
    g2d.draw(hairPath);
     
}
    public void drawbang(Graphics2D g2d){
    int dx = -40;
    int dy =100;
    int x=10;
   GeneralPath bang  = new GeneralPath();
   bang.moveTo(341+dx, 144+dy);
   bang.curveTo(341+dx,144+dy,298+dx,191+dy,385+dx,191+dy);
   bang.curveTo(471+dx,191+dy,374+dx,111+dy,341+dx,144+dy);
    
   g2d.setColor(new Color(238, 157, 236)); // สีชมพู
   g2d.fill(bang);
   g2d.setColor(Color.BLACK);
   bezierCurve(g2d, 288+x,250, 250+x,303, 360+x,288, 360+x,288);
}

    public void drawShirt(Graphics2D g2d){
       int dx = 10;
       int dy = 170;
      
       GeneralPath shirt = new GeneralPath();
       shirt.moveTo(336+dx,178+dy);
       shirt.curveTo(336+dx, 178+dy, 318+dx, 192+dy, 299+dx, 182+dy);
         shirt.curveTo(278+dx, 172+dy, 240+dx, 261+dy, 244+dx, 275+dy);
        shirt.curveTo(244+dx, 275+dy, 223+dx, 291+dy, 215+dx, 306+dy);
        shirt.curveTo(215+dx, 306+dy, 237+dx, 282+dy, 256+dx, 310+dy);
        shirt.curveTo(256+dx, 310+dy, 292+dx, 281+dy, 294+dx, 335+dy);
        shirt.curveTo(294+dx, 335+dy, 373+dx, 329+dy, 377+dx, 302+dy);
        shirt.curveTo(381+dx, 275+dy, 379+dx, 220+dy, 336+dx, 178+dy);
            g2d.setColor(new Color(131,192,231)); // สีน้ำเงิน
      
       g2d.fill(shirt);
       g2d.setColor(Color.BLACK);
       g2d.draw(shirt);
       
        

        g2d.setColor(Color.BLACK);
        bezierCurve(g2d, 329+dx, 227+dy, 330+dx, 271+dy, 323+dx, 277+dy, 323+dx, 277+dy);
        bezierCurve(g2d, 323+dx, 277+dy, 270+dx, 293+dy, 256+dx, 310+dy, 256+dx, 310+dy);
        bezierCurve(g2d, 281+dx, 224+dy, 262+dx, 249+dy, 256+dx, 310+dy, 256+dx, 310+dy);
       
        
    }
    private void drawBone(Graphics2D g2d) {
        int dx = 94;
        int dy = 170;

        GeneralPath bone3 = new GeneralPath();
        bone3.moveTo(250+dx, 132+dy);
        bone3.curveTo(250+dx, 132+dy, 262+dx, 145+dy, 248+dx, 157+dy);
        bone3.lineTo(189+dx, 159+dy);
        bone3.curveTo(189+dx, 157+dy, 201+dx, 148+dy, 190+dx, 137+dy);
        bone3.lineTo(250+dx, 132+dy);
        g2d.setColor(new Color(74,68,70));
        g2d.fill(bone3);

        GeneralPath bone = new GeneralPath();
        bone.moveTo(174+dx, 130+dy);
        bone.curveTo(174+dx, 130+dy, 152+dx, 102+dy, 175+dx, 72+dy);
        bone.curveTo(198+dx, 40+dy, 269+dx, 46+dy, 285+dx, 77+dy);
        bone.curveTo(299+dx, 109+dy, 297+dx, 153+dy, 258+dx, 156+dy);
        bone.curveTo(258+dx, 156+dy, 267+dx, 179+dy, 216+dx, 175+dy);
        bone.curveTo(216+dx, 175+dy, 183+dx, 177+dy, 189+dx, 162+dy);
        bone.curveTo(189+dx, 162+dy, 182+dx, 153+dy, 194+dx, 153+dy);
        bone.curveTo(194+dx, 153+dy, 202+dx, 152+dy, 199+dx, 162+dy);
        bone.curveTo(199+dx, 162+dy, 199+dx, 153+dy, 207+dx, 153+dy);
        bone.curveTo(207+dx, 153+dy, 214+dx, 152+dy, 213+dx, 163+dy);
        bone.curveTo(213+dx, 163+dy, 213+dx, 151+dy, 221+dx, 153+dy);
        bone.curveTo(221+dx, 153+dy, 227+dx, 154+dy, 226+dx, 162+dy);
        bone.curveTo(226+dx, 162+dy, 228+dx, 142+dy, 241+dx, 158+dy);
        bone.curveTo(242+dx, 159+dy, 263+dx, 146+dy, 241+dx, 132+dy);
        bone.curveTo(241+dx, 132+dy, 238+dx, 138+dy, 239+dx, 144+dy);
        bone.curveTo(239+dx, 144+dy, 225+dx, 156+dy, 225+dx, 137+dy);
        bone.curveTo(225+dx, 137+dy, 218+dx, 158+dy, 210+dx, 138+dy);
        bone.curveTo(210+dx, 138+dy, 207+dx, 159+dy, 199+dx, 138+dy);
        bone.curveTo(199+dx, 138+dy, 193+dx, 159+dy, 188+dx, 139+dy);
        bone.curveTo(188+dx, 139+dy, 190+dx, 150+dy, 180+dx, 148+dy);
        bone.curveTo(180+dx, 148+dy, 172+dx, 145+dy, 174+dx, 130+dy);

        g2d.setColor(Color.WHITE);
        g2d.fill(bone);
        g2d.setColor(Color.BLACK);
        g2d.draw(bone);

        g2d.setColor(new Color(74,68,70));
        g2d.fillOval(171+dx, 95+dy, 20, 26);
        g2d.fillOval(218+dx, 95+dy, 20, 26);
        g2d.setColor(Color.BLACK);
        g2d.drawOval(171+dx, 95+dy, 20, 26);
        g2d.drawOval(218+dx, 95+dy, 20, 26);

        bezierCurve(g2d, 240+dx, 142+dy, 235+dx, 128+dy, 254+dx, 126+dy, 254+dx, 126+dy);
        bezierCurve(g2d, 261+dx, 124+dy, 273+dx, 122+dy, 271+dx, 108+dy, 271+dx, 108+dy);

        GeneralPath bone2 = new GeneralPath();  
        bone2.moveTo(200+dx, 126+dy);
        bone2.curveTo(200+dx, 126+dy, 205+dx, 132+dy, 211+dx, 126+dy);
        bone2.curveTo(211+dx, 126+dy, 209+dx, 116+dy, 201+dx, 114+dy);
        bone2.curveTo(201+dx, 114+dy, 188+dx, 122+dy, 192+dx, 128+dy);
        bone2.curveTo(192+dx, 128+dy, 193+dx, 132+dy, 200+dx, 126+dy);
        g2d.setColor(new Color(74,68,70));
        g2d.fill(bone2);
        g2d.setColor(Color.BLACK);
        g2d.draw(bone2);  
    }
    private void drawPieceDesk(Graphics2D g2d){
         int y = -33;
        int x = -15;
        GeneralPath d = new GeneralPath();
        d.moveTo(276+x, 473+y);
        d.lineTo(308+x, 491+y);
        d.curveTo(308+x, 491+y,285+x,499+y,270+x,511+y);
        d.curveTo(270+x,511+y, 271+x,475+y, 276+x, 473+y);
        g2d.setColor(new Color(153, 102, 51));
        g2d.fill(d);
    }
    
    private void drawPants(Graphics2D g2d){
        int dx = 20;
        int dy = 150;
        GeneralPath pant  = new GeneralPath();
        pant.moveTo(363+dx,320+dy);
        pant.curveTo(363+dx,320+dy, 397+dx,401+dy,288+dx,386+dy);
        pant.lineTo(287+dx,455+dy);
        pant.curveTo(287+dx,455+dy,252+dx,476+dy,232+dx,458+dy);
        pant.curveTo(232+dx,458+dy,212+dx,466+dy,199+dx,451+dy);
        pant.curveTo(199+dx,451+dy,174+dx,352+dy,206+dx,331+dy);
        pant.curveTo(237+dx,309+dy,251+dx,306+dy,268+dx,306+dy);
        pant.lineTo(363+dx, 320+dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);
        GeneralPath p2 = new GeneralPath();
        p2.moveTo(261+dx, 327+dy);
        p2.curveTo(261+dx, 327+dy, 223+dx, 324+dy, 222+dx, 359+dy);
        p2.curveTo(219+dx, 392+dy, 225+dx, 444+dy, 232+dx, 458+dy);
        g2d.setColor(new Color(68,71,73));
        g2d.draw(p2);
    }
    
    private void drawPantsStanding1(Graphics2D g2d){
        int dx = 20;
        int dy = 150; // ยกตัวขึ้นเล็กน้อย (จาก 150 -> 120)
        GeneralPath pant  = new GeneralPath();
        pant.moveTo(360+dx,300+dy);
        pant.curveTo(360+dx,300+dy, 410+dx,355+dy,288+dx,386+dy); // ลดความโค้งลง
        pant.lineTo(287+dx,455+dy);
        pant.curveTo(287+dx,455+dy,252+dx,470+dy,232+dx,458+dy);
        pant.curveTo(232+dx,458+dy,212+dx,460+dy,199+dx,451+dy);
        pant.curveTo(199+dx,451+dy,174+dx,350+dy,206+dx,331+dy);
        pant.curveTo(206+dx,331+dy,243+dx,297+dy,268+dx,296+dy);
        pant.lineTo(360+dx, 300+dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);

    GeneralPath p2 = new GeneralPath();
            p2.moveTo(261+dx, 327+dy);
            p2.curveTo(261+dx, 327+dy, 223+dx, 324+dy, 222+dx, 359+dy);
            p2.curveTo(219+dx, 392+dy, 225+dx, 444+dy, 232+dx, 458+dy);
            g2d.setColor(new Color(68,71,73));
            g2d.draw(p2);
    }
    private void drawPantsStanding2(Graphics2D g2d){
        int dx = 20;
        int dy = 150; // ยกขึ้นมากกว่าเดิม
        GeneralPath pant  = new GeneralPath();
        pant.moveTo(337+dx,276+dy);
        pant.curveTo(363+dx,310+dy, 380+dx,317+dy,288+dx,386+dy);
        pant.lineTo(287+dx,455+dy);
    pant.curveTo(287+dx,455+dy,252+dx,470+dy,232+dx,458+dy);
    pant.curveTo(232+dx,458+dy,212+dx,466+dy,199+dx,451+dy);
        pant.curveTo(199+dx,451+dy,174+dx,350+dy,206+dx,331+dy);
        pant.curveTo(206+dx,331+dy,238+dx,282+dy,250+dx,276+dy);
        pant.lineTo(337+dx,276+dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);

    GeneralPath p2 = new GeneralPath();
            p2.moveTo(261+dx, 327+dy);
            p2.curveTo(261+dx, 327+dy, 223+dx, 324+dy, 222+dx, 359+dy);
            p2.curveTo(219+dx, 392+dy, 225+dx, 444+dy, 232+dx, 458+dy);
            g2d.setColor(new Color(68,71,73));
            g2d.draw(p2);
    }
    private void drawPantsStanding3(Graphics2D g2d){
        int dx = 20;
        int dy = 150; // ยกเต็มที่
        GeneralPath pant  = new GeneralPath();
        pant.moveTo(315+dx,263+dy);
        pant.curveTo(315+dx,263+dy, 347+dx,291+dy,283+dx,372+dy); // ขาตรงขึ้น
        pant.lineTo(287+dx,455+dy);
        pant.curveTo(287+dx,455+dy,252+dx,470+dy,232+dx,458+dy);
    pant.curveTo(232+dx,458+dy,212+dx,466+dy,199+dx,451+dy);
        pant.curveTo(189+dx,441+dy,185+dx,374+dy,190+dx,360+dy);
        pant.curveTo(193+dx,346+dy,219+dx,276+dy,243+dx,263+dy);
        pant.lineTo(315+dx, 263+dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);

        GeneralPath p2 = new GeneralPath();
            p2.moveTo(261+dx, 327+dy);
            p2.curveTo(261+dx, 327+dy, 223+dx, 324+dy, 222+dx, 359+dy);
            p2.curveTo(219+dx, 392+dy, 225+dx, 444+dy, 232+dx, 458+dy);
            g2d.setColor(new Color(68,71,73));
            g2d.draw(p2);
    }
    private void drawPantsStanding4(Graphics2D g2d) {
        int dx = 20;
        int dy = 150; // ยกเต็มที่
        GeneralPath pant  = new GeneralPath();
        pant.moveTo(293+dx,260+dy);
        pant.curveTo(293+dx,260+dy, 311+dx,290+dy,283+dx,372+dy);
        pant.lineTo(287+dx,455+dy);
        pant.curveTo(287+dx,455+dy,252+dx,470+dy,232+dx,458+dy);
        pant.curveTo(232+dx,458+dy,212+dx,466+dy,199+dx,451+dy);
        pant.curveTo(185+dx,437+dy,188+dx,364+dy,190+dx,348+dy);
        pant.curveTo(195+dx,334+dy,202+dx,281+dy,221+dx,260+dy);
        pant.lineTo(293+dx,260+dy);
        g2d.setColor(Color.BLACK);
        g2d.draw(pant);
        g2d.fill(pant);

        GeneralPath p2 = new GeneralPath();
        p2.moveTo(261+dx, 327+dy);
        p2.curveTo(261+dx, 327+dy, 223+dx, 324+dy, 222+dx, 359+dy);
        p2.curveTo(219+dx, 392+dy, 225+dx, 444+dy, 232+dx, 458+dy);
        g2d.setColor(new Color(68,71,73));
        g2d.draw(p2);
    }
    private void drawPantsStand5(Graphics2D g2d) {
     int dx = 20;
    int dy = 150; // ยกเต็มที่
    GeneralPath pant  = new GeneralPath();
    pant.moveTo(297+dx, 260+dy);
    pant.curveTo(297+dx,260+dy,299+dx, 427+dy, 287+dx, 455+dy);
    pant.curveTo(287+dx,455+dy,467+dx,472+dy,240+dx,457+dy);
    pant.curveTo(240+dx,457+dy,215+dx,472+dy,199+dx,456+dy);
    pant.curveTo(199+dx,456+dy,179+dx,288+dy,188+dx,261+dy);
    pant.lineTo(297+dx, 260+dy);
    g2d.setColor(Color.BLACK);
    g2d.draw(pant);
    g2d.fill(pant);

    GeneralPath p2 = new GeneralPath();
    p2.moveTo(233+dx, 342+dy);
    p2.curveTo(233+dx, 342+dy, 232+dx, 419+dy, 240+dx,457+dy);
    
    g2d.setColor(new Color(68,71,73));
    g2d.draw(p2);

}
    private void drawDesk(Graphics2D g2d){
        g2d.setColor(new Color(153, 102, 51)); // สีน้ำตาลโต๊ะ
        int dx= 0;
    // กำหนดจุดสี่เหลี่ยมคางหมูเพื่อทำให้โต๊ะเอียง
    int[] xPoints = {0+dx, 550+dx, 600+dx, 0+dx};
    int[] yPoints = {300, 600, 600, 600};
    

    Polygon desk = new Polygon(xPoints, yPoints, 4);
    g2d.fillPolygon(desk);
    }
    private void drawLaptop(Graphics2D g2d) {
    
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
      
        g.fillRect((int)x,(int)y,1,1); //ใช้การวาดสี่เหลี่ยมเเทนการวาดจุด
       
    }
      public static BufferedImage floodFill(BufferedImage image, int x, int y, Color targetColor,
            Color replacementColor) {

        int targetRGB = targetColor.getRGB();
        int replacementRGB = replacementColor.getRGB();

        if (image.getRGB(x, y) != targetRGB || targetRGB == replacementRGB) {
            return image;
        }

        int width = image.getWidth();
        int height = image.getHeight();

        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.remove();
            int px = p.x;
            int py = p.y;

            if (px < 0 || px >= width || py < 0 || py >= height)
                continue;
            if (image.getRGB(px, py) != targetRGB)
                continue;

            image.setRGB(px, py, replacementRGB);

            queue.add(new Point(px + 1, py));
            queue.add(new Point(px - 1, py));
            queue.add(new Point(px, py + 1));
            queue.add(new Point(px, py - 1));
        }

        return image;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Animation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new frame2());
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }  
}
