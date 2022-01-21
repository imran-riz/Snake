public class Snake
{
	private int x_coord ;
	private int y_coord ;		

	public Snake(int x, int y)
	{
		this.x_coord = x ;
		this.y_coord = y ;
	}

	public void setX(int x)
	{
		this.x_coord = x ;
	}

	public Integer getX()
	{
		return this.x_coord ;
	}

	public void setY(int y)
	{
		this.y_coord = y ;
	}

	public Integer getY()
	{
		return this.y_coord ;
	}
}