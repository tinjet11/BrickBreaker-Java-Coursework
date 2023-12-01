# COMP2042_CW_hcytl1
# Name - Leong Tin Jet



change the logic of destory block count to remaining block count,cause the previos logic will have error when
it compare destroyblockcount == block.size, if after pause the game , the block.size is 4, and the destroyblockcount is 
also 4, it will go to nextlevel, which is not what we want

implement mvc pattern in the code, successfully separate the ui code with the main.java using fxml and controller



Compilation Instructions: 
Provide a clear, step-by-step guide on how to compile the
code to produce the application. Include any dependencies or special settings
required.

Implemented and Working Properly: 
List the features that have been successfully
implemented and are functioning as expected. Provide a brief description of each.
- `Menu Scene` added with `load game`,`start game`, `open settings` and `exit` function, also reused and act as the `Pause`, `Win` and `Lose` Scene
- All time `highest score` can be `record` and will `update` when user win/lose with score higher than the previous highest score
- Add a` pause button` to the game scene, user can `pause` and will open up menu scene, and can `resume` back by clicking resume button in menu scene
- `Settings Scene `added with `adjusting end level`, `initial heart number function`, `also show brief description of Game Item` to user
- Added a `penalty block`, when it is hit, a bomb will drop, if user not able to catch it, `deduct 10 points`
- Added a new block, call `concrete block`, will be created when level reach 10. Player need to hit it 2 time to be able to destroy it, first hit will convert it to normal block
- Added 3 `additional level`, previously playable level was until 17 only, now until 20.
- improve the UI by adding a nice purple color with shadow and gradient

Implemented but Not Working Properly: 

Features Not Implemented: 
- background game music and block hit sound effect, I have created a sound manager and successfully can play, pause, mute the background music,
  but after that I notice a significant growth of thread usage in the program, and the game will crash after few level.
  it took me 3 day to figure out is the problem of game sound, so I remove it and after that the game run smoothly

- adjusting music volume in settings page, due to the above problem mention, I decide to give up the background game music so there is no need for this features
- creating JUnit tests, I don't know how am I going to create Junit tests for game.



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




# Refactoring Work Summary:
- use lambda expression
- set multiple field to final and static (to share among classes)

#### Change variable name for readability and more understandable
- change break to paddle in InitGameComponent
- change rect to paddle in InitGameComponent
- fixing some typo among the code base
#### Use enum to replace static int constants
- HIT_CODE in Block.java
- BLOCK_TYPE in Block.java
- GameState (newly added) in GameStateManager
#### Apply Singleton framework to several file
- encapsulate the variable which is static in my early development
- make them private and create getter and setter method
#### Combine code that repeatedly call in one method
- prevent code repetition
- easier for maintainence

#### Put the file into meaningful package


## New Files:

### GameScene.fxml DONE

- Moved all game scene related UI code to this file.
- Added a pause button.
- Moved level label to the left.

### GameSceneController.java DONE

- Set up the game scene, called inside `Main.java` through this controller.
- Moved gameScene related methods from `Main.java`.
- Make this class follows the Singleton pattern, ensuring that only one instance of GameSceneController exists.
- Utilizes FXML to define the structure of the main game scene, allowing for a clean separation of UI and logic.
- Switch game states such as WIN, GAME_OVER, and PAUSED.
- load "Win" and "Lose" Scene by utilizing MenuController 
- Implements smooth paddle movement in response to left and right arrow key presses.
- Initializes game components, including blocks, ball, and paddle.
- Dynamically updates the score, heart count, and level labels on the game interface.
- Allows pausing and resuming the game, saving the current game state when paused.


### Menu.fxml DONE

- Added start, loadGame, settings, and quit buttons.
- Added a brick breaker image at the top.
- Reused this file when the pause button is clicked; the text of the start button changes to resume, while the others remain the same.
- Reused this file for showing WIN and LOSE Scene

### MenuController.java DONE

- get instance of `gameLogicHandler`,`gameStateManager`,`gameSceneController`,`primaryStage` in Constructor
- When the start button is clicked, it checks the current gameState. If it's "ON_START," it calls `startGame();` if it's "PAUSED" it calls `loadGame()`, if it's 'GAME_OVER' or 'WIN', it calls `restartGame()`
- When the settings button is clicked, open Settings Scene
- When load Game button is clicked, check is SaveGame file exists, if yes then loadGame else open alert dialog to ask user want to start a new game or not


### Settings.fxml DONE
- Added an interface for users to adjust the initial heart number, end level
- Show brief description of penalty and bonus object

### SettingsController.java DONE
- Provided functionality to adjust the initial heart number.
- Provided functionality to adjust the end level.
- only can adjust settings before the game start or after the game end

### BallControl.java DONE
- Make this class follows the Singleton pattern, ensuring that only one instance of BallControl exists.
- encapsulate all the variable into private
- Moved all methods and fields related to ball movement and collision from `Main.java` for a cleaner file.
- Fixed a bug: when colliding with the left block, `goRightBall` should be `false`.

