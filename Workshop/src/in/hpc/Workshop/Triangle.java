package in.hpc.Workshop;

import android.opengl.GLES20;

public class Triangle extends Shape {

    int vertexCount;

    // lets setup coordinates for triangle
    private float[] coordinates = new float[]{
            0.0f, 0.5f, 0.0f,  //  top vertex
            -0.5f, -0.5f, 0.0f,    // left vertex
            0.5f, -0.5f, 0.0f      // right vertex
    };

    // shaders
    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Triangle() {
        vertexCount = getCoordinates().length / COORDS_PER_VERTEX;
    }


    @Override
    protected short[] getDrawOrder() {
        return new short[0];
    }

    @Override
    protected float[] getCoordinates() {
        return coordinates;
    }

    @Override
    protected String getFragmentShaderCode() {
        return fragmentShaderCode;
    }

    @Override
    protected String getVertexShaderCode() {
        return vertexShaderCode;
    }

    @Override
    protected void drawShape() {

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
    }
}
