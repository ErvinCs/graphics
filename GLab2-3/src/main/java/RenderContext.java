public class RenderContext extends Bitmap {
    private float[] zBuffer;        // Array of the pixels that should be drawn
                                    // Compares the pixels by z

    public RenderContext(int width, int height){
        super(width, height);
        zBuffer = new float[width * height];
    }

    // Set all the pixels to an arbitrary value that denotes it is empty (Float.MAX_VALUE)
    public void clearDepthBuffer()
    {
        for(int i = 0; i < zBuffer.length; i++)
        {
            zBuffer[i] = Float.MAX_VALUE;
        }
    }

    public void DrawMesh(Mesh mesh, Matrix4f transform, Bitmap texture)
    {
        for(int i = 0; i < mesh.getNumIndices(); i += 3)
        {
            fillTriangle(mesh.getVertex(mesh.getIndex(i)).transform(transform),
                        mesh.getVertex(mesh.getIndex(i + 1)).transform(transform),
                        mesh.getVertex(mesh.getIndex(i + 2)).transform(transform),
                        texture);
        }
    }

    public void fillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture)
    {
        // Normalize the coordinates
        Matrix4f screenSpaceTransform = new Matrix4f().initScreenSpaceTransform(getWidth()/2, getHeight()/2);
        Vertex minYVert = v1.transform(screenSpaceTransform).perspectiveDivide();
        Vertex midYVert = v2.transform(screenSpaceTransform).perspectiveDivide();
        Vertex maxYVert = v3.transform(screenSpaceTransform).perspectiveDivide();

        // ??
        if(minYVert.triangleArea(maxYVert, midYVert) >= 0)
        {
            return;
        }

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

        scanTriangle(minYVert, midYVert, maxYVert, minYVert.triangleArea(maxYVert, midYVert) >= 0, texture);
    }

    public void scanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean orientation, Bitmap texture)
    {
        Gradient gradient = new Gradient(minYVert, midYVert, maxYVert);
        Edge topToBot = new Edge(minYVert, maxYVert, gradient, 0);
        Edge topToMid = new Edge(minYVert, midYVert, gradient, 0);
        Edge midToBot = new Edge(midYVert, maxYVert, gradient, 1);

        // If orientation is false, then topToBot is the left edge, otherwise topToMid is the left edge
        ScanEdges(topToBot, topToMid, orientation, texture);
        ScanEdges(topToBot, midToBot, orientation, texture);
    }

    private void ScanEdges(Edge a, Edge b, boolean handedness, Bitmap texture)
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
            drawScanLine(left, right, j, texture);
            left.step();
            right.step();
        }
    }

    private void drawScanLine(Edge left, Edge right, int j, Bitmap texture)
    {
        int xMin = (int)Math.ceil(left.getX());
        int xMax = (int)Math.ceil(right.getX());
        float xPrestep = xMin - left.getX();

        float xDist = right.getX() - left.getX();
        float texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX())/xDist;
        float texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY())/xDist;
        float oneOverZXStep = (right.getOneOnZ() - left.getOneOnZ())/xDist;
        float depthXStep = (right.getDepth() - left.getDepth())/xDist;

        float texCoordX = left.getTexCoordX() + texCoordXXStep * xPrestep;
        float texCoordY = left.getTexCoordY() + texCoordYXStep * xPrestep;
        float oneOnZ = left.getOneOnZ() + oneOverZXStep * xPrestep;
        float depth = left.getDepth() + depthXStep * xPrestep;

        for(int i = xMin; i < xMax; i++)
        {
            int index = i + j * getWidth();
            if(depth < zBuffer[index])
            {
                zBuffer[index] = depth;
                float z = 1.0f/oneOnZ;
                int srcX = (int)((texCoordX * z) * (float)(texture.getWidth() - 1) + 0.5f);
                int srcY = (int)((texCoordY * z) * (float)(texture.getHeight() - 1) + 0.5f);

                CopyPixel(i, j, srcX, srcY, texture);
            }

            oneOnZ += oneOverZXStep;
            texCoordX += texCoordXXStep;
            texCoordY += texCoordYXStep;
            depth += depthXStep;
        }
    }
}
