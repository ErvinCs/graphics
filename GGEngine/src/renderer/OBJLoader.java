package renderer;

import models.Model3D;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class OBJLoader {
    public static Model3D loadObjModel(String filepath, ModelLoader loader) {
        FileReader reader = null;
        try {
            reader = new FileReader(new File("src/models/res/" + filepath));
        } catch (FileNotFoundException ex) {
            System.err.println("Could not load OBJ file!");
            ex.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        List<Vector3f> vertices = new ArrayList<>();
        List<Vector2f> textures = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        float[] vertexArray = null;
        float[] normalArray = null;
        float[] textureArray = null;
        int[] indexArray = null;

        try {
            while (true) {
                line = bufferedReader.readLine();
                String[] currLine = line.split(" ");
                if (line.startsWith("vt")) {
                    Vector2f texture = new Vector2f(
                            Float.parseFloat(currLine[1]),
                            Float.parseFloat(currLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn")) {
                    Vector3f normal = new Vector3f(
                            Float.parseFloat(currLine[1]),
                            Float.parseFloat(currLine[2]),
                            Float.parseFloat(currLine[3]));
                    normals.add(normal);
                } else  if (line.startsWith("v")) {
                    Vector3f vertex = new Vector3f(
                            Float.parseFloat(currLine[1]),
                            Float.parseFloat(currLine[2]),
                            Float.parseFloat(currLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("f")) {
                    textureArray = new float[vertices.size() * 2];
                    normalArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while(line != null) {
                if(!line.startsWith("f")) {
                    line = bufferedReader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex2, indices, textures, normals, textureArray, normalArray);
                processVertex(vertex3, indices, textures, normals, textureArray, normalArray);

                line = bufferedReader.readLine();
            }

            bufferedReader.close();
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        vertexArray = new float[vertices.size() * 3];
        indexArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex : vertices) {
            vertexArray[vertexPointer++] = vertex.x;
            vertexArray[vertexPointer++] = vertex.y;
            vertexArray[vertexPointer++] = vertex.z;
        }

        for(int i = 0; i < indices.size(); i++) {
            indexArray[i] = indices.get(i);
        }

        return loader.loadToVAO(vertexArray, textureArray, normalArray, indexArray);
    }

    private static void processVertex(String[] vertexData,
                                      List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals,
                                      float[] textureArray, float[] normalArray) {
        int currentVertexPosition = Integer.parseInt(vertexData[0]) - 1;        //Note that the OBJ file starts at 1
        indices.add(currentVertexPosition);

        Vector2f currentTexture = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPosition * 2    ] = currentTexture.x;
        textureArray[currentVertexPosition * 2 + 1] = 1 - currentTexture.y;     //Not sure about the 1 - Y

        Vector3f currentNormal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArray[currentVertexPosition * 3    ] = currentNormal.x;
        normalArray[currentVertexPosition * 3 + 1] = currentNormal.y;
        normalArray[currentVertexPosition * 3 + 2] = currentNormal.z;


    }
}
