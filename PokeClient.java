/**
* @author Alex Twomey, Ethan Ruszanowski
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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
* Main class
*/
public class PokeClient extends JFrame implements ActionListener {
//GUI ATTRIBUTES------------------------------------------

   // Create JButtons
   private JButton jbFight = new JButton("Fight");
   private JButton jbRun = new JButton("Run");
   private JButton jbSend = new JButton("Send");
   private JButton jbOne = new JButton("One");
   private JButton jbTwo = new JButton("Two");
   private JButton jbThree = new JButton("Three");
   private JButton jbFour = new JButton("Four");
   private JButton jBattle;
   private JButton jbConfirm;
   //Attribute for lobby list
   private JPanel jpList;
   private JTextArea jtList;
   private JComboBox nameSelect;
   private DefaultComboBoxModel model = new DefaultComboBoxModel();
   // Output text area
   private JTextArea jtaOut = new JTextArea(2, 10);
   private JScrollPane jspOut = new JScrollPane(jtaOut);
   // Chat assets
   private JTextPane jtaChat = new JTextPane();
   private JScrollPane jspChat = new JScrollPane(jtaChat);
   private JFrame chat;
   // Message box
   private JTextArea jtaMessageBox = new JTextArea(2, 10);
   private JScrollPane jspMessageBox = new JScrollPane(jtaMessageBox);
   //holders for updating graphics
   private String yourPokemon = "";
   private String theirPokemon = "";
   // Global JFrame for choosing Pokemon
   private JFrame choosePokemonFrame;
   // Global JCheckBox's
   private JCheckBox jcbAbsol;
   private JCheckBox jcbBulb;
   private JCheckBox jcbCharizard;
   private JCheckBox jcbCyndaquill;
   private JCheckBox jcbDrifblim;
   private JCheckBox jcbFeraligatr;
   private JCheckBox jcbGardevoir;
   private JCheckBox jcbKadabra;
   private JCheckBox jcbMilotic;
   private JCheckBox jcbPikachu;
   private JCheckBox jcbScizor;
   private JCheckBox jcbScolipede;
   //JFrame for game window
   private JFrame jfGame;

//END OF GUI ATTRIBUTES------------------------------------

//NETWORKING ATTRIBUTES------------------------------------

   //networking attributes
   private JTextPane myReadArea;
   private Socket s;
   private Socket s2;
   private Socket s3;
   private boolean connected = false;
   private String ipaddress = "null";
   private String name= "null";
   private int PORT = 27015;
   private int PORT2 = 27016;
   private int PORT3 = 27017;
   private OutputStream out;
   private PrintWriter pout;
   private boolean listFlag = false;
   private ArrayList<String> cNames = new ArrayList<String>();
   private boolean battling = false;

//END OF NETWORKING ATTRUBITES-----------------------------

//LOGIC ATTRIBUTES-----------------------------------------
   //music thread atrribute
   ThreadMusic tm;
   //stop music thread attribute
   boolean musicContinue;
   //synchronized assets
   String syncMe = "pls pls pls sync meee";
   // ArrayList for chosen Pokemon
   private ArrayList<JCheckBox> jcbList = new ArrayList<JCheckBox>();
   //Array for Poemon names
   private String[] chosenPokemon = new String[6];

//END OF LOGIC ATTRIBUTES----------------------------------

   public static void main(String[] args) {
      new PokeClient();
   }//end of main

   public PokeClient() {
      //START HERE
      //setupChoiceWindow();

      //TEST HERE
      setUpGameWindow();


      //setUpChatWindow();

      //lobbyThreadPrep();
      //setupWindow();


      //setUpGameWindow();

   }//End of PokeClient

//NETWORKING PREP THREAD METHODS--------------------------

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
            }//end of else
         }//end of while
         ipaddress = ipaddress.replace("\n","").replace("\r","");
         ipaddress = ipaddress.trim();
         while(!nameSelected){
            name = JOptionPane.showInputDialog("Enter your name:  ");
            if(name.equals("null")|| name.equals("")){
               JOptionPane.showMessageDialog(null, "Please enter a name");
            }else{
               nameSelected = true;
               //System.out.println(name);
            }//end of else
         }//end of while
         name = name.replace("\n","").replace("\r","");
         name = name.trim();
         Thread chatThread = new ThreadChatClient(ipaddress);
         chatThread.start();

      }catch(Exception e){}
   }//end ofchat thread prep

   //Starting a battle thread
   public void battleThreadPrep(String eN){
      String ee = eN;
      try{
         ThreadBattle battleThread = new ThreadBattle(ee);
         battleThread.start();
      }catch(Exception e){
         e.printStackTrace();
      }
   }

   //Starting lobby thread
   public void lobbyThreadPrep(){
      try{
         Thread lobbyThread = new ThreadLobby();
         lobbyThread.start();
      }catch(Exception e){
         e.printStackTrace();
      }

   }

