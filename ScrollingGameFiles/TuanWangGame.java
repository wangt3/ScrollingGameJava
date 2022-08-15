import java.awt.event.KeyEvent;
import java.awt.Color;
import java.awt.Desktop;
import java.io.*;

public class TuanWangGame extends ScrollingGame {
		                           
	public String userImg = "user1.gif";    // specify user image file
	                                               // ADD others for Avoid/Get items	

	// all files for image names
    private final String AVOID_IMG = "ninja.gif";
    private final String GET_IMG = "crabToken.gif";
    private final String INTRO_IMG = "Intro.png";
    private final String WIN_IMG = "GameOver.png";
    private final String LOSE_IMG = "Victory.png";
    private static int screenshotNmbr = 1;
    
    private final int COLOR_NUM = 255; //change this for different color cell borders
    
    //sound files
    private final String CRAB_INTRO = "C:/Users/TUAN/Downloads/ScrollingGameFiles/speech.wav";
    private final String CRAB_OUTRO = "C:/Users/TUAN/Downloads/ScrollingGameFiles/speech.wav";
    private final String NINJA_PAIN = "C:/Users/TUAN/Downloads/ScrollingGameFiles/ow.wav";
    private final String WIN_SONG = "C:/Users/TUAN/Downloads/ScrollingGameFiles/Victory.wav";
    private final String LOSE_SONG = "C:/Users/TUAN/Downloads/ScrollingGameFiles/Lose.wav";
    
    protected boolean crabMode = false;
    protected int UserX = 0;
    protected int UserY = 0;
    protected int health = 10;
    protected int ninjasDefeated = 0;
    protected int crabTokens = 0;
    protected int score = 0;
    protected int pauseTimer = 0;
    protected boolean lose = false;
    
    //constructor for game. 
	public TuanWangGame() {
		this(H_DIM, W_DIM, U_ROW);
	}
		
	public TuanWangGame(int hdim, int wdim, int urow) {
		super.init(hdim, wdim, urow);
	}
	private boolean isValid(Location loc) {
    
    int row = loc.getRow();
    int col = loc.getCol();
    if (0 <= row && row < grid.getNumRows() && 0 <= col && col < grid.getNumCols())
      return true;  
  	else 
      return false;
    
  }
  
	
	/******************** Methods **********************/
	// changes sprite to become a crab, if certain conditions are met
	public boolean becomeCrab(){
		if (score >= 50 && crabMode == false){ //must have collected 5 crab tokens, each token gives 10 crab points
			playAudio(CRAB_INTRO);
			userImg = "mrKrabs.gif";
			crabMode = true;
			return true;
		}
		else if (score >= 50 && crabMode == true){
			
			return true;
		}
		return false;
	}
	public boolean becomeHuman(){ // changes sprite back to human when you run out of crab points
		if (score == 0 && crabMode == true){
			userImg = "user1.gif";
			crabMode = false;
			playAudio(CRAB_OUTRO);
			return true;
		}
		else if (score == 0 && crabMode == false){
			return true;
		}
		return false;
	}
	
	//returns crabMode
	public boolean getCrabMode(){
		return crabMode;
	}
	
	//deducts crab points if you are in crab mmode by 5.
	public void reduceTokens(){
		if (crabMode){
			score-=5;
		}
	}
	
	
	
	// update game state to reflect adding in new cells in the right-most column
	public void populateRightEdge() {
		
		for (int i = 0; i < H_DIM; i++){
			if (rand.nextInt(10)>7){
				if (rand.nextInt(10)>5){
					grid.setCellImage(new Location(i, W_DIM-1),AVOID_IMG);
				}
				else{
					grid.setCellImage(new Location(i, W_DIM-1),GET_IMG);
				}
			}
			else{
				grid.setCellImage(new Location(i, W_DIM-1),null);
				
			}
		}
	}
	
