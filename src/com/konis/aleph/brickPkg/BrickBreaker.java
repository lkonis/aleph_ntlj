package com.konis.aleph.brickPkg;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



@SuppressWarnings("serial")
public class BrickBreaker extends JFrame{
	
	public static boolean useReset=false;
	
    private ControlPanel control;
    private ImagePanel image;
    private TextfieldPanel text;

    //declares variable for title label
    private JLabel titleLabel;

    //declares array of bricks, ball, & paddle
    private Brick[] bricks;
    private Ball ball;
    private Paddle paddle;

    // project constants
    public static int BallSize=20;
    public static int BallInitX;
    public static int BallInitY;
    public static int PaddleWidth = 150;
    public static int PaddleHeight = 20;
    public static int PaddleInitX;
    public static int PaddleInitY;
    public static int BrickWidth = 70;
    public static int BrickHeight = 30;
    public static int paddleStep = 8;
    public static Dimension appletSize;
    public static boolean useRestart = false;

	/**
	 * @param title
	 * @throws HeadlessException
	 */
/*	public BrickBreaker(String title) throws HeadlessException {
		super(title);
		// TODO Auto-generated constructor stub
	}
*/
	public BrickBreaker(String argin) {
		int constd = Integer.parseInt(argin);
		setName("mainAssChosFrame");
		setTitle("Brick Breaker game");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		appletSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    	appletSize.height = (int)Math.round(appletSize.height*0.7);
    	appletSize.width = (int)Math.round(appletSize.width*0.8);

    	// create text panel
    	text = new TextfieldPanel();
    	//creates paddle for game
    	paddle = new Paddle((int)(0.3*appletSize.width - text.getWidth()), (int)(0.85*appletSize.height - PaddleHeight));
    	PaddleInitX = paddle.getX();
    	PaddleInitY = paddle.getY();
    	//creates an instance of the ball for the game
//    	ball = Ball.getBall((int)(paddle.getX()+ PaddleWidth/2 - BallSize/2), paddle.getY() - BallSize);
    	ball = Ball.getBall((int)(paddle.getX()+ PaddleWidth/2 - BallSize/2), paddle.getY() - BallSize, constd);
    	BallInitX = ball.getX();
    	BallInitY = ball.getY();
    	// fill bricks
    	fillBricks(appletSize.width*0.7);
    	//instantiates panels for program
    	image = new ImagePanel(bricks, ball, paddle);
    	control = new ControlPanel(text, image, bricks, ball, paddle);
		setSize(appletSize);

    	//creates title label for program
		titleLabel = new JLabel("BrickBreaker");
		titleLabel.setForeground(Color.WHITE);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));

		//sets up title panel
		JPanel titlePanel = new JPanel();
		titlePanel.add(titleLabel);
		titlePanel.setBackground(Color.BLUE);
    	//sets up panel for the textfields panel and control panels
    	JPanel infoPanel = new JPanel(new BorderLayout());
    	infoPanel.add(text, BorderLayout.NORTH);
    	infoPanel.add(control, BorderLayout.SOUTH);
    	infoPanel.setBackground(Color.BLUE);
    	
    	// sets up intro panel
    	IntroBrick introPanel = new IntroBrick();
    	setContentPane(introPanel);
		setVisible(true);
		// TODO: set timer for 5 sec or until intropanel sends 'resume'
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//sets up main panel of the applet
    	JPanel mainPanel = new JPanel(new BorderLayout());
    	mainPanel.add(titlePanel, BorderLayout.NORTH);
    	mainPanel.add(infoPanel, BorderLayout.EAST);
    	mainPanel.add(image, BorderLayout.CENTER);
    	setContentPane(mainPanel);
		setVisible(true);
    	appletSize.width = appletSize.width - infoPanel.getWidth();
	}
    public void fillBricks(double appletsize_width) {
    	//creates bricks and puts them in an array for the game
    	int numberBricks=3;
    	bricks = new Brick[numberBricks];
    	int totalWidth=0;
    	int etage=0;
    	int lineBrick=0;
    	int widthOffset;
    	for(int i = 0; i < numberBricks; i++){
    		Brick temp;
    		
    		if (etage%2==0)
    			widthOffset = (int)(BrickWidth*0.5);
    		else
    			widthOffset = 0;
    		temp = new Brick((lineBrick + etage)*BrickWidth + widthOffset, BrickHeight*etage);
    		bricks[i] = temp;

    		totalWidth+=BrickWidth;
    		//            if(i < 8){
    		if ((totalWidth+etage*BrickWidth*2) < appletsize_width){
    			lineBrick+=1;
    		}
    		else{
    			etage+=1;
    			lineBrick=0;
    			totalWidth=0;
    		}
    	}
    }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String argin="5";
		if (args.length>0)
			argin = args[0];			
		new BrickBreaker(argin);
	}

	public void exit() {
//		this.dispose();
	    this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}
}
