package models;

/**
 * A 3D Object Model associated with a VAO & the number of vertices it uses.
 */
public class Model3D {
    private int vaoID;
    private int vertexCount;

    public Model3D(int vaoID, int vertexCount)
    {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }
}
