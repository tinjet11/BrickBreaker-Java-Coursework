## Brick Breaker Game Refactoring
### Author - Leong Tin Jet

---

## Compilation Instructions:
### Make sure you have correct java version and maven installed

### git clone this repository and run the following command using maven:
#### `mvn clean javafx:run`

### git clone this repository and add javafx library in your IDE manually


---

### Features that Implemented and Working Properly:
- `Menu Scene` added with `load game`,`start game`, `open settings` and `exit` function, also reused and act as the `Pause`, `Win` and `Lose` Scene
- All time `highest score` can be `record` and will `update` when user win/lose with score higher than the previous highest score
- Add a` pause button` to the game scene, user can `pause` and will open up menu scene, and can `resume` back by clicking resume button in menu scene
- `Settings Scene `added with function such as `adjusting end level`, `initial heart number `, `adjusting music volume`, `mute and unmute music` 
- Added a `penalty block`, when it is hit, a bomb will drop, if user not able to catch it, `deduct 10 points`
- Added a new block, call `concrete block`, will be created when level reach 10. Player need to hit it 2 time to be able to destroy it, first hit will convert it to normal block
- Added 3 `additional level`, previously playable level was until 17 only, now until 20.
- improve the UI by adding a nice purple color with shadow and gradient
- add `background music` to the game 
- add `Game Description Scene` page which show brief description about block type and drop item in the game
- make paddle `change color` randomly when hit by ball
- add `2 point` when in `goldStatus` Mode instead of previously 1 point only

### Features that Implemented but Not Working Properly:
- block hit sound effect, not able to implement properly, also cause my thread usage increase significantly causing 
  unresponsive game, so i remove the block hit sound effect from my game

### Features Not Implemented:
- create multiple ball in the game
- change game theme 
(i don't play game so can't think of other features already ...)

---

## Refactoring Work Summary:

### General 
- use lambda expression
- fixing typo 
- separate the code into multiple classes
- delete unused code and imports

### Renaming variable for readability and more understandable
- change break to paddle in InitGameComponent
- change rect to paddle in InitGameComponent
- fixing some typo among the code base

### Use enum to replace static int constants
- HIT_CODE in Block.java
- BLOCK_TYPE in Block.java
- GameState (newly added) in GameStateManager

### Implemented a Mediator class for central coordination.
- acts as a central coordinator, enabling communication and coordination among various game components.
- promotes loose coupling, making the system more modular, extensible, and easier to maintain.

### Apply `MVC pattern` in the refactoring process
- create `controller`, `model` and `fxml`(view) in the project
- separate the game logic from view 
- centralized the scene control in the controller class

### Apply `Singleton framework` to several file
- encapsulate the variable which is static in my early development
- make them private and create getter and setter method

### Apply `template method` in the refactoring process
- pull the duplicate code into a superclass
- let the subclass override some methods cope with its own functionality
- prevent code repetition
- Example, `DropItemHandler`(superclass), `BonusDropHandler` and `BombDropHandler` (subclasses) inside package `brickgame.handler.dropitem`

### Apply `strategy method` in the refactoring process
- The `BlockHitHandler` interface represents the strategy for handling block hits. It defines a family of algorithms, encapsulates each algorithm, and makes them interchangeable.
- Other class which implement `BlockHitHandler` like `ChocoBlockHitHandler`,`StarBlockHitHandler` and other class is a concrete strategy that implements the BlockHitHandler interface.
  It provides a specific implementation for handling various block hits.
- example: inside package `brickgame.handler.blockhit`

### Apply `factory method` in the refactoring process
- inside package `brickgame.factory`

#### BlockHitHandlerFactoryProvider
-   Description: Centralized factory for creating block hit handlers.
-   Responsibility: Maps different block types to their corresponding handler factories.
-   Implementation: Uses a map to associate each block type with a specific factory. 

#### BlockHitHandlerFactory:
- Description: Interface for creating instances of BlockHitHandler.
- Responsibility: Provides a method for creating a specific type of BlockHitHandler.
- Implementation: Implemented by various concrete factories for different block types. 

#### Concrete BlockHitHandler Factories:
- Description: Factories for creating specific BlockHitHandler instances.
- Responsibility: Each factory creates a BlockHitHandler for a particular block type.
- Implementation: Concrete factories include ChocoBlockHitHandlerFactory, PenaltyBlockHitHandlerFactory, etc.

### Combine code that repeatedly call in one method
- easier for maintenance
- prevent code repetition

### Put the file into meaningful package
- create package like `controller`,`handler`,`factory`,`model`,`serialization`,`fxml`,`images` and more sub packages

