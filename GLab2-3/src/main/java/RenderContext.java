public class RenderContext extends Bitmap {
    private final int[] scanBuffer;

    public RenderContext(int width, int height){
        super(width, height);
        scanBuffer = new int[height * 2];   // 2 values foreach y coordinate
    }

    public void drawScanBuffer(int yCoord, int xMin, int xMax)
    {
        scanBuffer[yCoord * 2] = xMin;
        scanBuffer[yCoord * 2 + 1] = xMax;
    }

    // Fill the pixels using the frame buffer
    public void fillShape(int yMin, int yMax)
    {
        for(int j = yMin; j < yMax; j++)
        {
            int xMin = scanBuffer[j * 2];
            int xMax = scanBuffer[j * 2 + 1];

            for(int i = xMin; i < xMax; i++)
            {
                byte colorByte =  (byte)(0xFF);
                drawPixel(i, j, colorByte, colorByte, colorByte, colorByte);
            }
        }
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

        float area = minYVert.triangleArea(maxYVert, midYVert);
        int orientation = area >= 0 ? 1 : 0;

        scanConvertTriangle(minYVert, midYVert, maxYVert, orientation);
        // Using top-left ceil-convention
        fillShape((int)Math.ceil(minYVert.getY()), (int)Math.ceil(maxYVert.getY()));
    }

    public void scanConvertTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, int orientation)
    {
        scanConertLine(minYVert, maxYVert, 0 + orientation);
        scanConertLine(minYVert, midYVert, 1 - orientation);
        scanConertLine(midYVert, maxYVert, 1 - orientation);

    }

    // Assume the vertexes are sorted
    private void scanConertLine(Vertex minYVertex, Vertex maxYVertex, int side)
    {
        // Using top-left ceil-convention
        int yStart = (int)Math.ceil(minYVertex.getY());
        int yEnd   = (int)Math.ceil(maxYVertex.getY());
        int xStart = (int)Math.ceil(minYVertex.getX());
        int xEnd   = (int)Math.ceil(maxYVertex.getX());

        float yDist = maxYVertex.getY() - minYVertex.getY();
        float xDist = maxYVertex.getX() - minYVertex.getX();

        if(yDist <= 0)
        {
            return;
        }

        // For each y coordinate, defines how far to move on the x axis
        float xStep = xDist/yDist;
        float yStep = yStart - minYVertex.getY();
        float currX = (float)xStart + yStep * xStep;

        for(int j = yStart; j < yEnd; j++)
        {
            scanBuffer[j * 2 + side] = (int)Math.ceil(currX);
            currX += xStep;
        }
    }
}
