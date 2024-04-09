package BlockBreaker;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("serial")
public class Game extends JPanel{
	Image background, platform, ballImage;
	JLabel blocksArr[];
	Ball ball, prevBall;
	Powerup powerup;
	Timer powerupGenerator, missileTimer, gameTimer;
	Spaceship spaceship;
	Missile missile;
	int platformX, tempPlatformX, currBallX, clickCounter, ballSpeed, numOfLives, level, blockIndexesArr[], blockIndex,
	initalNumOfLives, platformWidth, LeftX, CenterX1, CenterX2, RightX, offset, powerupTime, platformSpeed, 
	prevPowerupNum;
	Game game;
	
	public Game() {
		platformX=275;
		currBallX=330;
		blocksArr=new JLabel[98];
		blockIndexesArr=new int[98];
		spaceship=new Spaceship(this);
		ballSpeed=3;
		blockIndex=0;
		clickCounter=0;
		numOfLives=3;
		level=1;
		platformWidth=125;
		LeftX=-30;
		CenterX1=45;
		CenterX2=65;
		RightX=140;
		offset=125;
		powerupTime=0;
		platformSpeed=6;
		prevPowerupNum=-1;
		game=this;

		BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB); //34-36 makes the cursor invisible while in-game
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), null);
		setCursor(blankCursor);

		try {
			background=ImageIO.read(new File("Images//space.jpg"));
			platform=ImageIO.read(new File("Images//platform.png"));
			ballImage=ImageIO.read(new File("Images//ball.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ball=new Ball(this, currBallX);
		
		String filePaths[]= {"Images//block0.png","Images//block1.png","Images//block2.png",
				"Images//block3.png","Images//block4.png"};
		
		for(int i=0;i<blocksArr.length;i++) //Creates an array of JLabels and fills them with random block images
		{
			int num=(int) (Math.random()*5);
			try {
				Image myPicture = ImageIO.read(new File(filePaths[num]));
				blocksArr[i]=new JLabel(new ImageIcon(myPicture));
			} catch (IOException e) {
				e.printStackTrace();
			}
			add(blocksArr[i]);
		}
		
		for(int i=0;i<blockIndexesArr.length;i++) //Initialises the array of block's indexes with -1's and not 0's so to be able to check the 0 index as well during collision check
		{
			blockIndexesArr[i]=-1;
		}
		
		addMouseMotionListener(new MouseHandler());
		addMouseListener(new MouseHandler());
	}
	
	public class MouseHandler extends MouseAdapter{
		@Override
        public void mouseMoved(MouseEvent e) {
			if(e.getXOnScreen()>tempPlatformX && platformX<getWidth()-offset) //moves the platform to the right and updates the ball's direction
			{
				platformX+=platformSpeed;
				currBallX+=6;
			}
			else if(e.getXOnScreen()<tempPlatformX && platformX>0) //moves the platform to the left and updates the ball's direction
			{
				platformX-=platformSpeed;
				currBallX-=6;
			}
			if(!ball.isAlive()) //sets the ball's updated direction if there's no thread running
			{
				ball.ballX=currBallX;
			}
			tempPlatformX=e.getXOnScreen(); //saves the previous direction of the mouse's movement
			repaint();
		}
		 
		@Override
		public void mouseClicked(MouseEvent e) {
			clickCounter++;
			if(clickCounter==1) //starts a new ball thread after your first mouse click 
			{
				ball.start();
				initalNumOfLives=numOfLives;
				startCreatingPowerups();
			}
			else if(clickCounter>1 && !ball.isAlive()) //creates a new ball thread whether you die or level up
			{
				if(ball.isPlayerLeveledUp) 
				{
					initalNumOfLives=numOfLives;
				}
				
				prevBall=ball;
				ball=new Ball(game, currBallX);
				ball.start();
				
				if(prevBall.isPlayerDead || prevBall.isPlayerLeveledUp) // the timer restarts only when the player loses the ball or when he levels up
				{
					if(game.level==4)
					{
						startCreatingSpaceshipAndMissiles();
					}
					startCreatingPowerups();
				}
			}
		}
	}
	
	public void startCreatingPowerups() { //Starts generating a new powerup every 35 seconds
		powerupGenerator=new Timer();
		powerupGenerator.schedule(new TimerTask() {
			@Override
			public void run() {
				powerup=new Powerup(game,game.ball);
				powerup.start();
				prevPowerupNum=powerup.powerupNum;
			}}, 1000, 30000);
	}
	
	public void startCreatingSpaceshipAndMissiles() { //Creates and activates a spaceship that shoots missiles
		spaceship=new Spaceship(game);
		spaceship.start();
		missileTimer=new Timer();
		missileTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				missile=new Missile(spaceship,game);
				missile.start();
			}}, 0, 4500);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background,0,0,getWidth(),getHeight(),null);
		g.drawImage(platform,platformX,550,platformWidth,60,null);
		ball.drawBall(g);
		if(powerup!=null)
		{
			powerup.drawPowerup(g);
		}
		if(missile!=null)
		{
			missile.drawMissile(g);
		}
		if(spaceship!=null && level==4)
		{
			spaceship.drawSpaceship(g);
		}
	}
}