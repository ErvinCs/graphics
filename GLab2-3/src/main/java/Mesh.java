import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class Mesh {
    private List<Vertex> vertexArray;
    private List<Integer> indexArray;

    public Vertex getVertex(int i) { return vertexArray.get(i); }
    public int getIndex(int i) { return indexArray.get(i); }
    public int getNumIndices() { return indexArray.size(); }

    public Mesh(String fileName) throws IOException
    {
        IndexedModel model = new OBJModel(fileName).ToIndexedModel();

        vertexArray = new ArrayList<Vertex>();
        for(int i = 0; i < model.getPositions().size(); i++)
        {
            vertexArray.add(new Vertex(
                    model.getPositions().get(i),
                    model.getTexCoords().get(i)));
        }

        indexArray = model.getIndices();
    }
}
