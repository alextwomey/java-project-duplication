/*
 *
 * @author Alex Twomey
 *
 */



public class Pokemon{
   //-----------------------------------------------------------
   //name
   private String name = "";
   //number stats
   private int hp = 0;
   private int att = 0;
   private int def = 0;
   //private int acc = 0;
   private int spd = 0;
   private String type = "";
   private int m1PP = 0;
   private int m2PP = 0;
   private int m3PP = 0;
   private int m4PP = 0;
   private String m1Name = "";
   private String m2Name = "";
   private String m3Name = "";
   private String m4Name = "";
   
   
       /*********************
     * Stats Key
     * hp - Health
     * att - Attack
     * def - Defense
     * acc - Accuracy
     * spd - Speed
     ********************/
     
     
     
   //-----------------------------------------------------------
   public Pokemon(){
   /*
      name = statsIn[0];   
      hp = Integer.parseInt(statsIn[1]);
      att = Integer.parseInt(statsIn[2]);      
      def = Integer.parseInt(statsIn[3]);      
      acc = Integer.parseInt(statsIn[4]);      
      spd = Integer.parseInt(statsIn[5]);      
   */  
   }
   
   //-----------------------------------------------------------
   //name getter/setter
   public String getName(){
      return name;
   }
   public void setName(String nameIn){
      name = nameIn;
   }
   //Move 1
   public String getM1Name(){
      return m1Name;
   }
   public void setM1Name(String nameIn){
      m1Name = nameIn;
   }
   //Move 2
   public String getM2Name(){
      return m2Name;
   }
   public void setM2Name(String nameIn){
      m2Name = nameIn;
   }
   //Move 3
   public String getM3Name(){
      return m3Name;
   }
   public void setM3Name(String nameIn){
      m3Name = nameIn;
   }
   //Move 4
   public String getM4Name(){
      return m4Name;
   }
   public void setM4Name(String nameIn){
      m4Name = nameIn;
   }
   
   //-----------------------------------------------------------------
   //PP section 
   public int getM1PP(){
      return m1PP;
   }
   public void setM1PP(int in){
      m1PP = in;
   }
   public void lowerM1PP(){
      m1PP -= 1;
   }
   //
   public int getM2PP(){
      return m2PP;
   }
   public void setM2PP(int in){
      m2PP = in;
   }
   public void lowerM2PP(){
      m2PP -= 1;
   }
   //
   public int getM3PP(){
      return m3PP;
   }
   public void setM3PP(int in){
      m3PP = in;
   }
   public void lowerM3PP(){
      m3PP -= 1;
   }
   //
   public int getM4PP(){
      return m4PP;
   }
   public void setM4PP(int in){
      m4PP = in;
   }
   public void lowerM4PP(){
      m4PP -= 1;
   }
   
   
   
   
   //-----------------------------------------------------
   //Types
   public String getType(){
      return type;
   }
   public void setType(String t){
      type = t;
   }
   //-----------------------------------------------------------
   //att getter/setter/increment/decrement
   public int getAtt(){
      return att;
   }
   public void setAtt(int attIn){
      att = attIn;
   }
   public void raiseAtt(int attUp){
      att += attUp;
   }
   public void lowerAtt(int attDown){
      att -= attDown;
   }
   
   //-----------------------------------------------------------
   //def getter/setter/increment/decrement
   public int getDef(){
      return def;
   }
   public void setDef(int defIn){
      def = defIn;
   }
   public void raiseDef(int defUp){
      def += defUp;
   }
   public void lowerDef(int defDown){
      def -= defDown;
   }
   
   //-----------------------------------------------------------
   //spd getter/setter/increment/decrement
   public int getSpd(){
      return spd;
   }
   public void setSpd(int spdIn){
      spd = spdIn;
   }
   public void raiseSpd(int spdUp){
      spd += spdUp;
   }
   public void lowerSpd(int spdDown){
      spd -= spdDown;
   }
   
   /*
   //acc getter/setter/increment/decrement
   public int getAcc(){
      return acc;
   }
   public void setAcc(int accIn){
      acc = accIn;
   }
   public void raiseAcc(int accUp){
      acc += accUp;
   }
   public void lowerAcc(int accDown){
      acc -= accDown;
   }
   */
   //-----------------------------------------------------------
   //hp getter/setter/increment/decrement
   public int getHp(){
      return hp;
   }
   public void setHp(int hpIn){
      hp = hpIn;
   }
   public void raiseHp(int hpUp){
      hp += hpUp;
   }
   public void lowerHp(int hpDown){
      hp -= hpDown;
   }
   //-----------------------------------------------------------
   //Abstract movelist 
   public void move1(Pokemon enemy){};
   public void move2(Pokemon enemy){};
   public void move3(Pokemon enemy){};
   public void move4(Pokemon enemy){};
}
   /*   
       Steel
       normal
       bug
       poison
       dark
       electric
       water
       fairy
       psychic
       ice
       flying
       ghost
       fire
       grass
       
       fire
       water
       psychic
       bug
       electric
       ghost
       dark
       grass

       
       */