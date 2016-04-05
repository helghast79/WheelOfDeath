package custom;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

/**
 * Created by macha on 03/04/2016.
 */
public class MyPiePanel extends JPanel {

    private double start;
    private double finish;
    private String label;
    private int fontSize;
    private Color color;



    public MyPiePanel(Color color, double start, double finish, String label) {
        this.start = start;
        this.finish = finish;
        this.label = label;
        this.fontSize = 20;
        this.color = color;
    }


    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int w = getSize().width;
        int h = getSize().height;


        g2d.setColor(color);
        Arc2D arc;
        arc = new Arc2D.Float(0.0f, 0.0f, w, h, (float)start, (float)finish - (float)start, Arc2D.PIE);
        g2d.fill(arc);


        fontSize = Math.round(w / 26);

        g2d.setColor(getContrastColor(color));

        Font font = new Font("Arial", Font.PLAIN, fontSize);
        g2d.setFont(font);


        double angle =  -(start + (finish - start) / 2);
        double corrX = (((w/2)-10)) * Math.cos(Math.toRadians(angle));
        double corrY =  (((w/2)-10)) * Math.sin(Math.toRadians(angle));

        drawRotate(g2d, (w / 2) + corrX, (h / 2)+ corrY,angle-180, label);


    }


    public void drawRotate(Graphics2D g2d, double x, double y, double angle, String text) {
        g2d.translate((float) x, (float) y);
        g2d.rotate(Math.toRadians(angle));
        g2d.drawString(text, 0, 0);
        g2d.rotate(-Math.toRadians(angle));
        g2d.translate(-(float) x, -(float) y);
    }

    public static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color.getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }


}
