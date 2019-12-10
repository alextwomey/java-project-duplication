/**
 * @author Ethan Ruszanowski
 * @version 0.2.1
 * */

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main class
 */
public class PokeClient extends JFrame implements ActionListener {
  // Create JButtons
  private JButton jbFight = new JButton("Fight");
  private JButton jbRun = new JButton("Run");
  private JButton jbSend = new JButton("Send");

  // Output text area
  private JTextArea jtaOut = new JTextArea(2, 10);
    private JScrollPane jspOut = new JScrollPane(jtaOut);

  // Chat assets
  private JTextArea jtaChat = new JTextArea();
    private JScrollPane jspChat = new JScrollPane(jtaChat);

  // Message box
  private JTextArea jtaMessageBox = new JTextArea(2, 10);
    private JScrollPane jspMessageBox = new JScrollPane(jtaMessageBox);

  public static void main(String[] args) {
		new PokeClient();
	}

	public PokeClient() {
	   setupWindow();

	   this.setVisible(true);
	}

	public void setupWindow() {
    JPanel jpSouth = new JPanel(new GridLayout(1, 2));
    JPanel jpRunFight = new JPanel(new GridLayout(1, 2));
    JPanel jpChatSouth = new JPanel(new GridLayout(1, 2));
    this.add(jpSouth, BorderLayout.SOUTH);

    // Add components to south
    jpSouth.add(jspOut);
    jpSouth.add(jpRunFight);
    jpRunFight.add(jbRun);
    jpRunFight.add(jbFight);

    // Add action stuff
    jbFight.addActionListener(this);
    jbRun.addActionListener(this);
    jbSend.addActionListener(this);

    // Set up jtaOut
    jtaOut.setEditable(false);
    jtaOut.setText("What would you like to do?");

    this.setTitle("PokeClient - Game");
    this.setSize(480, 224);
    this.setResizable(false);
    this.setLocation(100,100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Chat window setup
    JFrame chat = new JFrame();
    chat.setTitle("PokeClient - Chat");
    chat.setSize(400, 400);
    chat.setResizable(false);
    chat.setLocation(300,200);
    // Prevent close from chat window
    chat.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

    // Add components to chat center
    chat.add(jspChat, BorderLayout.CENTER);

    // Add components to jpChatSouth
    chat.add(jpChatSouth, BorderLayout.SOUTH);
    jpChatSouth.add(jspMessageBox);
    jpChatSouth.add(jbSend);
    jtaChat.setEditable(false);
    jtaChat.setWrapStyleWord(true);
    // jtaMessageBox.setWrapStyleWord(true);

    chat.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
      JOptionPane.showMessageDialog(
        chat,
        "Please close from the game window.",
        "Oops!",
        JOptionPane.INFORMATION_MESSAGE);
      }
    });

    chat.setVisible(true);
	}

  // GUI button switch
  public void actionPerformed(ActionEvent ae) {
    switch(ae.getActionCommand()) {
      case "Fight":
        doFight();
        break;
      case "Run":
        doRun();
        break;
      case "Send":
        doSend();
        break;
    }
  }

  public void doFight() {
    //TODO add run event
  }

  public void doRun() {
    // TODO add fight event
  }

  public void doSend() {
    jtaChat.append("Me: " + jtaMessageBox.getText() + "\n");
    jtaMessageBox.setText("");
    // TODO finish send method
  }
}
