package BlockBreaker;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class AppFrame extends JFrame{
	Game game;
	JMenuBar menuBar;
	static JLabel gameInfo; //shows number of lives and your current level
	
	public AppFrame () { 
		game=new Game();
		gameInfo=new JLabel(" Lives: "+game.numOfLives+"    Level: "+game.level+ "    Powerup Time: "+game.powerupTime);
		menuBar=new JMenuBar();
		menuBar.add(gameInfo);
		
		add(game,BorderLayout.CENTER);
		add(menuBar,BorderLayout.NORTH);
		setTitle("Block Breaker");
		setSize(700,650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);
	}
}