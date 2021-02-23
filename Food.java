import java.util.* ;
import java.lang.Math.* ;

public class Food
{
	private int x_point, y_point ;

	private Grid theGrid = new Grid() ;

	public void newFood(ArrayList<Snake> snake)
	{
		boolean validFood = false ;
		int x, y ;

		while(1==1)
		{
			y = (int)(Math.random() * (theGrid.getMaxRow() - 1)) ;
			x = (int)(Math.random() * (theGrid.getMaxCol() - 1)) ; 

			// loop to check if the acquired coordinates above are not the same as the coordinates of the snake
			for(int index = 0 ; index < snake.size() ; index++)			
			{				
				if((x != snake.get(index).getX()) && (y != snake.get(index).getY()) && x > 0 && y > 0)
					validFood = true ;
				else
				{
					validFood = false ;	
					break ;
				}	
			}

			// check if the food is away from the walls
			if(x >= (theGrid.getMaxCol() - 1) || y >= (theGrid.getMaxRow() - 1) || x <= 0 || y <= 0)
			{
				validFood = false ;
			}

			if(validFood)
			{
				this.x_point = x ;
				this.y_point = y ;
				break ;
			}
		}
	}

	public int getFoodX()
	{
		return this.x_point ;
	}

	public int getFoodY()
	{
		return this.y_point ;
	}

	public void setFoodX(int x)
	{
		this.x_point = x ;
	}

	public void setFoodY(int y)
	{
		this.y_point = y ;
	}	
}