package com.konis.aleph.brickPkg;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
@SuppressWarnings("serial")
public class ImagePanel extends JPanel{

    //declares array of bricks, ball, & paddle to be drawn
    private Brick[] bricks;
    private Ball ball;
    private Paddle paddle;

    //constructor of panel takes in copies of the bricks array, ball, & paddle
    public ImagePanel(Brick[] bricks, Ball ball, Paddle paddle){

        this.bricks = bricks;
        this.ball = ball;
        this.paddle = paddle;

        //sets border of panel and background color
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 4));
        setBackground(new Color(30,150,180));

        //sets focusable of panel to allow key press
        this.setFocusable(true);

        //paints initial position upon instantiation
        repaint();
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        //paints  bricks only if they are not hit
        for(int i = 0; i < bricks.length; i++){
            if(!bricks[i].isHit()){
                g.setColor(bricks[i].getColor());
                int arcWidth = 15;
                int arcHeight = 5;
                g.fillRoundRect(bricks[i].getX(), bricks[i].getY(), BrickBreaker.BrickWidth, BrickBreaker.BrickHeight,arcWidth, arcHeight);
                g.setColor(new Color(200,100,100));
                g.drawRoundRect(bricks[i].getX(), bricks[i].getY(), BrickBreaker.BrickWidth, BrickBreaker.BrickHeight, arcWidth, arcHeight);
            }
        }

        //paints paddle
        g.setColor(Color.DARK_GRAY);
        g.fillRect(paddle.getX(), paddle.getY(), BrickBreaker.PaddleWidth, BrickBreaker.PaddleHeight);
        g.setColor(Color.BLUE);
        g.drawRect(paddle.getX(), paddle.getY(), BrickBreaker.PaddleWidth, BrickBreaker.PaddleHeight);

        //paints ball
        g.setColor(Color.BLACK);
        g.fillOval(ball.getX(), ball.getY(), BrickBreaker.BallSize, BrickBreaker.BallSize);
    }

    //method that calls the repaint method
    public void updateBoard(){
        repaint();
    }

}
