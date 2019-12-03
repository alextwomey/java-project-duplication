



public class Pokemon{
   //name
   private String name = "";
   //number stats
   private int hp = 0;
   private int att = 0;
   private int def = 0;
   private int acc = 0;
   private int spd = 0;

    /*********************
     * Stats Key
     * hp - Health
     * att - Attack
     * def - Defense
     * acc - Accuracy
     * spd - Speed
     ********************/

   public Pokemon(String[] statsIn){
      name = statsIn[0];
      hp = Integer.parseInt(statsIn[1]);
      att = Integer.parseInt(statsIn[2]);
      def = Integer.parseInt(statsIn[3]);
      acc = Integer.parseInt(statsIn[4]);
      spd = Integer.parseInt(statsIn[5]);

   }
   //name getter/setter
   public String getName(){
      return name;
   }
   public void setName(String nameIn){
      name = nameIn;
   }

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
}
