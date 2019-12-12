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
   private Map<String, ArrayList<Pokemon>> partyList = new HashMap<String, ArrayList<Pokemon>>();


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
    //private Map<String, Socket> battlers = new HashMap<String, Socket>();
    private String[] pokeList;
    private String pokeLString;
    private ArrayList<Pokemon> party = new ArrayList<Pokemon>();
    private ArrayList<Pokemon> p1party = new ArrayList<Pokemon>();
    private ArrayList<Pokemon> p2party = new ArrayList<Pokemon>();

    private InputStream in = null;
    private OutputStream out = null;
    private BufferedReader bin = null;
    private PrintWriter bout = null;



    private Socket b1 = null;
    private Socket b2 = null;
    private String b1n = "";
    private String b2n = "";

    public ThreadBattleServer(Socket sB){


      bSocket = sB;
      try{
        //System.out.println("Started");
        //only get input from challenger socket
        in = bSocket.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));
        battlingString = bin.readLine();
        //System.out.println(battlingString);
        if(battlingString.equals("NB")){
          //logic to challenger other user
          cName = bin.readLine();
          enemy = bin.readLine();
          pokeLString = bin.readLine();
          pokeList = pokeLString.split(",");
          System.out.println(pokeLString+cName);
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
          pokeLString = bin.readLine();
          pokeList = pokeLString.split(",");
          System.out.println(pokeLString+cName);
          connectedSocketsBattle.put(cName, bSocket);
          battlers.put(cName,bSocket);
          battling = true;
          jtaLog.append("\nBattle Started between "+cName +" and "+ enemy);

        }

        connectedSocketsLobby.remove(cName);
        connectedSocketsLobby.remove(enemy);
        updateNameList();

        for(int i = 0; i < pokeList.length; i++){
          if(pokeList[i].equals("Pikachu")){
            Pikachu pika = new Pikachu();
            party.add(pika);
          }
          else if(pokeList[i].equals("Charizard")){
            Charizard chari = new Charizard();
            party.add(chari);
          }
        }
        partyList.put(cName,party);
        System.out.println(cName+"ready");
      }catch(Exception e){
        e.printStackTrace();
      }
    }

    public void run(){

      System.out.println(battlers.size());
      try{
        Thread.sleep(1000);
        for(Map.Entry mapElement : battlers.entrySet()){
          String key = (String)mapElement.getKey();
          Socket s = (Socket)mapElement.getValue();

          if(key.equals(enemy)){
            b2 = s;
            b2n = key;
            p2party = partyList.get(key);
          }
          else if(key.equals(cName)){
            b1 = s;
            b1n = key;
            p1party = partyList.get(key);
          }
        }
          //System.out.println(b1n+b2n+"hhh");


          /*
          in = s.getInputStream();
          bin = new BufferedReader(new InputStreamReader(in));
          String ins = bin.readLine();
          if(ins.equals("R")){
            System.out.println(cName+" is ready 2 battle");
          }
          */
          if(battlingString.equals("NB")){
            ThreadBattleLogic tbl = new ThreadBattleLogic(b1,b2,b1n,b2n,p1party,p2party);
            tbl.start();
          }


      }catch(Exception e){
        //e.printStackTrace();
      }
      /*

      */
    }//nd of run

  }//end of that class

  public class ThreadBattleLogic extends Thread{
    private boolean battling = true;
    private Socket p1s = null;
    private Socket p2s = null;
    private String p1n = "";
    private String p2n = "";
    private ArrayList<Pokemon> p1Party = new ArrayList<Pokemon>();
    private ArrayList<Pokemon> p2Party = new ArrayList<Pokemon>();
    private InputStream in1 = null;
    private OutputStream out1 = null;
    private BufferedReader bin1 = null;
    private PrintWriter bout1 = null;

    private InputStream in2 = null;
    private OutputStream out2 = null;
    private BufferedReader bin2 = null;
    private PrintWriter bout2 = null;

    private Pokemon cbp1;
    private Pokemon cbp2;
    private int p1cpi = 0;
    private int p2cpi = 0;

    public ThreadBattleLogic (Socket p1, Socket p2, String n1, String n2, ArrayList<Pokemon> p1p, ArrayList<Pokemon> p2p){
      p1s = p1;
      p2s = p2;
      p1n = n1;
      p2n = n2;
      p1Party = p1p;
      p2Party = p2p;
    }

    public void run(){
      System.out.println("battle starting between "+p1n+p2n);
      try{
        String mc1 = "";
        String mc2 = "";
        in1 = p1s.getInputStream();
        bin1 = new BufferedReader(new InputStreamReader(in1));
        out1 = p1s.getOutputStream();
        bout1 = new PrintWriter(out1);

        in2 = p2s.getInputStream();
        bin2 = new BufferedReader(new InputStreamReader(in2));
        out2 = p2s.getOutputStream();
        bout2 = new PrintWriter(out2);

        cbp1 = p1Party.get(p1cpi);
        cbp2 = p2Party.get(p2cpi);

        while(battling){
          if(cbp1.getSpd()>=cbp2.getSpd()){
            bout1.println("MOVE");
            bout1.flush();
            mc1 = bin1.readLine();
            if(mc1.equals("1")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM1Name());
              cbp1.move1(cbp2);
              //cbp1.lowerM1PP();
            }
            else if(mc1.equals("2")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM2Name());
              cbp1.move2(cbp2);
              //cbp1.lowerM2PP();
            }
            else if(mc1.equals("3")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM3Name());
              cbp1.move3(cbp2);
              //cbp1.lowerM3PP();
            }
            else if(mc1.equals("4")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM4Name());
              cbp1.move4(cbp2);
              //cbp1.lowerM4PP();
            }
            if(!cbp2.getAlive()){
              jtaLog.append("\n"+p2n+"s "+cbp2.getName()+" has fainted!");
              battling = false;
              break;
            }
            //jtaLog.append("\n"+p1n+" has chosen move: "+);
            bout2.println("MOVE");
            bout2.flush();
            mc2 = bin2.readLine();
            if(mc2.equals("1")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM1Name());
              cbp2.move1(cbp1);
            }
            else if(mc2.equals("2")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM2Name());
              cbp2.move2(cbp1);
            }
            else if(mc2.equals("3")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM3Name());
              cbp2.move3(cbp1);
            }
            else if(mc2.equals("4")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM4Name());
              cbp2.move4(cbp1);
            }
            if(!cbp1.getAlive()){
              jtaLog.append("\n"+p1n+"s "+cbp1.getName()+" has fainted!");
              battling = false;
              break;
            }
          }//end of speed check if
          else{
            bout2.println("MOVE");
            bout2.flush();
            mc2 = bin2.readLine();
            if(mc2.equals("1")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM1Name());
              cbp2.move1(cbp1);
            }
            else if(mc2.equals("2")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM2Name());
              cbp2.move2(cbp1);
            }
            else if(mc2.equals("3")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM3Name());
              cbp2.move3(cbp1);
            }
            else if(mc2.equals("4")){
              jtaLog.append("\n"+p2n+" has chosen move: "+cbp2.getM4Name());
              cbp2.move4(cbp1);
            }
            if(!cbp1.getAlive()){
              jtaLog.append("\n"+p1n+"s "+cbp1.getName()+" has fainted!");
              battling = false;
              break;
            }

            bout1.println("MOVE");
            bout1.flush();
            mc1 = bin1.readLine();
            if(mc1.equals("1")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM1Name());
              cbp1.move1(cbp2);
            }
            else if(mc1.equals("2")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM2Name());
              cbp1.move2(cbp2);
            }
            else if(mc1.equals("3")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM3Name());
              cbp1.move3(cbp2);
            }
            else if(mc1.equals("4")){
              jtaLog.append("\n"+p1n+" has chosen move: "+cbp1.getM4Name());
              cbp1.move4(cbp2);
            }
            if(!cbp2.getAlive()){
              jtaLog.append("\n"+p2n+"s "+cbp2.getName()+" has fainted!");
              battling = false;
              break;

            }
          }//end of speed check else

          bout1.println("EOT");
          bout1.flush();
          //send back pokemon health and info, cbp1,cbp2
          bout2.println("EOT");
          bout2.flush();
          //send back pokemon health and info, cbp1,cbp2
          jtaLog.append("\n"+cbp1.getName()+"s health is "+cbp1.getHp());
          jtaLog.append("\n"+cbp2.getName()+"s health is "+cbp2.getHp());
          jtaLog.append("\n");
          Thread.sleep(5000);
        }//end of while

        jtaLog.append("\nBattle Over!");
        bout1.println("OVER");
        bout1.flush();
        bout2.println("OVER");
        bout2.flush();

        connectedSocketsLobby.put(p1n,connectedSocketsBattle.get(p1n));
        connectedSocketsLobby.put(p2n,connectedSocketsBattle.get(p2n));
        updateNameList();
        battlers.remove(p1n);
        battlers.remove(p2n);

      }catch(Exception e){
        //e.printStackTrace();
        jtaLog.append("\nUnexpected disconnect");

      }
    }

  }
}
