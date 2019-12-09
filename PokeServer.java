/**
 * @author Ethan Ruszanowski
 * @version 0.1.0
 * */

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main class
 */
public class PokeServer extends JFrame implements ActionListener {
  // Log
  private JTextArea jtaLog = new JTextArea();
    private JScrollPane jtaLogScroll = new JScrollPane(jtaLog);
  // Chat log
  private JTextArea jtaCLog = new JTextArea();
    private JScrollPane jtaCLogScroll = new JScrollPane(jtaCLog);
  // Buttons
  private JButton jbDisco = new JButton("Disconnect");
  private JButton jbQuit = new JButton("Quit");

  public static void main(String[] args) {
		new PokeServer();
	}

	public PokeServer() {
	   setupWindow();

		this.setVisible(true);
	}

	public void setupWindow() {
    // North JPanel
    JPanel jpNorth = new JPanel(new GridLayout(1, 1));
    this.add(jpNorth, BorderLayout.NORTH);
    // Add components
    jpNorth.add(new JLabel("Log:"));
    jpNorth.add(new JLabel("Chat Log:"));

    // Center JPanel
    JPanel jpCenter = new JPanel(new GridLayout(1, 1));
    this.add(jpCenter, BorderLayout.CENTER);
    // Add components
    jpCenter.add(jtaLogScroll);
    jpCenter.add(jtaCLogScroll);
    // Disable text input for JTextAreas
    jtaLog.setEditable(false);
    jtaCLog.setEditable(false);

    // South JPanel
    JPanel jpSouth = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    this.add(jpSouth, BorderLayout.SOUTH);
    // Add components
    jpSouth.add(jbDisco);
    jpSouth.add(jbQuit);

    // Actions
    jbQuit.addActionListener(this);
    jbDisco.addActionListener(this);

    // Define window
    this.setTitle("PokeServer");
    this.setSize(700, 600);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

  // GUI button switch
  public void actionPerformed(ActionEvent ae) {
    switch(ae.getActionCommand()) {
      case "Disconnect":
        doDisco();
        break;
      case "Quit":
        doDisco();
        doQuit();
        break;
    }
  }

  public void doDisco() {
    // TODO add Disconnect
    System.out.println("I work!");
  }

  public void doQuit() {
    // TODO Add quit
  }
}
