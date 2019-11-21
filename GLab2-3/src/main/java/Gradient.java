public class Gradient {
    private Vector4f[] color;
    private Vector4f colorXStep;
    private Vector4f colorYStep;

    public Gradient(Vertex minYVert, Vertex midYVert, Vertex maxYVert)
    {
        // Holds color for each YVert
        color = new Vector4f[3];
        float oneOnDx = 1.0f / (((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert.getY())) -
                               ((minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert.getY())));
        float oneOnDy = -oneOnDx;

        color[0] = minYVert.getColor();
        color[1] = midYVert.getColor();
        color[2] = maxYVert.getColor();

        //dC/dx = ((C1-C2) * (y0-y2) - (C0-C2) * (y1-y2)) /  ((x1-x2) * (y0-y2) - (x0 - x2) * (y1-y2))
        colorXStep = (((color[1].sub(color[2]))
                .mul((minYVert.getY() - maxYVert.getY())))
                .sub(((color[0].sub(color[2]))
                        .mul((midYVert.getY() - maxYVert.getY())))))
                .mul(oneOnDx);

        //dC/dy = ((C1-C2) * (x0-x2) - (C0-C2) * (x1-x2)) /  ((x0-x2) * (y1-y2) - (x1 - x2) * (y0-y2))
        colorYStep = (((color[1].sub(color[2]))
                .mul((minYVert.getX() - maxYVert.getX())))
                .sub(((color[0].sub(color[2]))
                        .mul((midYVert.getX() - maxYVert.getX())))))
                .mul(oneOnDy);
    }


    public Vector4f getColor(int index) {
        return color[index];
    }

    public Vector4f getColorXStep() {
        return colorXStep;
    }

    public Vector4f getColorYStep() {
        return colorYStep;
    }
}
