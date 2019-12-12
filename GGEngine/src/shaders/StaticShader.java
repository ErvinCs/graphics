package shaders;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import util.MathUtil;

public class StaticShader extends Shader {
    private static final String VERTEX_FILE = "src/shaders/res/vertexShader.shader";
    private static final String FRAGMENT_FILE = "src/shaders/res/fragmentShader.shader";

    private int transformMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        this.transformMatrixLocation = super.getUniformLocation("transformMatrix");
        this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        this.viewMatrixLocation = super.getUniformLocation("viewMatrix");

    }

    public void loadTransformMatrix(Matrix4f mat) {
        super.loadMat4f(transformMatrixLocation, mat);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = MathUtil.createViewMatrix(camera);
        super.loadMat4f(viewMatrixLocation, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f mat) {
        super.loadMat4f(projectionMatrixLocation, mat);
    }



}
