package notusedclasses;

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
public class RotatingBoardPanel extends JPanel {


    int count;

    int countToChangeAddToCount = 0;
    int countChange = 5;
    int addToCount = 20;
    int acelerate = -1;

    Timer timer;
    Graphics2D g2d;

    boolean rotating = false;

    public RotatingBoardPanel() {

        setTimer();
        setMouseListener();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        if (rotating) {
            float x = this.getWidth() / 2.0f;
            float y = this.getHeight() / 2.0f;
            g2d.rotate(count / 180.0 * Math.PI, x, y);

        }
    }

    @Override
    public void paintChildren(Graphics g) {
        if (rotating) super.paintChildren(g2d);
        else super.paintChildren(g);


    }


    private void setTimer() {
        timer = new Timer(40, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //count += 10;
                countToChangeAddToCount++;
                if (countToChangeAddToCount == countChange) {
                    addToCount = Math.max(1, addToCount + acelerate);
                    countToChangeAddToCount = 0;

                    int totalBoxes = getComponentCount();
                    if (totalBoxes > 1) {
                        remove(new Random().nextInt(totalBoxes - 1));

                    } else {
                        getComponent(0).setSize(new Dimension(100, 100));
                        getComponent(0).setLocation(200, 200);
                        rotating = false;
                    }

                }
                count += addToCount;


                if (count > 360) count -= 360;
                repaint();


            }
        });

    }

    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (rotating) {
                    rotating = false;
                    timer.stop();
                    count = 0;
                    addToCount = 10;
                    repaint();
                } else {
                    rotating = true;
                    timer.start();
                }
            }
        });
    }
}


