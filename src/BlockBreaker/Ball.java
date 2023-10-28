package BlockBreaker;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Ball extends Thread{
	Game game;
	Image ballImage, blankImage;
	int ballX, ballY, dirY, dirX, width, height, ballSpeed;
	boolean isPlayerLeveledUp, isIndexInArr, isPlayerDead, isBallTransparent, isMagnetActive, isGameFinished;
	
	public Ball(Game game, int currBallX) {
		this.game=game;
		ballX=currBallX;
		this.width=13;
		this.height=13;
		this.ballSpeed=3;
		ballY=538;
		dirX=0;
		dirY=-1;
		isPlayerLeveledUp=false;
		isPlayerDead=false;
		isIndexInArr=false;
		this.isBallTransparent=false;
		isMagnetActive=false;
		isGameFinished=false;
	
		try {
			blankImage=ImageIO.read(new File("Images//blank.png"));
			ballImage=ImageIO.read(new File("Images//ball.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	@Override
	public void run() {
		while(!isPlayerLeveledUp && !isPlayerDead && !isMagnetActive) {
			ballX+=dirX;
			ballY+=dirY;
			if(ballY<=0) //checks collision with the panel's upper border
			{
				dirY=1;
			}
			else if(ballX<=0) //checks collision with the panel's left border
			{
				dirX=1;
			}
			else if(ballX>=game.getWidth()-10) //checks collision with the panel's right border
			{
				dirX=-1;
			}
			else if(ballX>=game.platformX+game.LeftX && ballX<=game.platformX+game.CenterX1 && ballY==538) //checks collision with the platform's left side
			{
				if(!Powerup.isMagnetActive) //makes the ball bounce away from the block only if the magnet powerup isn't activated
				{
					dirY=-1;
					dirX=-1;
				}
				else
				{
					game.currBallX=ballX;
					isMagnetActive=true;
				}
			}
			else if(ballX>=game.platformX+game.CenterX1 && ballX<=game.platformX+game.CenterX2 && ballY==538) //checks collision with the platform's centre
			{
				if(!Powerup.isMagnetActive)
				{
					dirY=-1;
				}
				else
				{
					game.currBallX=ballX;
					isMagnetActive=true;
				}
			}
			else if(ballX>=game.platformX+game.CenterX2 && ballX<=game.platformX+game.RightX && ballY==538) //checks collision with the platform's right side
			{	
				if(!Powerup.isMagnetActive)
				{
					dirY=-1;
					dirX=1;
				}
				else
				{
					game.currBallX=ballX;
					isMagnetActive=true;
				}
			}	
			else if(game.blockIndex==game.blockIndexesArr.length) //If you broke all the blocks, it levels you up, restores the blocks, the ball's and the platform's positions and makes the ball faster in the next level 
			{
				if(game.level==4) //returns to the main menu if you beat the game
				{
					isGameFinished=true;
					JOptionPane.showMessageDialog(new JPanel(), "You beat the game! Well done!", "Info",JOptionPane.INFORMATION_MESSAGE);
					BlockBreakerMenu.gameFrame.setVisible(false);
					BlockBreakerMenu.menuFrame.setVisible(true);
				}
				
				String filePaths[]= {"Images//block0.png","Images//block1.png","Images//block2.png",
						"Images//block3.png","Images//block4.png"};
				
				for(int i=0;i<game.blocksArr.length;i++)
				{
					int num=(int) (Math.random()*5);
					try {
						Image myPicture = ImageIO.read(new File(filePaths[num]));
						game.blocksArr[i].setIcon(new ImageIcon(myPicture));
					} catch (IOException e) {
						e.printStackTrace();
					}
					game.blockIndexesArr[i]=-1;
				}
				game.blockIndex=0;
				game.level=game.level+1;
				ballX=330; 
				ballY=538;
				game.platformX=275;
				game.currBallX=330;
				game.powerupGenerator.cancel();
				
				if(game.missileTimer!=null)
				{
					game.missileTimer.cancel();
				}
				if(game.numOfLives==game.initalNumOfLives) //gives an extra life if you finished the level w/o dying
				{
					game.numOfLives=game.numOfLives+1;
				}
				
				AppFrame.gameInfo.setText(" Lives: "+game.numOfLives+"    Level: "+game.level+ "    Powerup Time: "+game.powerupTime);
				isPlayerLeveledUp=true;
			}
			else if(ballY==game.getHeight() || Missile.missileHitPlatform) //resets the platform and the ball if you lost the ball. Also, takes one life from you
			{
				if(game.numOfLives==1) //closes the app if you ran out of lives
				{
					JOptionPane.showMessageDialog(new JPanel(), "Game Over", "Info",JOptionPane.INFORMATION_MESSAGE);
					BlockBreakerMenu.gameFrame.setVisible(false);
					BlockBreakerMenu.menuFrame.setVisible(true);
				}
				game.numOfLives=game.numOfLives-1;
				AppFrame.gameInfo.setText(" Lives: "+game.numOfLives+"    Level: "+game.level+ "    Powerup Time: "+game.powerupTime);
				ballX=330; 
				ballY=538;
				game.platformX=275;
				game.currBallX=330;
				game.powerupGenerator.cancel();
				
				if(game.missileTimer!=null)
				{
					game.missileTimer.cancel();
				}
				if(game.level==4) //Creates a new spaceship thread after you die, since the old one is dead
				{
					game.spaceship=new Spaceship(game);
				}
				
				isPlayerDead=true;
				Missile.missileHitPlatform=false;
			}
			else //check of collisions between the ball and the blocks. The directions are changed accordingly 
			{
				for(int i=0;i<game.blocksArr.length;i++)
				{
					
					if(ballX>=game.blocksArr[i].getX()-25 && ballX<=game.blocksArr[i].getX()-10
							&& ballY>=game.blocksArr[i].getY()-20 && ballY<=game.blocksArr[i].getY() 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // top-left corner
					{
						if(!isBallTransparent) //makes the ball bounce away from the block only if the transparent ball power isn't activated
						{
							dirX=-1;
							dirY=-1;
						}	
						isIndexInArr=true;
					}
					else if(ballX>=game.blocksArr[i].getX()-10 && ballX<=game.blocksArr[i].getX()+10
							&& ballY==game.blocksArr[i].getY()-20 && ballY<=game.blocksArr[i].getY() 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // top-center
					{
						if(!isBallTransparent)
						{
							dirY=-1;
						}	
						isIndexInArr=true;
					}
					else if(ballX>=game.blocksArr[i].getX()+10 && ballX<=game.blocksArr[i].getX()+40
							&& ballY==game.blocksArr[i].getY()-20 && ballY<=game.blocksArr[i].getY() 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // top-right corner
					{
						if(!isBallTransparent)
						{
							dirX=1;
							dirY=-1;
						}
						isIndexInArr=true;
					}
					else if(ballX>=game.blocksArr[i].getX()-25 && ballX<=game.blocksArr[i].getX()-10 
							&& ballY>=game.blocksArr[i].getY() && ballY<=game.blocksArr[i].getY()+20 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // bottom-left corner
					{
						if(!isBallTransparent)
						{
							dirX=-1;
							dirY=1;
						}	
						isIndexInArr=true;
					}
					else if(ballX>=game.blocksArr[i].getX()-10 && ballX<=game.blocksArr[i].getX()+10 
							&& ballY>=game.blocksArr[i].getY() && ballY<=game.blocksArr[i].getY()+20 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // bottom-center
					{
						if(!isBallTransparent)
						{
							dirY=1;
						}	
						isIndexInArr=true;
					}
					else if(ballX>=game.blocksArr[i].getX()+10 && ballX<=game.blocksArr[i].getX()+40
							&& ballY>=game.blocksArr[i].getY() && ballY<=game.blocksArr[i].getY()+20 
							&& !checkBlockIndexInArr(game.blockIndexesArr,i)) // bottom-right corner
					{
						if(!isBallTransparent)
						{
							dirX=1;
							dirY=1;
						}
						isIndexInArr=true;
					}		
					if(isIndexInArr) //fills the array with indexes of broken blocks so the ball won't collide with them again
					{
						game.blockIndexesArr[game.blockIndex]=i;
						game.blockIndex=game.blockIndex+1;
						game.blocksArr[i].setIcon(new ImageIcon(blankImage));
						isIndexInArr=false;
					}
				}
			}
			try { //sets the ball's speed by stopping it for a brief moment every time it moves
				sleep(ballSpeed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.repaint();
		}
	}
	
	public boolean checkBlockIndexInArr(int arr[], int num) { //checks if the index of a block exists in the array of indexes
		for(int i=0;i<arr.length;i++)
		{
			if(arr[i]==num)
			{
				return true;
			}
		}
		return false;
	}
	
	public void drawBall(Graphics g){
		g.drawImage(ballImage,ballX,ballY,width,height,null);
	}	
}