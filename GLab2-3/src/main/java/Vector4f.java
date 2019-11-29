public class Vector4f {
    private final float x;
    private final float y;
    private final float z;
    private final float w;

    public Vector4f(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length()
    {
        return (float)Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public float max()
    {
        return Math.max(Math.max(x, y), Math.max(z, w));
    }

    public float dot(Vector4f r)
    {
        return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
    }

    public Vector4f cross(Vector4f r)
    {
        float x_ = y * r.getZ() - z * r.getY();
        float y_ = z * r.getX() - x * r.getZ();
        float z_ = x * r.getY() - y * r.getX();

        return new Vector4f(x_, y_, z_, 0);
    }

    public Vector4f normalized()
    {
        float length = length();

        return new Vector4f(x / length, y / length, z / length, w / length);
    }

    public Vector4f rotate(Vector4f axis, float angle)
    {
        float sinAngle = (float)Math.sin(-angle);
        float cosAngle = (float)Math.cos(-angle);

        return this.cross(axis.mul(sinAngle)).add(                      //Rotation on local X
                (this.mul(cosAngle)).add(                               //Rotation on local Z
                        axis.mul(this.dot(axis.mul(1 - cosAngle)))));   //Rotation on local Y
    }


    public Vector4f lerp(Vector4f dest, float lerpFactor)
    {
        return dest.sub(this).mul(lerpFactor).add(this);
    }

    public Vector4f add(Vector4f r)
    {
        return new Vector4f(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
    }

    public Vector4f add(float r)
    {
        return new Vector4f(x + r, y + r, z + r, w + r);
    }

    public Vector4f sub(Vector4f r)
    {
        return new Vector4f(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
    }

    public Vector4f sub(float r)
    {
        return new Vector4f(x - r, y - r, z - r, w - r);
    }

    public Vector4f mul(Vector4f r)
    {
        return new Vector4f(x * r.getX(), y * r.getY(), z * r.getZ(), w * r.getW());
    }

    public Vector4f mul(float r)
    {
        return new Vector4f(x * r, y * r, z * r, w * r);
    }

    public Vector4f div(Vector4f r)
    {
        return new Vector4f(x / r.getX(), y / r.getY(), z / r.getZ(), w / r.getW());
    }

    public Vector4f div(float r)
    {
        return new Vector4f(x / r, y / r, z / r, w / r);
    }

    public Vector4f abs()
    {
        return new Vector4f(Math.abs(x), Math.abs(y), Math.abs(z), Math.abs(w));
    }

    public String toString()
    {
        return "Vector4f(" + x + ", " + y + ", " + z + ", " + w + ")";
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getZ()
    {
        return z;
    }

    public float getW()
    {
        return w;
    }

    public boolean equals(Vector4f r)
    {
        return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
    }
}
