package BlockBreaker;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Missile extends Thread{
	Game game;
	Image missileImage, blank;
	int missileY, missileX;
	boolean stopMissiles;
	static boolean missileHitPlatform;
	
	public Missile(Spaceship spaceship, Game game) {
		this.game=game;
		missileY=190;
		stopMissiles=false;
		missileHitPlatform=false;
		missileX=spaceship.spaceshipX+24;
		
		try {
			missileImage=ImageIO.read(new File("Images//missile.png"));
			blank=ImageIO.read(new File("Images//blank.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(!stopMissiles) {
			missileY+=1;
			if(missileY==600) //kills the thread if the missile reaches the bottom
			{
				missileImage=blank;
				stopMissiles=true;
			}
			if(game.ball.isPlayerDead || game.ball.isGameFinished) 
			{ //kills the thread and stops creating new missiles if the player dies or finished the game
				missileImage=blank;
				stopMissiles=true;
				game.missileTimer.cancel();	
			}
			if(missileX>=game.platformX+game.LeftX && missileX<=game.platformX+game.RightX && missileY>=520 && missileY<= 570)
			{ //kills the thread, stops creating new missiles and activates the missile flag to reset the platform and the ball if the missile hit the platform
				missileImage=blank;
				stopMissiles=true;
				game.missileTimer.cancel();
				missileHitPlatform=true;
			}
			
			try {
				sleep(4);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.repaint();
		}
	}

	public void drawMissile(Graphics g) {
		g.drawImage(missileImage,missileX,missileY,50,50, null);
	}
}