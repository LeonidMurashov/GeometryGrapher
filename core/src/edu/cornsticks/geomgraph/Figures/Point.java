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

    public Point(Vector3 position){
        pos = new Vector3(position);

        ModelBuilder modelBuilder = new ModelBuilder();

        Color color = (pos.isZero() ? Color.RED : Color.BLACK);

        model = modelBuilder.createSphere(3,3,3,16,16, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        instance = new ModelInstance(model);
        instance.transform.setTranslation(pos);

        setName("Point " + pos);
    }

    @Override
    public void dispose() {
        super.dispose();
        pos.set(0f,0f,0f);
    }

    /*public Point()
    {
    }

	private ArrayList<ArrayList<Float>> equations = new ArrayList<ArrayList<Float>>();

    @Override
    protected void InitDrawable() {

        this.center = pos;
        ModelBuilder modelBuilder = new ModelBuilder();

        Color color = Color.BLACK;
        if(pos.len() == 0)//(new Vector3(0,0,0)))
            color = Color.RED;

        model = modelBuilder.createSphere(3,3,3,16,16, new Material(ColorAttribute.createDiffuse(color)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);
        instance.transform.setTranslation(pos);
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
        return new ArrayList<Float>(Arrays.asList(new Float[]{ center.x, center.y, center.z }));
    }

    private void belongTo(Figure fig) throws IOException {
        if (!(fig instanceof Plane || fig instanceof Line))
            throw new IOException(fig.GetName() + " is not a plane or a line");

        ArrayList<Float> pParams = fig.getParams();
        if (fig instanceof Plane) {
            ArrayList<Float> equation = new ArrayList<Float>(Arrays.asList(new Float[]{
                    pParams.get(0), pParams.get(1), pParams.get(2), -pParams.get(3)
            }));
            equations.add(equation);
        }
    }

    private List<Float> getRandomSolution()
    {
        int var_num = 3;
        Random r = new Random();

        // ANTI BUG SOLUTION
        ArrayList<ArrayList<Float>> equations2 = new ArrayList<ArrayList<Float>>();
        for (ArrayList<Float> ar : equations)
        {
            ArrayList<Float> new_ar = new ArrayList<Float>();
            for (Float f : ar)
                new_ar.add(f);
            equations2.add(new_ar);
        }

        if (equations2.size() < var_num)
            while(equations2.size() != var_num){//
                ArrayList<Float> eq = new ArrayList<Float>();
                eq.add(r.nextFloat()*20.f - 10.f);//
                eq.add(r.nextFloat()*20.f - 10.f);
                eq.add(r.nextFloat()*20.f - 10.f);
                eq.add(r.nextFloat()*200.f - 100.f);
                equations2.add(eq);
            }
        else if (equations2.size() > var_num) {
            while (equations2.size() != var_num) {
                equations2.remove(equations2.size());
            }
        }

        return EquationSolver.solveSystem(equations2);
    }

    @Override
    public void Solve() {

        for (Action act : actions){
            try {
                if (act.action.equals("intersect"))
                    belongTo(act.figure);
                else
                    throw new IOException("No such operation: " + act.action);
            }
            catch (IOException ignored){}
        }

        List<Float> vec = getRandomSolution();
        pos = new Vector3(vec.get(0), vec.get(1), vec.get(2));

        initialized = true;
        InitDrawable();
    }*/
}
