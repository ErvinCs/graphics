package renderer;

import models.Model3D;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;
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

    public int loadTexture(String filename) {
        Texture texture = null;
        try {
        texture = TextureLoader.getTexture("PNG", new FileInputStream("src/textures/res/" + filename));
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
}
