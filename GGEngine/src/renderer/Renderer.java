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

public class Renderer {
    private static final float FOV_ANGLE = 45;
    private static final float NEAR_Z = 0.1f;
    private static final float FAR_Z = 1000f;
    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader) {
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

    public void draw(Entity entity, StaticShader shader) {
        TexturedModel modelTex = entity.getModel();
        Model3D model = modelTex.getModel();
        // Start - bind
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        // Load the transformation matrix
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTex.getTexture().getID());
        // Update - draw calls
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
        // End - unbind
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

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
