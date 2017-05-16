package com.botijasoftware.utils;


import java.util.ArrayList;


public class SceneNode implements Renderable{

    private ArrayList<SceneNode> children = new ArrayList<SceneNode>();
    private SceneNode parent = null;
    private Model model;
    private String name;
    private Transform transform;

    @Override
    public void Update(float time) {

    }

    @Override
    public void LoadContent(ResourceManager resources) {

    }

    @Override
    public void Draw() {

    }

    @Override
    public void freeContent(ResourceManager resources) {
        for (int i =0; i< children.size(); i++) {
            children.get(i).freeContent(resources);
        }
    }

    @Override
    public void scale(float x, float y) {

    }

    @Override
    public void move(float x, float y) {

    }

    public void setScale(float x, float y, float z) {
        transform.scale.setValue(x, y, z);
    }

    public void scale(float x, float y, float z) {
        transform.scale.X *= x;
        transform.scale.Y *= y;
        transform.scale.Z *= z;
    }

    public void setPosition(float x, float y, float z) {
        transform.translation.setValue(x, y, z);
    }
    public void move(float x, float y, float z) {

        transform.translation.X += x;
        transform.translation.Y += y;
        transform.translation.Z += z;

    }

    public void setRotation(float x, float y, float z) {
        transform.rotation.identity();
        transform. rotation.rotate( new Vector3(x ,y , z));
    }

    public void setRotation(float x, float y, float z, float w) {
        transform.rotation.set(x, y, z ,w);
    }

    public void rotateX(float angle) {
        transform.rotation.rotateX((float) Math.toRadians( angle ));
    }

    public void rotateY(float angle) {
        transform.rotation.rotateY((float) Math.toRadians( angle ));
    }

    public void rotateZ(float angle) {
        transform.rotation.rotateZ((float) Math.toRadians( angle ));
    }

    public void addNode(SceneNode node) {
        node.parent = this;
        children.add(node);
    }

    public SceneNode findNode(String searchname) {
        if (name.equals(searchname)) {
            return this;
        }
        for (int i=0; i< children.size(); i++) {
            SceneNode n = children.get(i);
            if (n.findNode(searchname) != null) {
                return n;
            }
        }

        return null;
    }
}