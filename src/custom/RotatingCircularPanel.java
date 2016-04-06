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


    //int count;
double count;
    int countToChangeAddToCount = 0;
    int countChange = 5;
    double addToCount = 20;
    int acelerate = -1;

    Timer timer;
    Graphics2D g2d;

    boolean rotating = false;

    //the angle that the panel will apply when rotate is called
    double angleOfRotation = 0.0d;

    // keep track of the current rotation
    double angle =0.0d;

    //parent class calls rotate method and the setAngle is set
    double setAngle=0.0d;



    public RotatingCircularPanel() {

        setTimer();
        //setMouseListener();

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2d = (Graphics2D) g;
        if (rotating) {
            float x = this.getWidth() / 2.0f;
            float y = this.getHeight() / 2.0f;
            g2d.rotate(Math.toRadians(angle),x,y);//count / 180.0 * Math.PI, x, y);
        }


    }

    @Override
    public void paintChildren(Graphics g) {
        if (rotating) super.paintChildren(g2d);
        else super.paintChildren(g);


    }


    private void setTimer() {

        double dr = Math.floor(angleOfRotation)/angleOfRotation;



        timer = new Timer(40, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //count += 10;
               /* countToChangeAddToCount++;
                if (countToChangeAddToCount == countChange) {
                    addToCount = Math.max(1, addToCount + acelerate);
                    countToChangeAddToCount = 0;

                    if(count >= setAngle){
                        rotating = false;
                    }

                }*/

                System.out.println(count);
                if(count >= setAngle){
                    rotating = false;
                }

                count += dr;


                if (count > 360) count -= 360;
                repaint();


            }
        });

    }

   /* private void setMouseListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent m) {

                //rotate(angleOfRotation);
              rotate(1);
                *//*if (rotating) {
                    rotating = false;
                    timer.stop();
                    count = 0;
                    addToCount = 10;
                    repaint();
                } else {
                    rotating = true;
                    timer.start();
                }*//*
            }
        });
    }*/

public double getAngle(){
    return this.angle;
}

    public void setAngleOfRotation(double angleToRotate){
        angleOfRotation = angleToRotate;
    }

    public void rotate(double angle){
        rotating = true;
        this.setAngle = angle;
        setTimer();
        timer.start();
        //this.angle+=angle;
        //repaint();
    }
    public void rotate(int positions){
        rotating = true;
        this.setAngle = positions*angleOfRotation;
        setTimer();
        timer.start();
        //this.angle+=positions*angleOfRotation;
        //paintComponent(g2d);
        //repaint();
    }
}


