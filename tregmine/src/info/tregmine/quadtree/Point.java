package info.tregmine.quadtree;

public class Point
{
    public int x, y;
    
    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public String toString()
    {
        return String.format("%d,%d", x, y);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Point)) {
            return false;
        }
        
        Point b = (Point)obj;
        return b != null && b.x == x && b.y == y;
    }
    
    @Override
    public int hashCode()
    {
        int code = 17;
        code = 31 * code + x;
        code = 31 * code + y;
        
        return code;
    }
}