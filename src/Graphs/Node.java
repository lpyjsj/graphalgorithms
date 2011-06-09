package Graphs;

/**
 *
 * @author Christian Hildebrandt (210238835), Matthias Bady (210235131)
 */
public class Node {

    private int label, x, y, bucketNumber = -1;
    protected float cost = 0.f;
    private Node pred = null;

    public float getCost()
    {
        return cost;
    }

    public void setCost(float cost)
    {
        this.cost = cost;
    }

    public int getBucketNumber()
    {
        return bucketNumber;
    }

    public void setBucketNumber(int bucketNumber)
    {
        this.bucketNumber = bucketNumber;
    }

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
        final Node other = (Node) obj;
        if (this.label != other.label) {
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

    public Node getPred()
    {
        return pred;
    }

    public void setPred(Node pred)
    {
        this.pred = pred;
    }
}
