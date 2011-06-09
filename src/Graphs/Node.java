package Graphs;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Node {

    private int label, x, y;

    public Node(int label)
    {
        this(label, -1, -1);
    }

    public Node(int label, int _x, int _y)
    {
        this.label = label;
        this.x = _x;
        this.y = _y;
    }

    public int getLabel()
    {
        return this.label;
    }

    public void setLabel(int l)
    {
        this.label = l;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "v " + Integer.toString(label) + " " + Integer.toString(x) + " " + Integer.toString(y);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (this.label != other.label) {
            return false;
        }
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 41 * hash + this.label;
        hash = 41 * hash + this.x;
        hash = 41 * hash + this.y;
        return hash;
    }
}
