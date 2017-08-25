package edu.cornsticks.geomgraph.Figures;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

public class Point extends Figure {

    private Vector3 pos;

    public Point(float x, float y, float z){

        pos = new Vector3(x,y,z);

        ModelBuilder modelBuilder = new ModelBuilder();

        Color color = (pos.isZero()) ? Color.RED : Color.BLACK;

        model = modelBuilder.createSphere(3,3,3,16,16, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        instance = new ModelInstance(model);
        instance.transform.setTranslation(pos);
    }

    public Point(Vector3 position){
        this(position.x, position.y, position.z);
    }

    @Override
    public void dispose() {
        model.dispose();
        pos.set(0.0f, 0.0f, 0.0f);
    }

/*    @Override
    public ArrayList<Float> getParams() throws IOException {
        if (!initialized)
            throw new IOException("Not initialized");
        return new ArrayList<Float>(Arrays.asList(new Float[]{ center.x, center.y, center.z }));
    }*/

/*    @Override
    public void Solve() {
        int coefs_num = 4;//equations.get(0).size();
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
        pos = new Vector3(vec.get(0), vec.get(1), vec.get(2));

        initialized = true;
        InitDrawable();
    }*/
}
