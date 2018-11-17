package com.konis.aleph.brickPkg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class IntroBrick extends JPanel {
	public int BallSize=30;
	public IntroBrick() {
		JLabel jl = new JLabel("BOOO");
		add(jl);
	}


	public IntroBrick(Dimension size) {
		this();
		setSize(size);

	}
	static int cnt=0;

	public void paint(Graphics g){
		super.paint(g);
		int[] X = {10,15,25};
		int[] Y = {10,15,25};
		Rectangle r = getBounds();
		System.out.println("width = "+r.width+ ", height = "+r.height+", cnt="+cnt++);
		for(int i = 0; i < 3; i++){
			//paints ball
			g.setColor(new Color(100,200-i,100+i));
			g.fillOval(X[i], Y[i], BallSize, BallSize);
		}

	}

    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setTitle("Stand Alone intro");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	Dimension appletSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    	appletSize.height = (int)Math.round(appletSize.height*0.7);
    	appletSize.width = (int)Math.round(appletSize.width*0.8);
    	frame.setSize(appletSize);

		IntroBrick ibrck = new IntroBrick(appletSize);
    	frame.setContentPane(ibrck);
		frame.setVisible(true);
	}

}
