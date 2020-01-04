package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * The Camera entity (currently attached to the player) whose parameters are used in matrix calculation.
 */
public class Camera {
    private Vector3f position;      // XYZ
    private float pitch;            // Rise/Fall
    private float yaw;              // Left/Right aiming
    private float absYaw;
    private float roll;             // Rotation
    private float moveSpeed;

    //Good starting points:
    //#1
    //Position: Vector3f[135, 20,175]
    //Yaw:      -245.0f
    //Pitch:     5.0f
    //Roll:      0.0f
    //#2
    //Position: Vector3f[424, 65, 702]
    //Yaw:      -17.0f
    //Pitch:     14.0f
    //Roll:      0.0f
    public Camera() {
        this.position = new Vector3f(424, 65, 650);
        this.yaw = -17.0f;
        this.pitch = 14.0f;
        this.roll = 0.0f;
        this.moveSpeed = 1.5f;
    }

    public Camera(Vector3f position, float pitch, float yaw, float roll, float moveSpeed) {
        this.position = position;
        this.pitch = pitch;
        this.yaw = yaw;
        this.absYaw = Math.abs(yaw);
        this.roll = roll;
        this.moveSpeed = moveSpeed;
    }

    /**
     * Handles the movement input for the camera entity.
     * Z-Axis Movement: W, S
     * X-Axis Movement: A, D
     * Y-Axis Movement: Z, C
     * Rotation:        Q, E
     * Pitch:           X. V
     */
    public void move() {
        // Z
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += moveSpeed;
        }
        // X
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x -= moveSpeed;
        }
        // Y
        if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
            position.y += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
            position.y -= moveSpeed;
        }

        // YAW
        yaw %= 360;
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            yaw += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            yaw -= moveSpeed;
        }

        // PITCH
        if (Keyboard.isKeyDown(Keyboard.KEY_X)) {
            pitch += moveSpeed;
        }else if (Keyboard.isKeyDown(Keyboard.KEY_V)) {
            pitch -= moveSpeed;
        }

        // ROLL
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            this.roll += moveSpeed;
        } else if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
            this.roll -= moveSpeed;
        }

        //System.out.println(this.toString());
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    @Override
    public String toString() {
        return  "Camera: " + '\n' +
                "\tPosition: " + position.toString() + '\n' +
                "\tYaw: " + yaw + '\n' +
                "\tPitch: " + pitch + '\n' +
                "\tRoll: " + roll + '\n';
    }
}
