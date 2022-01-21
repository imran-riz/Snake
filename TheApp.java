import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.event.* ;
import javafx.scene.control.* ;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.scene.input.* ;
import javafx.animation.*;
import javafx.util.*;
import java.util.* ;
import java.io.* ;

public class TheApp extends Application
{
	public static void main(String[] args) 
	{
		Application.launch(args) ;
	}

	enum Direction
	{
		UP, DOWN, LEFT, RIGHT ;
	}

	private Pane pane ;
	private VBox vbox ;
	private Scene scene ;
	private KeyFrame keyFrame ;
	private Timeline time_line ;
	private Label score_lbl = new Label("") ;
	private Label highScore_lbl = new Label("") ;
	
	private Food food = new Food() ;
	private Food bonusFood = new Food() ;
	private Circle foodCirc = new Circle() ;
	private Circle bonusCirc = new Circle() ;
	private ArrayList<Rectangle> snakeList = new ArrayList<Rectangle>() ;
	
    private final String PATH_TO_DIRECTORY = System.getProperty("user.home") + "\\.snake-game" ;
    private final String PATH_TO_FILE = PATH_TO_DIRECTORY + "/high_score.txt" ;
	private final int mapWidth = 1100 ;
	private final int mapHeight = 650 ;
	private final int size = 20 ;
	private final int movePX = 25 ;


	private Direction dir, prev_dir ;
	private int lastX, lastY, score, numEaten, createdOn, highScore ;
	private Boolean gameOver, playing, haveBonus ;

	@Override
	public void start(Stage window)
	{
		window.setTitle("S N A K E") ;
		window.setResizable(false) ;

		pane = new Pane() ;
		pane.getChildren().addAll(highScore_lbl, score_lbl, foodCirc) ;

		vbox = new VBox(pane) ;
		vbox.setStyle("-fx-background-color : black") ;

		scene = new Scene(vbox, mapWidth, mapHeight) ;
		scene.getStylesheets().add("snake_style.css") ;

		window.setScene(scene) ;
		window.show() ;		

		PlayGame() ;
	}


