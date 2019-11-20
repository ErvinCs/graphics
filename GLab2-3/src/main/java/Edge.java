public class Edge {
    // Takes in the information of RenderContext.scanConvertLine*()
    private float x;
    private float xStep;
    private int yStart;
    private int yEnd;

    public Edge(Vertex start, Vertex end)
    {
        yStart = (int)Math.ceil(start.getY());
        yEnd = (int)Math.ceil(end.getY());

        float yDist = end.getY() - start.getY();
        float xDist = end.getX() - start.getX();

        float yStep = yStart - start.getY();
        xStep = xDist/yDist;
        x = start.getX() + yStep * xStep;
    }

    public void step()
    {
        // Move to the new X on a scan line
        x += xStep;
    }

    public float getX() {
        return x;
    }

    public int getYStart() {
        return yStart;
    }

    public int getYEnd() {
        return yEnd;
    }
}
