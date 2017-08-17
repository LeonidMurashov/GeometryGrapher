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
    ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();
    boolean initialized = false;

    public abstract void Draw(ModelBatch modelBatch, Environment environment);
    protected abstract void dispose();
    public Vector3 GetCenter() { return center; }
    public abstract void TranslateCenter(Vector3 center);
    public abstract void SolveSystem();
    protected abstract void InitDrawable();

    public void SetName(String name){this.name = name; }
    public String GetName(){ return this.name; }
    public abstract ArrayList<Float> getParams() throws IOException;
}
