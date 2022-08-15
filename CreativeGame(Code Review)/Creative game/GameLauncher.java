public class GameLauncher{ 
 /**
  * Only CHANGE boolean values constants BASE/DEMO (l.26-27)
  *     - to view the demo: DEMO = true;
  *     - to play your games DEMO = false;
  *
  *     - for running the Base Game: BASE = true;
  *     - for running your Creative Version: BASE = false;
  * 
  * DO NOT ADD CODE TO THIS CLASS
  * 
  * The Scrolling Game is described the Project Handout. 
  *  
  *
  * @author Elodie Fourquet, Sandra Jackson 
  * @date February, 2019
  */
     
   private static ScrollingGame game;
  
  
   public static void main(String[] args) {
      // Construct sized version of the creative game you've written
      game = new PrinceAgbo(10, 10, 0, 0);
      System.out.println("Running student version of the creative game.");
      game.play();
   }

}