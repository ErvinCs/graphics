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
        Edge topToBot = new Edge(minYVert, maxYVert);
        Edge topToMid = new Edge(minYVert, midYVert);
        Edge midToBot = new Edge(midYVert, maxYVert);

        // If orientation is false, then topToBot is the left edge, otherwise topToMid is the left edge
        ScanEdges(topToBot, topToMid, orientation);
        ScanEdges(topToBot, midToBot, orientation);
    }

    private void ScanEdges(Edge a, Edge b, boolean handedness)
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
            drawScanLine(left, right, j);
            left.step();
            right.step();
        }
    }

    private void drawScanLine(Edge left, Edge right, int j)
    {
        int xMin = (int)Math.ceil(left.getX());
        int xMax = (int)Math.ceil(right.getX());

        for(int i = xMin; i < xMax; i++)
        {
            byte colorByte =  (byte)(0xFF);
            drawPixel(i, j, colorByte, colorByte, colorByte, colorByte);
        }
    }
}