	// updates the game state to reflect scrolling left by one column
	public void scrollLeft() {
		String tempImageName = "";
		for (int y = 0; y < H_DIM; y++){
			for (int x = 0; x < W_DIM; x++){
				if (x < W_DIM-1 && grid.getCellImage(new Location(y,x))!=userImg){
					if (grid.getCellImage(new Location (y,x+1))==userImg ){ 
						try{
							tempImageName = grid.getCellImage(new Location(y,x));
							grid.setCellImage((new Location(y,x-1)),tempImageName);
							grid.setCellImage((new Location(y,x)),null);
						}
						catch (Exception e){
						}
					}
					else{ 
						tempImageName = grid.getCellImage(new Location(y,x+1));
						grid.setCellImage((new Location(y,x)),tempImageName);
					}  
				}
				//this removes bug where image would be persistant if User was one block
				//ahead of it and the image was about to be deleted. These two lines
				//ensure its deletion
				if (x == 0 && grid.getCellImage(new Location(y,x+1))==userImg ){ 
					grid.setCellImage((new Location(y,x)),null);
				}
			
				// handles if the screen automatically collides with User 
				
				if (grid.getCellImage(new Location (y,x))==userImg){ 
					handleCollision(y,x+1);
				}
				
			}
		}
	}
	// returns the user image
	public String getUserImg() {
		return userImg;
	}
	
	/* handleCollision()
	 * handle a collision between the user and an object in the game
	 */    
	public void handleCollision(int ObjectY, int ObjectX) {
		if (grid.getCellImage(new Location(ObjectY, ObjectX))== AVOID_IMG && crabMode == false){
			health--; // you take damage if you are not in crab mode
			playAudio(NINJA_PAIN);
		}
		else if (grid.getCellImage(new Location(ObjectY, ObjectX))== AVOID_IMG && crabMode == true){
			ninjasDefeated++; // you defeat ninjas if you are in crab mode and run into them
			playAudio(NINJA_PAIN);
		}
		else if (grid.getCellImage(new Location(ObjectY, ObjectX))== GET_IMG){
			score+=10; // collect 1 crab token for 10 points
		}
	}
	
	//---------------------------------------------------//
	
	// handles actions upon mouse click in game
	public void handleMouseClick() {
		
		Location loc = grid.checkLastLocationClicked();
		
		if (loc != null)
			System.out.println("You clicked on a square " + loc);
	
	}
	
