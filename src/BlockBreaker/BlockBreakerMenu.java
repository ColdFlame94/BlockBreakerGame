package BlockBreaker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class BlockBreakerMenu extends JPanel implements ActionListener{
	static AppFrame gameFrame;
	static JFrame menuFrame;
	JPanel panel;
	JButton startGame, help, quit;
	Image menuBackGround, title;
	
	public BlockBreakerMenu() {
		menuFrame=new JFrame();
		panel=new JPanel();
		startGame=new JButton("New Game");
		help=new JButton("Help");
		quit=new JButton("Quit");
		panel.setLayout(new GridLayout(3,1));
		panel.add(startGame);
		panel.add(help);
		panel.add(quit);
		
		startGame.addActionListener(this);
		help.addActionListener(this);
		quit.addActionListener(this);
		
		try {
			menuBackGround=ImageIO.read(new File("Images//Menu.png"));
			title=ImageIO.read(new File("Images//Title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		setLayout(new GridBagLayout());
		add(panel);
		
		menuFrame.setSize(300,300);
		menuFrame.add(this);
		menuFrame.setTitle("Block Breaker");
		menuFrame.setLocationRelativeTo(null);
		menuFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		menuFrame.setVisible(true);
		menuFrame.setResizable(false);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==startGame) //lets you start the game
		{
			gameFrame=new AppFrame(); 
			menuFrame.setVisible(false);
		}
		else if(e.getSource()==help) //This button gives general info about the gameplay
		{
			JOptionPane.showMessageDialog(new JPanel(), "-Rotate the mouse to move the platform from side to side. Left-click on the mouse to launch the ball.\n"
					+ "-If you finished a level without dying, you'll get an extra life.\n"
					+ "-While playing, the game will offer different powerups which you can obtain. Some are good and some are bad and better be avoided. "
					+ "Each powerup has a limited time.\n"
					+ "Good powerups:\n"
					+ "1) Makes the ball go through the blocks without bouncing away from them.\n"
					+ "2) Makes the platform wider.\n"
					+ "3) The ball sticks to the platform everytime it touches the platform which allows you to aim the ball to wherever you want.\n"
					+ "Bad powerups:\n"
					+ "1) Narrows the platform.\n"
					+ "2) Makes the platform slower.\n"
					+ "3)Makes the ball go faster.", "Info",JOptionPane.INFORMATION_MESSAGE);
			
		}
		else if(e.getSource()==quit)//lets you quit the game
		{
			int res=JOptionPane.showConfirmDialog(new JPanel(), "Are you sure?", "Select an Option", JOptionPane.YES_NO_OPTION);
			if(res==JOptionPane.YES_OPTION)
			{
				System.exit(0);
			} 
		}
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(menuBackGround,0,0,getWidth(), getHeight(),null);
		g.drawImage(title,15,10,getWidth()/2+110, getHeight()/2-30,null);
	}
	
	public static void main(String[] args) {
		new BlockBreakerMenu();	
	}
}