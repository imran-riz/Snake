public class Grid
{
	private int maxRows = 25 ;
	private int maxCol = 45 ;

	private char grid[][] = new char [maxRows][maxCol] ;
	
	public void initialize(char symbol)						// this method fills the entire grid with whatever symbol the user wants
	{
		for(int row = 0 ; row < maxRows ; row++) 
		{
			for (int col = 0 ; col < maxCol ; col++) 
			{
				grid[row][col] = symbol ;

				if(row == 0 || row == (maxRows - 1))		// these 2 conditions are only for the walls
					grid[row][col] = '_' ;
				
				else if(col == 0 || col == (maxCol - 1))
					grid[row][col] = '|' ;					
			}			
		}
	}

	public void dispGrid()							// method to display the grid
	{		
		for(int row = 0 ; row < maxRows ; row++) 
		{
			System.out.println() ;
			
			for (int col = 0 ; col < maxCol ; col++) 
			{
				System.out.print(grid[row][col] + " ") ;
			}			
		}

		System.out.println();
	}

	public char getCharAt(int col, int row)
	{
		return this.grid[row][col] ;
	}

	public boolean putChar(int col, int row, char symbol)
	{
		if(symbol == 'O')
		{
			if(this.grid[row][col] == 'O')					// check if the snake hits itself
			{
				this.grid[row][col] = 'X' ;					// an X is displayed to indicate where the snake hit itself
				return false ;
			}
		}
				
		this.grid[row][col] = symbol ;
		return true ;
	}

	public void setMaxRow(int rows)
	{
		this.maxRows = rows ;
	}

	public void setMaxCol(int columns)
	{
		this.maxCol = columns ;
	}

	public Integer getMaxRow()
	{
		return this.maxRows ;
	}

	public Integer getMaxCol()
	{
		return this.maxCol ;
	}
}