package com.botijasoftware.vrtest

import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent

import com.botijasoftware.utils.Camera
import com.botijasoftware.utils.ColorHSV
import com.botijasoftware.utils.ColorRGBA
import com.botijasoftware.utils.GLMatrix
import com.botijasoftware.utils.Joystick
import com.botijasoftware.utils.Mesh
import com.botijasoftware.utils.Model
import com.botijasoftware.utils.Quaternion
import com.botijasoftware.utils.ResourceManager
import com.botijasoftware.utils.Scene
import com.botijasoftware.utils.SceneNode
import com.botijasoftware.utils.ScreenManager
import com.botijasoftware.utils.ScreenManagerVR
import com.botijasoftware.utils.Screens.Screen
import com.botijasoftware.utils.Screens.ScreenVR
import com.botijasoftware.utils.ShaderProgram
import com.botijasoftware.utils.Texture
import com.botijasoftware.utils.Transform
import com.botijasoftware.utils.Vector3
import com.botijasoftware.utils.renderer.Renderer
import com.botijasoftware.utils.Viewport
import com.google.vr.sdk.base.Eye
import com.google.vr.sdk.base.HeadTransform

import java.util.ArrayList
import java.util.Random


class MainScreen(screenManager: ScreenManagerVR) : ScreenVR(screenManager) {

    var color = ColorRGBA(ColorRGBA.CORNFLOWERBLUE)
    private val random = Random()
    internal lateinit var model: Model
    internal lateinit var model2: Model
    internal lateinit var shader: ShaderProgram
    internal lateinit var mResourceManager: ResourceManager
    internal var shaderid: Int = 0
    internal var uvertex: Int = 0
    internal var ucolor: Int = 0
    internal var utextcoord: Int = 0
    internal var unormal: Int = 0
    internal var mMVPMatrixUniformLocation: Int = 0
    internal var texture0: Int = 0
    internal lateinit var camera: Camera
    internal var modelview_matrix = GLMatrix()
    internal var projection_matrix = GLMatrix()
    internal var modelview_projection_matrix = GLMatrix()
    internal var model_matrix = GLMatrix()
    internal lateinit var mainScene: Scene
    internal lateinit var skysphere: Model
    internal var cameraposition = Vector3(0f, 0f, 0f)

    private val headViewMatrix = GLMatrix()

    internal var angle = 0.0f
    internal var resourcesloaded = false
    internal var resourcesloading = false
    internal lateinit var node: SceneNode
    internal var joystick: Joystick? = null
    internal var headposition = Vector3(0f, 0f, 0f)

    override fun LoadContent(resources: ResourceManager) {
        mResourceManager = resources

        val joyids = Joystick.gameControllerIds

        if (joyids.size > 0) {
            joystick = Joystick(joyids[0])
        }
    }

    override fun Update(time: Float) {
        if (state === ScreenVR.ScreenState.ACTIVE) {

            headposition.X = headposition.X + joystick!!.thumb_left_x * 10.0f
            headposition.Y = headposition.Y + joystick!!.thumb_left_y * 10.0f
            Log.d("VRTest", "X=" + headposition.X + " Y=" + headposition.Y)
        } else if (state === ScreenVR.ScreenState.ENDED) {
            mScreenManager.removeScreen(this)
        }
    }

    override fun onNewFrame(headTransform: HeadTransform) {

        headTransform.getHeadView(headViewMatrix.matrix, 0)

        val forward = FloatArray(4)
        val POS_MATRIX_MULTIPLY_VEC = floatArrayOf(0f, 0f, 0f, 1.0f)
        Matrix.multiplyMV(forward, 0, headViewMatrix.matrix, 0, POS_MATRIX_MULTIPLY_VEC, 0)


        if (joystick != null) {
            headposition.X = headposition.X + 0.2f * joystick!!.thumb_left_y
            //headposition.Y += 0.2f * joystick.thumb_left_y;
            //headposition.Z += 0.2f * joystick.thumb_left_y;

        }

        //Log.d("VRTest", "X=" + headposition.X + " Y="+headposition.Y);
    }