//End of networking prep thread methods------------------

//GUI THREAD methods--------------------------------------

   public void setUpLobby(){
      JPanel lobby = new JPanel(new BorderLayout());

      lobby.setSize(400, 400);

      JPanel jpTitle = new JPanel(new FlowLayout());
      JLabel jlChoose = new JLabel("Please choose someone to battle!");
      jpTitle.add(jlChoose);

      jpList = new JPanel(new BorderLayout());
      jtList = new JTextArea(15,15);
      jtList.setEditable(false);
      jpList.add(jtList,"Center");

      model = new DefaultComboBoxModel();
      nameSelect = new JComboBox();
      nameSelect.setModel(model);
      jpList.add(nameSelect,"South");

      JPanel jpBattle = new JPanel(new FlowLayout());
      jBattle = new JButton("Battle!");
      jpBattle.add(jBattle);

      lobby.add(jpTitle, "North");
      lobby.add(jpList, "Center");
      lobby.add(jpBattle,"South");

      chat.add(lobby,"East");
      //lobby.setVisible(true);
      jBattle.addActionListener(this);


   }//end of lobby

   public void setUpChatWindow(){
      // Chat window setup
      chat = new JFrame();
      JPanel jpChatSouth = new JPanel(new GridLayout(1, 2));
      chat.setTitle("PokeClient - Chat");
      chat.setSize(800, 400);
      chat.setResizable(false);
      chat.setLocation(300,200);
      // Prevent close from chat window
      chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // Add components to chat center
      JPanel jpChatHolder = new JPanel(new BorderLayout());
      jpChatHolder.setSize(400,400);
      jpChatHolder.add(jspChat, "Center");


      // Add components to jpChatSouth

      jpChatSouth.add(jspMessageBox);
      jpChatSouth.add(jbSend);
      jbSend.addActionListener(this);
      jtaChat.setEditable(false);
      jpChatHolder.add(jpChatSouth, BorderLayout.SOUTH);
      chat.add(jpChatHolder, "Center");


      setUpLobby();
      chatThreadPrep();
      chat.setVisible(true);
   }//end of chat window

   public void setupChoiceWindow() {

      JPanel jpTitle = new JPanel( new FlowLayout());
      JLabel jlChoose = new JLabel("Please choose 6 Pokemon!");
      jpTitle.add(jlChoose);

      // holds all pokemon
      JPanel jpList = new JPanel(new GridLayout( 0, 1));
      // individual JPanels for Pokemon
      // Absol
      JPanel jpAbsol = new JPanel( new GridLayout( 1, 0));
      jcbAbsol = new JCheckBox("Absol");
      JLabel jlAbsolIcon = new JLabel();
      jlAbsolIcon.setIcon( new ImageIcon(getClass().getResource("AbsolICON.png")));
      jpAbsol.add(jcbAbsol);
      jpAbsol.add(jlAbsolIcon);
      jpList.add(jpAbsol);
      //Bulbasaur
      JPanel jpBulb = new JPanel( new GridLayout( 1, 0));
      jcbBulb = new JCheckBox("Bulbasaur");
      JLabel jlBulbIcon = new JLabel();
      jlBulbIcon.setIcon( new ImageIcon(getClass().getResource("BulbasaurICON.png")));
      jpBulb.add(jcbBulb);
      jpBulb.add(jlBulbIcon);
      jpList.add(jpBulb);
      //Charizard
      JPanel jpCharizard = new JPanel( new GridLayout( 1, 0));
      jcbCharizard = new JCheckBox("Charizard");
      JLabel jlCharizardIcon = new JLabel();
      jlCharizardIcon.setIcon( new ImageIcon(getClass().getResource("CharizardICON.png")));
      jpCharizard.add(jcbCharizard);
      jpCharizard.add(jlCharizardIcon);
      jpList.add(jpCharizard);
      //Cyndaquil
      JPanel jpCyndaquill = new JPanel( new GridLayout( 1, 0));
      jcbCyndaquill = new JCheckBox("Cyndaquil");
      JLabel jlCyndaquillIcon = new JLabel();
      jlCyndaquillIcon.setIcon( new ImageIcon(getClass().getResource("CyndiquillICON.png")));
      jpCyndaquill.add(jcbCyndaquill);
      jpCyndaquill.add(jlCyndaquillIcon);
      jpList.add(jpCyndaquill);
      //Drifblim
      JPanel jpDrifblim = new JPanel( new GridLayout( 1, 0));
      jcbDrifblim = new JCheckBox("Drifblim");
      JLabel jlDrifblimIcon = new JLabel();
      jlDrifblimIcon.setIcon( new ImageIcon(getClass().getResource("DrifblimICON.png")));
      jpDrifblim.add(jcbDrifblim);
      jpDrifblim.add(jlDrifblimIcon);
      jpList.add(jpDrifblim);
      //Feraligatr
      JPanel jpFeraligatr = new JPanel( new GridLayout( 1, 0));
      jcbFeraligatr = new JCheckBox("Feraligatr");
      JLabel jlFeraligatrIcon = new JLabel();
      jlFeraligatrIcon.setIcon( new ImageIcon(getClass().getResource("FeraligatrICON.png")));
      jpFeraligatr.add(jcbFeraligatr);
      jpFeraligatr.add(jlFeraligatrIcon);
      jpList.add(jpFeraligatr);
      //Gardevoir
      JPanel jpGardevoir = new JPanel( new GridLayout( 1, 0));
      jcbGardevoir = new JCheckBox("Gardevoir");
      JLabel jlGardevoir = new JLabel();
      jlGardevoir.setIcon( new ImageIcon(getClass().getResource("GardevoirICON.png")));
      jpGardevoir.add(jcbGardevoir);
      jpGardevoir.add(jlGardevoir);
      jpList.add(jpGardevoir);
      //Kadabra
      JPanel jpKadabra = new JPanel( new GridLayout( 1, 0));
      jcbKadabra = new JCheckBox("Kadabra");
      JLabel jlKadabra = new JLabel();
      jlKadabra.setIcon( new ImageIcon(getClass().getResource("KadabraICON.png")));
      jpKadabra.add(jcbKadabra);
      jpKadabra.add(jlKadabra);
      jpList.add(jpKadabra);
      //Milotic
      JPanel jpMilotic = new JPanel( new GridLayout( 1, 0));
      jcbMilotic = new JCheckBox("Milotic");
      JLabel jlMilotic = new JLabel();
      jlMilotic.setIcon( new ImageIcon(getClass().getResource("MiloticICON.png")));
      jpMilotic.add(jcbMilotic);
      jpMilotic.add(jlMilotic);
      jpList.add(jpMilotic);
      //Pikachu
      JPanel jpPikachu = new JPanel( new GridLayout( 1, 0));
      jcbPikachu = new JCheckBox("Pikachu");
      JLabel jlPikachu = new JLabel();
      jlPikachu.setIcon( new ImageIcon(getClass().getResource("PikachuICON.png")));
      jpPikachu.add(jcbPikachu);
      jpPikachu.add(jlPikachu);
      jpList.add(jpPikachu);
      //Scizor
      JPanel jpScizor = new JPanel( new GridLayout( 1, 0));
      jcbScizor = new JCheckBox("Scizor");
      JLabel jlScizor = new JLabel();
      jlScizor.setIcon( new ImageIcon(getClass().getResource("ScizorICON.png")));
      jpScizor.add(jcbScizor);
      jpScizor.add(jlScizor);
      jpList.add(jpScizor);
      //Scolipede
      JPanel jpScolipede = new JPanel( new GridLayout( 1, 0));
      jcbScolipede = new JCheckBox("Scolipede");
      JLabel jlScolipede = new JLabel();
      jlScolipede.setIcon( new ImageIcon(getClass().getResource("ScolipedeICON.png")));
      jpScolipede.add(jcbScolipede);
      jpScolipede.add(jlScolipede);
      jpList.add(jpScolipede);

      // South Panel
      JPanel jpConfirm = new JPanel();
      jbConfirm = new JButton("Confirm");
      jpConfirm.add(jbConfirm);
      jbConfirm.addActionListener( this);

      // Choose Pokemon window setup
      choosePokemonFrame = new JFrame();
      choosePokemonFrame.setTitle("Choose Your Pokemon!");
      choosePokemonFrame.setResizable(false);
      choosePokemonFrame.setLocationRelativeTo(null);
      // Prevent close from chat window
      choosePokemonFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      //add to choosePokemonFrame
      choosePokemonFrame.add(jpTitle, BorderLayout.NORTH);
      choosePokemonFrame.add(jpList, BorderLayout.CENTER);
      choosePokemonFrame.add(jpConfirm, BorderLayout.SOUTH);
      // pack frame to match dimensions
      choosePokemonFrame.pack();

      choosePokemonFrame.addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
         public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            System.exit(0);
         }//end of window Closing
      });

      choosePokemonFrame.setVisible(true);
   }//end of choice window

   public void setUpGameWindow(){
      //starts music thread
      tm = new ThreadMusic();
      //tm.start();

      jfGame = new JFrame();

      JPanel jpSouth = new JPanel(new GridLayout(1, 2));
      JPanel jpRunFight = new JPanel(new GridLayout(2, 3));

      /*

      JPanel friendlyPokemon = new JPanel( new FlowLayout());
      JPanel enemyPokemon = new JPanel( new FlowLayout());

      //POKEMOOON
      // Absol
      JLabel AbsolFRONT = new JLabel();
      AbsolFRONT.setIcon( new ImageIcon(getClass().getResource("AbsolFRONT.png")));
      JLabel AbsolBACK = new JLabel();
      AbsolBACK.setIcon( new ImageIcon(getClass().getResource("AbsolBACK.png")));
      // Bulbasaur
      JLabel BulbasaurFRONT = new JLabel();
      BulbasaurFRONT.setIcon( new ImageIcon(getClass().getResource("BulbasaurFRONT.png")));
      JLabel BulbasaurBACK = new JLabel();
      BulbasaurBACK.setIcon( new ImageIcon(getClass().getResource("BulbasaurBACK.png")));
      // Charizard
      JLabel CharizardFRONT = new JLabel();
      CharizardFRONT.setIcon( new ImageIcon(getClass().getResource("CharizardFRONT.png")));
      JLabel CharizardBACK = new JLabel();
      CharizardBACK.setIcon( new ImageIcon(getClass().getResource("CharizardBACK.png")));
      // Cyndaquill
      JLabel CyndaquillFRONT = new JLabel();
      CyndaquillFRONT.setIcon( new ImageIcon(getClass().getResource("CyndiquillFRONT.png")));
      JLabel CyndaquillBACK = new JLabel();
      CyndaquillBACK.setIcon( new ImageIcon(getClass().getResource("CyndiquillBACK.png")));
      // Drifblim
      JLabel DrifblimFRONT = new JLabel();
      DrifblimFRONT.setIcon( new ImageIcon(getClass().getResource("DrifblimFRONT.png")));
      JLabel DrifblimBACK = new JLabel();
      DrifblimBACK.setIcon( new ImageIcon(getClass().getResource("DrifblimBACK.png")));
      // Feraligatr
      JLabel FeraligatrFRONT = new JLabel();
      FeraligatrFRONT.setIcon( new ImageIcon(getClass().getResource("FeraligatrFRONT.png")));
      JLabel FeraligatrBACK = new JLabel();
      FeraligatrBACK.setIcon( new ImageIcon(getClass().getResource("FeraligatrBACK.png")));
      // Gardevoir
      JLabel GardevoirFRONT = new JLabel();
      GardevoirFRONT.setIcon( new ImageIcon(getClass().getResource("GardevoirFRONT.png")));
      JLabel GardevoirBACK = new JLabel();
      GardevoirBACK.setIcon( new ImageIcon(getClass().getResource("GardevoirBACK.png")));
      // Kadabra
      JLabel KadabraFRONT = new JLabel();
      KadabraFRONT.setIcon( new ImageIcon(getClass().getResource("KadabraFRONT.png")));
      JLabel KadabraBACK = new JLabel();
      KadabraBACK.setIcon( new ImageIcon(getClass().getResource("KadabraBACK.png")));
      // Milotic
      JLabel MiloticFRONT = new JLabel();
      MiloticFRONT.setIcon( new ImageIcon(getClass().getResource("MiloticFRONT.png")));
      JLabel MiloticBACK = new JLabel();
      MiloticBACK.setIcon( new ImageIcon(getClass().getResource("MiloticBACK.png")));
      // Pikachu
      JLabel PikachuFRONT = new JLabel();
      PikachuFRONT.setIcon( new ImageIcon(getClass().getResource("PikachuFRONT.png")));
      JLabel PikachuBACK = new JLabel();
      PikachuBACK.setIcon( new ImageIcon(getClass().getResource("PikachuBACK.png")));
      // Scizor
      JLabel ScizorFRONT = new JLabel();
      ScizorFRONT.setIcon( new ImageIcon(getClass().getResource("ScizorFRONT.png")));
      JLabel ScizorBACK = new JLabel();
      ScizorBACK.setIcon( new ImageIcon(getClass().getResource("ScizorBACK.png")));
      // Scolipede
      JLabel ScolipedeFRONT = new JLabel();
      ScolipedeFRONT.setIcon( new ImageIcon(getClass().getResource("ScolipedeFRONT.png")));
      JLabel ScolipedeBACK = new JLabel();
      ScolipedeBACK.setIcon( new ImageIcon(getClass().getResource("ScolipedeBACK.png")));

      yourPokemon = "Absol";
      theirPokemon = "Charizard";

      if( yourPokemon.equals("Absol")) {
          friendlyPokemon.add( AbsolBACK);
      }
      if( yourPokemon.equals( "Bulbasaur")) {
         friendlyPokemon.add( BulbasaurBACK);
      }
      if( yourPokemon.equals( "Charizard")) {
         friendlyPokemon.add( CharizardBACK);
      }
      if( yourPokemon.equals( "Cyndaquill")) {
         friendlyPokemon.add( CyndaquillBACK);
      }
      if( yourPokemon.equals( "Drifblim")) {
         friendlyPokemon.add( DrifblimBACK);
      }
      if( yourPokemon.equals( "Feraligatr")) {
         friendlyPokemon.add( FeraligatrBACK);
      }
      if( yourPokemon.equals( "Gardevoir")) {
         friendlyPokemon.add( GardevoirBACK);
      }
      if( yourPokemon.equals( "Kadabra")) {
         friendlyPokemon.add( KadabraBACK);
      }
      if( yourPokemon.equals( "MiloticFRONT")) {
         friendlyPokemon.add( MiloticBACK);
      }
      if( yourPokemon.equals( "Pikachu")) {
         friendlyPokemon.add( PikachuBACK);
      }
      if( yourPokemon.equals( "Scizor")) {
         friendlyPokemon.add( ScizorBACK);
      }
      if( yourPokemon.equals( "Scolipede")) {
         friendlyPokemon.add( ScolipedeBACK);
      }

      //enemy Pokemon
      if( theirPokemon.equals( "Absol")) {
         enemyPokemon.add( AbsolFRONT);
      }
      if( theirPokemon.equals( "Bulbasaur")) {
         enemyPokemon.add( BulbasaurFRONT);
      }
      if( theirPokemon.equals( "Charizard")) {
         enemyPokemon.add( CharizardFRONT);
      }
      if( theirPokemon.equals( "Cyndaquill")) {
         enemyPokemon.add( CyndaquillFRONT);
      }
      if( theirPokemon.equals( "Drifblim")) {
         enemyPokemon.add( DrifblimFRONT);
      }
      if( theirPokemon.equals( "Feraligatr")) {
         enemyPokemon.add( FeraligatrFRONT);
      }
      if( theirPokemon.equals( "Gardevoir")) {
         enemyPokemon.add( GardevoirFRONT);
      }
      if( theirPokemon.equals( "Kadabra")) {
         enemyPokemon.add( KadabraFRONT);
      }
      if( theirPokemon.equals( "MiloticFRONT")) {
         enemyPokemon.add( MiloticFRONT);
      }
      if( theirPokemon.equals( "Pikachu")) {
         enemyPokemon.add( PikachuFRONT);
      }
      if( theirPokemon.equals( "Scizor")) {
         enemyPokemon.add( ScizorFRONT);
      }
      if( theirPokemon.equals( "Scolipede")) {
         enemyPokemon.add( ScolipedeFRONT);
      }


      this.add( friendlyPokemon, BorderLayout.WEST);
      this.add( enemyPokemon, BorderLayout.EAST);

      */
      Icon mPoke = new ImageIcon(getClass().getResource("BulbasaurBACK.png"));
      Icon tPoke = new ImageIcon(getClass().getResource("KadabraFRONT.png"));
      Icon bg = new ImageIcon(getClass().getResource("background.jpg"));

      class PokeField extends JPanel{

         public PokeField(){
            setPreferredSize(new Dimension(480,140));
         }
         protected void paintComponent(Graphics g){
            bg.paintIcon(jfGame,g,-0,0);
            mPoke.paintIcon(jfGame,g,50,90);
            tPoke.paintIcon(jfGame,g,380,10);
         }

      }
      PokeField pf = new PokeField();
      jfGame.add(pf);

      jfGame.add(jpSouth, BorderLayout.SOUTH);

      // Add components to south
      jpSouth.add(jspOut);
      jpSouth.add(jpRunFight);
      jpRunFight.add(jbRun);
      jpRunFight.add(jbOne);
      jpRunFight.add(jbTwo);
      jpRunFight.add(jbFight);
      jpRunFight.add(jbThree);
      jpRunFight.add(jbFour);

      // Add action stuff
      jbFight.addActionListener(this);
      jbRun.addActionListener(this);
      jbSend.addActionListener(this);
      jbOne.addActionListener(this);
      jbTwo.addActionListener(this);
      jbThree.addActionListener(this);
      jbFour.addActionListener(this);

      // Set up jtaOut
      jtaOut.setEditable(false);
      jtaOut.setText("What would you like to do?");

      jfGame.setTitle("PokeClient - Game");
      jfGame.setSize(480, 224);
      jfGame.setResizable(false);
      jfGame.setLocationRelativeTo(chat);
      //jfGame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      jfGame.setVisible(true);
      /*
      this.addWindowListener(new java.awt.event.WindowAdapter() {
         @Override
         public void windowClosing(java.awt.event.WindowEvent windowEvent) {
            JOptionPane.showMessageDialog(
            chat,
            "Please close from the Chat window.",
            "Oops!",
            JOptionPane.INFORMATION_MESSAGE);
         }//end of windowClosing
      });
      */
   }//end of game window

   public void closeGameWindow(){
      this.setVisible(false);
      musicContinue = false;
      jBattle.setEnabled(true);
   }