### Create a constants class to put all the constants
- easier to manage and update constant values

---

## New Files:

### Mediator.java
- acts as a central coordination point, facilitating communication and interactions between different components (singletons) in the game 
- The Mediator helps in keeping these components loosely coupled by allowing them to communicate through a central mediator, 
- promoting a more maintainable and extensible design.
- Components that need to communicate with each other can do so indirectly through the Mediator
- contains references to other significant components in the system, such as the GameSceneController, GameLogicHandler, and other critical controllers or managers.

### GameScene.fxml

- Moved all game scene related UI code to this file.
- Added a pause button.
- Moved level label to the left.

### GameSceneController.java 

- Set up the game scene through this controller.
- Moved gameScene related methods from `Main.java`.
- Make this class follows the Singleton pattern, ensuring that only one instance of GameSceneController exists.
- Utilizes FXML to define the structure of the main game scene, allowing for a clean separation of UI and logic.
- Switch game states such as WIN, GAME_OVER, and PAUSED.
- load "Win" and "Lose" Scene by utilizing MenuController
- Initializes game components, including blocks, ball, and paddle.
- Dynamically updates the score, heart count, and level labels on the game interface.
- Allows pausing the game, saving the current game state when paused.


### Menu.fxml 

- Added start, loadGame, settings,tutorial and quit buttons.
- Added a brick breaker image at the top.
- Reused this file when the pause button is clicked; the text of the start button changes to resume, while the others remain the same.
- Reused this file for showing WIN and LOSE Scene

### MenuController.java
- When the start button is clicked, it checks the current gameState. If it's "ON_START," it calls `startGame();` if it's "PAUSED" it calls `loadGame()`, if it's 'GAME_OVER' or 'WIN', it calls `restartGame()`
- When the settings button is clicked, open Settings Scene
- When load Game button is clicked, check is SaveGame file exists, if yes then loadGame else open alert dialog to ask user want to start a new game or not

### Settings.fxml 
- Added an interface for users to adjust the initial heart number, end level, game sound volume and mute button

### SettingsController.java 
- Provide functionality to adjust the initial heart number.
- Provide functionality to adjust the end level.
- only can adjust settings before the game start or after the game end
- Provide functionality to adjust the game sound volume
- Provide functionality to mute and unmute the game sound volume
- Provide functionality to navigate back to menu scene

### GameDescription.fxml
- provide description about block and dropItem in the game
- make use of Vbox and Hbox

### GameDescriptionController.java
- Provide functionality to navigate back to menu scene

### SoundController,java
- Calls initializeMediaPlayer() during instance creation for initial setup.
- Sets up looping when the media reaches its end.
- provide playback control function like play(),stop(),pause(),resume() and dispose()
- Binds a JavaFX Slider to the volume property, allowing dynamic volume adjustment.
- Binds a JavaFX CheckBox to mute functionality.
- Pauses audio when the checkbox is selected; resumes from the paused position when deselected.
- Retrieves the current status of the MediaPlayer (e.g., playing, paused, stopped).
- Utilizes a constant, SOUND_FILE_PATH, for specifying the path to the sound file.

### BallControlHandler.java
- Make this class follows the Singleton pattern, ensuring that only one instance of BallControlHandler exists.
- encapsulate all the variable into private
- Moved all methods and fields related to ball movement and collision from `Main.java` for a cleaner file.
- Fixed a bug: when colliding with the left block, `goRightBall` should be `false`.

### InitGameComponent.java
- Make this class follows the Singleton pattern, ensuring that only one instance of InitGameComponent exists.
- encapsulate all the variable into private
- all methods and fields is related to initialization of game component
- all methods and fields moved from `Main.java`

### Ball.java
- Make this class follows the Singleton pattern, ensuring that only one instance of Ball exists.
- encapsulate all the variable into private
- provide getter and setter method

### Paddle.java
- Make this class follows the Singleton pattern, ensuring that only one instance of Paddle exists.
- put the function of moving paddle in this class
- encapsulate all the variable into private
- provide getter and setter method

### GameStateManager.java
- Make this class follows the Singleton pattern, ensuring that only one instance of GameStateManager exists.
- encapsulate all the variable into private
- Moved `loadGame()`, `saveGame()`, `restartGame()`, and `nextLevel()` from `Main` into this file.
- Create method call `startGame()`  to load GameScene fxml and pass the gameScene to GameScene controller.
  also call gameLogicHandler to set up the game engine here. 
- It is worth to mention that `startGame()` is a very important method as it is the initial point which
  the game start, load, restart or after progress to next level
