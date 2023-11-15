# COMP2042_CW_hcytl1
# Name - Leong Tin Jet


## General refactoring work that I have done
- fixing typo issue to have a more readable code
- use lambda expression
- set multiple field to final and static (to share among classes)
- change variable involve break to paddle (better naming, the previous one is not easy to understand)


## New File

### GameScene.fxml
- move all the UI code to this file
- add a pause button
- move level label to left

### GameSceneController.java
- add a constructor which have main and stage as parameter
- the code to set up the game scene is here, but is call inside Main.java through this controller
- add the ball, block , paddle in this file instead of the Main.java
- Most of the method is moved from Main.java

### Menu.fxml
- add start, tutorial and quit button in here
- add a brick breaker image at the top
- reuse this file when pause button is clicked, and the text of start button will change to resume, the other is same


### MenuController.java
- when start button is click, it will check the text inside the button, if is start then call startGame(),else call loadGame() 
- add a constructor which have main and stage as parameter
- all the onAction functionality of the button in menu is defined here

### Tutorial.fxml
### TutorialController.java 

### Win.fxml
- screen that i add for game winning and losing
- show the score after win or lose
- added play again and quit button 

### WinController.java
- restart() and exit() function is defined here

### BallControl.java
- all the method and field is moved from Main.java to hava a cleaner Main.java file
- change the private variable to public static variable because it is being used among the project, and it is unique
- fix one bug that when collide to left block, gorightball should be equal false

### Init.java
- all the method and field is moved from Main.java to hava a cleaner Main.java file
- move to this file because 

### Actionable.java
- interface that I move from GameEngine.java
- rename the interface from OnAction to Actionable

## Existing file

### Block.java
- change the hit logic so that don't have penetrated issue

### BlockSerializable.java
- rename file to BlockSerialize

### Bonus.java
- change the public variable to private variable
- create getter and setter for some private variable

### GameEngine.java
- inside stop() method, I change all the thread.stop() to thread.interrupt
- move OnAction interface to its own file
- add platform run later to Update() and PhysicsCalculation()
- add a break statement inside the while loop of Update() and PhysicsCalculation() to prevent the program being stucked

### LoadSave.java
- change some variable typo error

### Main.java
- 

### Score.java
- add a constructor,which have main instance as parameter
- utilizes JavaFX's Timeline and KeyFrame for animation instead of creating a separate thread and using Thread.sleep()
- removed unused import statements
- Code formatting is improved, and lambda expressions are used for conciseness in certain places, like the Platform.runLater() calls.
- remove showWin() and showGameOver() method
- change the show() method, utilise the showMessage() method thus prevent any code duplication





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


nextlevel run twice


