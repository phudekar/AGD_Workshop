package in.hpc.Workshop;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GameActivity extends Activity implements GLSurfaceView.Renderer {

    private float[] projectionMatrix;
    private float[] viewMatrix;
    private Triangle triangle;
    private SurfaceView surfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceView = new SurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        projectionMatrix = new float[16];
        viewMatrix = new float[16];
        surfaceView.setRenderer(this);
        setContentView(surfaceView);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        triangle = new Triangle();
        triangle.initialize();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        float aspectRatio = (float) width / height;
        GLES20.glViewport(0, 0, width, height);
        int numberOfUnits = 1;
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio * numberOfUnits, aspectRatio * numberOfUnits, -numberOfUnits, numberOfUnits, 2, 10);
    }

    @Override
    public void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        surfaceView.onResume();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(0f, 0f, 0f, 1f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float[] cameraPosition = new float[]{0, 0, -5};
        float[] lookAt = new float[]{0, 0, 0};
        float[] upVector = new float[]{0, 1, 0};

        // create view matrix
        Matrix.setLookAtM(viewMatrix, 0,
                cameraPosition[0],
                cameraPosition[1],
                cameraPosition[2],

                lookAt[0],
                lookAt[1],
                lookAt[2],

                upVector[0],
                upVector[1],
                upVector[2]);

        float[] viewProjectionMatrix = new float[16];
        Matrix.multiplyMM(viewProjectionMatrix, 0, viewMatrix, 0, projectionMatrix, 0);
        triangle.draw();
    }
}
