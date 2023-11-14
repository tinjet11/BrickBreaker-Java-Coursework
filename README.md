# COMP2042_CW_hcytl1
# Name - Leong Tin Jet



## New File
Game.fxml
GameSceneController.java
Menu.fxml
MenuController.java
Tutorial.fxml
TutorialController.java
Win.fxml
WinController.java

BallControl.java
Init.java
OnAction.java (interface from GameEngine)

## Existing file
Score.java
- I add a constructor,which have main instance as parameter
- I  add multiple thread to each function to prevent the UI from being lagged
- I  change the way that the score show when one block get hitted, using transition

Block.java
- 

BlockSerializable.java
- no changes

Main.java
- 

GameEngine.java
* inside stop() method, i change all the thread.stop() to thread.interrupt
* move OnAction interface to its own file
* 


General refactoring work that i have done
- fixing typo issue to have a more readable code
- replace name  to lambda expression 
- set multiple field to final and static


Menu added, with a start button and a image




implement the pause and resume button
change the logic of destory block count to remaining block count,cause the previos logic will have error when
it compare destroyblockcount == block.size, if after pause the game , the block.size is 4, and the destroyblockcount is 
also 4, it will go to nextlevel, which is not what we want

implement mvc pattern in the code, successfully separate the ui code with the main.java using fxml and controller

fix one bug that is sometime when the ball touch the bottom wall, it will stuck there and wont go up, and keep decreasing the heart count

https://www.reddit.com/r/javahelp/comments/4pnbuk/javafx_constructor_parameters_for_controller/
find the solution of cannot have parameter for controller class from the above link, which solve my issue when implementing mvc pattern because 
i need to passing the primary stage and instance of Main class to other controller class

one bug that i face after implement mvc pattern is when the gold ball is hit,there will abnormal scenario that the right and bottom part of the scene will not
become gold background, instead it will become a "bouncing wall" which whenever the ball or the platform hit the right wall, that part will bounce to right, after 
that it will bounce back to left
-fix already



Compilation Instructions: 
Provide a clear, step-by-step guide on how to compile the
code to produce the application. Include any dependencies or special settings
required.

Implemented and Working Properly: 
List the features that have been successfully
implemented and are functioning as expected. Provide a brief description of each.

Implemented but Not Working Properly: 
List any features that have been
implemented but are not working correctly. Explain the issues you encountered,
and if possible, the steps you took to address them.

Features Not Implemented: 
Identify any features that you were unable to
implement and provide a clear explanation for why they were left out.


New Java Classes: 
Enumerate any new Java classes that you introduced for the
assignment. Include a brief description of each class's purpose and its location in the
code.

Modified Java Classes: 
List the Java classes you modified from the provided code
base. Describe the changes you made and explain why these modifications were
necessary.

Unexpected Problems: 
Communicate any unexpected challenges or issues you
encountered during the assignment. Describe how you addressed or attempted to
resolve them.



