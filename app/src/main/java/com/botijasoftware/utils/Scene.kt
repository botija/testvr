package com.botijasoftware.utils

import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Element
import com.botijasoftware.utils.materials.MaterialManager


class Scene(var mSceneID: Int) {

    var root: SceneNode
    lateinit var mCamera: Camera
    lateinit var sky: SkySphere
    lateinit var mMaterialManager: MaterialManager
    private val matrix = FloatArray(16)


    init {
        root = SceneNode("root", null)

    }

    fun LoadContent(resources: ResourceManager) {

        try {

            val dbf = DocumentBuilderFactory.newInstance()
            val db = dbf.newDocumentBuilder()

            val `is` = resources.context.resources.openRawResource(mSceneID)
            val doc = db.parse(`is`)
            doc.documentElement.normalize()

            val root = doc.getElementsByTagName("scene")

            for (i in 0..root.length - 1) {
                val scene = root.item(i)

                val fstElmnt = scene as Element
                val sceneList = fstElmnt.getElementsByTagName("nodes")

                for (j in 0..sceneList.length - 1) {
                    val nodes = sceneList.item(j) as Element
                    val nodesList = nodes.getElementsByTagName("node")

                    for (k in 0..nodesList.length - 1) {
                        val node = nodesList.item(k) as Element

                        val position = node.getElementsByTagName("position").item(0) as Element
                        val x = java.lang.Float.valueOf(position.getAttribute("x").trim { it <= ' ' })!!
                        val y = java.lang.Float.valueOf(position.getAttribute("y").trim { it <= ' ' })!!
                        val z = java.lang.Float.valueOf(position.getAttribute("z").trim { it <= ' ' })!!

                        val quaternion = node.getElementsByTagName("quaternion").item(0) as Element
                        val qx = java.lang.Float.valueOf(quaternion.getAttribute("x").trim { it <= ' ' })!!
                        val qy = java.lang.Float.valueOf(quaternion.getAttribute("y").trim { it <= ' ' })!!
                        val qz = java.lang.Float.valueOf(quaternion.getAttribute("z").trim { it <= ' ' })!!
                        val qw = java.lang.Float.valueOf(quaternion.getAttribute("w").trim { it <= ' ' })!!

                        val scale = node.getElementsByTagName("scale").item(0) as Element
                        val sx = java.lang.Float.valueOf(scale.getAttribute("x").trim { it <= ' ' })!!
                        val sy = java.lang.Float.valueOf(scale.getAttribute("y").trim { it <= ' ' })!!
                        val sz = java.lang.Float.valueOf(scale.getAttribute("z").trim { it <= ' ' })!!

                        val entity = node.getElementsByTagName("entity").item(0) as Element
                        val name = entity.getAttribute("name").trim { it <= ' ' }
                        val file = entity.getAttribute("meshFile").trim { it <= ' ' }

                        val m = Model(file)
                        m.LoadContent(resources)
                        /*mModels.add(m);
                        Transform t = new Transform();
                        t.mPosition = new Vector3(x, y, z);
                        t.mRotation = new Quaternion(qx, qy, qz, -qw);
                        //t.mRotation = t.mRotation.mul(new Quaternion(0,.707f,0,.707f)).invert();
                        t.mScale = new Vector3(sx, sy, sz);
                        mTransform.add(t);*/
                    }

                }
            }


        } catch (e: Exception) {
            //System.out.println("XML Pasing Excpetion = " + e);
            //mModels.clear();
            //mTransform.clear();
        }


    }

    fun findNode(name: String): SceneNode? {

        return root.findNode(name)
    }


    fun Draw() {
        /*for (int i = 0; i < mModels.size(); i++) {
			Transform t = mTransform.get(i);
            Renderer.pushModelViewMatrix();//  GLES20.glPushMatrix();

			//GLES20.glRotatef(180.0f, 0.0f, 1f,0f);

			Renderer.modelview.rotate(90.0f, 1.0f, 0f,0f);  //GLES20.glRotatef(90.0f, 1.0f, 0f,0f);

            Renderer.modelview.translate(t.mPosition.X, t.mPosition.Y, t.mPosition.Z); // GLES20.glTranslatef(t.mPosition.X, t.mPosition.Y, t.mPosition.Z);
			//GLES20.glRotatef(90.0f, 1.0f, 0f,0f);
			t.mRotation.getMatrix(matrix);

			//
			// Renderer.modelview.multMatrix(matrix, Renderer.modelview);//  GLES20.glMultMatrixf(matrix, 0);
            Renderer.modelview.scale(t.mScale.X, t.mScale.Y, t.mScale.Z);//GLES20.glScalef(t.mScale.X, t.mScale.Y, t.mScale.Z);


			mModels.get(i).Draw();
			Renderer.popModelViewMatrix();  //GLES20.glPopMatrix();
		}*/
    }
}


