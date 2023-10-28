package BlockBreaker;

import java.awt.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Powerup extends Thread{
	Game game;
	Image powerupImage, blank, wreckingBall, originalBall;
	Ball ball;
	Timer timer;
	int dirX, dirY, powerupNum, counter, width, height;
	boolean stopPowerupFlag, isPowerupOn;
	static boolean isMagnetActive;
	
	public Powerup(Game game, Ball ball) {
		this.game=game;
		this.ball=ball;
		dirY=-50;
		dirX=(int) (Math.random()*600); //the powerup appears at a random x position
		counter=0;
		stopPowerupFlag=false;
		isPowerupOn=false;
		isMagnetActive=false;
		
		String powerupImages[]= {"Images//fist.png", "Images//magnet.png", "Images//toofast.png", 
				"Images//plus.png", "Images//minus.png", "Images//tooslow.png",};
		 
		do {// generates a random number to choose a random powerup image and create a random powerup with it
			//powerupNum=1;
			powerupNum=(int) (Math.random()*6);
		}while(powerupNum==game.prevPowerupNum);

		try {
			powerupImage=ImageIO.read(new File(powerupImages[powerupNum])); 
			blank=ImageIO.read(new File("Images//blank.png"));
			wreckingBall=ImageIO.read(new File("Images//ball2.png"));
			originalBall=ImageIO.read(new File("Images//ball.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		if(powerupNum==0 || powerupNum==4 || powerupNum==5)
		{
			width=35;
			height=35;
		}
		else if(powerupNum==1 || powerupNum==2 || powerupNum==3)
		{
			width=50;
			height=50;
		}	
	}

	public void run() {
		while(!stopPowerupFlag) {
			dirY+=1;
			if(dirX>=game.platformX+game.LeftX && dirX<=game.platformX+game.RightX && dirY>=520  && dirY<= 570 && !isPowerupOn) //activates the obtained powerup and sets its' timer
			{ 
				powerupImage=blank;
				isPowerupOn=true;
				
				switch(powerupNum) {
				case 0: //activates the transparent ball powerup which makes the ball go through the blocks w/o bouncing away from them  
					ball.isBallTransparent=true;
					ball.ballImage=wreckingBall;
					ball.width=18;
					ball.height=18;
					counter=16;
					break;
				case 1: //activates the magnet powerup which makes the ball stick to the platform  
					isMagnetActive=true;
					counter=21;
					break;
				case 2: //this powerup makes the ball go faster
					ball.ballSpeed=1;
					counter=21;
					break;
				case 3: //this powerup makes the platform wider
					game.platformWidth=225;
					game.CenterX1=125;
					game.CenterX2=145;
					game.RightX=240;
					game.offset=225;
					counter=21;
					break;
				case 4: //this powerup narrows the platform 
					game.platformWidth=60;
					game.CenterX1=0;
					game.CenterX2=30;
					game.RightX=70;
					game.offset=60;
					counter=16;
					break;
				case 5: //activates the slowdown powerup which makes the platform move slower
					game.platformSpeed=2;
					counter=11;
					break;
				}
				
				timer=new Timer();
				timer.schedule(new TimerTask() { // activates the powerup's timer 
					@Override
					public void run() {
						counter--;
						game.powerupTime=counter;
						AppFrame.gameInfo.setText(" Lives: "+game.numOfLives+"    Level: "+game.level+ 
								"    Powerup Time: "+game.powerupTime);
					}}, 0, 1000);
			}
			if(isPowerupOn) //stops the thread and reverts the game to its' previous state depending on the powerup, 
			{               //whether the powerup ran out or the player died/levelled up
				if(counter==0 || game.ball.isPlayerLeveledUp || game.ball.isPlayerDead || Missile.missileHitPlatform)
				{
					stopPowerupFlag=true;
					switch(powerupNum) {
					case 0:	
						ball.isBallTransparent=false;
						ball.ballImage=originalBall;
						ball.width=13;
						ball.height=13;
						break;
					case 1:
						if(game.ball.isMagnetActive)
						{
							game.ball=new Ball(game, game.currBallX);
							game.ball.start();
						}
						isMagnetActive=false;
						break;
					case 2:
						ball.ballSpeed=3;
						break;
					case 3,4:
						game.platformWidth=125;
						game.CenterX1=45;
						game.CenterX2=65;
						game.RightX=140;
						game.offset=125;
						break;
					case 5:
						game.platformSpeed=6;
						break;
					}
				}
				if(stopPowerupFlag || game.ball.isGameFinished) // if the thread should die or the game has finished, I cancel the powerup's timer and nullify the in-game timer
				{
					timer.cancel();
					game.powerupTime=0;
					AppFrame.gameInfo.setText(" Lives: "+game.numOfLives+"    Level: "+game.level+ "    Powerup Time: "+game.powerupTime);
				}
			}
			else if(!isPowerupOn && ball.isPlayerLeveledUp || !isPowerupOn && ball.isPlayerDead 
					|| !isPowerupOn && ball.isGameFinished || !isPowerupOn && Missile.missileHitPlatform) // cancels the created powerup if you lose a life or level up
			{
				powerupImage=blank;
				stopPowerupFlag=true;
			}
			
			try {
				sleep(10);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			game.repaint();
		}
	}
	
	public void drawPowerup(Graphics g) {
		g.drawImage(powerupImage,dirX,dirY,width,height,null);
	}
}