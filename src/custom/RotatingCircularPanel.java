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

    int count;
    int countToChangeAddToCount = 0;
    int countChange = 5;
    int addToCount = 20;
    int acelerate = -1;

    Timer timer;
    Graphics2D g2d;

    boolean rotating = false;

    int totalCount = 0;
    int targetCount = 0;

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

        timer = new Timer(40, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                //stop criteria
                if (totalCount >= targetCount) {
                    countChange = Math.round(targetCount/10);
                    addToCount = 20;
                    totalCount = 0;
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
                count += addToCount;
                totalCount+=addToCount;

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
                    targetCount = rand(1999, 2000, 1);
                    countChange = Math.round(targetCount/10);
                    addToCount = 20;
                    rotating = true;
                    timer.start();
                }
            }
        });
    }

    private int rand(int min, int max, int interval) {

        return (int) (Math.round(((Math.random() * 1000 * (Math.floor((max - min) / interval))) / 1000)) * interval) + min;

    }


}