	private void PlayGame()
	{
		Initialize() ;
		
		scene.setOnKeyPressed(event ->
		{
			switch(event.getCode()) 
			{
				case UP : if(!playing) pausePlayGame() ;
						  if(prev_dir != Direction.DOWN)		// we do the verification so that the snake doesn't turn around and eat itself
						      dir = Direction.UP ;
						  break ;

				case DOWN : if(!playing) pausePlayGame() ;
							if(prev_dir != Direction.UP)
				 				dir = Direction.DOWN ;
				 			break ;

				case LEFT : if(!playing) pausePlayGame() ;
							if(prev_dir != Direction.RIGHT)
								dir = Direction.LEFT ;
							break ;

				case RIGHT : if(!playing) pausePlayGame() ;
							 if(prev_dir != Direction.LEFT)
							  	dir = Direction.RIGHT ;
							 break ;

				case ESCAPE : pausePlayGame() ;
							  break ;
			}
		}) ;

		keyFrame = new KeyFrame(Duration.millis(100), e ->
		{
			lastX = (int)snakeList.get(snakeList.size() - 1).getLayoutX() ;
			lastY = (int)snakeList.get(snakeList.size() - 1).getLayoutY() ;
			
			// update the corrdinates of snake's body but the head
			for(int index = snakeList.size() - 1 ; index > 0 ; index--)
			{
				snakeList.get(index).setLayoutX(snakeList.get(index - 1).getLayoutX()) ;
				snakeList.get(index).setLayoutY(snakeList.get(index - 1).getLayoutY()) ;
			}

			// update the snake's head depending on the direction its moving at. Note we check if it hits the walls as well
			switch(dir)
			{
				case UP : snakeList.get(0).setLayoutY(snakeList.get(0).getLayoutY() - movePX) ;
						  if(snakeList.get(0).getLayoutY() < 0)
						  	gameOver = true ;						  
						  break ;

				case DOWN : snakeList.get(0).setLayoutY(snakeList.get(0).getLayoutY() + movePX) ;
							if(snakeList.get(0).getLayoutY()+size > mapHeight)
								gameOver = true ;									
							break ;

				case RIGHT : snakeList.get(0).setLayoutX(snakeList.get(0).getLayoutX() + movePX) ;
							 if(snakeList.get(0).getLayoutX()+size > mapWidth)
							  	gameOver = true ;									
							 break ;

				case LEFT : snakeList.get(0).setLayoutX(snakeList.get(0).getLayoutX() - movePX) ;
							if(snakeList.get(0).getLayoutX() < 0)
								gameOver = true ;									
							break ;
			}

			// check if the snake ate the food
			if(snakeList.get(0).getLayoutX() == food.getFoodX() && snakeList.get(0).getLayoutY() == food.getFoodY())
			{
				score+=10 ;
				numEaten++ ;		// count the number of foods that was eaten
				
				// create a new cell for the snake (it gets longer)
				Rectangle rect = new Rectangle(size, size) ;
				rect.setFill(Color.LIME) ;
				rect.setLayoutX(lastX) ;
				rect.setLayoutY(lastY) ;

				if(numEaten % 5 == 0)		// every 5 food thats been eaten, a bonus food is diplayed
				{
					newBonusFood() ;					

					Calendar timeCreated = Calendar.getInstance() ;
					createdOn = timeCreated.get(Calendar.SECOND) ;  // get the second when the bonus food appears

					haveBonus = true ;
				}

				snakeList.add(rect) ;
				pane.getChildren().add(rect) ;

				newFood() ;
			}

			// check if the snake ate the bonus food
			if(snakeList.get(0).getLayoutX() == bonusFood.getFoodX() && snakeList.get(0).getLayoutY() == bonusFood.getFoodY())
			{
				score+=30 ;

				pane.getChildren().remove(bonusCirc) ;
				bonusFood.setFoodX(-1) ;
				bonusFood.setFoodY(-1) ;

				haveBonus = false ;
			}

			// check if the snake hit itself
			for(int index = 1 ; index < snakeList.size() ; index++)
			{
				if(snakeList.get(0).getLayoutX() == snakeList.get(index).getLayoutX() && snakeList.get(0).getLayoutY() == snakeList.get(index).getLayoutY())
				{					
					gameOver = true ;
					break ;
				}
			}

			if(haveBonus)		// this is done coz the bonus food disappears in 5 seconds
			{
				Calendar timePassed = Calendar.getInstance() ;
				int dummy = timePassed.get(Calendar.SECOND) ;
				int diff = dummy - createdOn ;
			
				if(Math.abs(diff) > 5)			// check the absolute value of diff
				{
					pane.getChildren().remove(bonusCirc) ;
					bonusFood.setFoodX(-1) ;
					bonusFood.setFoodY(-1) ;					

					haveBonus = false ;
				}				
			}

			prev_dir = dir ;
			score_lbl.setText("Score " + score) ;

			if(gameOver)
				endGame() ;
		}) ;

		time_line = new Timeline(keyFrame) ;
		time_line.setCycleCount(Timeline.INDEFINITE) ;
		time_line.play() ;
	}


	private void Initialize()
	{
		gameOver = false ;
		score = 0 ;

		int x = 600 ;
		int y = 400 ;
		for(int index = 0 ; index < 5 ; index++)
		{
			Rectangle r = new Rectangle(size, size) ;
			r.setFill(Color.LIME) ;
			r.setLayoutX(x) ;
			r.setLayoutY(y) ;

			snakeList.add(r) ;
			pane.getChildren().add(r) ;

			x+=movePX ;
		}

		newFood() ;

		dir = Direction.LEFT ;
		playing = true ;
		haveBonus = false ;
		numEaten = 0 ;

		if(getHighScore() == false) highScore = 0 ;			

		score_lbl.setText("Score " + score) ;
		score_lbl.setId("lbl_style") ;
		score_lbl.setLayoutX(0) ;
		score_lbl.setLayoutY(30) ;
		score_lbl.setPrefHeight(30) ;

		highScore_lbl.setText("High Score " + highScore) ;
		highScore_lbl.setId("lbl_style2") ;
		highScore_lbl.setPrefHeight(30) ;
		highScore_lbl.setLayoutX(0) ;
		highScore_lbl.setLayoutY(0) ;		
	}


