public class Edge {
    // Takes in the information of RenderContext.scanConvertLine*()
    private float x;
    private float xStep;
    private int yStart;
    private int yEnd;
    // Color
    private Vector4f color;
    private Vector4f colorStep;

    public Edge(Vertex start, Vertex end, Gradient gradient, int minYVertGradientIndex)
    {
        yStart = (int)Math.ceil(start.getY());
        yEnd = (int)Math.ceil(end.getY());

        float yDist = end.getY() - start.getY();
        float xDist = end.getX() - start.getX();

        float yPrestep = yStart - start.getY(); // change on y-axis
        xStep = xDist/yDist;
        x = start.getX() + yPrestep * xStep;
        float xPrestep = x - start.getX();      // change on x-axis

        // Adjust color to the x,y positions
        color = gradient.getColor(minYVertGradientIndex)
                .add(gradient.getColorYStep().mul(yPrestep))
                .add(gradient.getColorXStep().mul(xPrestep));
        colorStep = gradient.getColorYStep().add(gradient.getColorXStep().mul(xStep));
    }

    public void step()
    {
        // Move to the new X on a scan line
        x += xStep;
        color = color.add(colorStep);
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

    public Vector4f getColor() {
        return color;
    }
}
