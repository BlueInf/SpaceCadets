import javax.swing.*;
import java.awt.*;


/**
 * The Spirograph class
 * We use it to draw the spirograph
 */
public class Spirograph extends JPanel {


    private int nPoints = 10000; // Number of points we draw
    private double r1 = 104; // Big radius
    private double r2 = 96; // Small radius
    private double p = 80;// Offset of the pen point

    /**
     * We override the paint Component to paint a Spirograph
     * We use r1,r2,p1
     *
     * @param g
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(200, 200);

        int x1 = (int) (r1 + r2 - p);
        int y1 = 0;
        int x2;
        int y2;

        for (int i = 0; i < nPoints; i++) {

            double t = i * Math.PI / 90;
            x2 = (int) ((r1 + r2) * Math.cos(t) - p * Math.cos((r1 + r2) * t / r2));
            y2 = (int) ((r1 + r2) * Math.sin(t) - p * Math.sin((r1 + r2) * t / r2));
            g2.drawLine(x1, y1, x2, y2);
            x1 = x2;
            y1 = y2;

        }
    }
}
