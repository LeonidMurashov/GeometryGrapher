package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.cornsticks.geomgraph.Gauss.EquationSolver;

public class Plane extends Figure{

    private com.badlogic.gdx.math.Plane plane;
    private float A, B, C, D, width;
    private Color color;

    public Plane(Color color)
    {
        this.color = color;
    }

    @Override
    protected void InitDrawable() {

        plane = new com.badlogic.gdx.math.Plane(new Vector3(A,B,C), D);

        //vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(width*2+20,width*2+20,0.3f,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        //instance.transform.setToRotationRad();
        Vector3 normal = plane.getNormal();
        instance.transform.rotate(Vector3.Z, normal);
        this.A = normal.x;
        this.B = normal.y;
        this.C = normal.z;
        this.D = -1 * (A*center.x + B*center.y + C*center.z);

        // instance.transform.setToRotation(plane.getNormal(), )

        instance.transform.setTranslation(center);
    }

    @Override
    public void Draw(ModelBatch modelBatch, Environment environment) {

        modelBatch.render(instance, environment);
    }

    @Override
    public void TranslateCenter(Vector3 translation) {
        //plane.d += translation.z;
        instance.transform.set(instance.transform.getTranslation(new Vector3(0,0,0)).cpy().add(translation), instance.transform.getRotation(new Quaternion()));
       // instance.transform.translate(translation);
        //this.center.add(translation);
    }

    @Override
    public void dispose() {
        model.dispose();
        center.set(0,0,0);
    }

    @Override
    public ArrayList<Float> getParams() throws IOException {
        if (!initialized)
            throw new IOException("Not initialized");
        Vector3 normal = plane.getNormal();
        this.A = normal.x;
        this.B = normal.y;
        this.C = normal.z;
        D = -1 * (A*center.x + B*center.y + C*center.z);
        return new ArrayList<Float>(Arrays.asList(new Float[]{A, B, C, D}));
    }

    public void angleTo(Figure fig, float angle) throws IOException {
        Random r = new Random();
        if (fig instanceof Line){
            ArrayList<Float> lParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    lParams.get(3), lParams.get(4), lParams.get(5), 0.f,(float)(Math.PI/2.) - (float)Math.cos(angle)
            }));
            equations.add(equation);
        }
        else if (fig instanceof Plane){
            ArrayList<Float> pParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    pParams.get(0), pParams.get(1), pParams.get(2), 0.f, (float)Math.cos(angle)
            }));
            equations.add(equation);
        }
        else
            throw new IOException("is not plane or line");
    }

    @Override
    public void SolveSystem() {
        int coefs_num = 4+1;//equations.get(0).size();
        Random r = new Random();

        if (equations.size() < coefs_num-1)
        {
            while(equations.size() != coefs_num-1){
                ArrayList<Float> eq = new ArrayList<Float>();
                for (int i = 0; i < coefs_num; i++)
                    eq.add(r.nextFloat()*2 - 1.f);
                equations.add(eq);
            }
        }
        else if (equations.size() > coefs_num-1)
        {
            while(equations.size() != coefs_num-1)
                equations.remove(equations.size()-1);
        }

        List<Float> vec = EquationSolver.solveSystem(equations);
        A = vec.get(0);
        B = vec.get(1);
        C = vec.get(2);
        D = vec.get(3);
        width = 100;
        center = new Vector3(0,0,0);

        initialized = true;
        InitDrawable(); // TODO!!!
        //float A, float B, float C, float D, float width, Vector3 center,
    }

}
