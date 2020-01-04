package renderer;

import entities.Entity;
import models.Model3D;
import models.TexturedModel;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import shaders.StaticShader;
import textures.Texture;
import util.MathUtil;

import java.util.List;
import java.util.Map;

public class EntityRenderer {
    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.begin();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.end();
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
        shader.loadNumberOfRows(texture.getNumberOfRows());
        if (texture.getTransparent()) {
            RenderManager.disableCulling();
        }
        shader.loadSimulatedLighting(texture.getSimulatedLight());
        shader.loadShineAndReflectivity(texture.getShineDamp(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTex.getTexture().getID());
    }

    public void unbindTexturedModel() {
        RenderManager.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void loadEntityInstance(Entity entity) {
        Matrix4f transformMatrix = MathUtil.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.loadTransformMatrix(transformMatrix);
        shader.loadTexOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }


}
