package com.botijasoftware.utils;


import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class Node implements Renderable{

    private ArrayList<Node> children = new ArrayList<Node>();
    private Node parent = null;
    private Renderable rendereable;

    @Override
    public void Update(float time) {

    }

    @Override
    public void LoadContent(GL10 gl, ResourceManager resources) {

    }

    @Override
    public void Draw(GL10 gl) {

    }

    @Override
    public void freeContent(GL10 gl, ResourceManager resources) {
        for (int i =0; i< children.size(); i++) {
            children.get(i).freeContent(gl, resources);
        }
    }

    public void scale(float x, float y) {

        rendereable.scale(x,y);
        for (int i =0; i< children.size(); i++) {
            children.get(i).scale(x,y);
        }
    }

    public void move(float x, float y) {

        rendereable.move(x,y);
        for (int i =0; i< children.size(); i++) {
            children.get(i).move(x,y);
        }
    }
}