### InitGameComponent.java DONE
- Make this class follows the Singleton pattern, ensuring that only one instance of InitGameComponent exists.
- encapsulate all the variable into private
- all methods and fields is related to initialization of game component
- all methods and fields moved from `Main.java`

### GameStateManager.java DONE
- Make this class follows the Singleton pattern, ensuring that only one instance of GameStateManager exists.
- encapsulate all the variable into private
- Moved `loadGame()`, `saveGame()`, `restartGame()`, and `nextLevel()` from `Main` into this file.
- Create method call `startGame()`  to load GameScene fxml and pass the gameScene to GameScene controller.
  also call gameLogicHandler to set up the game engine here. 
- It is worth to mention that `startGame()` is a very important method as it is the initial point which
  the game start, load, restart or after progress to next level
- Added a `GameState` enum to better represent the game state.

### Actionable.java DONE
- Interface moved from `GameEngine.java`.
- Renamed the interface from `OnAction` to `Actionable`.
- implement by `GameLogicHandler`

### GameLogicHandler.java
- Implements `Actionable`.
- Make this class follows the Singleton pattern, ensuring that only one instance of GameLogicHandler exists.
- encapsulate all the variable into private
- Moved all methods that implement `Actionable` from `Main` into this file.
- Split long methods into separate short method
- The code has undergone several enhancements to improve its structure and maintainability. 
- The introduction of the `BlockHitHandler` interface provides a foundation
  for various handlers to manage block hit events. Several concrete classes implementing this interface have been created, each handling a specific block hit scenario.
- To further enhance extensibility, a `BlockHitHandlerFactory` interface has been introduced. Corresponding handler factories implementing this interface have been created,
  allowing for the dynamic creation of specific block hit handlers. This approach promotes a more modular design and facilitates future additions of new handlers without the
  need for extensive modifications.
- To streamline the instantiation of handler factories, a `BlockHitHandlerFactoryProvider` class has been introduced. This class replaces the original
  switch statement, offering a more scalable and maintainable solution.

### BlockHitHandler.java
- an interface which define the common method like that used by all the class that implements it

### ChocoBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a choco block is hit.
- Provides the implementation of the `handleBlockHit` function, specifying actions to be taken upon the choco block being hit.
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.
- Utilizes the `DropItemFactory` to create a bonus object when the choco block is hit, enhancing modularity and code organization.

### PenaltyBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a penalty block is hit.
- Provides the implementation of the `handleBlockHit` function, specifying actions to be taken upon the penalty block being hit.
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.

### HeartBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a heart block is hit.
- Provides the implementation of the `handleBlockHit` function, increase the heart number by 1 
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.

### StarBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a star block is hit.
- Provides the implementation of the `handleBlockHit` function, 
- update UI to change the ball to goldBall and add goldRoot to style.css, set goldTime and goldStatus
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.

### NormalBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a normal block is hit.
- Provides the implementation of the `handleBlockHit` function, invoked `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.

### ConcreteBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a concrete block is hit.
- Provides the implementation of the `handleBlockHit` function,
- update UI to change the appearance and type of block to normal block.

### BlockHitHandlerFactoryProvider.java
Purpose: This class provides a centralized point for obtaining specific BlockHitHandlerFactory instances based on the type of the block.

### BlockHitHandlerFactory.java
Purpose: serves as a blueprint for various concrete factories (e.g., ChocoBlockHitHandlerFactory, PenaltyBlockHitHandlerFactory, etc.), 
ensuring consistency in creating block hit handlers throughout your Brick Breaker game. Each factory implementing
this interface will provide its own logic for creating the corresponding BlockHitHandler instances.

### ChocoBlockHitHandlerFactory.java
Purpose: Factory class responsible for creating instances of ChocoBlockHitHandler.

### PenaltyBlockHitHandlerFactory.java
Purpose: Factory class responsible for creating instances of PenaltyBlockHitHandler.

### HeartBlockHitHandlerFactory.java
Purpose: Factory class responsible for creating instances of HeartBlockHitHandler.

### StarBlockHitHandlerFactory.java
Purpose: Factory class responsible for creating instances of StarBlockHitHandler.

### NormalBlockHitHandlerFactory
Purpose: Factory class responsible for creating instances of NormalBlockHitHandler.

### ConcreteBlockHitHandlerFactory.java
Purpose: Factory class responsible for creating instances of ConcreteBlockHitHandler.

### DropItemHandler.java
- handle the behavior of drop items in the game.
- contains methods for determining whether a drop item should be removed, 
- checking if it has been taken by the paddle, 
- handling the action when the drop item is taken, 
- updating the position of the drop item. 
- created to prevent code duplication because there are 2 type of DropItem which use the similar code
- easier for extend in the future also

### BonusDropHandler.java
- extends `DropItemHandler`

### BombDropHandler.java
- extends `DropItemHandler`
- override method `handleTaken` to provide different implementation from superclass
- add one method `executePenalty` which deduct the mark and call ScoreAnimation to show the mark deduct animation.


### HighestScore.java DONE
- save and get the highest Game Score to/from file

### DropItem.java DONE
- serves as an abstract base class for items that can be dropped in the game. (`Bonus` and `Penalty`)
- contains common attributes such as position (`x` and `y`), creation time (`timeCreated`), and a flag indicating whether the item has been taken (`taken`). 
- includes an abstract method draw() that subclasses must implement to define the visual representation of the drop item.

### DropItemFactory.java
- serves as a factory for creating droppable items in the game, specifically instances of `Bonus` and `Bomb` classes. 
- This factory encapsulates the logic for creating these items, providing a centralized and organized way to instantiate and add them to the game scene.

### Bomb.java DONE
- function: player will lose 10 point if not able to catch the penalty item 
- extends the `DropItem` class and represents `bomb` drop item in the game.
- overrides the `draw() `method to provide a visual representation for the `penalty` item.
- The `draw()` method uses JavaFX's `Platform.runLater` to ensure UI updates are performed on the JavaFX application thread.



## Existing Files:

### Block.java DONE
- Removed unnecessary constants and attributes.
- Simplified the constructor by using setter methods.
- Removed unused Block instance (block).
- Introduced BLOCK_TYPE enum for better readability and maintainability.
- Centralized image paths as constants in the class.
- Used the enum to determine the image path based on the block type.
- Renamed and reorganized methods for better clarity and adherence to naming conventions.
- Encapsulated attributes by providing appropriate getter and setter methods.
- Simplified and changed the collision detection logic for readability and prevent penetration issue
- Maintained consistency in naming conventions and coding style.

### BlockSerialize.java DONE

- Renamed the file to `BlockSerialize`.

### Bonus.java DONE
- function: player will get 3 point if able to catch the bonus item
- extends the `DropItem` class and represents a specific type of drop item in the game.
- overrides the `draw() `method to provide a visual representation for the `bonus` item. 
- The `draw()` method uses JavaFX's `Platform.runLater` to ensure UI updates are performed on the JavaFX application thread.

### GameEngine.java DONE
- Moved `OnAction` interface to its own file.
- Replaced individual threads with an ExecutorService for better thread management.
- Introduced a fixed-size thread pool (Executors.newFixedThreadPool(3)) for parallel execution of tasks.
- Updated method names to follow Java naming conventions (initialize, update, physicsCalculation, timeStart).
- Utilized lambda expressions for concise task execution within the thread pool.
- Enclosed Platform.runLater within the thread tasks to ensure JavaFX UI updates are performed on the JavaFX application thread.
- Properly handled thread interruptions (Thread.currentThread().isInterrupted()) and exceptions to enhance robustness.
- Simplified the stop method to shut down the ExecutorService instead of individually stopping threads.

### LoadSave.java DONE
- Fixed some variable typo errors.
- encapsulates the field to private 
- The reading of the ArrayList object has been updated to a more explicit form.
- move `SAVE_PATH_DIR` and `SAVE_PATH` to here

### Main.java DONE
- Extends javafx.application.Application and overrides the start method, which acts as the entry point for JavaFX applications.
- Activates and configures the necessary singleton classes, including `GameLogicHandler`, `BallControl`, `GameSceneController`, `GameStateManager`, and `InitGameComponent`.
- Establishes connections and dependencies between these components to facilitate smooth communication during the game.
- Introduces a method, initializeMenuScene(), responsible for loading the initial menu scene from the "Menu.fxml" file using JavaFX's FXMLLoader.
- Customizes the menu scene, for example, making the "Load" button visible only when the game is first opened.
- Invokes the launch method to initiate the JavaFX application.

### ScoreAnimation.java (Originally was Score.java) DONE
- Added a constructor with `root` instance as a parameter.
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
- Can record the highest score, show in lose and win scene
- add a new penalty block and penalty object, when the block is hit, the object will drop , if didn't catch it deduct 10 points.
- add a new block call concrete block, in the first hit with concrete block, it will turn into normal block and need to hit it again to break it.
- improve the UI by adding a nice purple color with shadow and gradient
- added some description about the game droppable object like bonus and penalty in the settings page.


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

4. **Difficulty Implementing MVC Pattern:**
    - **Issue:** Difficulty in separating UI code from the main logic using FXML and controllers.
    - **Solution:** Found a solution by referring to external sources, successfully implementing the MVC pattern by separating UI code into FXML files and corresponding controllers.

5. **Game become unresponsive unexpectedly:** (It takes me 3 day to figure out what happen)
    - **Issue:** When level increase, then the game will become unresponsive and this situation happen inconsistent.
                 The thread as i see in activity monitor will also increase by 100++ for each level, at the end the program crash                
    - **Solution:** Found that this is a issue after i added the game sound, fix it by commented out the code related to game sound manager
                    Right now the thread is remains at 40 and the program run smoothly
    - **Future** Will need to implement the game sound logic properly

6. **Gold root cannot be removed after pause:**
    - **Issue:** The goldRoot is added multiple times,but when remove only remove one goldRoot
    - **Solution:** Found out that this issue is because i added the goldRoot again and again when hit the Star block,
                    Solution to this is to check the goldStatus before add the goldRoot.         
nextlevel run twice