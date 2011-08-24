package info.tregmine.quadtree;

public class Rectangle
{
    // top left, top right
    Point tl, tr;
    // bottom left, bottom right
    Point bl, br;

    public Rectangle(int x1, int y1, int x2, int y2)
    {
        int tmp;
        
        // swap values so that we can guarantee the
        // relative position of the points
        if (x1 > x2) {
            tmp = x2;
            x2 = x1;
            x1 = tmp;
        }
        
        if (y1 < y2) {
            tmp = y2;
            y2 = y1;
            y1 = tmp;
        }
    
        this.tl = new Point(x1, y1);
        this.tr = new Point(x2, y1);
        this.bl = new Point(x1, y2);
        this.br = new Point(x2, y2);
    }
    
    public int getLeft() { return tl.x; }
    public int getRight() { return tr.x; }
    public int getTop() { return tl.y; }
    public int getBottom() { return bl.y; }
    
    public boolean contains(Point p)
    {
        return (p.x >= tl.x && p.x <= br.x) &&
            (p.y <= tl.y && p.y >= br.y);
    }
    
    public boolean intersects(Rectangle rect)
    {
        return !(rect.getLeft() > getRight() ||
            rect.getRight() < getLeft() ||
            rect.getTop() < getBottom() ||
            rect.getBottom() > getTop());
    }
    
    public Point[] getPoints()
    {
        return new Point[] { tl, tr, bl, br };
    }
    
    @Override
    public String toString()
    {
        return String.format("(%s),(%s),(%s),(%s)", tl, tr, bl, br);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Rectangle)) {
            return false;
        }
        
        Rectangle b = (Rectangle)obj;
        return tl.equals(b.tl) && br.equals(b.br);
    }
    
    @Override
    public int hashCode()
    {
        int code = 17;
        code = 31 * code + tl.hashCode();
        code = 31 * code + br.hashCode();
        
        return code;
    }
}
