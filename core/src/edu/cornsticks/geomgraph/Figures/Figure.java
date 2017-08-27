package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public abstract class Figure {

    Model model;
    ModelInstance instance;
    public String name;

    Figure(){
        model = null;
        instance = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void draw(ModelBatch modelBatch, Environment environment){
        modelBatch.render(instance, environment);
    }

    public void dispose(){
        if(model!=null)
            model.dispose();
    }

    public abstract void createInstance();
}