//End of gui method threads------------------------------

//OTHER METHODS------------------------------------------

   public void remakeComboBox(String[] n){
      String[] reDrawNameList = n;
      model = new DefaultComboBoxModel(reDrawNameList);
      nameSelect.setModel(model);
   }//end of remakecombo

   public void redrawNames(String nS){
      String nameString = nS;
      String[] arrOfName = nameString.split(",");
      String[] arrOfNameSansMe = new String[arrOfName.length-1];
      String namesNewL = "";
      for(int i = 0; i < arrOfName.length; i++){
         if(arrOfName[i].equals(name)){
         namesNewL = namesNewL +"\nMe: "+arrOfName[i]+",";
         }else{
         namesNewL = namesNewL +"\n"+arrOfName[i]+",";
         }//end of else
      }//end of for
      jtList.setText(namesNewL);
      ArrayList<String> al = new ArrayList<String>(Arrays.asList(arrOfName));
      al.remove(name);
      arrOfNameSansMe = al.toArray(arrOfNameSansMe);
      remakeComboBox(arrOfNameSansMe);
   }//end of redrawnames

   public void actionPerformed(ActionEvent ae) {
      Object choice = ae.getSource();
      if(choice.equals(jBattle)){
         String opponent = (String)nameSelect.getSelectedItem();
         //System.out.println(opponent);
         if(opponent != null){
            //System.out.println(opponent);
            ThreadBattle battleThread = new ThreadBattle(opponent);
            battleThread.start();
            jBattle.setEnabled(false);
            //battling = true;
         }//end of inner if
      }else if(choice.equals(jbSend)){
         doSend();
      }else if(choice.equals(jbRun)){
         doRun();
      }
      else if(choice.equals(jbFight)){
         doFight();
      }
      else if(choice.equals(jbConfirm)){
         doConfirm();
      }

   }//end of action listener

   public void music() {
      musicContinue = true;
      try {
         AudioInputStream ais = AudioSystem.getAudioInputStream( new File(
         "BackgroundMusic.wav"));
         Clip clip = AudioSystem.getClip();
         clip.open(ais);

      while(musicContinue){
         clip.start();
         clip.loop(clip.LOOP_CONTINUOUSLY);
      }//end of while
      if(!musicContinue){
         //clip.loop(-1);
         clip.stop();
         clip.flush();
      }
      }catch(Exception e) {
         e.printStackTrace();
      }
   }//end of music

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
   }//end of send

   public void doConfirm() {
      int count = 0;
      int index = 0;

      if(jcbAbsol.isSelected() ) {
         count += 1;
         jcbList.add( jcbAbsol);
      }
      if( jcbBulb.isSelected() ) {
         count += 1;
         jcbList.add( jcbBulb);
      }
      if( jcbCharizard.isSelected() ) {
         count+= 1;
         jcbList.add( jcbCharizard);
      }
      if( jcbCyndaquill.isSelected() ) {
         count += 1;
         jcbList.add( jcbCyndaquill);
      }
      if( jcbDrifblim.isSelected() ) {
         count += 1;
         jcbList.add( jcbDrifblim);
      }
      if( jcbFeraligatr.isSelected() ) {
         count += 1;
         jcbList.add( jcbFeraligatr);
      }
      if( jcbGardevoir.isSelected() ) {
         count += 1;
         jcbList.add( jcbGardevoir);
      }
      if( jcbKadabra.isSelected() ) {
         count += 1;
         jcbList.add( jcbKadabra);
      }
      if( jcbMilotic.isSelected() ) {
         count += 1;
         jcbList.add( jcbMilotic);
      }
      if( jcbPikachu.isSelected() ) {
         count += 1;
         jcbList.add( jcbPikachu);
      }
      if( jcbScizor.isSelected() ) {
         count += 1;
         jcbList.add( jcbScizor);
      }
      if( jcbScolipede.isSelected() ) {
         count += 1;
         jcbList.add( jcbScolipede);
      }
      // too many chosen
      if( count > 6 || count < 6) {
         JOptionPane.showMessageDialog(choosePokemonFrame,"You chose " + count + " Pokemon. Only choose SIX.");
         jcbAbsol.setSelected( false);
         jcbBulb.setSelected( false);
         jcbCharizard.setSelected( false);
         jcbCyndaquill.setSelected( false);
         jcbDrifblim.setSelected( false);
         jcbFeraligatr.setSelected( false);
         jcbGardevoir.setSelected( false);
         jcbKadabra.setSelected( false);
         jcbMilotic.setSelected( false);
         jcbPikachu.setSelected( false);
         jcbScizor.setSelected( false);
         jcbScolipede.setSelected( false);
         jcbList.clear();
      }//end of if
      //just enough chosen
      if( count == 6) {
         for( JCheckBox j : jcbList ) {
            String name = j.getText();
            chosenPokemon[index] = name;
            index += 1;
         }//end of for
         choosePokemonFrame.setVisible(false);
         setUpChatWindow();

         lobbyThreadPrep();

      }//end of if

   } // end doConfirm()

   public void appendToPane(JTextPane tp, String msg, Color c){
      //AppendToPane method taken from internet, so i can change color of text in text pane
      //this is the only code that i have taken from somewhere else and it is only for
      //visual purposes, I had it working with JTextArea initially but i wanted to add
      //color,  and you can see my JTextArea commented out.
      StyleContext sc = StyleContext.getDefaultStyleContext();
      AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

      aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
      aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

      int len = tp.getDocument().getLength();
      tp.setCaretPosition(len);
      tp.setCharacterAttributes(aset, false);
      tp.replaceSelection(msg);
   }

