package custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * Created by macha on 02/04/2016.
 */
public class DeathBoardPanel extends JPanel {


    Timer timer;
    Graphics2D g2d;


    public DeathBoardPanel() {
        setTimer();
        setMouseListener();


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

    }

    @Override
    public void paintChildren(Graphics g) {

      super.paintChildren(g);


    }

    private void setTimer() {


        timer = new Timer(350, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int totalBoxes = getComponentCount();

                if (totalBoxes > 1) {
                    remove(rand(0, totalBoxes - 1, 1));
                    repaint();
                } else {
                    getComponent(0).setSize(new Dimension(400, 400));
                    getComponent(0).setLocation(0, 0);
                    repaint();
                    timer.stop();
                }

            }

        });

    }


    private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (!timer.isRunning()) {

                    timer.start();
                }
            }
        });
    }


    private int rand(int min, int max, int interval) {

        return (int) (Math.round(((Math.random() * 1000 * (Math.floor((max - min) / interval))) / 1000)) * interval) + min;

    }


}

