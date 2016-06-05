package com.botijasoftware.vrtest;

import android.opengl.GLES20;

import com.botijasoftware.utils.Camera;
import com.botijasoftware.utils.ColorHSV;
import com.botijasoftware.utils.ColorRGBA;
import com.botijasoftware.utils.Mesh;
import com.botijasoftware.utils.Model;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.ScreenManagerVR;
import com.botijasoftware.utils.Screens.ScreenVR;
import com.botijasoftware.utils.ShaderProgram;
import com.botijasoftware.utils.Vector3;
import com.botijasoftware.utils.renderer.Renderer;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.botijasoftware.utils.Viewport;

import java.util.Random;


public class MainScreen extends ScreenVR {

    public ColorRGBA color = new ColorRGBA(ColorRGBA.CORNFLOWERBLUE);
    private Random random = new Random();
    Model model;
    ShaderProgram shader;
    int shaderid;
    int uvertex;
    int ucolor;
    int utextcoord;
    int mMVMatrixUniformLocation;
    int mPMatrixUniformLocation;
    int texture0;
    Camera camera;

    float angle = 0.0f;


    public MainScreen(ScreenManagerVR screenManager) {
        super(screenManager);
    }

    public void LoadContent(ResourceManager resources) {
        model = resources.loadModel(R.raw.monkey);
        shader = new ShaderProgram(R.raw.basic_vs, R.raw.basic_ps);
        shader.LoadContent(resources);

        shaderid = shader.getProgramID();
        uvertex = GLES20.glGetAttribLocation(shaderid, "aVertexPosition");
        ucolor = GLES20.glGetAttribLocation(shaderid, "aVertexColor");
        utextcoord = GLES20.glGetAttribLocation(shaderid, "aVertexTextureCoord");
        mMVMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uMVMatrix");
        mPMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uPMatrix");

        texture0 = GLES20.glGetUniformLocation(shaderid, "texture0");

        Viewport vw = new Viewport(0,0,1000,1000);
        camera = new Camera(vw, new Vector3(0,0, -100), Vector3.FORWARD, Vector3.UP);
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

    public void onDrawEye(Eye eye) {

        GLES20.glClearColor(color.R, color.G, color.B, color.A);
        GLES20.glClear( GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //camera.setLookAt(new Vector3(-100, -100, -100));
        camera.setLookAt( new Vector3(eye.getEyeView()[0],eye.getEyeView()[1], eye.getEyeView()[2]));

        camera.set();

        Renderer.BindAttribute(Renderer.ATTRIBUTE_VERTEX, uvertex);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_COLOR, ucolor);
        Renderer.BindAttribute(Renderer.ATTRIBUTE_TEXCOORDS, utextcoord);

        Renderer.setTextureHandle(Renderer.TEXTURE0, texture0);

        shader.Use();

        GLES20.glUniformMatrix4fv(mMVMatrixUniformLocation, 1, false, Renderer.modelview.matrix, 0);
        GLES20.glUniformMatrix4fv(mPMatrixUniformLocation, 1, false, Renderer.projection.matrix, 0);

        //Renderer.modelview.loadIdentity();
        //angle += 1.0f;
        //Renderer.modelview.rotate(angle, 0.5f, 0.5f, 0.5f);
        //Renderer.modelview.scale(100, 100, 100);

        for (int i = 0; i< model.mMesh.size(); i++) {

            Mesh m = model.mMesh.get(i);
            GLES20.glBindTexture(texture0, m.mTexture.getID());
            m.mVertexBuffer.Draw(m.mIndexBuffer);
        }
    }

    public void onCardboardTrigger() {
        color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
    }



}
