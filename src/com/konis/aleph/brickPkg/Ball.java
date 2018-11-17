package com.konis.aleph.brickPkg;
/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class Ball {

    //coordinates
    int x,y;

    //velocities
    int dx, dy;
    boolean paused=true;
    // const velocity
    int constd;

    int prevYV=0;
    int prevXV=0;


    //Singleton instance
    private static Ball ball = null;

    //private constructor
    private Ball(int x, int y){

        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;

    }
    
    //private constructor
    private Ball(int x, int y, int constd){
    	this(x,y);
        this.constd = constd;
    }

    //instantiates instance if there isn't already one
    public static Ball getBall(int x, int y, int constd){
        if(ball == null){
            ball = new Ball(x, y, constd);
        }

        return ball;
    }

    //instantiates instance if there isn't already one
    public static Ball getBall(int x, int y){
        if(ball == null){
            ball = new Ball(x, y);

        }
        return ball;
    }

    //gets x velocity of the ball
    public int getXVelocity() {
        return dx;
    }

    //sets x velocity of ball
    public void setXVelocity(int dx) {
        this.dx = dx;
    }

    //gets y velocity of ball
    public int getYVelocity() {
        return dy;
    }

    //sets y velocity of ball
    public void setYVelocity(int dy) {
        this.dy = dy;
    }

    //gets x-coordinate of ball
    public int getX() {
        return x;
    }

    //sets x-coordinate of ball
    public void setX(int x) {
        this.x = x;
    }

    //gets y-coordinate of ball
    public int getY() {
        return y;
    }

    //sets y-coordinate of ball
    public void setY(int y) {
        this.y = y;
    }

}