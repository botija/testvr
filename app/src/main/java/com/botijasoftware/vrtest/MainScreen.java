package com.botijasoftware.vrtest;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.botijasoftware.utils.Camera;
import com.botijasoftware.utils.ColorHSV;
import com.botijasoftware.utils.ColorRGBA;
import com.botijasoftware.utils.GLMatrix;
import com.botijasoftware.utils.Joystick;
import com.botijasoftware.utils.Mesh;
import com.botijasoftware.utils.Model;
import com.botijasoftware.utils.Quaternion;
import com.botijasoftware.utils.ResourceManager;
import com.botijasoftware.utils.Scene;
import com.botijasoftware.utils.SceneNode;
import com.botijasoftware.utils.ScreenManager;
import com.botijasoftware.utils.ScreenManagerVR;
import com.botijasoftware.utils.Screens.Screen;
import com.botijasoftware.utils.Screens.ScreenVR;
import com.botijasoftware.utils.ShaderProgram;
import com.botijasoftware.utils.Texture;
import com.botijasoftware.utils.Transform;
import com.botijasoftware.utils.Vector3;
import com.botijasoftware.utils.renderer.Renderer;
import com.botijasoftware.utils.Viewport;
import com.google.vr.sdk.base.Eye;
import com.google.vr.sdk.base.HeadTransform;

import java.util.ArrayList;
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
    Scene mainScene;
    Model skysphere;
    Vector3 cameraposition = new Vector3(0, 0, 0);

    private GLMatrix headViewMatrix = new GLMatrix();

    float angle = 0.0f;
    boolean resourcesloaded = false;
    boolean resourcesloading = false;
    SceneNode node;
    Joystick joystick = null;
    Vector3 headposition = new Vector3(0,0,0);


    public MainScreen(ScreenManagerVR screenManager) {
        super(screenManager);
    }

    public void LoadContent(ResourceManager resources) {
        mResourceManager = resources;

        ArrayList<Integer> joyids = Joystick.getGameControllerIds();

        if (joyids.size() > 0) {
            joystick = new Joystick(joyids.get(0));
        }
    }

    public void Update(float time) {
        if (state == ScreenState.ACTIVE) {

            headposition.X += joystick.thumb_left_x * 10.0f;
            headposition.Y += joystick.thumb_left_y * 10.0f;
            Log.d("VRTest", "X=" + headposition.X + " Y="+headposition.Y);
        }
        else if (state == ScreenState.ENDED) {
            mScreenManager.removeScreen(this);
        }
    }

    @Override
    public void onNewFrame(HeadTransform headTransform) {

        headTransform.getHeadView(headViewMatrix.matrix, 0);

        float forward[] = new float[4];
        float[] POS_MATRIX_MULTIPLY_VEC = {0, 0, 0, 1.0f};
        Matrix.multiplyMV(forward, 0, headViewMatrix.matrix, 0 , POS_MATRIX_MULTIPLY_VEC, 0);


        if (joystick != null) {
            headposition.X += 0.2f * joystick.thumb_left_y;
            //headposition.Y += 0.2f * joystick.thumb_left_y;
            //headposition.Z += 0.2f * joystick.thumb_left_y;

        }

        Log.d("VRTest", "X=" + headposition.X + " Y="+headposition.Y);
    }

    //public void Draw() {
    @Override
    public void onDrawEye(Eye eye) {

    if (!resourcesloaded) {
        model = mResourceManager.loadModel(R.raw.monkey);
        model2 = mResourceManager.loadModel(R.raw.model2);

        skysphere = mResourceManager.loadModel(R.raw.skysphere);

        mainScene = new Scene(0);

        node = new SceneNode("Monkey", model);
        node.setPosition(0.0f, 0.0f, 5.0f);
        node.setRotation(0.0f,0.0f,0.0f);
        node.setScale(1.0f, 1.0f, 1.0f);

        mainScene.getRoot().addNode(node);

        node = new SceneNode("Teapot", model2);
        node.setPosition(100.0f, -20.0f, 40.0f);
        node.setRotation(0.0f,0.0f,0.0f);
        node.setScale(2.0f, 2.0f, 2.0f);

        mainScene.getRoot().addNode(node);

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
        //GLES20.glDisable(GLES20.GL_CULL_FACE);


        //camera.setLookAt( new Vector3(eye.getEyeView()[0],eye.getEyeView()[1], eye.getEyeView()[2]));
        float x = (float)Math.sin(angle) * 10;
        float z = (float)Math.cos(angle) * 10;
        angle += 0.5f;
        if (angle > 360.0f)
            angle -= 360.0f;

        GLMatrix tmpmatrix = new GLMatrix();
        GLMatrix tmpmatrix2 = new GLMatrix();

        Matrix.setLookAtM(tmpmatrix.matrix, 0, -eye.getEyeView()[0] + headposition.X, eye.getEyeView()[1]  + headposition.Y , eye.getEyeView()[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        Matrix.multiplyMM(modelview_matrix.matrix, 0, headViewMatrix.matrix, 0, tmpmatrix.matrix, 0);

        //Render skysphere
        Transform skyTransform = new Transform();
        skyTransform.translation.setValue(0.0f, 0.0f, 0.0f);
        //skyTransform.scale.setValue( 200.0f);
        skyTransform.generateMatrix();

        Matrix.multiplyMM(tmpmatrix.matrix, 0, modelview_matrix.matrix, 0, skyTransform.getTransformMatrix().matrix, 0);
        Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmpmatrix.matrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);

        Mesh skym = skysphere.mMesh.get(0);
        Renderer.BindTexture(Renderer.TEXTURE0, skym.mTexture.getID());
        skym.mVertexBuffer.Draw(skym.mIndexBuffer);

        for (SceneNode n: mainScene.getRoot().getChildren()) {

            Transform transform = n.getTransform();
            transform.rotation.rotate(new Vector3(0, 0, 1), (float) Math.toRadians(angle));
            transform.generateMatrix();
            Matrix.multiplyMM(tmpmatrix.matrix, 0, modelview_matrix.matrix, 0, transform.getTransformMatrix().matrix, 0);
            Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmpmatrix.matrix, 0);

            GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0);

            Model mdl = n.getModel();
            for (int i = 0; i < mdl.mMesh.size(); i++) {

                Mesh m = mdl.mMesh.get(i);
                Renderer.BindTexture(Renderer.TEXTURE0, m.mTexture.getID());
                m.mVertexBuffer.Draw(m.mIndexBuffer);
            }
        }


    }

    @Override
    public void onCardboardTrigger() {
        //color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
        Log.d ("TestVR","OnCardboardTriegger()");
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.d ("TestVR","onGenericMotionEvent()");
        if (joystick != null)
            return joystick.onGenericMotionEvent(event);
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        Log.d ("TestVR","onTouchEvent()");
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.d ("TestVR","onKeyDown()");
        if (joystick != null)
            return joystick.onKeyDown(keyCode, event);
        else
            return false;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.d ("TestVR","onKeyUp()");
        if (joystick != null)
            return joystick.onKeyUp(keyCode, event);
        else
            return false;
    }



}
