package com.botijasoftware.utils;


import java.util.ArrayList;


public class Node implements Renderable{

    private ArrayList<Node> children = new ArrayList<Node>();
    private Node parent = null;
    private Vector3 position;
    private Vector3 scale;
    private Quaternion rotation;
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
        scale.setValue(x, y, z);
    }

    public void scale(float x, float y, float z) {
        scale.X *= x;
        scale.Y *= y;
        scale.Z *= z;
    }

    public void setPosition(float x, float y, float z) {
        position.setValue(x, y, z);
    }
    public void move(float x, float y, float z) {

        position.X += x;
        position.Y += y;
        position.Z += z;

    }

    public void setRotation(float x, float y, float z) {
        rotation.identity();
        rotation.rotate( new Vector3(x ,y , z));
    }

    public void setRotation(float x, float y, float z, float w) {
        rotation.set(x, y, z ,w);
    }

    public void rotateX(float angle) {
        rotation.rotateX((float) Math.toRadians( angle ));
    }

    public void rotateY(float angle) {
        rotation.rotateY((float) Math.toRadians( angle ));
    }

    public void rotateZ(float angle) {
        rotation.rotateZ((float) Math.toRadians( angle ));
    }
}