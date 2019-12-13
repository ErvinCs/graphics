package renderer;

import entities.Entity;
import models.Model3D;
import models.TexturedModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.TerrainShader;
import terrain.Terrain;
import textures.Texture;
import util.MathUtil;

import java.util.List;

public class TerrainRenderer {
    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.begin();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.end();
    }

    public void render(List<Terrain> terrains) {
        for(Terrain ter : terrains) {
            bindTerrain(ter);
            loadModelMatrix(ter);
            GL11.glDrawElements(GL11.GL_TRIANGLES, ter.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindTerrain();
        }
    }

    public void bindTerrain(Terrain terrain) {
        Model3D model = terrain.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Texture texture = terrain.getTexture();
        shader.loadShineAndReflectivity(texture.getShineDamp(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
    }

    public void unbindTerrain() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(
                new Vector3f(terrain.getX(), 0, terrain.getZ()), new Vector3f(0, 0, 0), 1
        );
        shader.loadTransformMatrix(transformMatrix);
    }
}