- Fixed a bug: By removing `mediator.getInitGameComponent().getDropItem().clear();` in `loadGame()` function, and add the dropItem
  back to root and set new time created for it  
- Added a `GameState` enum to better represent the game state.

### Actionable.java
- Interface moved from `GameEngine.java`.
- Renamed the interface from `OnAction` to `Actionable`.
- remove unused `onInit()` method
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
- applying strategy method
- an interface which define the common method that used by all the class that implements it

### ChocoBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a choco block is hit.
- Provides the implementation of the `handleBlockHit` function, specifying actions to be taken upon the choco block being hit.
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.
- Utilizes the `DropItemFactory` to create a bonus object when the choco block is hit, enhancing modularity and code organization.


### PenaltyBlockHandler.java
- implements the `BlockHitHandler` interface to define the behavior when a penalty block is hit.
- Provides the implementation of the `handleBlockHit` function, specifying actions to be taken upon the penalty block being hit.
- Invokes the `onHitAction` method, defined in the BlockHitHandler, to handle generic hit actions.
- Utilizes the `DropItemFactory` to create a bomb object when the penalty block is hit, enhancing modularity and code organization.

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
- This class provides a centralized point for obtaining specific BlockHitHandlerFactory instances based on the type of the block.

### BlockHitHandlerFactory.java
- serves as a blueprint for various concrete factories (e.g., ChocoBlockHitHandlerFactory, PenaltyBlockHitHandlerFactory, etc.), 
ensuring consistency in creating block hit handlers throughout your Brick Breaker game. Each factory implementing
this interface will provide its own logic for creating the corresponding BlockHitHandler instances.

### ChocoBlockHitHandlerFactory.java
- Factory class responsible for creating instances of ChocoBlockHitHandler.

### PenaltyBlockHitHandlerFactory.java
- Factory class responsible for creating instances of PenaltyBlockHitHandler.

### HeartBlockHitHandlerFactory.java
- Factory class responsible for creating instances of HeartBlockHitHandler.

### StarBlockHitHandlerFactory.java
- Factory class responsible for creating instances of StarBlockHitHandler.

### NormalBlockHitHandlerFactory
- Factory class responsible for creating instances of NormalBlockHitHandler.

### ConcreteBlockHitHandlerFactory.java
- Factory class responsible for creating instances of ConcreteBlockHitHandler.

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


### HighestScore.java
- save and get the highest Game Score to/from file

### DropItem.java
- serves as an abstract base class for items that can be dropped in the game. (`Bonus` and `Penalty`)
- contains common attributes such as position (`x` and `y`), creation time (`timeCreated`), and a flag indicating whether the item has been taken (`taken`). 
- includes an abstract method draw() that subclasses must implement to define the visual representation of the drop item.

### DropItemFactory.java
- serves as a factory for creating droppable items in the game, specifically instances of `Bonus` and `Bomb` classes. 
- This factory encapsulates the logic for creating these items, providing a centralized and organized way to instantiate and add them to the game scene.

### Bomb.java
- function: player will lose 10 point if not able to catch the penalty item 
- extends the `DropItem` class and represents `bomb` drop item in the game.
- overrides the `draw() `method to provide a visual representation for the `penalty` item.
- The `draw()` method uses JavaFX's `Platform.runLater` to ensure UI updates are performed on the JavaFX application thread.

### Constants.java
- included file path and image path
- easier for managing string constants

---


## Existing Files:

### Block.java
- Removed unnecessary constants and attributes.
- Simplified the constructor by using setter methods.
- Removed unused Block instance (block).
- Introduced `BLOCK_TYPE enum` for better readability and maintainability.
- Centralized image paths as constants in the class.
- Used the enum to determine the image path based on the block type.
- Renamed and reorganized methods for better clarity and adherence to naming conventions.
- Encapsulated attributes by providing appropriate getter and setter methods.
- Simplified and changed the collision detection logic for readability and prevent penetration issue
- Maintained consistency in naming conventions and coding style.

### BlockSerialize.java (Originally was BlockSerializable)

- Renamed the file to `BlockSerialize`.

### Bonus.java
- function: player will get 3 point if able to catch the bonus item
- extends the `DropItem` class and represents a specific type of drop item in the game.
- overrides the `draw() `method to provide a visual representation for the `bonus` item. 
- The `draw()` method uses JavaFX's `Platform.runLater` to ensure UI updates are performed on the JavaFX application thread.

