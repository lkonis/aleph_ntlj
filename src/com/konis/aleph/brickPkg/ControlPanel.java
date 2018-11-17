package com.konis.aleph.brickPkg;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.konis.aleph.AlephFrame;
import com.konis.aleph.Wav_say;


/**
 *@author Victoria Suwardiman & Andrew Dammann
 *@course CSC 2053
 *@instructor Dr. Helwig
 *@project Project 4: Brickbreaker
 */
@SuppressWarnings("serial")
public class ControlPanel extends JPanel {

	//declare buttons & button handler
	private JButton restart, pause, help;
	ButtonHandler action;

	//declare key listener
	DirectionListener direction;

	//declare panels
	private TextfieldPanel text;
	private ImagePanel image;

	//declare bricks array, ball, paddle
	private Brick[] bricks;
	private Ball ball;
	private Paddle paddle;

	//declare number formatter to display time
	NumberFormat formatter;

	//declare timer, timer handler, time delay
	Timer mainTimer;
	TimerHandler timer;
	final int TIMER_DELAY = 20;

	//declare time, score, lives
	int time, timeDisplay, score, count, lives = 3; //TODO: why lives are 5 in panel?



	/**
	 * constructor takes in copies of the TextfieldPanel and ImagePanel as well as
     copies of the brick array, ball, & paddle
	 * @param text
	 * @param image
	 * @param bricks
	 * @param ball
	 * @param paddle
	 */
	public ControlPanel(TextfieldPanel text, ImagePanel image, Brick[] bricks, Ball ball, Paddle paddle ){

		//instantiating panels and objects
		this.bricks = bricks;
		this.ball = ball;
		this.paddle = paddle;
		this.text = text;
		this.image = image;

		//instantiate action listeners
		action = new ButtonHandler();
		direction = new DirectionListener();
		timer = new TimerHandler();

		//instantiate formatter for time
		formatter = new DecimalFormat("00");

		//create timer
		mainTimer = new Timer(TIMER_DELAY, timer);
		time = 0;
		timeDisplay = 0;

		//add key listener
		image.addKeyListener (direction);
		image.requestFocusInWindow();

		//create buttons and add button listeners
		if (BrickBreaker.useRestart)
		{
			restart = new JButton("RESTART");
			restart.addActionListener(action);
		}
		pause = new JButton("PAUSE");
		pause.addActionListener(action);
		help = new JButton("HELP");
		help.addActionListener(action);

		//setting up panel with grid layout
		this.setLayout(new GridLayout(3,1));
		this.add(help);
		this.add(pause);
		if (BrickBreaker.useRestart)
			this.add(restart);

	}

	//button listener for restart and pause buttons
	class ButtonHandler implements ActionListener{

		public void actionPerformed(ActionEvent e){

			if(e.getActionCommand().equals("RESTART")){

				//resets every brick to being not hit
				for(int i = 0; i < bricks.length; i++){
					bricks[i].setHit(false);
				}

				//resets every textfield and variable back to 0
				lives = 3;
				score = 0;
				time = 0;
				timeDisplay = 0;
				text.setTime("" + formatter.format(timeDisplay/60) + ":" +
						formatter.format(timeDisplay%60));
				text.setLives("" + lives);
				text.setScore("" + score);

				//resets position of ball and paddle to initial position
				ball.setX(BrickBreaker.BallInitX);
				ball.setY(BrickBreaker.BallInitY);
				ball.setXVelocity(0);
				ball.setYVelocity(0);
				paddle.setX(BrickBreaker.PaddleInitX);
				paddle.setY(BrickBreaker.PaddleInitY);

				//stops timer resets up image panel
				mainTimer.stop();
				image.requestFocusInWindow();
				image.updateBoard();
			}
			else if(e.getActionCommand().equals("PAUSE")){

				//stops timer
				if(mainTimer.isRunning()){
					mainTimer.stop();
					image.requestFocusInWindow();}
				else{
					mainTimer.start();
					image.requestFocusInWindow();
				}
			}
			else if(e.getActionCommand().equals("HELP"))
			{
				JOptionPane.showMessageDialog (null,  "Press space bar to start the game, hit the ball with your paddle and try to get rid of all the bricks", "INSTRUCTIONS", JOptionPane.PLAIN_MESSAGE );
			}
		}

	}

