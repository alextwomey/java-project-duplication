/**
 * @author Ethan Ruszanowski
 * @version 0.1.0
 * */

import java.awt.*;
import javax.swing.*;

/**
 * Main class
 */
public class PokeClient extends JFrame {
  // Log
  private JButton jbFight = new JButton("Fight");
  private JButton jbRun = new JButton("Run");

  public static void main(String[] args) {
		new PokeClient();
	}

	public PokeClient() {
	   setupWindow();

		this.setVisible(true);
	}

	public void setupWindow() {
    

    this.setTitle("PokeClient");
    this.setSize(700, 600);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