    //public void Draw() {
    override fun onDrawEye(eye: Eye) {

        if (!resourcesloaded) {
            model = mResourceManager.loadModel(R.raw.cementery)
            model2 = mResourceManager.loadModel(R.raw.monkey)

            skysphere = mResourceManager.loadModel(R.raw.skysphere)

            mainScene = Scene(0)

            node = SceneNode("Monkey", model)
            node.setPosition(0.0f, 0.0f, 5.0f)
            node.setRotation(0.0f, 0.0f, 0.0f)
            node.setScale(1.0f, 1.0f, 1.0f)

            mainScene.root.addNode(node)

            node = SceneNode("Teapot", model2)
            node.setPosition(100.0f, -20.0f, 40.0f)
            node.setRotation(0.0f, 0.0f, 0.0f)
            node.setScale(2.0f, 2.0f, 2.0f)

            //mainScene.root.addNode(node);

            //var nd:SceneNode
            for (i in 0..100) {
                var nd = SceneNode("model"+i, model2)
                nd.setPosition(Math.random().toFloat() * 100.0f - 50.0f, Math.random().toFloat() * 50.0f, Math.random().toFloat() * 100.0f - 50.0f)
                nd.setRotation(0.0f, 0.0f, 0.0f)
                nd.setScale(2.0f, 2.0f, 2.0f)
                mainScene.root.addNode(nd)
            }



            shader = ShaderProgram(R.raw.shader_vs, R.raw.shader_ps)
            shader.LoadContent(mResourceManager)

            shaderid = shader.programID
            uvertex = GLES20.glGetAttribLocation(shaderid, "aVertexPosition")
            ucolor = GLES20.glGetAttribLocation(shaderid, "aVertexColor")
            utextcoord = GLES20.glGetAttribLocation(shaderid, "aVertexTextureCoord")
            unormal = GLES20.glGetAttribLocation(shaderid, "aVertexNormal")
            mMVPMatrixUniformLocation = GLES20.glGetUniformLocation(shaderid, "uMVPMatrix")


            texture0 = GLES20.glGetUniformLocation(shaderid, "texture0")

            Renderer.BindAttribute(Renderer.ATTRIBUTE_VERTEX, uvertex)
            Renderer.BindAttribute(Renderer.ATTRIBUTE_COLOR, ucolor)
            Renderer.BindAttribute(Renderer.ATTRIBUTE_TEXCOORDS, utextcoord)
            Renderer.BindAttribute(Renderer.ATTRIBUTE_NORMAL, unormal)

            Renderer.setTextureHandle(Renderer.TEXTURE0, texture0)

            shader.Use()

            GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0)

            val vw = Viewport(0, 0, width, height)
            //camera = new Camera(vw, new Vector3(0,0, 3), new Vector3(0, 1, 0), Vector3.UP);
            camera = Camera(vw, Vector3(0f, 0f, 10f), Vector3(0f, 0f, 0f), Vector3(0f, 1f, 0f))
            vw.enable()
            camera.setPerspective(width, height)
            Renderer.modelview.loadIdentity()

            //GLES20.glDisable(GLES20.GL_DEPTH_TEST);
            //GLES20.glDisable(GLES20.GL_CULL_FACE);
            GLES20.glEnable(GLES20.GL_BLEND)
            GLES20.glBlendFunc(GLES20.GL_ALPHA, GLES20.GL_ONE_MINUS_DST_ALPHA)
            //GLES20.glDepthMask(false);
            val ratio = width.toFloat() / height.toFloat()
            //Matrix.setIdentityM(projection_matrix.matrix, 0);
            Matrix.frustumM(projection_matrix.matrix, 0, -ratio, ratio, -1f, 1f, 1f, 200f)
            //shader.enable();
            resourcesloaded = true
        }

        shader.Use()

        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0)

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

        GLES20.glClearColor(color.R, color.G, color.B, color.A)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)
        //GLES20.glDisable(GLES20.GL_CULL_FACE);


        //camera.setLookAt( new Vector3(eye.getEyeView()[0],eye.getEyeView()[1], eye.getEyeView()[2]));
        val x = Math.sin(angle.toDouble()).toFloat() * 10
        val z = Math.cos(angle.toDouble()).toFloat() * 10
        angle += 0.5f
        if (angle > 360.0f)
            angle -= 360.0f

        val tmpmatrix = GLMatrix()
        val tmpmatrix2 = GLMatrix()

        Matrix.setLookAtM(tmpmatrix.matrix, 0, -eye.eyeView[0] + headposition.X, eye.eyeView[1] + headposition.Y, eye.eyeView[2], 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f)
        Matrix.multiplyMM(modelview_matrix.matrix, 0, headViewMatrix.matrix, 0, tmpmatrix.matrix, 0)

        //Render skysphere
        val skyTransform = Transform()
        skyTransform.translation.setValue(0.0f, 0.0f, 0.0f)
        //skyTransform.scale.setValue( 200.0f);
        skyTransform.generateMatrix()

        Matrix.multiplyMM(tmpmatrix.matrix, 0, modelview_matrix.matrix, 0, skyTransform.transformMatrix.matrix, 0)
        Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmpmatrix.matrix, 0)

        GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0)

        val skym = skysphere.mMesh[0]
        Renderer.BindTexture(Renderer.TEXTURE0, skym.mTexture.id)
        skym.mVertexBuffer.Draw(skym.mIndexBuffer)

        for (n in mainScene.root.children) {

            val transform = n.transform
            //transform.rotation.rotate(new Vector3(0, 1, 0), (float) Math.toRadians(angle));
            //transform.translation.setValue(0, -2, -20);
            //transform.rotation.rotate(new Vector3(0,1,0), (float) Math.toRadians(90));
            transform.generateMatrix()
            Matrix.multiplyMM(tmpmatrix.matrix, 0, modelview_matrix.matrix, 0, transform.transformMatrix.matrix, 0)
            Matrix.multiplyMM(modelview_projection_matrix.matrix, 0, projection_matrix.matrix, 0, tmpmatrix.matrix, 0)

            GLES20.glUniformMatrix4fv(mMVPMatrixUniformLocation, 1, false, modelview_projection_matrix.matrix, 0)

            val mdl = n.model
            for (i in 0..mdl!!.mMesh.size - 1) {

                val m = mdl.mMesh[i]
                Renderer.BindTexture(Renderer.TEXTURE0, m.mTexture.id )
                m.mVertexBuffer.Draw(m.mIndexBuffer)
            }
        }


    }

    override fun onCardboardTrigger() {
        //color.setValue(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1.0f);
        Log.d("TestVR", "OnCardboardTriegger()")
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        Log.d("TestVR", "onGenericMotionEvent()")
        if (joystick != null)
            return joystick!!.onGenericMotionEvent(event)
        else
            return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.d("TestVR", "onTouchEvent()")
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        Log.d("TestVR", "onKeyDown()")
        if (joystick != null)
            return joystick!!.onKeyDown(keyCode, event)
        else
            return false
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {

        Log.d("TestVR", "onKeyUp()")
        if (joystick != null)
            return joystick!!.onKeyUp(keyCode, event)
        else
            return false
    }


}
