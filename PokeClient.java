/**
 * @author Ethan Ruszanowski
 * @version 0.1.1
 * */

import java.awt.*;
import javax.swing.*;

/**
 * Main class
 */
public class PokeClient extends JFrame {
  // Create JButtons
  private JButton jbFight = new JButton("Fight");
  private JButton jbRun = new JButton("Run");

  // Java text area
  private JTextArea jtaOut = new JTextArea();
    private JScrollPane jspOut = new JScrollPane(jtaOut);

  public static void main(String[] args) {
		new PokeClient();
	}

	public PokeClient() {
	   setupWindow();

		this.setVisible(true);
	}

	public void setupWindow() {
    JPanel jpSouth = new JPanel(new GridLayout(1,2));
    JPanel jpRight = new JPanel(new GridLayout(1, 2));
    this.add(jpSouth, BorderLayout.SOUTH);

    // Add components to south
    jpSouth.add(jspOut);
    jpSouth.add(jpRight);
    jpRight.add(jbRun);
    jpRight.add(jbFight);

    // Set up jtaOut
    jtaOut.setEditable(false);
    jtaOut.setText("What would you like to do?");

    this.setTitle("PokeClient");
    this.setSize(700, 600);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
