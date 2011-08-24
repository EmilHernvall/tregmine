package info.tregmine.quadtree;

public class IntersectionException extends Exception
{
	private static final long serialVersionUID = -3877477189996381216L;

	public IntersectionException(String message)
    {
        super(message);
    }
    
    public IntersectionException()
    {
        super();
    }
}
