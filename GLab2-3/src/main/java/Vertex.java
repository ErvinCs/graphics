public class Vertex {
    private float x;
    private float y;

    public Vertex(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float triangleArea(Vertex b, Vertex c)
    {
        float x1 = b.getX() - x;
        float y1 = b.getY() - y;

        float x2 = c.getX() - x;
        float y2 = c.getY() - y;

        return (x1 * y2 - x2 * y1);
    }
}