//END OF OTHER METHODS----------------------------------

//THREAD CLASSES========================================
   public class ThreadChatClient extends Thread{
      private String ipA;
      private String display;

      public ThreadChatClient(String ip){
         ipA = ip;
      }//end of constructor

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
         }//end of while
      }//end of run


   }//end of ThredChatClient

   public class ThreadLobby extends Thread{
      private boolean connected1 = true;
      private InputStream in;
      private BufferedReader bin;
      public ThreadLobby(){

      }

      public void run(){
         try{
            s2 = new Socket(ipaddress, PORT2);
            //System.out.println("we made it");
            in = s2.getInputStream();
            bin = new BufferedReader(new InputStreamReader(in));
            out = s2.getOutputStream();
            pout = new PrintWriter(out);
            pout.println(name);
            pout.flush();
            while(connected1){
               in = s2.getInputStream();
               bin = new BufferedReader(new InputStreamReader(in));
               String inS = bin.readLine();
               //send to update list
               if(inS.equals("N")){
                  String holder = bin.readLine();
                  //System.out.println(holder);
                  redrawNames(holder);
                  //sent to challenge to a battle
               }else if(inS.equals("B")){
                  //Start battle thread
                  String challenger = bin.readLine();
                  jtaChat.setEditable(true);
                  String cBy = "\nChallenged! by: "+challenger;
                  appendToPane(jtaChat, cBy, Color.GREEN );
                  jtaChat.setEditable(false);

                  battling = true;

                  if(battling==true){
                     jBattle.setEnabled(false);
                     ThreadBattle battleThread1 = new ThreadBattle(challenger);
                     battleThread1.start();
                     battleThread1.join();
                  }//end of if


               }//end of else if

            }//end of while


         }catch(Exception e){
            System.out.println("NO LONGER WAITING ");
            e.printStackTrace();
            connected = false;
         }
      }//end of run

   }//end of thread lobby

   public class ThreadBattle extends Thread{
      private InputStream in;
      private BufferedReader bin;
      String enemy = "";

      public ThreadBattle(String opp){
         enemy = opp;
      }

      public void run(){
         try{
            s3 = new Socket(ipaddress, PORT3);
            in = s3.getInputStream();
            bin = new BufferedReader(new InputStreamReader(in));
            out = s3.getOutputStream();
            pout = new PrintWriter(out);
            if(battling){
               pout.println("BA");
               pout.flush();
               //System.out.println("send BA");
               pout.println(name);
               pout.flush();
               pout.println(enemy);
               pout.flush();
               String listOfPokemon = "";
               for(int i = 0; i < chosenPokemon.length; i++){
                  listOfPokemon = listOfPokemon + chosenPokemon[i]+",";
               }
               pout.println(listOfPokemon);
               pout.flush();
            }else if(!battling){
               pout.println("NB");
               pout.flush();
               pout.println(name);
               pout.flush();
               pout.println(enemy);
               pout.flush();
               String listOfPokemon = "";
               for(int i = 0; i < chosenPokemon.length; i++){
                  listOfPokemon = listOfPokemon + chosenPokemon[i]+",";
               }
               pout.println(listOfPokemon);
               pout.flush();
               battling = true;

            }


         }catch(Exception e){
            e.printStackTrace();
         }

         try{
            Thread.sleep(1000);
            setUpGameWindow();
            Thread.sleep(5000);
            System.out.println("Set up! "+name);

            /*
            while(battling){
               //BATTLE LOGIC
               in = s3.getInputStream();
               bin = new BufferedReader(new InputStreamReader(in));
               out = s3.getOutputStream();
               pout = new PrintWriter(out);

               String bCase = bin.readLine();
               //System.out.println(bCase+name);
               if(bCase.equals("MOVE")){
                  yourPokemon = bin.readLine();
                  theirPokemon = bin.readLine();
                  Scanner uip = new Scanner(System.in);
                  //System.out.print("1,2,3,4");
                  //String m = uip.nextLine();
                  String m = "1";
                  pout.println(m);
                  pout.flush();
               }
               else if(bCase.equals("EOT")){
                  //UPDATE GRAPHICS
                  try{
                     //Thread.sleep(5000);
                     String poke1Hp = bin.readLine();
                     String poke2Hp = bin.readLine();
                     String outString = poke1Hp+"\n"+poke2Hp;
                     jtaOut.setText(outString);



                  }catch(Exception e){
                     e.printStackTrace();
                  }
               }else if(bCase.equals("OVER")){
                  String poke1Hp = bin.readLine();
                  String poke2Hp = bin.readLine();
                  String outString = poke1Hp+"\n"+poke2Hp;
                  jtaOut.setText(outString);
                  battling=false;
                  jBattle.setEnabled(false);
                  jtaOut.append("\nGAME OVER");

               }

            }//end of while
            */
            /*
            while(battling){
               //BATTLE LOGIC
               in = s3.getInputStream();
               bin = new BufferedReader(new InputStreamReader(in));
               out = s3.getOutputStream();
               pout = new PrintWriter(out);

               String bCase = bin.readLine();
            }
            */
            pout.close();
            out.close();
            bin.close();
            in.close();
         }catch(Exception e){
            e.printStackTrace();
         }
         closeGameWindow();

      }//end of run

   }//end of Thread Battle

   public class ThreadMusic extends Thread{
      public ThreadMusic(){

      }

      public void run(){
            music();
      }
   }
//END OF THREAD CLASSES==================================

}//end of main class
