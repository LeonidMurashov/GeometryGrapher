package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public abstract class Figure {

    //Реально нужные вещи.

    Model model;

    ModelInstance instance;

    public String name;

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void draw(ModelBatch batch, Environment environment){
        batch.render(instance, environment);
    }

    public abstract void dispose();

    //Вещи, важность которых находится под сомнением.



    /*

    Vector3 center;

    public ArrayList<Action> actions = new ArrayList<Action>();

    ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();

    boolean initialized = false;


    public Vector3 GetCenter() { return center; }

    public abstract void TranslateCenter(Vector3 center);

    public abstract void Solve();

    protected abstract void InitDrawable();



    public abstract ArrayList<Float> getParams() throws IOException;

    public void PushAction(Figure fig, String act, String param){
        actions.add(new Action(fig, act, param));
    }

    public void PushAction(Figure fig, String act){
        actions.add(new Action(fig, act, null));
    }*/
}
