package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Figure {

    Model model;
    public ModelInstance instance;
    Vector3 center;
    public String name;
    public ArrayList<Action> actions = new ArrayList<Action>();
    protected ArrayList<ArrayList<Float>> __equations = new ArrayList<ArrayList<Float>>();
    public boolean initialized = false;

    protected abstract void initDrawable();
    public abstract void draw(ModelBatch modelBatch, Environment environment);
    public Vector3 getCenter() { return center; }
    public abstract void translateCenter(Vector3 center);
    public void setName(String name){this.name = name; }
    public String getName(){ return this.name; }
    public abstract void dispose();

    public abstract ArrayList<Float> getParams() throws IOException;
    public void pushAction(Figure fig, String act, String param){actions.add(new Action(fig, act, param));}
    public void pushAction(Figure fig, String act){actions.add(new Action(fig, act, null));}
    public abstract void solve() throws IOException;
}
