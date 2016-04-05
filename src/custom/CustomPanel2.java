package custom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * Created by macha on 02/04/2016.
 */
public class CustomPanel2 {

    JPanel panel;
    int count;

    int countToChangeAddToCount = 0;
    int countChange = 5;
    int addToCount = 20;
    int acelerate = -1;

    Timer timer;
    Graphics2D g2d;
    int size = 40;
    int aantal = 8;
    boolean rotating = false;

    public CustomPanel2() {

        panel = new JPanel() {

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
        };

        timer = new Timer(40, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //count += 10;
                countToChangeAddToCount++;
                if (countToChangeAddToCount == countChange) {
                    addToCount = Math.max(1, addToCount + acelerate);
                    countToChangeAddToCount = 0;

                    int totalBoxes = panel.getComponentCount();
                    if (totalBoxes > 1) {
                        panel.remove(new Random().nextInt(totalBoxes - 1));
                    } else {
                        panel.getComponent(0).setSize(new Dimension(100, 100));
                        panel.getComponent(0).setLocation(200, 200);
                        rotating = false;
                    }

                }
                count += addToCount;


                if (count > 360) count -= 360;
                panel.repaint();


            }
        });

        panel.setLayout(new GridLayout(aantal, aantal));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {
                if (rotating) {
                    rotating = false;
                    timer.stop();
                    count = 0;
                    addToCount = 10;
                    panel.repaint();
                } else {
                    rotating = true;
                    timer.start();
                }
            }
        });

        Random rnd = new Random();

        float pieAngle = 360 / (aantal * aantal);

        for (int i = 0; i < aantal; i++) {
            for (int j = 0; j < aantal; j++) {
                    JLabel label = new JLabel("Up");
                    label.setPreferredSize(new Dimension(size, size));
                    label.setOpaque(true);
                    label.setBackground(new Color(rnd.nextInt(256),
                                    rnd.nextInt(256),
                                    rnd.nextInt(256)
                            )
                    );
                    panel.add(label);

            }
        }


    }

    public JPanel getPanel() {
        return panel;
    }

}

