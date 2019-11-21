import java.util.Vector;

public class RenderContext extends Bitmap {

    public RenderContext(int width, int height){
        super(width, height);
    }

    public void fillTriangle(Vertex v1, Vertex v2, Vertex v3)
    {
        // Normalize the coordinates
        Matrix4f screenSpaceTransform = new Matrix4f().initScreenSpaceTransform(getWidth()/2, getHeight()/2);
        Vertex minYVert = v1.transform(screenSpaceTransform).perspectiveDivide();
        Vertex midYVert = v2.transform(screenSpaceTransform).perspectiveDivide();
        Vertex maxYVert = v3.transform(screenSpaceTransform).perspectiveDivide();

        if(maxYVert.getY() < midYVert.getY())
        {
            Vertex temp = maxYVert;
            maxYVert = midYVert;
            midYVert = temp;
        }
        if(midYVert.getY() < minYVert.getY())
        {
            Vertex temp = midYVert;
            midYVert = minYVert;
            minYVert = temp;
        }
        if(maxYVert.getY() < midYVert.getY())
        {
            Vertex temp = maxYVert;
            maxYVert = midYVert;
            midYVert = temp;
        }

        scanTriangle(minYVert, midYVert, maxYVert, minYVert.triangleArea(maxYVert, midYVert) >= 0);
    }

    public void scanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean orientation)
    {
        Gradient gradient = new Gradient(minYVert, midYVert, maxYVert);
        Edge topToBot = new Edge(minYVert, maxYVert, gradient, 0);
        Edge topToMid = new Edge(minYVert, midYVert, gradient, 0);
        Edge midToBot = new Edge(midYVert, maxYVert, gradient, 1);

        // If orientation is false, then topToBot is the left edge, otherwise topToMid is the left edge
        ScanEdges(topToBot, topToMid, orientation, gradient);
        ScanEdges(topToBot, midToBot, orientation, gradient);
    }

    private void ScanEdges(Edge a, Edge b, boolean handedness, Gradient gradient)
    {
        Edge left = a;
        Edge right = b;
        if(handedness)
        {
            Edge temp = left;
            left = right;
            right = temp;
        }

        int yStart = b.getYStart();
        int yEnd   = b.getYEnd();
        for(int j = yStart; j < yEnd; j++)
        {
            drawScanLine(left, right, j, gradient);
            left.step();
            right.step();
        }
    }

    private void drawScanLine(Edge left, Edge right, int j, Gradient gradient)
    {
        int xMin = (int)Math.ceil(left.getX());
        int xMax = (int)Math.ceil(right.getX());
        float xPrestep = xMin - left.getX();

        Vector4f color = left.getColor().add(gradient.getColorXStep().mul(xPrestep));

        for(int i = xMin; i < xMax; i++)
        {
            // Add 0.5 to be rounded properly
            byte a = (byte)0xFF;
            byte r = (byte)(color.getX() * 255.0f + 0.5f);
            byte g = (byte)(color.getY() * 255.0f + 0.5f);
            byte b = (byte)(color.getZ() * 255.0f + 0.5f);

            drawPixel(i, j, a, b, g, r);
            color = color.add(gradient.getColorXStep());
        }
    }
}
