import java.awt.event.KeyEvent;
import java.awt.*;

public class PrinceAgbo extends ScrollingGame {
		                           
	private final String USER = "user.gif";   
	private final String BLOCK = "block.jpg";
	private final String BACKGROUND = "background.jpg";
	private final String AVOID = "avoid.jpg";
	private final String FINAL_WIN = "finalwin.gif";
	private final String WIN = "win.gif";
	private final String SPLASH = "splash.gif";
	private final String LOST = "lost.gif";
	private final String INTRO = "intro.gif";
	
	private String TREASURE = "treasure1.gif";;
	private String[] next = {BLOCK, TREASURE, BLOCK, BLOCK, BLOCK, BLOCK};
	private int screenNum;//keep track of number of screenshots
	private int numDPress;//to highlight and un-highlight the lines

	public PrinceAgbo() {
		this(H_DIM, W_DIM, U_ROW, U_COL);
	}
		
	public PrinceAgbo(int hdim, int wdim, int urow, int ucol) {
		super.init(hdim, wdim, urow, ucol);
	}
	
	/******************** Methods **********************/
	
	// update game state to reflect adding in new cells in the right-most column
	public void populateRightEdge() {
		for(int k = 0; k < grid.getNumRows(); k++){
			int n = rand.nextInt(next.length);
		    grid.setCellImage(new Location(k, grid.getNumCols() - 1), next[n]); 
		}
	}
	
	// updates the game state to reflect scrolling left by one column
	public void scrollLeft() {
		for(int i = 0; i < grid.getNumRows(); i++){ 
  	  	  for(int j = 1; j < grid.getNumCols(); j++){ 
  	  	  	  grid.setCellImage(new Location(i, j - 1), grid.getCellImage(new Location(i, j)));
  	  	  }  	  	  
  	  	}
  	  	userCol--;
		handleCollision(1);
	} 
	
	//initializes the state of the screen
	public void initScreen(){
		grid.setSplash(null);
		for(int i = 0; i < grid.getNumRows(); i++){ 
  	  	  for(int j = 0; j < grid.getNumCols(); j++){ 
  	  	  	  grid.setCellImage(new Location(i, j), BLOCK);
  	  	  }  	  	  
  	  	}
  	  	initTreasures();
	}
	
	//intializes the location of treasures
	public void initTreasures(){
		for(int i = 0; i < grid.getNumRows(); i++){ 
			int trLoc = rand.nextInt(grid.getNumCols());
  	  	  for(int j = 0; j < grid.getNumCols(); j++){ 
  	  	  	  if (j == trLoc && (j != userCol && i != userRow)){
  	  	  	  	  grid.setCellImage(new Location(i, j), TREASURE);
  	  	  	  }
  	  	  }  	  	  
  	  	}	
	}

	
	// returns the user image
	public String getUserImg() {
		return USER;
	}
	
	/* 
	 * handle a collision between the user and an object in the game
	 */  
	public void handleCollision(int n) {
		if(grid.getCellImage(new Location(userRow, userCol)) != null){			
			if (grid.getCellImage(new Location(userRow, userCol)).equals(TREASURE)){
				score ++;
				setUserImg(); 
			}
			else if (grid.getCellImage(new Location(userRow, userCol)).equals(BLOCK)) {				
				checkCollideWithAvoid(n);
			}
		}
		else{
			setUserImg();
		}	
	}
	
	//Sets the user's image at it's cuurent location.
	private void setUserImg(){
		grid.setCellImage(new Location(userRow, userCol), getUserImg());
	}
	
	
	//Resets the user's current location image to the passed parameter.
	private void setUserImg(String n){
		grid.setCellImage(new Location(userRow, userCol), n);
	}
	
	//checks if the user collided with an avoid object or not
	private void checkCollideWithAvoid(int n){
		int harder1 = -1;
		int harder2 = -1;
		if (level >= FINAL_LEVEL - 2){
			harder1 =  rand.nextInt(grid.getNumRows());
			harder2 = rand.nextInt(grid.getNumCols());
		}
		if (n == 1){//when user movement is along the same row, but different column
			if(rand.nextInt(grid.getNumRows()) == userRow || harder1 == userRow){
				burnt();						
			}
			else{
				setUserImg();
			}
		}
		else if(n == 2){//when user movement is along the same column, but different rows
			if(rand.nextInt(grid.getNumCols()) == userCol || harder2 == userCol){
				burnt();						
			}
			else{
				setUserImg();
			}
		}	
	}
	
	//handles collision with avoid object
	private void burnt(){
		setUserImg(AVOID);
		grid.setTitle("You've been burnt");
		lifeline--;
		grid.sleep(sleep);
		grid.setTitle("Level: " + level + " Score:  " + getScore() + " Lifeline: " + lifeline);
		setUserImg();
	}
	
	//---------------------------------------------------//
	
	// handles actions upon mouse click in game
	public void handleMouseClick() {//not used in game play, legacy code
		
		Location loc = grid.checkLastLocationClicked();
		
		if (loc != null){
			System.out.println("You clicked on a square " + loc);
		}
	
	}
	
