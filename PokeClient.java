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

  private JButton jbFight = new JButton("Fight");
  private JButton jbRun = new JButton("Run");

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
    JPanel southPane = new JPanel();
    JPanel jpLeft = new JPanel(new GridLayout(1,1));
    JPanel jpRight = new JPanel(new GridLayout(1,2));

    this.add(southPane, BorderLayout.SOUTH);
    southPane.add(jpLeft, BorderLayout.EAST);
    southPane.add(jpRight, BorderLayout.WEST);

    jpRight.add(jbFight);
    jpRight.add(jbRun);

    jpLeft.add(jspOut);

    this.setTitle("PokeClient");
    this.setSize(700, 600);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
