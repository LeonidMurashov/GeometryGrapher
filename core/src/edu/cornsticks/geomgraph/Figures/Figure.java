package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;

public abstract class Figure {

    Model model;
    public ModelInstance instance;
    Vector3 center;
    public String name;
    public ArrayList<Action> actions = new ArrayList<Action>();
    ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();
    public boolean initialized = false;

    public abstract void Draw(ModelBatch modelBatch, Environment environment);
    protected abstract void dispose();
    public Vector3 GetCenter() { return center; }
    public abstract void TranslateCenter(Vector3 center);
    public abstract void Solve();
    protected abstract void InitDrawable();

    public void SetName(String name){this.name = name; }
    public String GetName(){ return this.name; }
    public abstract ArrayList<Float> getParams() throws IOException;
    public void PushAction(Figure fig, String act, String param){actions.add(new Action(fig, act, param));}
    public void PushAction(Figure fig, String act){actions.add(new Action(fig, act, null));}
}
