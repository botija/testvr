package com.botijasoftware.utils;


import java.util.ArrayList;


public class Node implements Renderable{

    private ArrayList<Node> children = new ArrayList<Node>();
    private Node parent = null;
    private Renderable rendereable;

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