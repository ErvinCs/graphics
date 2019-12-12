package util;

import entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import java.lang.Math;

public class MathUtil {
    public static Matrix4f createTransformMatrix(Vector3f translation, Vector3f rotation, float scale) {
        Matrix4f mat = new Matrix4f();
        mat.setIdentity();

        Matrix4f.translate(translation, mat, mat);

        Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), mat, mat);
        Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), mat, mat);

        Matrix4f.scale(new Vector3f(scale, scale, scale), mat, mat);

        return mat;
    }

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
}
