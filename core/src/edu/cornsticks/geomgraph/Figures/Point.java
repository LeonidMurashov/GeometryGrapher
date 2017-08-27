package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("unused")
public class Point extends Figure {

    private Vector3 pos;

    public Point(){
        super();
        pos = new Vector3(0,0,0);
    }

    public Point(Vector3 position){
        pos = new Vector3(position);

        createInstance();

        setName("Point " + pos);
    }

    @Override
    public void dispose() {
        super.dispose();
        pos.set(0f,0f,0f);
    }

    public void setParams(Vector3 newPos){
        dispose();
        pos.set(newPos.x, newPos.y, newPos.z);
        createInstance();
    }

    @Override
    public void createInstance() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Color color = (pos.isZero() ? Color.RED : Color.BLACK);

        model = modelBuilder.createSphere(3,3,3,16,16, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        instance = new ModelInstance(model);
        instance.transform.setTranslation(pos);
    }
}
