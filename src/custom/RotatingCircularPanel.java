package custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by macha on 05/04/2016.
 */
public class RotatingCircularPanel extends JPanel {

    double count;
    double countToChangeAddToCount = 0.0d;
    double countChange = 5.0d;
    double addToCount = 20.0d;
    double acelerate = -1.0d;
double pieAngle=1.0;

    Timer timer;
    Graphics2D g2d;

    boolean rotating = false;

    double totalCount = 0.0d;
    double targetCount = 0.0d;

    public RotatingCircularPanel() {

        setTimer();
        setMouseListener();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;

        float x = this.getWidth() / 2.0f;
        float y = this.getHeight() / 2.0f;
        g2d.rotate(count / 180.0 * Math.PI, x, y);



    }

    @Override
    public void paintChildren(Graphics g) {
        if (rotating) super.paintChildren(g2d);
        else super.paintChildren(g);


    }


    public void setTimer() {

        timer = new Timer(100, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //stop criteria
                if (totalCount >= targetCount) {
                    countChange = Math.round(targetCount/10);
                    addToCount = 1.0d;
                    totalCount = 0.0d;
                    repaint();
                    timer.stop();

                    return;
                }


                //count += 10;
                countToChangeAddToCount++;
                if (countToChangeAddToCount == countChange) {
                    addToCount = Math.max(1, addToCount + acelerate);
                    countToChangeAddToCount = 0;
                }
                count += addToCount*pieAngle;
                totalCount+=addToCount*pieAngle;

                if (count > 360) count -= 360;
                repaint();
            }
        });

    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (!timer.isRunning()) {
                    targetCount = rand(600, 2500, 1);
                    countChange = Math.round(targetCount/10);
                    addToCount = 1.0d;
                    rotating = true;
                    timer.start();
                }
            }
        });
    }

    private double rand(int min, int max, int interval) {

        return  (Math.round(((Math.random() * 1000 * (Math.floor((max - min) / interval))) / 1000)) * interval) + min;

    }



    public void rotate(double angle){

        count =angle;
        repaint();
    }

    public void setPieAngle(double val){
        pieAngle = val;
    }


}