### GameEngine.java
- Moved `OnAction` interface to its own file.
- Replaced individual threads with an ExecutorService for better thread management.
- Introduced a fixed-size thread pool (Executors.newFixedThreadPool(3)) for parallel execution of tasks.
- Updated method names to follow Java naming conventions (initialize, update, physicsCalculation, timeStart).
- Utilized lambda expressions for concise task execution within the thread pool.
- Enclosed Platform.runLater within the thread tasks to ensure JavaFX UI updates are performed on the JavaFX application thread.
- Properly handled thread interruptions (Thread.currentThread().isInterrupted()) and exceptions to enhance robustness.
- Simplified the stop method to shut down the ExecutorService instead of individually stopping threads.
- Check isLoadFromSave before set the time in `start()`, successfully fix the logic issue

### LoadSave.java
- Fixed some variable typo errors.
- encapsulates the field to private 
- The reading of the ArrayList object has been updated to a more explicit form.

### Main.java
- Extends javafx.application.Application and overrides the start method, which acts as the entry point for JavaFX applications.
- Activates and configures the necessary singleton classes, including `GameLogicHandler`, `BallControl`, `GameSceneController`, `GameStateManager`, `Ball`, `Paddle` and `InitGameComponent`
- Establishes connections and dependencies between these components to facilitate smooth communication during the game.
- Introduces a method, initializeMenuScene(), responsible for loading the initial menu scene from the "Menu.fxml" file using JavaFX's FXMLLoader.
- Customizes the menu scene, for example, making the "Load" button visible only when the game is first opened.
- Invokes the launch method to initiate the JavaFX application.

### ScoreAnimation.java (Originally was Score.java)
- Added a constructor with `root` instance as a parameter.
- Utilized JavaFX's Timeline and KeyFrame for animation instead of creating a separate thread and using `Thread.sleep()`.
- Removed unused import statements.
- Improved code formatting, and used lambda expressions for conciseness in certain places, like the `Platform.runLater()` calls.
- Removed `showWin()` and `showGameOver()` methods.
- Changed the `show()` method, utilized the `showMessage()` method to prevent any code duplication.

---

## Unexpected Problems:

1. **Constructor Parameter for Controller:**
    - **Issue:** Difficulty passing parameters to controller classes.
    - **Solution:** Found a solution by referencing [external sources](https://www.reddit.com/r/javahelp/comments/4pnbuk/javafx_constructor_parameters_for_controller/), enabling the passing of the primary stage and Main class instance to other controller classes.

2. **Ball Getting Stuck at Bottom Wall:**
    - **Issue:** Occasionally, the ball would get stuck at the bottom wall.
    - **Solution:** Implemented a fix to address the issue, ensuring that the ball continues its upward trajectory after touching the bottom wall.

3. **Block Count Logic Issue:**
    - **Issue:** The logic for block destruction count had a flaw, especially after pausing and resuming the game.
    - **Solution:** Modified the logic to check the remaining block count rather than the destroyed block count, preventing unintended level progression.

4. **Next level running many times unexpectedly:**
    - **Issue:** when advance to next level, the `nextLevel()` run multiple times unexpectedly
    - **Solution:** Modified the next level function, introduce one boolean variable to make sure the function cannot be call the second
                    time until the first function end.

5. **Difficulty Implementing MVC Pattern:**
    - **Issue:** Difficulty in separating UI code from the main logic using FXML and controllers.
    - **Solution:** Found a solution by referring to external sources, successfully implementing the MVC pattern by separating UI code into FXML files and corresponding controllers.

6. **Game become unresponsive unexpectedly:** (It takes me 3 day to figure out what happen)
    - **Issue:** When level increase, then the game will become unresponsive and this situation happen inconsistent.
                 The thread as i see in activity monitor will also increase by 100++ for each level, at the end the program crash                
    - **Solution:** Found that this is a issue after i added the game sound, fix it by remove the code related to block hit sound

7. **Gold root cannot be removed after pause:**
    - **Issue:** The goldRoot is added multiple times,but when remove only remove one goldRoot
    - **Solution:** Found out that this issue is because i added the goldRoot again and again when hit the Star block,
                    Solution to this is to check the goldStatus before add the goldRoot.         

8. **Ball will go through between two block:**
    - **Issue:** The ball will penetrate the block if it is hitting the center of two block
    - **Solution:** Adding another layer of collision logic so it bounce back when hit a block

9. **Time will become 0 everytime engine start:**
    - **Issue:** The time will become zero everytime engine start when pause and resume the game
    - **Solution:** Checking is loadFromSave before set the time value

10**The dropItem will disappear after pause and resume :**
    - **Issue:** The dropItem is being clear every time the game is load, which is after pause and resume
    - **Solution:** Remove the `mediator.getInitGameComponent().getDropItem().clear();`, add the dropItem back to root and set the new time created
    

