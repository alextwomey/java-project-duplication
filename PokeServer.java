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
   private int nameCounter = 0;
   private SimpleDateFormat ft = new SimpleDateFormat ("E, dd MMM yyyy 'at' hh:mm:ss");
   private ServerSocket listener;
   private ServerSocket listener2;
   private ServerSocket listener3;
   Socket cLobbySocket;
   private boolean isFinished = false;
   private boolean connected = false;
   private boolean cod = false;
   private String syncMe = "pls sync me...pls?";
   //private ArrayList<Socket> connections = new ArrayList<Socket>();
   //private ArrayList<Socket> connections2 = new ArrayList<Socket>();
   //private ArrayList<String> names = new ArrayList<String>();
   //private ArrayList<String> connectedNames = new ArrayList<String>();
   private int PORT = 27015;
   private int PORT2 = 27016;
   private int PORT3 = 27017;
   private Map<String, Socket> connectedSocketsChat = new HashMap<String, Socket>();
   private Map<String, Socket> connectedSocketsLobby = new HashMap<String, Socket>();
   private Map<String, Socket> connectedSocketsBattle = new HashMap<String, Socket>();
   private Map<String, Socket> battlers = new HashMap<String, Socket>();

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
         listener2 = new ServerSocket(PORT2);
         listener3 = new ServerSocket(PORT3);
      }
      catch(Exception e){e.printStackTrace();}

      ThreadWaitForBattle twfb = new ThreadWaitForBattle();
      twfb.start();

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

  public void updateNameList(){
    String nameString = "";
    Set set = connectedSocketsLobby.keySet();
    String[] namesArray = new String[set.size()];
    namesArray = (String[])set.toArray(namesArray);
    for(int i = 0; i<namesArray.length;i++){
      nameString = nameString + namesArray[i]+",";

    }
    //System.out.println(nameString);

    try{
      for(Map.Entry mapElement : connectedSocketsLobby.entrySet()){
        String key = (String)mapElement.getKey();
        OutputStream out = null;
        PrintWriter bout = null;

        Socket sOut = (Socket)mapElement.getValue();
        out = sOut.getOutputStream();

        bout = new PrintWriter(out);
        bout.println("N");
        bout.flush();
        bout.println(nameString);
        bout.flush();
      }
      /*
      for(int i = 0; i < connections2.size(); i++){
        //System.out.println("updating name list");
        OutputStream out = null;
        PrintWriter bout = null;
        out = connections2.get(i).getOutputStream();
        bout = new PrintWriter(out);
        bout.println("N");
        bout.flush();
        bout.println(nameString);
        bout.flush();

      }
      */

    }catch(Exception e){
      //e.printStackTrace();
      System.out.println("error updating name list");
    }
  }

  public class ThreadChatServer extends Thread{
      Socket myClientSocket;
      int indexConnection;
      String input;
      boolean runThread = true;
      private String cName;

      public ThreadChatServer(Socket s){


         myClientSocket = s;
         synchronized(syncMe){
            //connections.add(myClientSocket);
            //indexConnection = indexCounter;
            //indexCounter++;
         }
      }

      public void run(){
        try{
          cLobbySocket = listener2.accept();
          ThreadLobbyServer lServThread = new ThreadLobbyServer(cLobbySocket);
          lServThread.start();
        }catch(Exception e){e.printStackTrace();}
        //continue on normally with chat
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

            //recieve name from client and add to list
            in = myClientSocket.getInputStream();
            bin = new BufferedReader(new InputStreamReader(in));
            cName = bin.readLine();
            synchronized(syncMe){
              //names.add(cName);
              //connectedNames.add(cName);
              cod = true;
            }
            //names.get(indexConnection)
            jtaLog.append("\nAdded "+ cName +" to server list");
            connectedSocketsChat.put(cName,myClientSocket);
            updateNameList();
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
               for(Map.Entry mapElement : connectedSocketsChat.entrySet()){
                 String key = (String)mapElement.getKey();
                 if(!key.equals(cName)){
                   Socket sOut = (Socket)mapElement.getValue();
                   out = sOut.getOutputStream();
                   bout = new PrintWriter(out);
                   bout.println(input);
                   bout.flush();
                 }
               }
               /*
               for(int i = 0; i < connectedSocketsChat.size(); i++){
                  if(connectedSocketsChat){

                  }
               }
               */

            }
            catch(SocketException e){
               //System.out.println("Client closed connection");
              // names.get(indexConnection)
               jtaLog.append("\nRemoved "+ cName +" from server list");
               synchronized(syncMe){
                 //connectedNames.remove(indexConnection);
                 connectedSocketsChat.remove(cName);
                 cod = true;
               }
               connected = false;
               updateNameList();
            }
            catch(Exception ee){
            }
         }//end of while

      }//end of run

  }
  //name list thread
  public class ThreadLobbyServer extends Thread{
    private int indexConnection2;
    private Socket s2In;
    private int ic = indexCounter;
    private String cName;

    public ThreadLobbyServer(Socket s2){
      s2In = s2;
      indexConnection2 = ic--;
      cod = true;
      //connections2.add(s2In);

    }
    public void run(){
      //System.out.println("no Me");
      InputStream in = null;
      OutputStream out = null;
      BufferedReader bin = null;
      PrintWriter bout = null;
      boolean connected1 = true;
      //System.out.println("Started!");
      try{
        in = s2In.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
        cName = bin.readLine();
        connectedSocketsLobby.put(cName,s2In);

        /*
        out = s2In.getOutputStream();
        bout = new PrintWriter(out);
        ObjectOutputStream oos = new ObjectOutputStream(out);
        */
        while(connected1){

        }


    }catch(Exception e){
      //e.printStackTrace();
      connected1 = false;
      connectedSocketsLobby.remove(cName);

    }
    }
  }

  public class ThreadWaitForBattle extends Thread{
    public ThreadWaitForBattle(){

    }
    public void run(){
      try{
        while(!isFinished){
        Socket sBattle = listener3.accept();
        ThreadBattleServer bThread = new ThreadBattleServer(sBattle);
        bThread.start();
      }
      }catch(IOException ioe){
         System.out.println("Exception found on accept. Ignoring. Stack Trace :");
         ioe.printStackTrace();
      }
    }

  }

  public class ThreadBattleServer extends Thread{
    private String cName;
    private String enemy;
    private Socket bSocket;
    private String battlingString;
    private boolean battling = false;
    private boolean connected1 = true;
    public ThreadBattleServer(Socket sB){
      InputStream in = null;
      OutputStream out = null;
      BufferedReader bin = null;
      PrintWriter bout = null;

      bSocket = sB;
      try{
        System.out.println("Started");
        //only get input from challenger socket
        in = bSocket.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
        battlingString = bin.readLine();
        System.out.println(battlingString);
        if(battlingString.equals("NB")){
          //logic to challenger other user
          cName = bin.readLine();
          enemy = bin.readLine();
          battlers.put(cName,bSocket);
          jtaLog.append("\n"+cName+" has challenged "+enemy+" to a battle!");
          connectedSocketsBattle.put(cName,(Socket)connectedSocketsLobby.get(cName));
          connectedSocketsBattle.put(enemy,(Socket)connectedSocketsLobby.get(enemy));

          //System.out.println("NameList Updated");

          for(Map.Entry mapElement : connectedSocketsLobby.entrySet()){
            String key = (String)mapElement.getKey();
            if(key.equals(enemy)){
              Socket sOut = (Socket)mapElement.getValue();
              out = sOut.getOutputStream();
              bout = new PrintWriter(out);
              bout.println("B");
              bout.flush();
              bout.println(cName);
              bout.flush();
            }

          }
          battling = true;
        }

        else if(battlingString.equals("BA")){
          //challenged socket
          cName = bin.readLine();
          enemy = bin.readLine();
          System.out.println(cName+ enemy);
          battlers.put(cName, bSocket);
          battling = true;
          jtaLog.append("\nBattle Started between "+cName +" and "+ enemy);

        }

        connectedSocketsLobby.remove(cName);
        connectedSocketsLobby.remove(enemy);
        updateNameList();

      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public void run(){
      System.out.println("Battle Started!");
      while(battling){
        //dn
      }
    }

  }
}
