import java.util.Random;
import java.applet.*;
import sun.audio.*; 
import java.io.*;


abstract class ScrollingGame {
	// default song: CRAB RAVE. Change the file path here to specify where it is in your computer      
	protected static final String SONG_NAME = "C:/Users/TUAN/Downloads/ScrollingGameFiles/crabRave.wav";
	
	// default number of vertical cells: height of grid
	protected static final int H_DIM = 5;
	
	// default number of horizoantal cells: width of game
	protected static final int W_DIM = 10;
	
	// default location of user at start
	protected static final int U_ROW = 0;
	
	protected int FACTOR = 5;      // you might change that this
	                           // setting or declaration when working on timing
	                           // (speed up and down the game)
	                        
	protected static Random rand = new Random();  //USE ME 
	                                          // don't instantiate me every frame
	
	protected GameGrid grid; // graphical window to display the game (the VIEW)
	                       // a 2D game made of two dimensional array of Cells
	protected int userRow;
	
	protected long msElapsed;		// game clock: number of ms since start of
	                          // game main loop---see play() method
	protected int timerClicks;	// used to space animation and key presses
	protected int timerDelay;		// to control speed of game

	
	
	//---------------- Abstract methods -----------------//
	// methods you must implement in the Base and Creative Versions
	
	// update game state to reflect adding in new cells in the right-most column
	public abstract void populateRightEdge();
	
	// updates the game state to reflect scrolling left by one column
	public abstract void scrollLeft();
	
	// handles result of key press
	public abstract void handleKeyPress();
	
	// handles results of mouse click
	public abstract void handleMouseClick();
	
	// gets the user image for intial game set up
	public abstract String getUserImg();
	
	// checks for game over conditions
	public abstract boolean isGameOver();
	
	// changes the title bar in the game window
	public abstract void updateTitle();
	
	// gives the game introduction
	public abstract void displayIntro();
	
	// handles actions upon game end
	public abstract void displayOutcome(boolean win);
	
	// handles changing the user image to crab 
	public abstract boolean becomeCrab();
	
	// handles changing the user image to human
	public abstract boolean becomeHuman();
	
	// reduces crab power through tokens
	public abstract void reduceTokens();
	
	//checks if the person is in crab mode
	public abstract boolean getCrabMode();
	
	//checks the win condition, and if you won or lose.
	public abstract boolean getWin();
	
	//---------------------------------------------------//
	/*
	This code here was manipulated to have audio play in the JFrame.
	Key notes to mention.
	1.) only plays PURE .wav files. Mp3 files cannot be renamed to .wav and work, even if
	the file extension names are .wav, convert mp3 to wav using external media
	2.) running too many of these causes the program to stop playing the right audio,
	lag the user image, causing the crab to human interaction to have visual glitches, nothing
	is wrong with the code, just the fps
		- due to this, there is only two audio sources, the crab rave song and the pain of 
		the hero or the ninjas
	
	3.) No way to stop audio, unless a AudioStream object is called as a global variable, WHICH
	has its own issues with as.stop(), which should ideally stop the audio. The JFrame will
	freak out and cause all things to freeze, soft locking it.
	*/
	public static void playAudio(String filename){
		InputStream in = null;
		AudioStream as = null;
		try{
			//create audio data source
			in = new FileInputStream(filename);
		}
		catch(FileNotFoundException fnfe){ //required for running, must have an IOExecption and FileNotFoundException for the 
			// sun import audio
		System.out.println("The audio file was not found");
		}
		
		try	{
			//create audio stream from file stream
			as = new AudioStream(in);
		}
		catch(IOException ie){
			System.out.println("Audio stream could not be created");
		}
		
		AudioPlayer.player.start(as);
	}
		
	// Main loop for playing a single game.
	public void play() {
		
		displayIntro();
		grid.setGameBackground(grid.BACKGROUND_IMG);
		
		playAudio(SONG_NAME);
		/* main game loop */
		while (!isGameOver()) {
			
			grid.sleep(timerDelay); 	 // pause for some time (smooth animation)
			msElapsed += timerDelay;	 // count the total amount of time elapsed
			timerClicks++;				 // increment the timer count
		
			
			handleKeyPress();        // update state based on user key press
			handleMouseClick();      // when the game is running: 
			                         // click & read the console output 
			
			
			if (timerClicks % FACTOR == 0) {  // if it's the FACTOR timer tick
				                            // constant 3 initially
				scrollLeft(); // scrolls left,
				populateRightEdge(); // fills right with things
				becomeCrab(); //becomes crab IF conditions are met
				becomeHuman(); //becomes human '' '' '' '' 
				reduceTokens(); // reduces tokens by set amount everytime the game scrolls left.
			}
			
			updateTitle(); // 
			msElapsed += timerDelay;
		}	
		displayOutcome(getWin()); // displays a title screen based on what you did over the game
	}
	
	protected void init(int hdim, int wdim, int urow) {  
		
		/* initialize the game window

				NOTE: look at the various constructors to see what you can do!
				For example:
					grid = new GameGrid(hdim, wdim, Color.MAGENTA);
					
				You need to use the one that takes an image to implement the 
				splashscreen functionality (don't start there, but do it as your
				first extension)
		*/
		grid = new GameGrid(hdim, wdim);   
		
		/* initialize other aspects of game state */

		// animation settings
		timerClicks = 0;
		msElapsed = 0;
		timerDelay = 100;

		// store and initialize user position
		userRow = urow;
		grid.setCellImage(new Location(userRow, 0), getUserImg());
		
		
		updateTitle();
		
	}
	
	
}