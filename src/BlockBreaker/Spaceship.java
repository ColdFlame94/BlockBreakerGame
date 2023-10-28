package BlockBreaker;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Spaceship extends Thread{
	Image spaceshipImage;
	Game game;
	int spaceshipX, dir;
	boolean stopSpaceship;
	
	public Spaceship(Game game) {
		this.game=game;
		spaceshipX=584;
		dir=-1;
		stopSpaceship=false;
		
		try {
			spaceshipImage=ImageIO.read(new File("Images//ufo.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void run() {
		while(!stopSpaceship) {
			spaceshipX+=dir;
			if(spaceshipX==0) //checks collision with the left wall
			{
				dir+=1;
			}
			else if(spaceshipX==584) //checks collision with the right wall
			{
				dir-=1;
			}
			if(game.ball.isPlayerDead || game.ball.isGameFinished || Missile.missileHitPlatform) //restores the spaceship to its' original position
			{
				spaceshipX=584;
				stopSpaceship=true;
			}
			
			try {
				sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			game.repaint();
		}	
	}
	
	public void drawSpaceship(Graphics g) {
		g.drawImage(spaceshipImage,spaceshipX,160,100,100, null);
		
	}
}