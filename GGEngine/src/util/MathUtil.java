package util;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import java.lang.Math;

public class MathUtil {
    /**
     * Calculates the translation matrix, rotates according to "rotation" and scales everything by a factor of "scale"
     * @param translation - Vector3f
     * @param rotation - Vector3f
     * @param scale - float
     * @return the Transformation Matrix with he given translation, rotation, scale
     */
    public static Matrix4f createTransformMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f mat = new Matrix4f();
        mat.setIdentity();

        // Translate the source matrix and stash the result in the destination matrix
        Matrix4f.translate(translation, mat, mat);

        // Rotate the matrix around the given axis the specified angle
        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), mat, mat);

        // Scale the matrix
        Matrix4f.scale(new Vector3f(scale, scale, scale), mat, mat);

        return mat;
    }

    /**
     * Calculates the View Matrix according to the "camera" transform
     * @param camera - needed its position,
     *      used in calculating the View matrix
     * @return the View Matrix for the given camera
     */
    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);

        Vector3f cameraPosition = camera.getPosition();
        Vector3f inverseCameraPosition = new Vector3f(-cameraPosition.x, -cameraPosition.y, -cameraPosition.z);
        Matrix4f.translate(inverseCameraPosition, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    /**
     * @param p1 - triangle vertex
     * @param p2 - triangle vertex
     * @param p3 - triangle vertex
     * @param pos - object position
     */
    public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
        float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
        float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
        float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * p1.y + l2 * p2.y + l3 * p3.y;
    }
}
