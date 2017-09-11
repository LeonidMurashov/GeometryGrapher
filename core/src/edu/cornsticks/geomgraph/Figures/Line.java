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

import edu.cornsticks.geomgraph.Messenger;
import edu.cornsticks.geomgraph.Gauss.EquationSolver;

public class Line extends Figure {
    public Vector3 direction;
    public Vector3 point;
    private Messenger dealer;

    protected ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();

    public Line(Messenger dealer) {
        this.dealer = dealer;
    }

    @Override
    protected void initDrawable() {
        Vector3 vec1 = point.cpy().add(direction.cpy().nor().scl(-250));
        Vector3 vec2 = point.cpy().add(direction.cpy().nor().scl(250));

        center = vec1.cpy().add(vec2).scl(0.5f);
        Vector3 resultVector = vec1.cpy().sub(vec2);

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createArrow(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z,0.001f,10f*(100.f/resultVector.len()),16,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

    }

    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {

        modelBatch.render(instance, environment);
    }

    @Override
    public void translateCenter(Vector3 translation) {
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

    private void belongTo(Figure fig) throws IOException {

        ArrayList<Float> pParams = fig.getParams();
        ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
            0f, 0f, 0f,
            pParams.get(0), pParams.get(1), pParams.get(2), 0f
        }));
        equations.add(equation);

        equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                pParams.get(0), pParams.get(1), pParams.get(2),
                0f, 0f, 0f, -pParams.get(3)
        }));
        equations.add(equation);
    }

    private void angleTo(Figure fig, float angle) throws IOException {
        if (fig instanceof Line){
            ArrayList<Float> lParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    0.00001f,0.00001f,0.00001f,
                    lParams.get(3), lParams.get(4), lParams.get(5), (float)Math.cos(angle)
            }));
            equations.add(equation);
        }
        else if (fig instanceof Plane){
            ArrayList<Float> pParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    0.00001f,0.00001f,0.00001f,
                    pParams.get(0), pParams.get(1), pParams.get(2), (float)Math.cos((float)(Math.PI/2.) - angle)
            }));
            equations.add(equation);
        }
        else
            throw new IOException(fig.getName() + "is not plane or line");
    }

    private void intersect(Figure fig){
    }

    private void have(Figure fig) throws IOException {
        float r = new Random().nextFloat()*200 - 100.f;
        ArrayList<Float> pParams = fig.getParams();
        ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                1f, 0.00001f, 0.00001f,
                r, 0.00001f, 0.00001f, pParams.get(0)
        }));
        equations.add(equation);
        equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                0.00001f, 1f, 0.00001f,
                0.00001f, r, 0.00001f, pParams.get(1)
        }));
        equations.add(equation);
        equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                0.00001f, 0.00001f, 1f,
                0.00001f, 0.00001f, r, pParams.get(2)
        }));
        equations.add(equation);
    }

    private void belongingWith(Figure fig) throws IOException {
        if (fig instanceof Point)
            have(fig);
        else if (fig instanceof Plane)
            belongTo(fig);
        else
            throw new IOException(fig.getName() + " is not a plane or a point");
    }

    private List<Float> getRandomSolution(int depth) throws IOException {
        if (depth == 8000)
            throw new IOException("Can't solve this");

        // ANTI BUG SOLUTION
        ArrayList<ArrayList<Float>> equations2 = new ArrayList<ArrayList<Float>>();
        for (ArrayList<Float> ar : equations) {
            ArrayList<Float> new_ar = new ArrayList<Float>();
            for (Float f : ar)
                new_ar.add(f);
            equations2.add(new_ar);
        }

        int coefs_num = 6;//equations.get(0).size();
        Random r = new Random();
        if (equations2.size() < coefs_num)
        {
            while(equations2.size() != coefs_num){
                ArrayList<Float> eq = new ArrayList<Float>();
                eq.add(r.nextFloat()*2 - 1.f);
                eq.add(r.nextFloat()*2 - 1.f);
                eq.add(r.nextFloat()*2 - 1.f);
                eq.add(r.nextFloat()*200 - 100.f);
                eq.add(r.nextFloat()*200 - 100.f);
                eq.add(r.nextFloat()*200 - 100.f);
                eq.add(r.nextFloat()*100 - 50.f);
                equations2.add(eq);
            }
        }
        else if (equations2.size() > coefs_num)
            while(equations2.size() != coefs_num)
                equations2.remove(equations2.size()-1);

        List<Float> vec = EquationSolver.solveSystem(equations2);
        if (Math.abs(vec.get(3) * vec.get(3) + vec.get(4) * vec.get(4) + vec.get(5) * vec.get(5) - 1) > 0.005) {
            equations2.clear();
            vec.clear();
            System.gc();
            vec = getRandomSolution(depth + 1);
        }
        return vec;
    }

    @Override
    public void solve() throws IOException {
        for (Action act : actions){
            try {
                if (act.action.equals("parallel"))
                    angleTo(act.figure, 0.f);
                else if (act.action.equals("ortho"))
                    angleTo(act.figure, (float) (Math.PI / 2.f));
                else if (act.action.equals("angle"))
                    angleTo(act.figure, Float.parseFloat(act.param)/180.f*((float)Math.PI));
                else if (act.action.equals("belonging"))
                    belongingWith(act.figure);
                else
                    throw new IOException("No such operation: " + act.action);
            }
            catch (IOException ignored){}
        }

        List<Float> vec = getRandomSolution(0);

        if (!name.equals("test")) {
            point = new Vector3(vec.get(0), vec.get(1), vec.get(2));
            direction = new Vector3(vec.get(3), vec.get(4), vec.get(5)).nor();
        }

        initialized = true;
        initDrawable();
    }
}
