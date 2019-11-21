public class Edge {
    // Takes in the information of RenderContext.scanConvertLine*()
    private float x;
    private float xStep;
    private int yStart;
    private int yEnd;
    // Texture
    private float texCoordX;
    private float texCoordXStep;
    private float texCoordY;
    private float texCoordYStep;
    private float oneOverZ;
    private float oneOnZStep;
    private float depth;
    private float depthStep;


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

        texCoordX = gradient.getTexCoordX(minYVertGradientIndex) +
                gradient.getTexCoordXXStep() * xPrestep +
                gradient.getTexCoordXYStep() * yPrestep;
        texCoordXStep = gradient.getTexCoordXYStep() + gradient.getTexCoordXXStep() * xStep;

        texCoordY = gradient.getTexCoordY(minYVertGradientIndex) +
                gradient.getTexCoordYXStep() * xPrestep +
                gradient.getTexCoordYYStep() * yPrestep;
        texCoordYStep = gradient.getTexCoordYYStep() + gradient.getTexCoordYXStep() * xStep;

        oneOverZ = gradient.getOneOverZ(minYVertGradientIndex) +
                gradient.getOneOverZXStep() * xPrestep +
                gradient.getOneOverZYStep() * yPrestep;
        oneOnZStep = gradient.getOneOverZYStep() + gradient.getOneOverZXStep() * xStep;

        depth = gradient.getDepth(minYVertGradientIndex) +
                gradient.getDepthXStep() * xPrestep +
                gradient.getDepthYStep() * yPrestep;
        depthStep = gradient.getDepthYStep() + gradient.getDepthXStep() * xStep;
    }

    public void step()
    {
        // Move to the new vars on a scan line
        x += xStep;
        texCoordX += texCoordXStep;
        texCoordY += texCoordYStep;
        oneOverZ += oneOnZStep;
        depth += depthStep;
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
    public float getTexCoordX() { return texCoordX; }
    public float getTexCoordY() { return texCoordY; }
    public float getOneOnZ() { return oneOverZ; }
    public float getDepth() { return depth; }
}
