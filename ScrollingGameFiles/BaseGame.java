import java.awt.event.KeyEvent;
import java.awt.Color;

public class BaseGame extends ScrollingGame {
		                           
	private final String USER_IMG = "user.gif";    // specify user image file
	                                               // ADD others for Avoid/Get items	
    private final String AVOID_IMG = "avoid.gif";
    private final String GET_IMG = "get.gif";
    private final String INTRO_IMG = "ink.png";
    private final String WIN_IMG = "ink.png";
    private final String LOSE_IMG = "ink.png";
    private final int COLOR_NUM = 255;
    
    protected int UserX = 0;
    protected int UserY = 0;
    protected int ninjasDefeated = 0;
    protected int health = 10;
    protected int score = 0;
    protected int pauseTimer = 0;
    
	public BaseGame() {
		this(H_DIM, W_DIM, U_ROW);
	}
		
	public BaseGame(int hdim, int wdim, int urow) {
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
	//so baseGame can stop complaining about abstract
	
	public boolean getWin(){
		return false;
	}
	public void reduceTokens(){
		System.out.println();
	}
	public boolean becomeCrab(){
		System.out.println();
		return false;
	}
	public boolean becomeHuman(){
		System.out.println();
		return false;
	}
	public boolean getCrabMode(){
		return false;
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
				if (x < W_DIM-1 && grid.getCellImage(new Location(y,x))!=USER_IMG){
					if (grid.getCellImage(new Location (y,x+1))==USER_IMG ){ 
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
				if (x == 0 && grid.getCellImage(new Location(y,x+1))==USER_IMG ){ 
					grid.setCellImage((new Location(y,x)),null);
				}
			
				// handles if the screen automatically collides with User 
				
				if (grid.getCellImage(new Location (y,x))==USER_IMG ){ 
					handleCollision(y,x+1);
				}
				
			}
		}
	}
	// returns the user image
	public String getUserImg() {
		return USER_IMG;
	}
	
	/* handleCollision()
	 * handle a collision between the user and an object in the game
	 */    
	public void handleCollision(int ObjectY, int ObjectX) {
		if (grid.getCellImage(new Location(ObjectY, ObjectX))== AVOID_IMG ){
			health--;
		}
		else if (grid.getCellImage(new Location(ObjectY, ObjectX))== GET_IMG){
			score+=10;
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
		
		else if (key == KeyEvent.VK_S){
			grid.save("screenshot.png");
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
				grid.sleep(20);
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
		
		else if (key == KeyEvent.VK_UP ||key == KeyEvent.VK_W){
			if (isValid(new Location(UserY-1, UserX))){
				handleCollision(UserY-1, UserX);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY-1,UserX)),USER_IMG);
				UserY--;
			}
		}                                               
		else if (key == KeyEvent.VK_DOWN){//||key == KeyEvent.VK_S){ //wasd controls cant work without s
			if (isValid(new Location(UserY+1, UserX))){
				handleCollision(UserY+1, UserX);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY+1,UserX)),USER_IMG);
				UserY++;
			}
		}
		else if (key == KeyEvent.VK_RIGHT||key == KeyEvent.VK_D){
			if (isValid(new Location(UserY, UserX+1))){
				handleCollision(UserY, UserX+1);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY,UserX+1)),USER_IMG);
				UserX++;
			}
		}
		else if (key == KeyEvent.VK_LEFT||key == KeyEvent.VK_A){
			if (isValid(new Location(UserY, UserX-1))){
				handleCollision(UserY, UserX-1);
				grid.setCellImage((new Location(UserY,UserX)),null);
				grid.setCellImage((new Location(UserY,UserX-1)),USER_IMG);
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
	
	// update the title bar of the game window 
	public void updateTitle() {
		grid.setTitle("Scrolling Game:  " + getScore());
	}
	
	// return true if the game is finished, false otherwise
	//      used by play() to terminate the main game loop 
	public boolean isGameOver() {
		if (ninjasDefeated == 20){
			return true;
		}
		if (health <= 0){
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
	
	// display the game over screen, blank for now
	public void displayOutcome(boolean win) {
		grid.setSplash(LOSE_IMG);
		while (grid.checkLastLocationClicked ()==null){
			grid.setSplash(LOSE_IMG);
			if (timerClicks % FACTOR == 0 || grid.checkLastLocationClicked ()!= null){
				continue;
				
			}
		}
		System.exit(0);
	}
}