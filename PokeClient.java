/**
 * @author Ethan Ruszanowski
 * @version 1.0.1
 * */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.util.*;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;

/**
 * Main class
 */
public class PokeClient extends JFrame implements ActionListener {
  // Create JButtons
  private JButton jbFight = new JButton("Fight");
  private JButton jbRun = new JButton("Run");
  private JButton jbSend = new JButton("Send");

  //networking attributes
  private JTextPane myReadArea;
  private Socket s;
  private Socket s2;
  private Socket sList;
  private boolean connected = false;
  private String ipaddress = "null";
  private String name= "null";
  private int PORT = 27015;
  private int PORT2 = 27016;
  private OutputStream out;
  private PrintWriter pout;
  private boolean listFlag = false;
  private ArrayList<String> cNames = new ArrayList<String>();

  //Attribute for lobby list
  private JPanel jpList;

  // Output text area
  private JTextArea jtaOut = new JTextArea(2, 10);
    private JScrollPane jspOut = new JScrollPane(jtaOut);

  // Chat assets
  private JTextPane jtaChat = new JTextPane();
    private JScrollPane jspChat = new JScrollPane(jtaChat);

  // Message box
  private JTextArea jtaMessageBox = new JTextArea(2, 10);
    private JScrollPane jspMessageBox = new JScrollPane(jtaMessageBox);

  public static void main(String[] args) {
		new PokeClient();
	}

	public PokeClient() {

      chatThreadPrep();
      lobbyThreadPrep();
      setupWindow();
      setUpLobby();
	   this.setVisible(true);
	}
   //Networking chat start area, connects and starts thread
   public void chatThreadPrep(){
      try{
        boolean nameSelected = false;
        boolean ipEntered = false;
        while(!ipEntered){
          ipaddress = JOptionPane.showInputDialog("Enter Ip Address to connect to: ");
          if(ipaddress.equals("null")||ipaddress.equals("")){
            JOptionPane.showMessageDialog(null, "Please enter an Ip address");
          }else{
            ipEntered = true;
            //System.out.println(ipaddress);
          }
        }

        while(!nameSelected){
          name = JOptionPane.showInputDialog("Enter your name:  ");
          if(name.equals("null")|| name.equals("")){
            JOptionPane.showMessageDialog(null, "Please enter a name");
          }else{
            nameSelected = true;
            //System.out.println(name);
          }
        }
        Thread chatThread = new ThreadChatClient(ipaddress);
        chatThread.start();

      }catch(Exception e){}
   }

   public void lobbyThreadPrep(){
     try{
       Thread lobbyThread = new ThreadLobby();
       lobbyThread.start();
     }catch(Exception e){
       e.printStackTrace();
     }

   }
   public void lobbyList(){
     for(int i = 0; i < cNames.size();i++){
       JLabel jlName = new JLabel(cNames.get(i));
       jpList.add(jlName);
     }
   }
   public void setUpLobby(){
     JFrame lobby = new JFrame();
     lobby.setTitle("PokeClient - Lobby");
     lobby.setSize(400, 400);
     lobby.setResizable(false);
     lobby.setLocation(700,200);

     JPanel jpTitle = new JPanel(new FlowLayout());
     JLabel jlChoose = new JLabel("Please choose someone to battle!");
     jpTitle.add(jlChoose);
     lobby.add(jpTitle, "North");

     jpList = new JPanel(new FlowLayout());

     lobby.add(jpList, "Center");


     JPanel jpBattle = new JPanel(new FlowLayout());
     JButton jBattle = new JButton("Battle!");
     jpBattle.add(jBattle);
     lobby.add(jpBattle,"South");

     lobby.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
     lobby.addWindowListener(new java.awt.event.WindowAdapter() {
     @Override
     public void windowClosing(java.awt.event.WindowEvent windowEvent) {
       JOptionPane.showMessageDialog(
         lobby,
         "Please close from the game window.",
         "Oops!",
         JOptionPane.INFORMATION_MESSAGE);
       }
     });

     lobby.setVisible(true);

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
    this.setLocation(500,601);
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
    // jtaChat.setWrapStyleWord(true);
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
   try{
      out = s.getOutputStream();
      pout = new PrintWriter(out);
      pout.println(name+": "+jtaMessageBox.getText().trim());
      pout.flush();
      String formatOut = String.format("\n%s", "Me: " + jtaMessageBox.getText().trim());
      jtaChat.setEditable(true);
      appendToPane(jtaChat, formatOut,Color.BLUE );
      jtaChat.setEditable(false);
      jtaMessageBox.setText("");
   }catch(Exception eee){}

    //jtaChat.append("Me: " + jtaMessageBox.getText() + "\n");
    //jtaMessageBox.setText("");
    // TODO finish send method
  }

   public class ThreadChatClient extends Thread{
      private String ipA;
      private String display;

      public ThreadChatClient(String ip){
         ipA = ip;


      }

      public void run(){
         try{
            s = new Socket(ipA, PORT);
            connected = true;

         }catch(Exception e){e.printStackTrace();}
         try{
            //recieve date and time from server
            InputStream in = s.getInputStream();
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            display = bin.readLine();
            jtaChat.setEditable(true);
            appendToPane(jtaChat, display,Color.GREEN );
            jtaChat.setEditable(false);

            //send name to server
            out = s.getOutputStream();
            pout = new PrintWriter(out);
            pout.println(name);
            pout.flush();

            }catch(Exception e){e.printStackTrace();}
         while(connected){
            try{
               InputStream in = s.getInputStream();
               BufferedReader bin = new BufferedReader(new InputStreamReader(in));
               display = bin.readLine();
               String outS = String.format("\n%s",display);
               jtaChat.setEditable(true);
               appendToPane(jtaChat, outS ,Color.RED );
               jtaChat.setEditable(false);
            }catch(Exception e){}
         }
      }


   }
   //AppendToPane method taken from internet, so i can change color of text in text pane
      //this is the only code that i have taken from somewhere else and it is only for
      //visual purposes, I had it working with JTextArea initially but i wanted to add
      //color,  and you can see my JTextArea commented out.
   private void appendToPane(JTextPane tp, String msg, Color c)
      {
         StyleContext sc = StyleContext.getDefaultStyleContext();
         AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

         aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
         aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

         int len = tp.getDocument().getLength();
         tp.setCaretPosition(len);
         tp.setCharacterAttributes(aset, false);
         tp.replaceSelection(msg);
       }

   public class ThreadLobby extends Thread{
        private boolean connected1 = true;
         public ThreadLobby(){

         }

         public void run(){
           try{
              s2 = new Socket(ipaddress, PORT2);
              System.out.println("we made it");
              InputStream in = s2.getInputStream();
              BufferedReader bin = new BufferedReader(new InputStreamReader(in));
              

              while(connected1){
                ObjectInputStream ois = new ObjectInputStream(in);
                System.out.println("Wauiting");
                cNames = (ArrayList<String>)ois.readObject();
                listFlag = true;
                while(listFlag){
                  System.out.println(cNames);
                  lobbyList();
                  listFlag = false;
                }

              }
           }catch(Exception e){
             e.printStackTrace();
             connected = false;
           }
         }

       }
}
