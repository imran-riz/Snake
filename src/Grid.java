public class Grid
{
	private int maxRows = 25 ;
	private int maxCol = 45 ;

	private char grid[][] = new char [maxRows][maxCol] ;

	public Integer getMaxRow()
	{
		return this.maxRows ;
	}

	public Integer getMaxCol()
	{
		return this.maxCol ;
	}
}