package renderer;

import de.matthiasmann.twl.utils.PNGDecoder;
import models.Model3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import textures.TextureData;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Load models into memory
 * Store positional vertex data
 */
public class ModelLoader {
    // Keep track of VAOs & VBOs such that they can be freed later
    private List<Integer> vaoList = new ArrayList<>();
    private List<Integer> vboList = new ArrayList<>();
    private List<Integer> texList = new ArrayList<>();

    /**
     *
     * @param positions - positions of the model vertices
     * @param texCoords
     * @param normals
     * @param indices
     * @return Model3D
     */
    public Model3D loadToVAO(float[] positions, float[] texCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndexBuffer(indices);
        createVBO(0, 3, positions);
        createVBO(1, 2, texCoords);
        createVBO(2, 3, normals);
        unbindVAO();
        return new Model3D(vaoID, indices.length);    // Each vertex has 3 floats
    }

    public Model3D loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        createVBO(0, dimensions, positions);
        unbindVAO();
        return new Model3D(vaoID, positions.length / dimensions);
    }

    public int loadTexture(String filename) {
        Texture texture = null;
        try {
        texture = TextureLoader.getTexture("PNG", new FileInputStream("src/textures/res/" + filename));
        // When the object is rendered at a smaller dimension than the texture, lower the texture size
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        //Render the texture at a higher resolution using LOD Bias
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -1.0f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int textureId = texture.getTextureID();
        texList.add(textureId);
        return textureId;
    }

    /**
     * Free VAO, VBO, Texture memory
     */
    public void delete() {
        for (int vaoID : vaoList) {
            GL30.glDeleteVertexArrays(vaoID);
        }
        for (int vboID : vboList) {
            GL15.glDeleteBuffers(vboID);
        }
        for (int texID : texList) {
            GL11.glDeleteTextures(texID);
        }
    }

    public int loadCubeMap(String[] textureFiles) {
        int textureID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
        for(int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile("src/skybox/res/" + textureFiles[i]);
            // Load texture data to each face of the cube map
            // Cube map int codes are contiguous
            // Face order: Right, Left, Top, Bottom, Back, Front
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,
                    GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0,
                    GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        // Set filter parameters
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        texList.add(textureID);
        return textureID;
    }

    /**
     * Create a vertex array and return its ID
     * @return int - VAO id
     */
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaoList.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }

    /**
     * Create & bind a VBO with the given vertex data
     * @param index - index of the vertex attribute
     * @param size - number of components per vertex attribute
     * @param data - vertex buffer data
     */
    private void createVBO(int index, int size, float[] data) {
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = createFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndexBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vboList.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = createIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer createIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        // Notify that writing was finished and flip to reading mode
        buffer.flip();
        return buffer;
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);  // Bind VA 0 to unbind the currently bound VA
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        // Notify that writing was finished and flip to reading mode
        buffer.flip();
        return buffer;

    }

    /**
     * Load an image into a byte buffer and return a TextureData with PNG data
     * @param fileName - input file
     * @return TextureData
     */
    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load texture: <" + fileName + ">!");
            System.exit(-1);
        }
        return new TextureData(width, height, buffer);
    }
}
