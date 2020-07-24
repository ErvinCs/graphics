package terrain;

import models.Model3D;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderer.ModelLoader;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;
import util.MathUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {
    public static final float SIZE = 800;
    private final float MAX_HEIGHT = 40;
    private final float MIN_HEIGHT = -40;
    private final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x;
    private float z;
    private Model3D model;
    private TerrainTexturePack texturePack;
    private TerrainTexture blendMap;
    private float[][] heights;

    public Terrain(int gridX, int gridZ, ModelLoader loader, TerrainTexturePack texturePack, TerrainTexture blendMap, String heightMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;
        this.model = createTerrain(loader, heightMap);
    }

    public float getTerrainHeight(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float) heights.length - 1);
        int gridX = (int)Math.floor(terrainX / gridSquareSize);
        int gridZ = (int)Math.floor(terrainZ / gridSquareSize);
        if (gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
            return 0;
        }
        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
        float result;
        //Determine the triangle - barycentric interpolation
        if (xCoord <= (1-zCoord)) {
            result = MathUtil.barryCentric(
                    new Vector3f(0, heights[gridX][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        } else {
            result = MathUtil.barryCentric(
                    new Vector3f(1, heights[gridX + 1][gridZ], 0),
                    new Vector3f(1, heights[gridX + 1][gridZ + 1], 1),
                    new Vector3f(0, heights[gridX][gridZ + 1], 1),
                    new Vector2f(xCoord, zCoord));
        }
        return result;
    }

    private Model3D createTerrain(ModelLoader loader, String heightMapFile) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("res/terrain/" + heightMapFile));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int VERTEX_COUNT = image.getHeight();
        heights = new float[VERTEX_COUNT][VERTEX_COUNT];
        int count = VERTEX_COUNT * VERTEX_COUNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count*2];
        int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
        int vertexPointer = 0;
        for(int i=0;i<VERTEX_COUNT;i++){
            for(int j=0;j<VERTEX_COUNT;j++){
                vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
                float vertexHeight = getHeight(j, i, image);   // Calculate the height for the vertex
                heights[j][i] = vertexHeight;
                vertices[vertexPointer*3+1] = vertexHeight;
                vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer*3] = normal.x;
                normals[vertexPointer*3+1] = normal.y;
                normals[vertexPointer*3+2] = normal.z;
                textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
                textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
                vertexPointer++;
            }
        }
        int pointer = 0;
        for(int gz=0;gz<VERTEX_COUNT-1;gz++){
            for(int gx=0;gx<VERTEX_COUNT-1;gx++){
                int topLeft = (gz*VERTEX_COUNT)+gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
                int bottomRight = bottomLeft + 1;
                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    private Vector3f calculateNormal(int x, int y, BufferedImage image) {
        // Calculate the height of the 4 surrounding vertices
        float heightL = getHeight(x-1, y, image);
        float heightR = getHeight(x+1, y, image);
        float heightD = getHeight(x, y-1, image);
        float heightU = getHeight(x, y+1, image);
        Vector3f normal = new Vector3f(heightL-heightR, 2.0f, heightD-heightU);
        normal.normalise();
        return normal;
    }

    private float getHeight(int x, int y, BufferedImage image) {
        if (x < 0 || x >= image.getHeight() || y < 0 || y >= image.getHeight()) {
            return 0;
        }
        //Get the color of a pixel
        float height = image.getRGB(x, y);
        //Get height in range [-1,1]
        height += MAX_PIXEL_COLOR / 2.0f;
        height /= MAX_PIXEL_COLOR / 2.0f;
        //Get height in range [MIN_HEIGHT, MAX_HEIGHT]
        height *= MAX_HEIGHT;
        return height;
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public Model3D getModel() {
        return model;
    }

    public TerrainTexturePack getTexturePack() {
        return texturePack;
    }

    public TerrainTexture getBlendMap() {
        return blendMap;
    }
}
