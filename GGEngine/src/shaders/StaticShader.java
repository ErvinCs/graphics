package shaders;

import entities.Camera;
import entities.Light;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import util.MathUtil;

public class StaticShader extends Shader {
    private static final String VERTEX_FILE = "src/shaders/res/vertexShader.shader";
    private static final String FRAGMENT_FILE = "src/shaders/res/fragmentShader.shader";

    private int transformMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDampLocation;
    private int reflectivityLocation;
    private int useSimulatedLightingLocation;
    private int skyColorLocation;
    private int numberOfRowsLocation;
    private int textureOffsetLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        this.transformMatrixLocation = super.getUniformLocation("transformMatrix");
        this.projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
        this.lightPositionLocation = super.getUniformLocation("lightPosition");
        this.lightColorLocation = super.getUniformLocation("lightColor");
        this.shineDampLocation = super.getUniformLocation("shineDamp");
        this.reflectivityLocation = super.getUniformLocation("reflectivity");
        this.useSimulatedLightingLocation = super.getUniformLocation("useSimulatedLighting");
        this.skyColorLocation = super.getUniformLocation("skyColor");
        this.numberOfRowsLocation = super.getUniformLocation("numberOfRows");
        this.textureOffsetLocation = super.getUniformLocation("texOffset");
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

    public void loadSimulatedLighting(boolean useSimulatedLighting) {
        super.looadBoolean(useSimulatedLightingLocation, useSimulatedLighting);
    }

    public void loadLight(Light light) {
        super.loadVec3(lightPositionLocation, light.getPosition());
        super.loadVec3(lightColorLocation, light.getColor());
    }

    public void loadShineAndReflectivity(float shineDamp, float reflectivity) {
        super.loadFloat(shineDampLocation, shineDamp);
        super.loadFloat(reflectivityLocation, reflectivity);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVec3(skyColorLocation, new Vector3f(r, g, b));
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadInt(numberOfRowsLocation, numberOfRows);
    }

    public void loadTexOffset(float xOffset, float yOffset) {
        super.loadVec2(textureOffsetLocation, new Vector2f(xOffset, yOffset));
    }
}
