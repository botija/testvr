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
import com.botijasoftware.utils.Viewport;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.util.Random;


public class MainScreen extends ScreenVR {

    public ColorRGBA color = new ColorRGBA(ColorRGBA.CORNFLOWERBLUE);
    private Random random = new Random();
    Model model, model2;
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


    public MainScreen(ScreenManagerVR screenManager) {
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

    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    //public void Draw() {
    @Override
    public void onDrawEye(Eye eye) {

    if (!resourcesloaded) {
        model = mResourceManager.loadModel(R.raw.monkey);
        model2 = mResourceManager.loadModel(R.raw.teapot);
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

        Viewport vw = new Viewport(0, 0, width, height);
        //camera = new Camera(vw, new Vector3(0,0, 3), new Vector3(0, 1, 0), Vector3.UP);
        camera = new Camera(vw, new Vector3(0, 0, 10), new Vector3(0, 0, 0), new Vector3(0, 1, 0));
        vw.enable();
        camera.setPerspective(width, height);
        Renderer.modelview.loadIdentity();
        // camera.setOrtho( width, height);
        //mCamera.setOrtho(gl, width, height);
        //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
        //GLES20.glDisable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA);
        //GLES20.glDepthMask(false);
        float ratio = (float)width / (float)height;
        //Matrix.setIdentityM(projection_matrix.matrix, 0);
        Matrix.frustumM(projection_matrix.matrix, 0, -ratio, ratio, -1, 1, 1, 200);
        //shader.enable();
        resourcesloaded = true;
    }

        shader.Use();

        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);

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
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);


        //camera.setLookAt( new Vector3(eye.getEyeView()[0],eye.getEyeView()[1], eye.getEyeView()[2]));
        float x = (float)Math.sin(angle) * 10;
        float z = (float)Math.cos(angle) * 10;
        angle += 0.5f;
        if (angle > 360.0f)
            angle -= 360.0f;

        //camera.setLookAt(new Vector3(x, 0, z));

        //camera.set();
        //modelview_matrix.setLookAt(0, 0, 10, x, 0, z, 0, 1, 0);
        //Matrix.setIdentityM(modelview_matrix.matrix, 0);
        //Matrix.setLookAtM(modelview_matrix.matrix, 0, 0.0f, 0.0f, -6.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.setLookAtM(modelview_matrix.matrix, 0, -eye.getEyeView()[0], eye.getEyeView()[1], eye.getEyeView()[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        Matrix.setIdentityM(model_matrix.matrix, 0);

        //Matrix.setRotateEulerM( model_matrix.matrix, 0, angle, 1, angle);
        //Matrix.setRotateEulerM( model_matrix.matrix, 0, 0, angle, 0); //bug on android implementation
        //Matrix.setRotateEulerM( model_matrix.matrix, 0, 0, 0, angle);
        GLMatrix trans = new GLMatrix();
        Matrix.setIdentityM(trans.matrix, 0);
        GLMatrix rot = new GLMatrix();
        Matrix.setIdentityM(rot.matrix, 0);
        Matrix.translateM( trans.matrix, 0, 0.0f, 0.0f, 5.0f);
        Matrix.setRotateM( rot.matrix, 0, angle, 1, 1, 1);

        Matrix.multiplyMM(model_matrix.matrix, 0, trans.matrix, 0, rot.matrix,0);

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

        //model 2
        Matrix.setIdentityM(model_matrix.matrix, 0);
        Matrix.setLookAtM(modelview_matrix.matrix, 0, -eye.getEyeView()[0], eye.getEyeView()[1], eye.getEyeView()[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.setIdentityM(trans.matrix, 0);
        Matrix.setIdentityM(rot.matrix, 0);
        Matrix.translateM( trans.matrix, 0, 100.0f, -20.0f, 40.0f);
        Matrix.setRotateM( rot.matrix, 0, angle, 0, 0, 1);

        Matrix.multiplyMM(model_matrix.matrix, 0, trans.matrix, 0, rot.matrix,0);

        Matrix.multiplyMM(tmp.matrix, 0, modelview_matrix.matrix, 0, model_matrix.matrix,0);
        Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmp.matrix,0);
        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);

        for (int i = 0; i< model2.mMesh.size(); i++) {

            Mesh m = model2.mMesh.get(i);
            Renderer.BindTexture(Renderer.TEXTURE0, m.mTexture.getID());
            m.mVertexBuffer.Draw(m.mIndexBuffer);
        }
        //end model 2 render


    }

    public void onCardboardTrigger() {
        //color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
    }



}
