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
        Vertex minYVert = v1;
        Vertex midYVert = v2;
        Vertex maxYVert = v3;

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
        fillShape((int)minYVert.getY(), (int)maxYVert.getY());
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
        int yStart = (int)minYVertex.getY();
        int yEnd   = (int)maxYVertex.getY();
        int xStart = (int)minYVertex.getX();
        int xEnd   = (int)maxYVertex.getX();

        int yDist = yEnd - yStart;
        int xDist = xEnd - xStart;

        if(yDist <= 0)
        {
            return;
        }

        // For each y coordinate, defines how far to move on the x axis
        float xStep = (float)xDist/(float)yDist;
        float currX = (float)xStart;

        for(int j = yStart; j < yEnd; j++)
        {
            scanBuffer[j * 2 + side] = (int)currX;
            currX += xStep;
        }
    }
}
