package app;

import javax.swing.*;
import javax.swing.text.FlowView;
import java.awt.*;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


/**
 * Created by macha on 14/03/2016.
 */
public class MyPanel extends JPanel{

    private String text;
    private Graphics2D g2;

    public  MyPanel(String text)
    {
       this.text=text;

    }

public void paint(Graphics g){
    Graphics2D g2 = (Graphics2D) g;    // local variable not an instance one

        g2.setColor(Color.green);
int x= 50;
    int y = 50;
    int angle = 45;

    g2.translate((float)x,(float)y);
    g2.rotate(Math.toRadians(angle));
    g2.drawString(text,0,0);
    g2.rotate(-Math.toRadians(angle));
    g2.translate(-(float)x,-(float)y);


}

    @Override
    public void paintComponent(Graphics g)
    {
        g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        String s = text;
        Font font = new Font("Serif", Font.PLAIN, 24);
        FontRenderContext frc = g2.getFontRenderContext();
        g2.translate(40, 80);

        GlyphVector gv = font.createGlyphVector(frc, s);
        int length = gv.getNumGlyphs();
        for (int i = 0; i < length; i++) {
            Point2D p = gv.getGlyphPosition(i);
            double theta = 0;//(double) i / (double) (length - 1) * Math.PI / 4;
            AffineTransform at = AffineTransform.getTranslateInstance(p.getX(),
                    p.getY());
            at.rotate(theta);
            Shape glyph = gv.getGlyphOutline(i);
            Shape transformedGlyph = at.createTransformedShape(glyph);
            g2.fill(transformedGlyph);
        }

    }

    public void drawRotate(double x, double y, int angle, String text)
    {
        g2.translate((float)x,(float)y);
        g2.rotate(Math.toRadians(angle));
        g2.drawString(text,0,0);
        g2.rotate(-Math.toRadians(angle));
        g2.translate(-(float)x,-(float)y);
    }




}
