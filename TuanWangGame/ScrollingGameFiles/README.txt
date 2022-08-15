The theme was for something that reminded people of that very old desktop game they had from the early 2000s. I added sound, a help txt that would open when the user pressed h, and a few moving gifs that would give the feeling of motion in the game. 

The code I am most proud of is the sound, and how it was implemented across the code. I read through multiple websites trying to figure out how this thing worked, how the audio was implemented, input streams, input output exceptions, etc. I was running through fields of class instanceOf, getResource, FileInputSctreams to /com, with non stop chaos and misinformation. I nearly downloaded a new API just to do this. In the end, I managed to get it working, and ta dah, it plays audio now! 

There also is the crab transformation and detransformation, how the tokens interact as score to increase and decrease your time to remain as a crab and defeat ninjas. By having a variable that handleCollision could use to tell if I was in crabMode or not, it could easily create a win condition. 

I also included a function that uses Desktop to open a help.txt and pauses the game, so the user can get a better understanding of what is going on. Also crab rave still plays while you read the instructions, so its great.

In ScrollingGame, the audio is implemented there. The method for that contains all the audio related methods and objects locally, and continually plays. Otherwise,
variables are abstracted for TuanWangGame
In TuanWangGame, there are a ton of constants for files, a variable that controls crab Mode, points for crab points, and win and lose conditionals, ninjasDefeated and health, to determine for the end of the game whether you won or lost, using getWin.

UserX and UserY help the program handle collisions, and has been talked about in the milestone. Otherwise, most other variables are just constants or given.