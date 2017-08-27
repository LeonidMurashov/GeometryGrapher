package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

@SuppressWarnings("unused")
public class Plane extends Figure{

    private float a;
    private float b;
    private float c;
    private float d;

    private com.badlogic.gdx.math.Plane plane;



    private Color color;

    public Plane() {
        super();
        a = b = d = 0;
        c = 1;
        plane = null;
        color = null;
    }

    public Plane(float a, float b, float c, float d, Color color) {
        float norm = (float) Math.sqrt(a*a+b*b+c*c);
        this.a = a/norm;
        this.b = b/norm;
        this.c = c/norm;
        this.d = d/norm;

        this.color = color;


        plane = new com.badlogic.gdx.math.Plane(new Vector3(a,b,c), d);

        setName("Plane: A: " + a + " B: " + b + " C: " + c + " D: " + d);
    }

    public Plane(Vector3 normal, Vector3 point, Color color){
        Vector3 normalizedNorm = new Vector3(normal.cpy().nor());
        a = normalizedNorm.x;
        b = normalizedNorm.y;
        c = normalizedNorm.z;
        d = -(a*point.x + b*point.y + c*point.z);

        this.color = color;

        plane = new com.badlogic.gdx.math.Plane(normalizedNorm, point);

        setName("Plane: A: " + a + " B: " + b + " C: " + c + " D: " + d);
    }

    public void setParams(int a, int b, int c, int d, Color color){
        dispose();
        float norm = (float) Math.sqrt(a*a+b*b+c*c);
        this.a = a/norm;
        this.b = b/norm;
        this.c = c/norm;
        this.d = d/norm;

        this.color = color;


        plane = new com.badlogic.gdx.math.Plane(new Vector3(a,b,c), d);
        createInstance();
    }

    public void setParams(Vector3 normal, Vector3 point, Color color){
        dispose();
        Vector3 normalizedNorm = new Vector3(normal.cpy().nor());
        a = normalizedNorm.x;
        b = normalizedNorm.y;
        c = normalizedNorm.z;
        d = -(a*point.x + b*point.y + c*point.z);

        this.color = color;

        plane = new com.badlogic.gdx.math.Plane(normalizedNorm, point);
    }

    @Override
    public void dispose() {
        super.dispose();
        a = b = c = d = 0;
    }

    @Override
    public void createInstance() {
        ModelBuilder builder = new ModelBuilder();
        model = builder.createBox(100,100,0.3f, GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        instance = new ModelInstance(model);

        Vector3 normal = plane.getNormal();
        instance.transform.rotate(Vector3.Z, normal);
    }
}