	private void newFood()
	{
		int x, y ;				

		while(1==1)
		{
			Boolean validFood = false ;

			x = (int)(Math.random() * (mapWidth)) ;
			y = (int)(Math.random() * (mapHeight)) ;

			if(x % movePX == 0 && y % movePX == 0 && x > 0 && y > 0)
			{
				for(int index = 0 ; index < snakeList.size() ; index++)
				{
					if(snakeList.get(index).getLayoutX() != (x) && snakeList.get(index).getLayoutY() != (y) && snakeList.get(index).getLayoutX() != x && snakeList.get(index).getLayoutY() != y)
						validFood = true ;
					else
						validFood = false ;
				}
			}	

			if(validFood)
			{				
				food.setFoodX(x) ;
				food.setFoodY(y) ;
				break ;
			}
		}

		int num = (int)(Math.random() * 6) ;	// get a random integer from 0 to 5 inclusive
		switch (num) 
		{
			case 0 : foodCirc.setFill(Color.LAVENDER) ;
					 break ;			
			case 1 : foodCirc.setFill(Color.RED) ;
					 break ;
			case 2 : foodCirc.setFill(Color.SKYBLUE) ;
					 break ;
			case 3 : foodCirc.setFill(Color.CYAN) ;
					 break ;
			case 4 : foodCirc.setFill(Color.MAGENTA) ;
					 break ;
			case 5 : foodCirc.setFill(Color.CORNFLOWERBLUE) ;
					 break ;
		}

		foodCirc.setRadius(10) ;
		foodCirc.setLayoutX(food.getFoodX() + 10) ;		// the 10 is added for better precise placement
		foodCirc.setLayoutY(food.getFoodY() + 10) ;
	}


	private void newBonusFood()
	{
		boolean validBonus = false ;
		int bonusX, bonusY ;

		while(1==1)
		{
			bonusX = (int)(Math.random() * (mapWidth)) ;
			bonusY = (int)(Math.random() * (mapHeight)) ;

			if(bonusX == food.getFoodX() || bonusX == food.getFoodY() || bonusY >= (mapHeight - 10) || bonusX >= (mapWidth - 10) || bonusY <= 10 || bonusX <= 10 || bonusX % movePX != 0 || bonusY % movePX != 0)
				continue ;
	
			for(int index = 0 ; index < snakeList.size() ; index++)
			{
				if(snakeList.get(index).getLayoutX() != bonusX && snakeList.get(index).getLayoutY() != bonusY)
					validBonus = true ;
				else
					validBonus = false ;
			}

			if(validBonus)	break ;
		}	

		RadialGradient bonusFoodColor = new RadialGradient(
			0, 0,
			0.5, 0.5,
			0.5,
			true,
			CycleMethod.NO_CYCLE,
			new Stop(0.0, Color.YELLOW),
			new Stop(1.0, Color.GOLD));		

		bonusFood.setFoodX(bonusX) ;
		bonusFood.setFoodY(bonusY) ;

		// make a bigger circle than the normal food to represent the bonus food
		bonusCirc.setLayoutX(bonusFood.getFoodX() + 10) ;		// the 10 is added for better look
		bonusCirc.setLayoutY(bonusFood.getFoodY() + 10) ;
		bonusCirc.setFill(bonusFoodColor) ;
		bonusCirc.setRadius(20) ;

		pane.getChildren().add(bonusCirc) ;
	}


	private void pausePlayGame()
	{
		if(playing)
		{
			time_line.pause() ;
			playing = false ;
		}	
		else
		{
			time_line.play() ;
			playing = true ;
		}
	}

	private void setHighScore()			// record the highest score in a text file
	{
		try
		{
			FileWriter writer = new FileWriter(PATH_TO_FILE) ;
			writer.write(Integer.toString(score)) ;
			writer.close() ;
		}
		catch(Exception e)
		{
			System.out.println("Exceptions in setHighScore(): " + e) ;
		}
	}

	private Boolean getHighScore()		// get the highest score from the text file
	{
		try
		{
			File file = new File(PATH_TO_FILE) ;
			
			Scanner reader = new Scanner(file) ;

			if(reader.hasNextLine())
				highScore = Integer.parseInt(reader.nextLine()) ;
			else
				return false ;			
		}
		catch(FileNotFoundException fe) 
		{
			try
			{
	            File file = new File(PATH_TO_DIRECTORY) ;

	            if(file.mkdirs())
	            {
	                File newFile = new File(PATH_TO_FILE) ;

	                if(newFile.createNewFile())
	                {                    
	                    return false ;
	                }
	            }
	        }
	        catch (Exception ex) 
	        {
	        	System.out.println("Exceptions in getHighScore() when creating the directory: " + ex) ;	
	        }
		}
		catch(Exception e)
		{
			System.out.println("Exceptions in getHighScore(): " + e) ;
			return false ;
		}

		return true ;
	}

	private void endGame()
	{
		if(score > highScore) setHighScore() ;		

		sleep(3) ;								// put the program to sleep for 3 seconds before terminating
		Runtime.getRuntime().exit(0) ;			// terminate the program
	}

	private void sleep(int num)		// a seperate method to put the system to sleep
	{
		try		
		{
			Thread.sleep(1000 * num) ;			// putting the system to sleep for 'num' seconds
		} 
		catch(InterruptedException e)
		{
			e.printStackTrace() ;
		}
	}
}