	class DirectionListener implements KeyListener{

		public void keyPressed(KeyEvent event){
			switch(event.getKeyCode()){
			case KeyEvent.VK_LEFT:
				//sets paddle left boundary and sets position of paddle
				if(paddle.getX() > 0 && mainTimer.isRunning()) {
					//                   paddle.setX(paddle.getX() - 16);
					paddle.moveLeft=true;
				}
				break;
			case KeyEvent.VK_RIGHT:
				//sets paddle right boundary and sets position of paddle 
				if(paddle.getX() + BrickBreaker.PaddleWidth < BrickBreaker.appletSize.width && mainTimer.isRunning()) {
					paddle.moveRight=true;

					//                   paddle.setX(paddle.getX() + 16);
				}
				break;
			case KeyEvent.VK_SPACE:
				//starts timer and releases the ball
				if (!ball.paused)
				{
					mainTimer.stop();
					ball.prevXV = ball.getXVelocity();
					ball.prevYV = ball.getYVelocity();
					ball.setXVelocity(0);
					ball.setYVelocity(0);
					ball.paused=true;
				}
				else {
					mainTimer.start();
					if(ball.getX() == BrickBreaker.BallInitX && ball.getY() == BrickBreaker.BallInitY)
					{
						ball.setYVelocity(-ball.constd);
						ball.setXVelocity(-ball.constd);
						ball.paused=false;
					}
					else if (ball.paused)
					{
						ball.setXVelocity(ball.prevXV);
						ball.setYVelocity(ball.prevYV);
						ball.paused=false;
					}
				}
				break;
			}
		}

		public void keyTyped(KeyEvent event) {}
		public void keyReleased(KeyEvent event) {
			paddle.moveLeft = false;
			paddle.moveRight = false;
		}
	}

	class TimerHandler implements ActionListener{

		public void actionPerformed(ActionEvent e) {

			//time is increased once timer begins
			time++;
			//used for displaying time
			if(time % 10 == 0){
				timeDisplay++;
			}

			//sets text fields
			text.setTime("" + formatter.format(timeDisplay/60) + ":" +
					formatter.format(timeDisplay%60));
			text.setScore("" + score);
			text.setLives("" + lives);
			// check if paddle should be moved
			if (paddle.moveLeft)
				paddle.setX(paddle.getX() - BrickBreaker.paddleStep);
			if (paddle.moveRight)
				paddle.setX(paddle.getX() + BrickBreaker.paddleStep);


			//checks if ball has hit anything and sets velocities; repaint
			ball.setX(ball.getX() + ball.getXVelocity());
			ball.setY(ball.getY() + ball.getYVelocity());
			checkContact();
			image.updateBoard();
		}

	}

