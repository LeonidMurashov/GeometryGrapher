package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Line extends Figure {

    private Vector3 direction;
    private Vector3 point;

    public Line(){
        super();
        direction = new Vector3(0,0,0);
        point = new Vector3(1,1,1);
    }

    public Line(Vector3 point, Vector3 direction) {
        this.direction = new Vector3(direction);
        this.direction.nor();
        this.point = new Vector3(point);

        createInstance();

        setName("Line point: " + point + " direction: " + direction);
    }

    public void setParams(Vector3 newPoint, Vector3 newDir){
        dispose();
        point.set(newPoint.x, newPoint.y, newPoint.z);
        direction.set(newDir.x, newDir.y, newDir.z);
    }

    @Override
    public void dispose() {
        super.dispose();
        direction.set(0f,0f,0f);
        point.set(0f,0f,0f);
    }

    @Override
    public void createInstance() {
        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createArrow(point, point.cpy().add(direction),
                new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
    }
}