	//handles actions upon mouse click before, in between(levels) or after gameplay
	public void handleClickOutsideGame(){
		Location loc = grid.checkLastLocationClicked();
		while(loc == null){
			grid.sleep(sleep);
			loc = grid.checkLastLocationClicked();
		}
		
	}
	
	//handles key presses to restart, go to new level or end game.
	public void handleKeyLevel() {
		int key = grid.checkLastKeyPressed();
		while (key != KeyEvent.VK_Q && key != KeyEvent.VK_N && key != KeyEvent.VK_S && key != KeyEvent.VK_R){
			System.out.print("");
			key = grid.checkLastKeyPressed();
		}
		keyConditions(key);		
		initLevel();
		play();
	}
	
	//intializes game screen and fields when the user goes to a new level or restarts
	private void initLevel(){
		score = 0;
		userCol = 0;
		userRow = 0;
		lifeline = life;
		initScreen();
	}
	
	//decides the next step to take when a key is pressed after the game or a level is completed.
	private void keyConditions(int key){
		if (key == KeyEvent.VK_S){
			System.out.println("could save the screen: add the call");
			grid.save("PrinceAgboScreenshot" + screenNum + ".gif");
			screenNum++;
			handleKeyLevel();
		}
		else if (key == KeyEvent.VK_Q)
			System.exit(0);
		else if(key == KeyEvent.VK_N && score == finalScore){
			finalScore += scoreIncrement;
			life += lifeIncrement;	
			level++;
		}
		else if (key == KeyEvent.VK_R){
			finalScore = initialScore;
			life = INITIAL_LIFE;
			level = 1;
		}
		else{
			handleKeyLevel();
		}
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
			System.out.println("could save the screen: add the call");
			grid.save("PrinceAgboScreenshot" + screenNum + ".gif");
			screenNum++;
		}
		else if(key == KeyEvent.VK_LEFT && userCol > 0){
			setUserImg(null);
			userCol--;
			handleCollision(1);
		}
		else if(key == KeyEvent.VK_RIGHT && userCol < grid.getNumCols() - 2){
			setUserImg(null);
			userCol++;
			handleCollision(1);
		}
		else if(key == KeyEvent.VK_DOWN && userRow < grid.getNumRows() - 1 ){
			setUserImg(null);
			userRow++;
			handleCollision(2);	
		}
		else if(key == KeyEvent.VK_UP && userRow > 0 ){
			setUserImg(null);
			userRow--;
			handleCollision(2);	
		}
		else if (key == KeyEvent.VK_P){
			int key2 = grid.checkLastKeyPressed();
			while (key2 != KeyEvent.VK_P ){
				grid.sleep(sleep);
				key2 = grid.checkLastKeyPressed();
			}
		}
		else if (key == KeyEvent.VK_D){
			if(numDPress == 0){
				grid.setLineColor(Color.GREEN);
				numDPress++;
			}
			else{
				grid.setLineColor(null);
				numDPress--;
			}				 
		}
		else if (key == KeyEvent.VK_C){
			level = FINAL_LEVEL;
			score = finalScore - 1;
			updateTitle();
		}
	}
	
	// returns the "score" of the game 
	public int getScore() {
		return score;    
	}
	
	//returns number, of allowed collisions with avoid object, remaining
	public int getLifeLine(){
		return lifeline;
	}
	
	// update the title bar of the game window 
	public void updateTitle() {
		grid.setTitle("Level: " + level + "/" + FINAL_LEVEL + " Score:  " + getScore() + "/" + finalScore + " Lifeline: " + lifeline);
	}
	
	// return true if the game is finished, false otherwise
	//      used by play() to terminate the main game loop 
	public boolean isGameOver() {
		if (score == finalScore && level == FINAL_LEVEL){
			displayOutcome(1);
		}
		return(score == finalScore || lifeline == 0);
	}
	
	// displays the intro and instruction screens
	public void displayIntro(){
		grid.setTitle(" Welcome to the CRAFT OF MINING.");
		grid.setSplash(SPLASH);
		handleClickOutsideGame();			
		grid.setTitle(" INSTRUCTIONS.");
		grid.setSplash(INTRO);
		handleClickOutsideGame();		
	}
	
	//initializes game background, the screen and position of the user
	public void start(){
		TREASURE = "treasure" + level % 5 + ".gif";
		next[1] = TREASURE;
		grid.setGameBackground(BACKGROUND);
		grid.setSplash(null);
		initScreen();
		setUserImg();
	}
	
	// Decides the outcome, continuation or restart of the game
	public void displayOutcome(int n) {
		grid.sleep(sleep);
		endingConditions(n);
		grid.sleep(sleep);
		handleKeyLevel();
	}
	
	//Decides the display and message based on the outcome of the game
	private void endingConditions(int n){
		if (n == 1){//n is just a signal that the last level has been completed
			grid.setSplash(FINAL_WIN);
			grid.setTitle("Congratulations, you beat the game!!!");
		}	
		else if (score == finalScore){
			grid.setSplash(WIN);
			grid.setTitle("Congratulations, you passed level " + level );
		}
		else if (lifeline == 0){
			grid.setSplash(LOST);
			grid.setTitle("Too bad, you lost!!!");
		}
	}
	
}