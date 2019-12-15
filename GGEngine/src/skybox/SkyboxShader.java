package skybox;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import shaders.Shader;
import util.MathUtil;

public class SkyboxShader extends Shader {

    private static final String VERTEX_FILE = "src/skybox/res/skyboxVertexShader.shader";
    private static final String FRAGMENT_FILE = "src/skybox/res/skyboxFragmentShader.shader";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyboxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMat4f(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f matrix = MathUtil.createViewMatrix(camera);
        // The last column of the matrix determines the translation
        // Setting it to 0 makes the viewMatrix not move in relation to the camera
        matrix.m30 = 0;
        matrix.m31 = 0;
        matrix.m32 = 0;
        super.loadMat4f(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
