import java.util.Random;
/* @pokemon Milotic
 * @version 1
 *
 * @original_author Alex Twomey
 *@author Tyler Repard
 */



public class Milotic extends Pokemon{
   private int CONSTANT = 22;

   public Milotic(){
      //Change all of these <<<<<<<<<<<<<
      setName("Milotic");
      setHp(285);
      setAtt(60);
      setDef(79);
      setSpd(81);

      setType("Water");

      setM1PP(15);
      setM2PP(5);
      setM3PP(10);
      setM4PP(5);
      
      setM1Name("Surf");
      setM2Name("Hydro Pump");
      setM3Name("Recover");
      setM4Name("Rain Dance");
   }

   public void move1(Pokemon enemy){
      //CHANGE NAME v<<<<<<<<<<<<<
      //Surf

      //DO NOT CHANGE THESE 5 v
      int randCrit = (int)(22 * Math.random()+1);
      boolean crit = false;
      boolean superEffective = false;
      boolean reduced = false;
      boolean moveHit = true;

      //CHANGE ATTACK TO FALSE IF MOVE DOES NO DAMAGE<<<<<<<<<<<<<
      boolean attack = true;
      //CHANGE TYPE TO TYPE OF MOVE POWER AND ACCURACY<<<<<<<<<<<<<
      String type = "Water";
      int power = 90;
      int accuracy = 100;

      //DO NOT CHANGE BELOW THIS
      int damage = ((CONSTANT * power * ( getAtt() / enemy.getDef() )) /50);
      if(randCrit == CONSTANT){
         damage = damage * 2;
         crit = true;
      }
      if(checkSE(type,enemy.getType())){
         damage = damage * 2;
         superEffective = true;
      }
      if(checkNVE(type, enemy.getType())){
         damage = damage / 2;
         reduced = true;
      }
      if(attack  && hit(accuracy)){
         enemy.lowerHp(damage);
      }
      else{
         if(hit(accuracy)){
            //DO STATUS EFFECT STUFF HERE (ill figure out later)
         }

      }
      lowerM1PP();
   }
   public void move2(Pokemon enemy){
      //CHANGE NAME v <<<<<<<<<<<<<
      // Hydropump

      //DO NOT CHANGE THESE 5 v
      boolean crit = false;
      boolean superEffective = false;
      boolean reduced = false;
      boolean moveHit = true;

      int randCrit = (int)(22 * Math.random()+1);

      //CHANGE ATTACK TO FALSE IF MOVE DOES NO DAMAGE<<<<<<<<<<<<<
      boolean attack = true;
       //CHANGE TYPE TO TYPE OF MOVE POWER AND ACCURACY<<<<<<<<<<<<<
      String type = "Water";
      int power = 110;
      int accuracy = 80;

      //DO NOT CHANGE BELOW THIS
      int damage = ((CONSTANT * power * ( getAtt() / enemy.getDef() )) /50);
      if(randCrit == CONSTANT){
         damage = damage * 2;
         crit = true;
      }
      if(checkSE(type,enemy.getType())){
         damage = damage * 2;
         superEffective = true;
      }
      if(checkNVE(type, enemy.getType())){
         damage = damage / 2;
         reduced = true;
      }
      if(attack  && hit(accuracy)){
         enemy.lowerHp(damage);
      }
      else{
         if(hit(accuracy)){
            //DO STATUS EFFECT STUFF HERE (ill figure out later)
         }
      }
      lowerM2PP();
   }
   public void move3(Pokemon enemy){
      //CHANGE NAME v <<<<<<<<<<<<<
      // Recover
      // *** Restore's up to 50% of yser's max HP *** ***

      //DO NOT CHANGE THESE 5 v
      boolean crit = false;
      boolean superEffective = false;
      boolean reduced = false;
      boolean moveHit = true;
      int randCrit = (int)(22 * Math.random()+1);

      //CHANGE ATTACK TO FALSE IF MOVE DOES NO DAMAGE<<<<<<<<<<<<<
      boolean attack = false;
       //CHANGE TYPE TO TYPE OF MOVE POWER AND ACCURACY<<<<<<<<<<<<<
      String type = "Normal";
      int power = 0;
      int accuracy = 100;

      //DO NOT CHANGE BELOW THIS
      int damage = ((CONSTANT * power * ( getAtt() / enemy.getDef() )) /50);
      if(randCrit == CONSTANT){
         damage = damage * 2;
         crit = true;
      }
      if(checkSE(type,enemy.getType())){
         damage = damage * 2;
         superEffective = true;
      }
      if(checkNVE(type, enemy.getType())){
         damage = damage / 2;
         reduced = true;
      }
      if(attack  && hit(accuracy)){
         enemy.lowerHp(damage);
      }
      else{
         if(hit(accuracy)){
            //DO STATUS EFFECT STUFF HERE (ill figure out later)
         }
      }
      lowerM3PP();
   }
   public void move4(Pokemon enemy){
      //CHANGE NAME v<<<<<<<<<<<<<
      // Rain Dance
      // *** Increases user's damage ***

      //DO NOT CHANGE THESE 5 v
      boolean crit = false;
      boolean superEffective = false;
      boolean reduced = false;
      boolean moveHit = true;
      int randCrit = (int)(22 * Math.random()+1);

      //CHANGE ATTACK TO FALSE IF MOVE DOES NO DAMAGE<<<<<<<<<<<<<
      boolean attack = false;
       //CHANGE TYPE TO TYPE OF MOVE POWER AND ACCURACY<<<<<<<<<<<<<
      String type = "Water";
      int power = 0;
      int accuracy = 100;

      //DO NOT CHANGE BELOW THIS
      int damage = ((CONSTANT * power * ( getAtt() / enemy.getDef() )) /50);
      if(randCrit == CONSTANT){
         damage = damage * 2;
         crit = true;
      }
      if(checkSE(type,enemy.getType())){
         damage = damage * 2;
         superEffective = true;
      }
      if(checkNVE(type, enemy.getType())){
         damage = damage / 2;
         reduced = true;
      }
      if(attack && hit(accuracy)){
         enemy.lowerHp(damage);
      }
      else{
         if(hit(accuracy)){
            //DO STATUS EFFECT STUFF HERE (ill figure out later)
         }
      }
      lowerM4PP();
   }


