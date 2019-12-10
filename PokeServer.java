/**
 * @author Ethan Ruszanowski
 * @version 0.1.0
 * */

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import java.text.*;
import java.util.*;

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


   //Network attributes
   private int indexCounter = 0;
   private SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy 'at' hh:mm:ss");
   ServerSocket listener;
   boolean isFinished = false;
   boolean connected = false;
   private String syncMe = "pls sync me...pls?";
   private ArrayList<Socket> connections = new ArrayList<Socket>();
   private int PORT = 27015;


  public static void main(String[] args) {
		new PokeServer();
	}

	public PokeServer() {
	   setupWindow();
      this.setVisible(true);
      serverPrep();

	}
      //set up connections and start threads
   public void serverPrep(){
      try{
         listener = new ServerSocket(PORT);
      }
      catch(Exception e){e.printStackTrace();}

      while(!isFinished){
         try{
            Socket clientSocket = listener.accept();
            ThreadChatServer cliThread = new ThreadChatServer(clientSocket);

            cliThread.start();

         }catch(IOException ioe){
            System.out.println("Exception found on accept. Ignoring. Stack Trace :");
            ioe.printStackTrace();
         }
      }

      try{
         listener.close();
         System.out.println("Server Stopped");
      }catch(Exception IOE){
         System.out.println("Error Found stopping server socket");
         System.exit(-1);
      }
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
    jtaCLog.setWrapStyleWord(true);
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
  }

  public void doQuit() {
    System.exit(0);
  }

  public class ThreadChatServer extends Thread{
      Socket myClientSocket;
      int indexConnection;
      String input;
      boolean runThread = true;

      public ThreadChatServer(Socket s){
         myClientSocket = s;
         synchronized(syncMe){
            connections.add(myClientSocket);
            indexConnection = indexCounter;
            indexCounter++;
         }
      }

      public void run(){
         connected = true;
         InputStream in = null;
         OutputStream out = null;
         BufferedReader bin = null;
         PrintWriter bout = null;
         Date dNow = new Date();
         String dateString = ft.format(dNow);
         jtaLog.append("\nAccepted client address : "+myClientSocket.getInetAddress());
         try{

            //in = myClientSocket.getInputStream();
            out = myClientSocket.getOutputStream();
            //bin = new BufferedReader(new InputStreamReader(in));

            bout = new PrintWriter(out);

            bout.println("Connected to Server at "+dateString);
            bout.flush();
            //bout.close();

         }catch(Exception e){
            e.printStackTrace();
         }

         while(connected){
            //System.out.println("waitin");
            try{
               in = myClientSocket.getInputStream();
               bin = new BufferedReader(new InputStreamReader(in));


               input=bin.readLine();
               //System.out.println("Recieved input:"+input);
               jtaCLog.append("\n"+input);
               //out put area
               for(int i = 0; i < connections.size(); i++){
                  if(i != indexConnection){
                     out = connections.get(i).getOutputStream();
                     bout = new PrintWriter(out);
                     bout.println(input);
                     bout.flush();
                  }
               }


            }
            catch(SocketException e){
               System.out.println("Client closed connection");
               connected = false;
            }
            catch(Exception ee){
            }
         }//end of while

      }//end of run

  }
}
