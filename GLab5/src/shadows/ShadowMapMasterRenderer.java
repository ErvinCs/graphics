package shadows;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

/**
 * This class is in charge of using all of the classes in the shadows package to carry out the shadow render pass.
 * Renders the scene to the Shadow Map Texture.
 */
public class ShadowMapMasterRenderer {
	// Shadow Map Resolution
	private static final int SHADOW_MAP_SIZE = 2048;

	private ShadowFrameBuffer shadowFbo;
	private ShadowShader shader;
	private ShadowBox shadowBox;
	private Matrix4f projectionMatrix = new Matrix4f();
	private Matrix4f lightViewMatrix = new Matrix4f();
	private Matrix4f projectionViewMatrix = new Matrix4f();
	private Matrix4f offset = createOffset();

	private ShadowMapEntityRenderer entityRenderer;

	/**
	 * Creates instances of the objects needed for rendering the scene to the shadow map.
	 * This includes the {@link ShadowBox} which calculates the position and size of the "view cuboid",
	 * the  renderer and shader program that are used to render objects to the shadow map,
	 * and the {@link ShadowFrameBuffer} to which the scene is rendered.
	 * The size of the shadow map is determined here.
	 * 
	 * @param camera - the camera being used in the scene.
	 */
	public ShadowMapMasterRenderer(Camera camera) {
		shader = new ShadowShader();
		shadowBox = new ShadowBox(lightViewMatrix, camera);
		shadowFbo = new ShadowFrameBuffer(SHADOW_MAP_SIZE, SHADOW_MAP_SIZE);
		entityRenderer = new ShadowMapEntityRenderer(shader, projectionViewMatrix);
	}

	/**
	 * Carries out the shadow render pass. This renders the entities to the shadow map.
	 * First the shadow box is updated to calculate the size and position of the "view cuboid".
	 * The light direction is assumed to be the inverse "lightPosition" (the light is very far from the scene).
	 * It prepares to render, renders the entities to the shadow map, and finishes rendering.
	 * 
	 * @param entities - the lists of entities to be rendered.
	 * 		Each list is associated with the {@link TexturedModel} that all of the entities in that list use.
	 * @param sun - the light acting as the sun in the scene.
	 */
	public void render(Map<TexturedModel, List<Entity>> entities, Light sun) {
		// Takes in the entities to be rendered to the shadow map and the light from which perspective the scene will be rendered
		shadowBox.update();
		Vector3f sunPosition = sun.getPosition();
		// Inverse of the sun position
		Vector3f lightDirection = new Vector3f(-sunPosition.x, -sunPosition.y, -sunPosition.z);
		begin(lightDirection, shadowBox);
		entityRenderer.render(entities);
		end();
	}

	/**
	 * This biased projection-view matrix is used to convert fragments into "shadow map space" when rendering the main render pass.
	 * It converts a world space position into a 2D coordinate on the shadow map.
	 * This is needed for the second part of shadow mapping.
	 * 
	 * @return The to-shadow-map-space matrix.
	 */
	public Matrix4f getToShadowMapSpaceMatrix() {
		// toShadowSpaceMat = -0.5 + 0.5 * orthoProjectionMatrix * lightViewMatrix
		return Matrix4f.mul(offset, projectionViewMatrix, null);
	}

	/**
	 * Clean up the shader and FBO on closing.
	 */
	public void delete() {
		shader.delete();
		shadowFbo.cleanUp();
	}

	/**
	 * @return The ID of the shadow map texture.
	 */
	public int getShadowMap() {
		return shadowFbo.getShadowMap();
	}

	/**
	 * @return The light's "view" matrix.
	 */
	protected Matrix4f getLightSpaceTransform() {
		return lightViewMatrix;
	}

	/**
	 * Prepare for the shadow render pass.
	 * This first updates the dimensions of the orthographic "view cuboid" based on the information that was calculated in the {@link ShadowBox} class.
	 * The light's "view" matrix is also calculated based on the light's direction and the center position of the "view cuboid" calculated in the {@link ShadowBox} class.
	 * These two matrices are multiplied together to create the projection-view matrix.
	 * This matrix determines the size, position, and orientation of the "view cuboid" in the world.
	 * This method also binds the shadows FBO so that everything rendered after this gets rendered to the FBO.
	 * It enables depth testing, and clears any data that is in the FBOs depth attachment from last frame.
	 * Finally it begins the shader program.
	 * 
	 * @param lightDirection - the direction of the light rays coming from the "sun".
	 * @param box - the shadow box, which contains all the information about the "view cuboid".
	 */
	private void begin(Vector3f lightDirection, ShadowBox box) {
		updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
		updateLightViewMatrix(lightDirection, box.getCenter());
		Matrix4f.mul(projectionMatrix, lightViewMatrix, projectionViewMatrix);
		// Render to the Shadow FBO (no color buffer)
		shadowFbo.bindFrameBuffer();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		shader.begin();
	}

	/**
	 * Stops the shader and unbinds the shadow FBO,
	 * so everything rendered after this point is rendered to the screen, rather than to the shadow FBO.
	 */
	private void end() {
		shader.end();
		shadowFbo.unbindFrameBuffer();
	}

	/**
	 * Updates the "view" matrix of the light.
	 * This creates a view matrix which will line up the direction of the "view cuboid" with the direction of the light.
	 * The light itself has no position, so the "view" matrix is centered t the center of the "view cuboid".
	 * The created view matrix determines where and how the "view cuboid" is positioned in the world.
	 * The size of the view cuboid is determined by the projection matrix.
	 *
	 * @param direction - the light direction
	 *		(and therefore the direction that the "view cuboid" should be pointing)
	 * @param center - the center of the "view cuboid" in world space.
	 */
	private void updateLightViewMatrix(Vector3f direction, Vector3f center) {
		// The view matrix must line up the cubeoid and with the light direction
		direction.normalise();
		center.negate();
		lightViewMatrix.setIdentity();
		float pitch = (float) Math.acos(new Vector2f(direction.x, direction.z).length());
		Matrix4f.rotate(pitch, new Vector3f(1, 0, 0), lightViewMatrix, lightViewMatrix);
		float yaw = (float) Math.toDegrees(((float) Math.atan(direction.x / direction.z)));
		yaw = direction.z > 0 ? yaw - 180 : yaw;
		Matrix4f.rotate((float) -Math.toRadians(yaw), new Vector3f(0, 1, 0), lightViewMatrix,
				lightViewMatrix);
		Matrix4f.translate(center, lightViewMatrix, lightViewMatrix);
	}

	/**
	 * Creates the orthographic projection matrix.
	 * This projection matrix sets the width, length and height of the "view cuboid",
	 * based on values calculated in the {@link ShadowBox} class.
	 * 
	 * @param width  - shadow box width.
	 * @param height - shadow box height.
	 * @param length - shadow box length.
	 */
	private void updateOrthoProjectionMatrix(float width, float height, float length) {
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = 2f / width;
		projectionMatrix.m11 = 2f / height;
		projectionMatrix.m22 = -2f / length;
		projectionMatrix.m33 = 1;
	}

	/**
	 * Create the offset for part of the conversion to shadow map space.
	 * This conversion is necessary to convert from one coordinate system to the
	 * coordinate system that used to sample to shadow map.
	 * 
	 * @return The offset as a matrix
	 */
	private static Matrix4f createOffset() {
		Matrix4f offset = new Matrix4f();
		offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
		offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
		return offset;
	}
}