	private void checkContact()
	{
		//sets boundaries for ball in the horizontal direction
		if(ball.getX() + BrickBreaker.BallSize >= BrickBreaker.appletSize.width || ball.getX() <= 0)
		{
			ball.setXVelocity(-1 * ball.getXVelocity());
		}

		//sets bounndary for upper wall in vertical direction
		if(ball.getY() < 0)
		{
			ball.setYVelocity(-1 * ball.getYVelocity());
		}

		//checks if ball touches paddle; if so allows ball to go in reverse y velocity
		if(ball.getY() + BrickBreaker.BallSize >= paddle.getY() && (ball.getX() >= paddle.getX() && ball.getX() <= (paddle.getX() + BrickBreaker.PaddleWidth)))
		{
			if(ball.getY() + BrickBreaker.BallSize <= paddle.getY() + 9){
				int vY = ball.getYVelocity();
				int vX = ball.getXVelocity();
				if (paddle.moveLeft)
				{
					vY++;
					}
				else if (paddle.moveRight)
				{
					vY--;
					}
				if (Math.abs(vX)>ball.constd*2 || vY==0 || vX==0)
				{
					vX=ball.constd;
					vY=ball.constd;
				}

				ball.setXVelocity(vX);
				ball.setYVelocity(-vY);
			}
		}

		//if the ball drops below the paddle loses a life and resets to initial position
		if(ball.getY() + BrickBreaker.BallSize > paddle.getY() + BrickBreaker.PaddleHeight){
			lives--;
			text.setLives("" + lives);
			mainTimer.stop();
			ball.setX(BrickBreaker.BallInitX);
			ball.setY(BrickBreaker.BallInitY);
			ball.setXVelocity(0);
			ball.setYVelocity(0);
			paddle.setX(BrickBreaker.PaddleInitX);
			paddle.setY(BrickBreaker.PaddleInitY);
		}

		//checks if ball hits a brick; if so resets x velocity and sets brick to being hit; update score
		for(int i = 0; i < bricks.length; i++)
		{
			if(ball.getX() + BrickBreaker.BallSize >= bricks[i].getX() && ball.getX() <= bricks[i].getX() + BrickBreaker.BrickWidth)
			{
				if(ball.getY() + BrickBreaker.BallSize >= bricks[i].getY() && ball.getY() <= bricks[i].getY() + BrickBreaker.BrickHeight)
				{
					if(!bricks[i].isHit())
					{
						if(ball.getX() + BrickBreaker.BallSize - ball.getXVelocity() <= bricks[i].getX() || ball.getX() - ball.getXVelocity() >= bricks[i].getX() + BrickBreaker.BrickWidth)
						{
							ball.setXVelocity(-1 * ball.getXVelocity());
							bricks[i].setHit(true);
							score += 1;
						}
						else
						{
							ball.setYVelocity(-1 * ball.getYVelocity());
							bricks[i].setHit(true);
							score += 1;
						}
						if (score > 10 && score < 12)
							ball.constd += 1;
						if (score > 20 && score < 22)
							ball.constd += 1;
					}
				}
			}
			if(bricks[i].isHit())
			{
				count++;
			}
		}

		//if all bricks have been hit then stop the timer
		if(count == bricks.length)
		{
//			BrickBreaker bb = (BrickBreaker) getRootPane().getParent();
//			bb.exit();
//			mainTimer.stop(); // TODO: refill with blocks and higher speed
			for(int i = 0; i < bricks.length; i++){
				bricks[i].setHit(false);
			}
			String soundEffectPath = AlephFrame.getClassResourcePath("/data/sounds/effects/");
			soundEffectPath += "magic-chime-02";

			new Wav_say(soundEffectPath+".wav");


		}

		//if loses all lives resets everything
		if(lives == 0)
		{
			if (!BrickBreaker.useReset)
			{
				BrickBreaker bb = (BrickBreaker) getRootPane().getParent();
				bb.exit();
			}

			if (BrickBreaker.useRestart)
				System.out.println("");
			//        		Brickbreaker.dispose();


			mainTimer.stop();
			for(int i = 0; i < bricks.length; i++){
				bricks[i].setHit(false);
			}
			lives = 3;
			time = 0;
			text.setLives("" + lives);
			score = 0;
			timeDisplay = 0;

			text.setTime("" + formatter.format(timeDisplay/60) + ":" +
					formatter.format(timeDisplay%60));
			text.setScore("" + score);

			ball.setX(BrickBreaker.BallInitX);
			ball.setY(BrickBreaker.BallInitY);
			ball.setXVelocity(0);
			ball.setYVelocity(0);
			paddle.setX(BrickBreaker.PaddleInitX);
			paddle.setY(BrickBreaker.PaddleInitY);

		}
		count = 0;
	}
}
