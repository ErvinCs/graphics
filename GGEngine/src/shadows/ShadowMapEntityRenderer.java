package shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.Model3D;
import models.TexturedModel;
import renderer.RenderManager;
import util.MathUtil;

/**
 * Renders all the entities to the shadow map.
 */
public class ShadowMapEntityRenderer {

	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;

	/**
	 * @param shader - the shadow shader program being used for the shadow render pass.
	 * @param projectionViewMatrix - the orthographic projection matrix multiplied by the light's "view" matrix.
	 */
	protected ShadowMapEntityRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}

	/**
	 * Renders entities to the shadow map.
	 * Binds each model after which all of the entities using that model are rendered to the shadow map.
	 * 
	 * @param entities - the entities to be rendered to the shadow map.
	 */
	protected void render(Map<TexturedModel, List<Entity>> entities) {
		// Bind the VAO to each model
		for (TexturedModel model : entities.keySet()) {
			Model3D rawModel = model.getModel();
			bindModel(rawModel);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
			// Disbale backface culling for transparent models
			if (model.getTexture().getTransparent()) {
				RenderManager.disableCulling();
			}
			// Render each entity that uses the model
			for (Entity entity : entities.get(model)) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);
			}
			if (model.getTexture().getTransparent()) {
				RenderManager.enableCulling();
			}
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

	/**
	 * Binds a raw model before rendering.
     * Only enables  attrib0 for the VAO positions & attrib1 for the texture coordinates.
	 * 
	 * @param rawModel - the model to be bound.
	 */
	private void bindModel(Model3D rawModel) {
		GL30.glBindVertexArray(rawModel.getVaoID());
		// Enable the position attribute
		GL20.glEnableVertexAttribArray(0);
		// Enable textureCoords
		GL20.glEnableVertexAttribArray(1);
	}

	/**
	 * Prepares an entity to be rendered.
	 * Creates a model matrix which is then multiplied with the projection and view matrix
     * to create the mvp-matrix.
     * Loads the mvp-matrix to the vertex shader as a uniform.
	 * 
	 * @param entity - the entity to be prepared for rendering.
	 */
	private void prepareInstance(Entity entity) {
		// MVP matrix calculation & loading
		Matrix4f modelMatrix = MathUtil.createTransformMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		Matrix4f mvpMatrix = Matrix4f.mul(projectionViewMatrix, modelMatrix, null);
		shader.loadMvpMatrix(mvpMatrix);
	}

}
