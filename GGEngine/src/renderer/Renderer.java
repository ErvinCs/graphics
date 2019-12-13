package renderer;

import entities.Entity;
import models.Model3D;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.Shader;
import shaders.StaticShader;
import textures.Texture;
import util.MathUtil;

import java.util.List;
import java.util.Map;

public class Renderer {
    private static final float FOV_ANGLE = 45;
    private static final float NEAR_Z = 0.1f;
    private static final float FAR_Z = 1000f;
    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;
        // Cull the back faces of a model
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        createProjectionMatrix();
        shader.begin();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.end();
    }

    public void clear(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
    }

    public void draw(Map<TexturedModel, List<Entity>> entities) {
        // Render all the entities that use a texture model
        for(TexturedModel model : entities.keySet()) {
            bindTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for(Entity entity : batch) {
                loadEntityInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    public void bindTexturedModel(TexturedModel modelTex) {
        Model3D model = modelTex.getModel();
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        Texture texture = modelTex.getTexture();
        shader.loadShineAndReflectivity(texture.getShineDamp(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTex.getTexture().getID());
    }

    public void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadEntityInstance(Entity entity) {
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
    }

   /* public void draw(Entity entity, StaticShader shader) {
        TexturedModel modelTex = entity.getModel();

        // Load the transformation matrix & shader uniforms


        // Update - draw calls

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        // End - unbind

    }*/

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV_ANGLE / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_Z - NEAR_Z;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_Z + NEAR_Z) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_Z * FAR_Z) / frustumLength);
        projectionMatrix.m33 = 0;
    }
}
