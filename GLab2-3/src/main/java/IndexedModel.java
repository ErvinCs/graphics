import java.util.ArrayList;
import java.util.List;

public class IndexedModel {
    private List<Vector4f> positions;
    private List<Vector4f> texCoords;
    private List<Vector4f> normals;
    private List<Vector4f> tangents;
    private List<Integer> indices;

    public IndexedModel()
    {
        positions = new ArrayList<>();
        texCoords = new ArrayList<>();
        normals = new ArrayList<>();
        tangents = new ArrayList<>();
        indices = new ArrayList<>();
    }

    public void calculateNormals()
    {
        for(int i = 0; i < indices.size(); i += 3)
        {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector4f v1 = positions.get(i1).sub(positions.get(i0));
            Vector4f v2 = positions.get(i2).sub(positions.get(i0));

            Vector4f normal = v1.cross(v2).normalized();

            normals.set(i0, normals.get(i0).add(normal));
            normals.set(i1, normals.get(i1).add(normal));
            normals.set(i2, normals.get(i2).add(normal));
        }

        for(int i = 0; i < normals.size(); i++)
            normals.set(i, normals.get(i).normalized());
    }

    public void calculateTangents()
    {
        for(int i = 0; i < indices.size(); i += 3)
        {
            int i0 = indices.get(i);
            int i1 = indices.get(i + 1);
            int i2 = indices.get(i + 2);

            Vector4f edge1 = positions.get(i1).sub(positions.get(i0));
            Vector4f edge2 = positions.get(i2).sub(positions.get(i0));

            float deltaU1 = texCoords.get(i1).getX() - texCoords.get(i0).getX();
            float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
            float deltaU2 = texCoords.get(i2).getX() - texCoords.get(i0).getX();
            float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();

            float dividend = (deltaU1*deltaV2 - deltaU2*deltaV1);
            float f = dividend == 0 ? 0.0f : 1.0f/dividend;

            Vector4f tangent = new Vector4f(
                    f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()),
                    f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()),
                    f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()),
                    0);

            tangents.set(i0, tangents.get(i0).add(tangent));
            tangents.set(i1, tangents.get(i1).add(tangent));
            tangents.set(i2, tangents.get(i2).add(tangent));
        }

        for(int i = 0; i < tangents.size(); i++)
            tangents.set(i, tangents.get(i).normalized());
    }

    public List<Vector4f> getPositions() { return positions; }
    public List<Vector4f> getTexCoords() { return texCoords; }
    public List<Vector4f> getNormals() { return normals; }
    public List<Vector4f> getTangents() { return tangents; }
    public List<Integer>  getIndices() { return indices; }
}
