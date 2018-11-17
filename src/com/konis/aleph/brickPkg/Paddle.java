package com.konis.aleph.brickPkg;
/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class Paddle {

    //declares variables for x and y coordinates
    int x,y;
	public boolean moveLeft = false;
	public boolean moveRight = false;

    //constructor that takes in x and y coordinates for paddle
    public Paddle(int x, int y){
        this.x = x;
        this.y = y;
    }

    //gets x coordinate of paddle
    public int getX() {
        return x;
    }

    //sets x coordinate of paddle
    public void setX(int x) {
        this.x = x;
    }

    //gets y coordinate of paddle
    public int getY() {
        return y;
    }

    //sets y coordinate of paddle
    public void setY(int y) {
        this.y = y;
    }
    
}