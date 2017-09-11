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

import edu.cornsticks.geomgraph.Messenger;
import edu.cornsticks.geomgraph.Gauss.EquationSolver;

public class Plane extends Figure {

    private com.badlogic.gdx.math.Plane plane;
    public float A, B, C, D, width;
    private Color color;
    Messenger dealer;

    protected ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();

    public Plane(Color color, Messenger dealer)
    {
        this.color = color;
        this.dealer = dealer;
    }

    @Override
    protected void initDrawable() {

        plane = new com.badlogic.gdx.math.Plane(new Vector3(A,B,C), D);

        //vec1.cpy().add(vec2).add(vec3).scl(0.33333f).sub(vec1).len();

        ModelBuilder modelBuilder = new ModelBuilder();
        model = modelBuilder.createBox(width*2+20,width*2+20,0.3f,GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        //instance.transform.setToRotationRad();
        Vector3 normal = plane.getNormal();
        instance.transform.rotate(Vector3.Z, normal);
        // instance.transform.setToRotation(plane.getNormal(), )

        instance.transform.setTranslation(center);
    }

    @Override
    public void draw(ModelBatch modelBatch, Environment environment) {
        modelBatch.render(instance, environment);
    }

    @Override
    public void translateCenter(Vector3 translation) {
        instance.transform.set(instance.transform.getTranslation(new Vector3(0,0,0)).cpy().add(translation), instance.transform.getRotation(new Quaternion()));
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
        return new ArrayList<Float>(Arrays.asList(new Float[]{A, B, C, D}));
    }

    private void angleTo(Figure fig, float angle) throws IOException {
        Random r = new Random();
        if (fig instanceof Line){
            ArrayList<Float> lParams = fig.getParams();
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    lParams.get(3), lParams.get(4), lParams.get(5), 0.f,(float)Math.cos((float)(Math.PI/2.) - angle)
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
            throw new IOException(fig.getName() + "is not plane or line!");
    }

    private void have(Figure fig) throws IOException {
		if (fig instanceof Point) {
			ArrayList<Float> pParams = fig.getParams();
			ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
					pParams.get(0), pParams.get(1), pParams.get(2), 1f, 0f
			}));
			equations.add(equation);
		}
		else if (fig instanceof Line){
			ArrayList<Float> lParams = fig.getParams();
			ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
					lParams.get(3), lParams.get(4), lParams.get(5), 0f, 0f
			}));
			equations.add(equation);
			equation = new ArrayList<Float>(Arrays.asList(new Float[]{
					lParams.get(0), lParams.get(1), lParams.get(2), 1f, 0f
			}));
			equations.add(equation);
		}
		else
			throw new IOException(fig.getName() + " is not a point or a line");
	}

	private List<Float> getRandomSolution(int depth) throws IOException {
		if (depth == 8000)
			throw new IOException("Can't solve this");

		int var_num = 4;
		Random r = new Random();

		// ANTI BUG SOLUTION
		ArrayList<ArrayList<Float>> equations2 = new ArrayList<ArrayList<Float>>();
		for (ArrayList<Float> ar : equations) {
			ArrayList<Float> new_ar = new ArrayList<Float>();
			for (Float f : ar)
				new_ar.add(f);
			equations2.add(new_ar);
		}

		if (equations2.size() < var_num)
			while (equations2.size() != var_num) {//
				ArrayList<Float> eq = new ArrayList<Float>();
				eq.add(r.nextFloat() * 200.f - 100.f);//
				eq.add(r.nextFloat() * 200.f - 100.f);
				eq.add(r.nextFloat() * 200.f - 100.f);
				eq.add(r.nextFloat() * 2.f - 1.f);
				eq.add(r.nextFloat() * 200.f - 100.f);
				equations2.add(eq);
			}
		else if (equations2.size() > var_num) {
			while (equations2.size() != var_num) {
				equations2.remove(equations2.size()-1);
			}
		}

		List<Float> vec = EquationSolver.solveSystem(equations2);
		if (Math.abs(vec.get(0) * vec.get(0) + vec.get(1) * vec.get(1) + vec.get(2) * vec.get(2) - 1) > 0.005) {
			equations2.clear();
			vec.clear();
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
						have(act.figure);
					else
						throw new IOException("No such operation: " + act.action);
				}
				catch (IOException ignored){}
			}

			List<Float> vec = getRandomSolution(0);
			if (! name.equals("test")) {
				A = vec.get(0);
				B = vec.get(1);
				C = vec.get(2);
				D = vec.get(3);
			}
			if (C == 0.f)
				C = 0.001f;
			width = 100;

			do {
				Random r = new Random();
				float x = r.nextFloat() * 40 - 20,
						y = r.nextFloat() * 40 - 20;
				center = new Vector3(x, y, -(A * x + B * y + D) / C);
			}
			while (false);//;Math.abs(center.z) > 200);

			initialized = true;
			initDrawable();

    }
}