	// handles actions upon key press in game
	public void handleKeyPress() {
			
		int key = grid.checkLastKeyPressed();
		
		//use Java constant names for key presses
		//http://docs.oracle.com/javase/7/docs/api/constant-values.html#java.awt.event.KeyEvent.VK_DOWN
		
		// Q for quit
		
		if (key == KeyEvent.VK_Q)
			System.exit(0);
		
		else if (key == KeyEvent.VK_H){
			if (Desktop.isDesktopSupported()) {
				try{
					Desktop.getDesktop().edit(new File("help.txt"));
					key = KeyEvent.VK_COLON;
					while (key != KeyEvent.VK_P){
						pauseTimer++;
						grid.sleep(20); // this must be here, or else checkLastKeyPressed() will update too quickly,
						// and return null, soft locking the game.
						if (pauseTimer % FACTOR == 0){
							key = grid.checkLastKeyPressed();
						}
					}
				}
				catch (IOException e){
					System.out.println("Can't open help");	
					
				}
			} else {
				System.out.println("Hi");
    		}
		}
		else if (key == KeyEvent.VK_S){
			grid.save("screenshot"+screenshotNmbr+".png");
			screenshotNmbr++;
		}
		/* To help you with step 9: 
		   use the 'T' key to help you with implementing speed up/slow down/pause
    	   this prints out a debugging message */
		else if (key == KeyEvent.VK_T)  {
			boolean interval =  (timerClicks % FACTOR == 0);
			System.out.println("timerDelay " + timerDelay + " msElapsed reset " + 
				msElapsed + " interval " + interval);
		}
		else if (key == KeyEvent.VK_P){
			key = KeyEvent.VK_COLON;
			while (key != KeyEvent.VK_P){
				pauseTimer++;
				grid.sleep(20); // this must be here, or else checkLastKeyPressed() will update too quickly,
				// and return null, soft locking the game.
				if (pauseTimer % FACTOR == 0){
					key = grid.checkLastKeyPressed();
				}
			}
		}
		else if (key == KeyEvent.VK_D){
			grid.sleep(20);
			if (grid.getLineColor() ==null){
				System.out.println("Lines On");
				grid.setLineColor(new Color(COLOR_NUM));
			}
			else{
				System.out.println("Lines Off");
				grid.setLineColor(null);
			}
		}
		// wanted to implemeneted wasd controls, but too many keybinds to 
		else if (key == KeyEvent.VK_UP ||key == KeyEvent.VK_W){
			if (isValid(new Location(UserY-1, UserX))){
				handleCollision(UserY-1, UserX);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY-1,UserX)),userImg);
				UserY--;
			}
		}                                               
		else if (key == KeyEvent.VK_DOWN){//||key == KeyEvent.VK_S){ //wasd controls cant work without s
			if (isValid(new Location(UserY+1, UserX))){
				handleCollision(UserY+1, UserX);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY+1,UserX)),userImg);
				UserY++;
			}
		}
		else if (key == KeyEvent.VK_RIGHT){ //||key == KeyEvent.VK_D){
			if (isValid(new Location(UserY, UserX+1)) && UserX+1 != grid.getNumCols()-1){ //prevents user from
				//entering the update zone, so you cant be hit by spawning coins or ninjas.
				handleCollision(UserY, UserX+1);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY,UserX+1)),userImg);
				UserX++;
			}
		}
		else if (key == KeyEvent.VK_LEFT||key == KeyEvent.VK_A){
			if (isValid(new Location(UserY, UserX-1))){
				handleCollision(UserY, UserX-1);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY,UserX-1)),userImg);
				UserX--;
			}
		}
		else if (key == KeyEvent.VK_COMMA){
			FACTOR+=.5;
		}
		else if (key == KeyEvent.VK_PERIOD){
			if (FACTOR >1){
				FACTOR-=.5;
			}
		}
		
	}
	
	// return the "score" of the game 
	private int getScore() {
		return score;    
	}
	private int getNinjas(){
		return ninjasDefeated;
	}
	
	// update the title bar of the game losedow 
	public void updateTitle() {
		grid.setTitle("CRAB POWER:  " + getScore() + "   NINJAS DEFEATED:  " + getNinjas()
			 +"  HEALTH:  "+ health);
	}
	
	// return true if the game is finished, false otherwise
	//      used by play() to terminate the main game loop 
	public boolean isGameOver() {
		if (ninjasDefeated == 10){
			lose = true;
			return true;
		}
		else if (health <= 0){
			lose = false;
			return true;
		}
		return false;
	}
	
	// display the intro screen blank for now
	public void displayIntro(){
		grid.setSplash(INTRO_IMG);
		while (grid.checkLastLocationClicked ()==null){
			grid.setSplash(INTRO_IMG);
			if (timerClicks % FACTOR == 0 || grid.checkLastLocationClicked ()!= null){
				continue;
				
			}
		}
		grid.setSplash(null);
	}
	// returns the win CONDITIONAL
	public boolean getWin(){
		return lose;
	}
	// display the game over screen, blank for now
	public void displayOutcome(boolean lose) {
		if (lose == false){
			grid.setSplash(WIN_IMG);
		}
		else if (lose == true){
			grid.setSplash(LOSE_IMG);
		}
		
		while (grid.checkLastLocationClicked ()==null){
			
			if (timerClicks % FACTOR == 0 || grid.checkLastLocationClicked ()!= null){
				continue;
				
			}
		}
		System.exit(0);
	}
}