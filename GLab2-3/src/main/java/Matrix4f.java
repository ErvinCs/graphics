public class Matrix4f {
    private float[][] matrix;

    public Matrix4f()
    {
        matrix = new float[4][4];
    }

    public Matrix4f initIdentity()
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++) {
                if(i == j)
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }

        return this;
    }

    // Transform the vertex positions from normalized coordinates to pixel coordinates
    public Matrix4f initScreenSpaceTransform(float halfWidth, float halfHeight)
    {
        // Note that 0 is at the top of the screen
        matrix[0][0] = halfWidth;	matrix[0][1] = 0;	        matrix[0][2] = 0;	matrix[0][3] = halfWidth;
        matrix[1][0] = 0;	        matrix[1][1] = -halfHeight;	matrix[1][2] = 0;	matrix[1][3] = halfHeight;
        matrix[2][0] = 0;	        matrix[2][1] = 0;	        matrix[2][2] = 1;	matrix[2][3] = 0;
        matrix[3][0] = 0;	        matrix[3][1] = 0;	        matrix[3][2] = 0;	matrix[3][3] = 1;

        return this;
    }

    public Matrix4f initTranslation(float x, float y, float z)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++) {
                if(i == j)
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }
        matrix[0][3] = x;
        matrix[1][3] = y;
        matrix[2][3] = z;
        //matrix[3][3] = 1

        return this;
    }

    public Matrix4f initRotation(float x, float y, float z, float angle)
    {
        float sin = (float)Math.sin(angle);
        float cos = (float)Math.cos(angle);

        matrix[0][0] = cos+x*x*(1-cos);   matrix[0][1] = x*y*(1-cos)-z*sin; matrix[0][2] = x*z*(1-cos)+y*sin; matrix[0][3] = 0;
        matrix[1][0] = y*x*(1-cos)+z*sin; matrix[1][1] = cos+y*y*(1-cos);	matrix[1][2] = y*z*(1-cos)-x*sin; matrix[1][3] = 0;
        matrix[2][0] = z*x*(1-cos)-y*sin; matrix[2][1] = z*y*(1-cos)+x*sin; matrix[2][2] = cos+z*z*(1-cos);   matrix[2][3] = 0;
        matrix[3][0] = 0;	              matrix[3][1] = 0;	                matrix[3][2] = 0;	              matrix[3][3] = 1;

        return this;
    }

    public Matrix4f initRotation(float x, float y, float z)
    {
        Matrix4f rx = new Matrix4f();
        Matrix4f ry = new Matrix4f();
        Matrix4f rz = new Matrix4f();

        rz.matrix[0][0] = (float)Math.cos(z);   rz.matrix[0][1] = -(float)Math.sin(z);  rz.matrix[0][2] = 0;				    rz.matrix[0][3] = 0;
        rz.matrix[1][0] = (float)Math.sin(z);   rz.matrix[1][1] = (float)Math.cos(z);   rz.matrix[1][2] = 0;					rz.matrix[1][3] = 0;
        rz.matrix[2][0] = 0;					rz.matrix[2][1] = 0;					rz.matrix[2][2] = 1;					rz.matrix[2][3] = 0;
        rz.matrix[3][0] = 0;					rz.matrix[3][1] = 0;					rz.matrix[3][2] = 0;					rz.matrix[3][3] = 1;

        rx.matrix[0][0] = 1;					rx.matrix[0][1] = 0;					rx.matrix[0][2] = 0;					rx.matrix[0][3] = 0;
        rx.matrix[1][0] = 0;					rx.matrix[1][1] = (float)Math.cos(x);   rx.matrix[1][2] = -(float)Math.sin(x);  rx.matrix[1][3] = 0;
        rx.matrix[2][0] = 0;					rx.matrix[2][1] = (float)Math.sin(x);   rx.matrix[2][2] = (float)Math.cos(x);   rx.matrix[2][3] = 0;
        rx.matrix[3][0] = 0;					rx.matrix[3][1] = 0;					rx.matrix[3][2] = 0;					rx.matrix[3][3] = 1;

        ry.matrix[0][0] = (float)Math.cos(y);   ry.matrix[0][1] = 0;					ry.matrix[0][2] = -(float)Math.sin(y);  ry.matrix[0][3] = 0;
        ry.matrix[1][0] = 0;					ry.matrix[1][1] = 1;					ry.matrix[1][2] = 0;					ry.matrix[1][3] = 0;
        ry.matrix[2][0] = (float)Math.sin(y);   ry.matrix[2][1] = 0;					ry.matrix[2][2] = (float)Math.cos(y);   ry.matrix[2][3] = 0;
        ry.matrix[3][0] = 0;					ry.matrix[3][1] = 0;					ry.matrix[3][2] = 0;					ry.matrix[3][3] = 1;

        matrix = rz.mul(ry.mul(rx)).getM();

        return this;
    }

    public Matrix4f initScale(float x, float y, float z)
    {
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++) {
                if(i == j)
                    matrix[i][j] = 1;
                else
                    matrix[i][j] = 0;
            }
        matrix[0][0] = x;
        matrix[1][1] = y;
        matrix[2][2] = z;

        return this;
    }

    public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar)
    {
        float tanHalfFOV = (float)Math.tan(fov / 2);
        float zRange = zNear - zFar;

        matrix[0][0] = 1.0f / (tanHalfFOV * aspectRatio);	matrix[0][1] = 0;					matrix[0][2] = 0;	                    matrix[0][3] = 0;
        matrix[1][0] = 0;						            matrix[1][1] = 1.0f / tanHalfFOV;	matrix[1][2] = 0;	                    matrix[1][3] = 0;
        matrix[2][0] = 0;						            matrix[2][1] = 0;					matrix[2][2] = (-zNear -zFar)/zRange;	matrix[2][3] = 2 * zFar * zNear / zRange;
        matrix[3][0] = 0;						            matrix[3][1] = 0;					matrix[3][2] = 1;	                    matrix[3][3] = 0;

        return this;
    }

    public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far)
    {
        float width = right - left;
        float height = top - bottom;
        float depth = far - near;

        matrix[0][0] = 2/width;
        matrix[0][1] = 0;	matrix[0][2] = 0;	matrix[0][3] = -(right + left)/width;
        matrix[1][0] = 0;	matrix[1][1] = 2/height;
        matrix[1][2] = 0;	matrix[1][3] = -(top + bottom)/height;
        matrix[2][0] = 0;	matrix[2][1] = 0;	matrix[2][2] = -2/depth;
        matrix[2][3] = -(far + near)/depth;
        matrix[3][0] = 0;	matrix[3][1] = 0;	matrix[3][2] = 0;	matrix[3][3] = 1;

        return this;
    }

    public Matrix4f initRotation(Vector4f forward, Vector4f up)
    {
        Vector4f f = forward.normalized();

        Vector4f r = up.normalized();
        r = r.cross(f);

        Vector4f u = f.cross(r);

        return initRotation(f, u, r);
    }

    public Matrix4f initRotation(Vector4f forward, Vector4f up, Vector4f right)
    {
        Vector4f f = forward;
        Vector4f r = right;
        Vector4f u = up;

        matrix[0][0] = r.getX();	matrix[0][1] = r.getY();	matrix[0][2] = r.getZ();	matrix[0][3] = 0;
        matrix[1][0] = u.getX();	matrix[1][1] = u.getY();	matrix[1][2] = u.getZ();	matrix[1][3] = 0;
        matrix[2][0] = f.getX();	matrix[2][1] = f.getY();	matrix[2][2] = f.getZ();	matrix[2][3] = 0;
        matrix[3][0] = 0;		matrix[3][1] = 0;		matrix[3][2] = 0;		matrix[3][3] = 1;

        return this;
    }

    public Vector4f transform(Vector4f r)
    {
        return new Vector4f(matrix[0][0] * r.getX() + matrix[0][1] * r.getY() + matrix[0][2] * r.getZ() + matrix[0][3] * r.getW(),
                matrix[1][0] * r.getX() + matrix[1][1] * r.getY() + matrix[1][2] * r.getZ() + matrix[1][3] * r.getW(),
                matrix[2][0] * r.getX() + matrix[2][1] * r.getY() + matrix[2][2] * r.getZ() + matrix[2][3] * r.getW(),
                matrix[3][0] * r.getX() + matrix[3][1] * r.getY() + matrix[3][2] * r.getZ() + matrix[3][3] * r.getW());
    }

    public Matrix4f mul(Matrix4f other)
    {
        Matrix4f res = new Matrix4f();

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                res.setAt(i, j, matrix[i][0] * other.getAt(0, j) +
                        matrix[i][1] * other.getAt(1, j) +
                        matrix[i][2] * other.getAt(2, j) +
                        matrix[i][3] * other.getAt(3, j));
            }
        }

        return res;
    }

    public float[][] getM()
    {
        float[][] res = new float[4][4];

        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++)
                res[i][j] = matrix[i][j];

        return res;
    }

    public float getAt(int x, int y)
    {
        return matrix[x][y];
    }

    public void setM(float[][] m)
    {
        this.matrix = m;
    }

    public void setAt(int x, int y, float value)
    {
        matrix[x][y] = value;
    }
}
