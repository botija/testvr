package com.botijasoftware.vrtest;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.botijasoftware.utils.Camera;
import com.botijasoftware.utils.ColorHSV;
import com.botijasoftware.utils.ColorRGBA;
import com.botijasoftware.utils.GLMatrix;
import com.botijasoftware.utils.Mesh;
import com.botijasoftware.utils.Model;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ScreenManager;
import com.botijasoftware.utils.ScreenManagerVR;
import com.botijasoftware.utils.Screens.Screen;
import com.botijasoftware.utils.Screens.ScreenVR;
import com.botijasoftware.utils.ShaderProgram;
import com.botijasoftware.utils.Vector3;
import com.botijasoftware.utils.renderer.Renderer;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.botijasoftware.utils.Viewport;

import java.util.Random;


public class MainScreen extends Screen {

    public ColorRGBA color = new ColorRGBA(ColorRGBA.CORNFLOWERBLUE);
    private Random random = new Random();
    Model model;
    ShaderProgram shader;
    ResourceManager mResourceManager;
    int shaderid;
    int uvertex;
    int ucolor;
    int utextcoord;
    int unormal;
    int mMVPMatrixUniformLocation;
    int texture0;
    Camera camera;
    GLMatrix modelview_matrix = new GLMatrix();
    GLMatrix projection_matrix =  new GLMatrix();
    GLMatrix modelview_projection_matrix =  new GLMatrix();
    GLMatrix model_matrix =  new GLMatrix();

    float angle = 0.0f;
    boolean resourcesloaded = false;
    boolean resourcesloading = false;


    public MainScreen(ScreenManager screenManager) {
        super(screenManager);
    }

    public void LoadContent(ResourceManager resources) {
        mResourceManager = resources;



    }

    public void Update(float time) {
        if (state == ScreenState.ACTIVE) {

        }
        else if (state == ScreenState.ENDED) {
            mScreenManager.removeScreen(this);
        }
    }

    public void onNewFrame(HeadTransform headTransform) {

    }

    public void Draw() {

    if (!resourcesloaded) {
        model = mResourceManager.loadModel(R.raw.teapot);
        shader = new ShaderProgram(R.raw.shader_vs, R.raw.shader_ps);
        shader.LoadContent(mResourceManager);

        shaderid = shader.getProgramID();
        uvertex = GLES20.glGetAttribLocation(shaderid, "aVertexPosition");
        ucolor = GLES20.glGetAttribLocation(shaderid, "aVertexColor");
        utextcoord = GLES20.glGetAttribLocation(shaderid, "aVertexTextureCoord");
        unormal = GLES20.glGetAttribLocation(shaderid, "aVertexNormal");
        mMVPMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uMVPMatrix");


        texture0 = GLES20.glGetUniformLocation(shaderid, "texture0");

        Renderer.BindAttribute(Renderer.ATTRIBUTE_VERTEX, uvertex);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_COLOR, ucolor);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_TEXCOORDS, utextcoord);

        Renderer.setTextureHandle(Renderer.TEXTURE0, texture0);

        shader.Use();

        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);

        Viewport vw = new Viewport(0, 0, 1920, 1080);
        //camera = new Camera(vw, new Vector3(0,0, 3), new Vector3(0, 1, 0), Vector3.UP);
        camera = new Camera(vw, new Vector3(0, 0, 10), new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        vw.enable();
        camera.setPerspective(1920, 1080);
        Renderer.modelview.loadIdentity();
        // camera.setOrtho( width, height);
        //mCamera.setOrtho(gl, width, height);
        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA);
        //GLES20.glDepthMask(false);
        float ratio = (float) 1920 / 1080;
        //Matrix.setIdentityM(projection_matrix.matrix, 0);
        Matrix.frustumM(projection_matrix.matrix,0, -ratio, ratio, -1, 1, 1, 1000);
        //shader.enable();
        resourcesloaded = true;
    }

    //public void onDrawEye(Eye eye) {

        /*if (!resourcesloaded && !resourcesloading) {
            resourcesloading = true;
            model = mResourceManager.loadModel(R.raw.monkey);
            shader = new ShaderProgram(R.raw.shader_vs, R.raw.shader_ps);
            shader.LoadContent(mResourceManager);

            shaderid = shader.getProgramID();
            uvertex = GLES20.glGetAttribLocation(shaderid, "aVertexPosition");
            ucolor = GLES20.glGetAttribLocation(shaderid, "aVertexColor");
            utextcoord = GLES20.glGetAttribLocation(shaderid, "aVertexTextureCoord");
            mMVMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uMVMatrix");
            mPMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uPMatrix");

            texture0 = GLES20.glGetUniformLocation(shaderid, "texture0");

            Viewport vw = new Viewport(0,0,1000,1000);
            camera = new Camera(vw, new Vector3(0, 0, 100), new Vector3(0, 0, 0), Vector3.UP);

            resourcesloaded = true;
        }
        if (!resourcesloaded) return;*/

        GLES20.glClearColor(color.R, color.G, color.B, color.A);
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);


        //camera.setLookAt( new Vector3(eye.getEyeView()[0],eye.getEyeView()[1], eye.getEyeView()[2]));
        float x = (float)Math.sin(angle) * 10;
        float z = (float)Math.cos(angle) * 10;
        angle += Math.PI * 0.01f;
        if (angle > Math.PI * 2.0f)
            angle -= Math.PI * 2.0f;

        //camera.setLookAt(new Vector3(x, 0, z));

        //camera.set();
        //modelview_matrix.setLookAt(0, 0, 10, x, 0, z, 0, 1, 0);
        //Matrix.setIdentityM(modelview_matrix.matrix, 0);
        Matrix.setLookAtM(modelview_matrix.matrix, 0, 0.0f, 0.0f, -10, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        Matrix.setIdentityM(model_matrix.matrix, 0);
        Matrix.setRotateEulerM( model_matrix.matrix, 0, x, 1.0f, z);
        GLMatrix tmp = new GLMatrix();
        Matrix.multiplyMM(tmp.matrix, 0, modelview_matrix.matrix, 0, model_matrix.matrix,0);
        Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmp.matrix,0);
        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);
        /*Renderer.modelview.loadIdentity();
        angle += 1.0f;
        Renderer.modelview.rotate(angle, 0.5f, 0.5f, 0.5f);
        Renderer.modelview.scale(10, 10, 10);*/

        //Renderer.modelview.scale(100, 100, 100);
        //Renderer.modelview.loadIdentity();
        //camera.set();
        for (int i = 0; i< model.mMesh.size(); i++) {

            Mesh m = model.mMesh.get(i);
            //GLES20.glBindTexture(texture0, m.mTexture.getID());
            Renderer.BindTexture(Renderer.TEXTURE0, m.mTexture.getID());
            m.mVertexBuffer.Draw(m.mIndexBuffer);
        }
    }

    public void onCardboardTrigger() {
        color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
    }



}
