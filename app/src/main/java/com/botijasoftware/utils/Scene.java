package com.botijasoftware.utils;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.botijasoftware.utils.materials.MaterialManager;
import com.botijasoftware.utils.renderer.Renderer;



public class Scene {


	public Scene(int sceneid) {
	
		mSceneID = sceneid;

	}
	
	public int mSceneID;
    public SceneNode root;
	public Camera mCamera;
	public MaterialManager mMaterialManager;
	private float[] matrix = new float[16];

	
	public void LoadContent(ResourceManager resources) {
		
        try {

        	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            InputStream is = resources.getContext().getResources().openRawResource(mSceneID);
            Document doc = db.parse( is );
            doc.getDocumentElement().normalize();

            NodeList root = doc.getElementsByTagName("scene");
            
            for (int i = 0; i < root.getLength(); i++) {
                Node scene = root.item(i);

                Element fstElmnt = (Element) scene;
                NodeList sceneList = fstElmnt.getElementsByTagName("nodes");
                
                for (int j = 0; j < sceneList.getLength(); j++) {
                    Element nodes = (Element) sceneList.item(j);
                    NodeList nodesList = nodes.getElementsByTagName("node");
                    
                    for (int k = 0; k < nodesList.getLength(); k++) {
                        Element node = (Element) nodesList.item(k);
                        
                        Element position = (Element) node.getElementsByTagName("position").item(0);
                        float x = Float.valueOf(position.getAttribute("x").trim());
                        float y = Float.valueOf(position.getAttribute("y").trim());
                        float z = Float.valueOf(position.getAttribute("z").trim());
                        
                        Element quaternion = (Element) node.getElementsByTagName("quaternion").item(0);
                        float qx = Float.valueOf(quaternion.getAttribute("x").trim());
                        float qy = Float.valueOf(quaternion.getAttribute("y").trim());
                        float qz = Float.valueOf(quaternion.getAttribute("z").trim());
                        float qw = Float.valueOf(quaternion.getAttribute("w").trim());

                        Element scale = (Element) node.getElementsByTagName("scale").item(0);
                        float sx = Float.valueOf(scale.getAttribute("x").trim());
                        float sy = Float.valueOf(scale.getAttribute("y").trim());
                        float sz = Float.valueOf(scale.getAttribute("z").trim());
                        
                        Element entity = (Element) node.getElementsByTagName("entity").item(0);
                        String name = entity.getAttribute("name").trim();
                        String file = entity.getAttribute("meshFile").trim();
                     
                        Model m = new Model(file);
                        m.LoadContent(resources);
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

            
        }
        catch (Exception e) {
            //System.out.println("XML Pasing Excpetion = " + e);
        	//mModels.clear();
        	//mTransform.clear();
        }

		
	}

    SceneNode findNode(String name) {

        return root.findNode(name);
    }
	
	
	public void Draw() {
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


