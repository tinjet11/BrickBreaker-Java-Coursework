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

### Settings.fxml
- add an interface for user to adjust the initial heart number, end Level, background music volume and on/off background music

### SettingsController.java 
- provide the functionality of adjust initial heart number
- provide the functionality of adjust end level
- provide the functionality of turn on/off background music and adjust volume

### BallControl.java
- all the method and field is moved from Main.java to hava a cleaner Main.java file
- change the private variable to public static variable because it is being used among the project, and it is unique
- fix one bug that when collide to left block, gorightball should be equal false

### InitGameComponent.java
- all the method and field is moved from Main.java to hava a cleaner Main.java file
- move to this file because 

### Actionable.java
- interface that I move from GameEngine.java
- rename the interface from OnAction to Actionable

### GameLogicHandler
- implements Actionable
- move all the method that implements Actionable from Main into this file

### GameStateManager
- move loadGame(),saveGame(), restartGame() and nextLevel() from Main into this file
- add a GameState enum to better represent the game state



## Existing file

### Block.java
- change the hit logic so that don't have penetrated issue

### BlockSerialize.java
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

# General Refactoring:

- Fixed typo issues for improved code readability.
- Utilized lambda expressions.
- Set multiple fields to `final` and `static` for sharing among classes.
- Changed variable involving `break` to `paddle` for better naming.

## New Files:

### GameScene.fxml

- Moved all game scene related UI code to this file.
- Added a pause button.
- Moved level label to the left.

### GameSceneController.java

- Added a constructor with `main` and `stage` as parameters.
- Set up the game scene, called inside `Main.java` through this controller.
- Added the ball, block, and paddle in this file instead of `Main.java`.
- Moved most methods from `Main.java`.

### Menu.fxml

- Added start, settings, and quit buttons.
- Added a brick breaker image at the top.
- Reused this file when the pause button is clicked; the text of the start button changes to resume, while the others remain the same.
- Reused this file when the pause button is clicked; the text of the start button changes to Play again, 
  a Vbox is shown which consist of resultLabel and scoreLabel

### MenuController.java

- When the start button is clicked, it checks the text inside the button. If it's "start," it calls `startGame();` otherwise, it calls `loadGame()`.
- Added a constructor with `main` and `stage` as parameters.
- Defined all `onAction` functionality of the buttons in the menu.

### Settings.fxml

- Added an interface for users to adjust the initial heart number, end level, background music volume, and on/off background music.

### SettingsController.java

- Provided functionality to adjust the initial heart number.
- Provided functionality to adjust the end level.
- Provided functionality to turn on/off background music and adjust volume.

### BallControl.java

- Moved all methods and fields from `Main.java` for a cleaner file.
- Changed private variables to public static variables since they are used among the project and are unique.
- Fixed a bug: when colliding with the left block, `goRightBall` should be `false`.

### InitGameComponent.java

- all methods and fields in this file are moved from `Main.java`

### Actionable.java

- Interface moved from `GameEngine.java`.
- Renamed the interface from `OnAction` to `Actionable`.

### GameLogicHandler.java

- Implements `Actionable`.
- Moved all methods that implement `Actionable` from `Main` into this file.

### GameStateManager.java

- Moved `loadGame()`, `saveGame()`, `restartGame()`, and `nextLevel()` from `Main` into this file.
- Added a `GameState` enum to better represent the game state.

## Existing Files:

### Block.java

- Changed the collision logic to avoid penetration issues.

### BlockSerialize.java

- Renamed the file to `BlockSerialize`.

### Bonus.java

- Changed public variables to private variables.
- Created getter and setter methods for some private variables.

### GameEngine.java

- Inside `stop()` method, changed all `thread.stop()` to `thread.interrupt()`.
- Moved `OnAction` interface to its own file.
- Added `Platform.runLater` to `Update()` and `PhysicsCalculation()`.
- Added a break statement inside the while loop of `Update()` and `PhysicsCalculation()` to prevent the program from being stuck.

### LoadSave.java

- Fixed some variable typo errors.

### Main.java

- No specific changes mentioned.

### Score.java

- Added a constructor with `main` instance as a parameter.
- Utilized JavaFX's Timeline and KeyFrame for animation instead of creating a separate thread and using `Thread.sleep()`.
- Removed unused import statements.
- Improved code formatting, and used lambda expressions for conciseness in certain places, like the `Platform.runLater()` calls.
- Removed `showWin()` and `showGameOver()` methods.
- Changed the `show()` method, utilized the `showMessage()` method to prevent any code duplication.

## Additional Information:

- Added a menu with a start button and an image.
- Implemented pause and resume buttons.
- Changed the logic of destroy block count to remaining block count to avoid issues after pausing and resuming the game.
- Implemented the MVC pattern, successfully separating the UI code from `Main.java` using FXML and controllers.
- Fixed a bug where the ball would get stuck at the bottom wall and not go up, causing continuous heart count decrease.
- Found a solution to the issue of not being able to have parameters for controller classes when implementing the MVC pattern. Referenced [link](https://www.reddit.com/r/javahelp/comments/4pnbuk/javafx_constructor_parameters_for_controller/) for the solution.

## Unexpected Problems:

1. **Constructor Parameter for Controller:**
    - **Issue:** Difficulty passing parameters to controller classes.
    - **Solution:** Found a solution by referencing external sources, enabling the passing of the primary stage and Main class instance to other controller classes.

2. **Ball Getting Stuck at Bottom Wall:**
    - **Issue:** Occasionally, the ball would get stuck at the bottom wall.
    - **Solution:** Implemented a fix to address the issue, ensuring that the ball continues its upward trajectory after touching the bottom wall.

3. **Block Count Logic Issue:**
    - **Issue:** The logic for block destruction count had a flaw, especially after pausing and resuming the game.
    - **Solution:** Modified the logic to check the remaining block count rather than the destroyed block count, preventing unintended level progression.

4. **Implementing MVC Pattern:**
    - **Issue:** Difficulty in separating UI code from the main logic using FXML and controllers.
    - **Solution:** Found a solution by referring to external sources, successfully implementing the MVC pattern by separating UI code into FXML files and corresponding controllers.

