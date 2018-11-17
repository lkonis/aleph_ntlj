package com.konis.aleph.brickPkg;

import java.awt.Color;
import java.util.Random;

/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
public class Brick {

    //declare x & y coordinates 
    int x,y;
    Color c;


    //declare boolean to determine if brick has been hit
    boolean hit;

    //constructor takes in x and y coordinates
    public Brick(int x, int y){

        this.x = x;
        this.y = y;
        hit = false;
        setColor();
    }

    //if hit returns true otherwise returns false
    public boolean isHit() {
        return hit;
    }

    //set if the brick is hit or not
    public void setHit(boolean hit) {
        this.hit = hit;
    }

    //gets x coordinate of brick
    public int getX() {
        return x;
    }

    //gets y coordinate of brick
    public int getY() {
        return y;
    }
    
    public Color getColor() {
		return c;
    }

	public void setColor() {
        Random rnd = new Random();
        int r=rnd.nextInt(80)+50;
        int g=rnd.nextInt(50)+20;
        int b=rnd.nextInt(50)+20;
        Color c = new Color(r,g,b);
		this.c = c;
	}

}