   //---------------------------------------------------------
   // DO NOT CHANGE BELOW THIS
   //---------------------------------------------------------
   public boolean checkSE(String myType, String theirType){

       if(theirType.equals("Fire")){
         if(myType.equals("Water")||myType.equals("Ground")||myType.equals("Rock")){
            return true;
         }
       }else if(theirType.equals("Water")){
         if(myType.equals("Grass")||myType.equals("Electric")){
            return true;
         }

       }else if(theirType.equals("Psychic")){
         if(myType.equals("Bug")||myType.equals("Ghost")||myType.equals("Dark")){
            return true;
         }

       }else if(theirType.equals("Bug")){
         if(myType.equals("Fire")||myType.equals("Flying")||myType.equals("Rock")){
            return true;
         }
       }else if(theirType.equals("Electric")){
         if(myType.equals("Ground")){
            return true;
         }

       }else if(theirType.equals("Ghost")){
         if(myType.equals("Ghost")||myType.equals("Dark")){
            return true;
         }

       }else if(theirType.equals("Dark")){
         if(myType.equals("Fighting")||myType.equals("Bug")||myType.equals("Fairy")){
            return true;
         }

       }
       else if(theirType.equals("Grass")){
         if(myType.equals("Fire")||myType.equals("Ice")||myType.equals("Poison")||myType.equals("Flying")||myType.equals("Bug")){
            return true;
         }
       }
       return false;

   }



      public boolean checkNVE(String myType, String theirType){
         if(myType.equals("Fire")){
            if(theirType.equals("Water")||theirType.equals("Ground")||theirType.equals("Rock")){
            return true;
         }
         }else if(myType.equals("Water")){
         if(theirType.equals("Grass")||theirType.equals("Electric")){
            return true;
         }

       }else if(myType.equals("Psychic")){
         if(theirType.equals("Bug")||theirType.equals("Ghost")||theirType.equals("Dark")){
            return true;
         }

       }else if(myType.equals("Bug")){
         if(theirType.equals("Fire")||theirType.equals("Flying")||theirType.equals("Rock")){
            return true;
         }
       }else if(myType.equals("Electric")){
         if(theirType.equals("Ground")){
            return true;
         }

       }else if(myType.equals("Ghost")){
         if(theirType.equals("Ghost")||theirType.equals("Dark")){
            return true;
         }

       }else if(myType.equals("Dark")){
         if(theirType.equals("Fighting")||theirType.equals("Bug")||theirType.equals("Fairy")){
            return true;
         }

       }
       else if(myType.equals("Grass")){
         if(theirType.equals("Fire")||theirType.equals("Ice")||theirType.equals("Poison")||theirType.equals("Flying")||theirType.equals("Bug")){
            return true;
         }
       }

         return false;
      }

      public boolean hit(int acc){
         double test = Math.random();
         double accTest = (double)acc;
         accTest = accTest / 100;
         if(test > accTest){
            return false;
         }
         return true;

      }
}
