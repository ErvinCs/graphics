public class Vertex {
    private Vector4f position;
    private Vector4f color;

    public Vertex(float x, float y)
    {
       position = new Vector4f(x, y, 1, 1);
    }

    public Vertex(float x, float y, float z)
    {
        position = new Vector4f(x, y, z, 1);
    }

    public Vertex(Vector4f position, Vector4f color)
    {
        this.position = position;
        this.color = color;
    }

    public Vertex(Vector4f position)
    {
        this.position = position;
    }

    public Vector4f getColor() { return color; }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getZ() { return position.getZ();}

    public float getW() { return position.getW();}

    public Vector4f getPosition() {
        return position;
    }

    public Vertex transform(Matrix4f transform)
    {
        return new Vertex(transform.transform(position), color);
    }

    public Vertex perspectiveDivide()
    {
        // Z - occlusion z-value
        // W - perspective z-value
        return new Vertex(new Vector4f(position.getX()/position.getW(),
                position.getY()/position.getW(),
                position.getZ()/position.getW(),
                   position.getW()), color);
    }

    public float triangleArea(Vertex b, Vertex c)
    {
        float x1 = b.getX() - position.getX();
        float y1 = b.getY() - position.getY();

        float x2 = c.getX() - position.getX();;
        float y2 = c.getY() - position.getY();;

        return (x1 * y2 - x2 * y1);
    }
}
