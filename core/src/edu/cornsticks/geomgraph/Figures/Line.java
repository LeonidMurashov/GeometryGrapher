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
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import edu.cornsticks.geomgraph.Gauss.EquationSolver;

public class Line extends Figure {
    private Vector3 direction;
    private Vector3 point;

    public Line() {
    }

    @Override
    protected void InitDrawable() {
        Vector3 vec1 = point;
        Vector3 vec2 = point.cpy().add(vec1).scl(1);

        center = vec1.cpy().add(vec2).scl(0.5f);
        Vector3 resultVector = vec1.cpy().sub(vec2);
        direction = resultVector.cpy().nor();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createArrow(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z,0.001f,10f*(100.f/resultVector.len()),16,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
    }

    @Override
    public void Draw(ModelBatch modelBatch, Environment environment) {

        modelBatch.render(instance, environment);
    }

    @Override
    public void TranslateCenter(Vector3 translation) {
        instance.transform.translate(translation);
        this.center.add(translation);
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
        return new ArrayList<Float>(Arrays.asList(new Float[]{center.x, center.y, center.z,
                                                              direction.x, direction.y, direction.z}));
    }

    public void belongTo(Figure fig) throws IOException {
        if (fig instanceof Plane)
            throw new IOException("is not plane");

        ArrayList<Float> pParams = fig.getParams();
        ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
            pParams.get(0), pParams.get(1), pParams.get(2),
            pParams.get(0), pParams.get(1), pParams.get(2), pParams.get(3)
        }));
        equations.add(equation);
    }

    public void angleTo(Figure fig, float angle) throws IOException {
        Random r = new Random();
        if (fig instanceof Line){
            ArrayList<Float> lParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    0.f,0.f,0.f,
                    lParams.get(3), lParams.get(4), lParams.get(5), (float)Math.cos(angle)
            }));
            equations.add(equation);
        }
        else if (fig instanceof Plane){
            ArrayList<Float> pParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    0.f,0.f,0.f, // TODO: CHECK IT!!!
                    pParams.get(0), pParams.get(1), pParams.get(2), (float)(Math.PI/2.) - (float)Math.cos(angle)
            }));
            equations.add(equation);
        }
        else
            throw new IOException("is not plane or line");
    }

    public void intersect(Figure fig){
    }

    public void have(Figure fig) throws IOException {
        if (!(fig instanceof Point))
            throw new IOException("is not a point");

    }

    @Override
    public void Solve() {
        int coefs_num = 6;//equations.get(0).size();
        Random r = new Random();

        if (equations.size() < coefs_num)
        {
            while(equations.size() != coefs_num){
                ArrayList<Float> eq = new ArrayList<Float>();
                for (int i = 0; i < coefs_num+1; i++)
                    eq.add(r.nextFloat()*2 - 1.f);
                equations.add(eq);
            }
        }
        else if (equations.size() > coefs_num)
        {
            while(equations.size() != coefs_num)
                equations.remove(equations.size()-1);
        }

        List<Float> vec = EquationSolver.solveSystem(equations);
        point = new Vector3(vec.get(0), vec.get(1), vec.get(2));
        direction = new Vector3(vec.get(3), vec.get(4), vec.get(5));

        initialized = true;
        InitDrawable();
    }
}
