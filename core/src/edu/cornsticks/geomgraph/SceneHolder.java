package edu.cornsticks.geomgraph;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import edu.cornsticks.geomgraph.Figures.Figure;
import edu.cornsticks.geomgraph.Figures.Line;
import edu.cornsticks.geomgraph.Figures.Plane;
import edu.cornsticks.geomgraph.Figures.Point;




class SceneHolder {

    private ModelInstance instance;
    private ArrayList<Figure> scene = new ArrayList<Figure>();
    private String inputLines;
    private Random r = new Random();
    private boolean rendering = true;

    SceneHolder() {

        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createXYZCoordinates(200, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        try {
            DrawThis("plane alfa\nplane beta\nbeta parallel alfa\nalfa parallel beta\nplane gamma\ngamma ortho alfa\nalfa ortho gamma");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void render(ModelBatch modelBatch, Environment environment)
    {
        if(!rendering)
            return;
            //Gdx.app.log(Float.toString(sceneCenter.x), Float.toString(sceneCenter.y));

        //instance.transform.set(sceneCenter, new Quaternion());

        for(Figure f : scene)
            f.Draw(modelBatch,environment);
        modelBatch.render(instance, environment);
    }

    private Color NewColor(){
        return new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1.f);
    }

    void ReDraw(){
        try {
            DrawThis(inputLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DrawThis(String str) throws IOException {
        rendering = false;

        //str = "plane alfa\nplane beta\nbeta parallel alfa\nalfa parallel beta\nplane gamma\ngamma ortho alfa\nalfa ortho gamma";
        this.inputLines = str;
        scene.clear();

        String [] commands = str.split("\n");
        Figure fig;
        ArrayList<ArrayList<String>> sepCmd = new ArrayList<ArrayList<String>>();
        sepCmd.add(new ArrayList<String>());

        for(String row : commands)
        {
            int num = row.split(" ").length;
            if (num == 2)
            {
                sepCmd.get(0).add(row);
            }
            else if (num == 3){
                boolean exited = false;
                for(int i = 1; i < sepCmd.size(); i++)
                {
                    if (row.split(" ")[0].equals(sepCmd.get(i).get(0).split(" ")[0])){
                        sepCmd.get(i).add(row);
                        exited = true;
                    }
                }
                if(!exited)
                {
                    ArrayList<String> l = new ArrayList<String>();
                    l.add(row);
                    sepCmd.add(l);
                }
            }
        }

        for(int i = 0; i < sepCmd.size(); i++)
        {
            for(int j = 0; j < sepCmd.get(i).size(); j++)
            {
                try {
                    String command = sepCmd.get(i).get(j);
                    String[] words = command.split(" ");
                    if (words.length == 2) {
                        Vector3[] f = new Vector3[]{new Vector3(r.nextInt() % 100 - 80, r.nextInt() % 100 - 80, r.nextInt() % 100 - 80), new Vector3(r.nextInt() % 100 - 80, r.nextInt() % 100 - 80, r.nextInt() % 100 - 80), new Vector3(r.nextInt() % 100 - 80, r.nextInt() % 100 - 80, r.nextInt() % 100 - 80)};
                        String name = words[1];
                        if (words[0].equals("plane")) {
                            fig = new Plane(NewColor());
                        } else if (words[0].equals("line")) {
                            fig = new Line();
                        } else if (words[0].equals("point")) {
                            fig = new Point();
                        } else {
                            continue;
                        }
                        scene.add(fig);
                        fig.SetName(words[1]);
                    } else if (words.length == 3) {
                        Figure fig1 = null, fig2 = null;
                        String name1 = words[0], name2 = words[2];
                        for (Figure f : scene) {
                            if (f.GetName().equals(name1))
                                fig1 = f;
                            if (f.GetName().equals(name2))
                                fig2 = f;
                        }
                        if (fig2 == null || fig1 == null)
                            continue;
                        if (words[1].equals("ortho")) {
                            if (fig1 instanceof Line) {
                                ((Line) fig1).angleTo(fig2, (float) (Math.PI / 2.f));
                            } else if (fig1 instanceof Plane)
                                ((Plane) fig1).angleTo(fig2, (float) (Math.PI / 2.f));

                        } else if (words[1].equals("parallel")) {
                            if (fig1 instanceof Line)
                                ((Line) fig1).angleTo(fig2, (float) (Math.PI));
                            else if (fig1 instanceof Plane)
                                ((Plane) fig1).angleTo(fig2, (float) (Math.PI));
                        }

                    }
                }
                catch (IOException ignored) {
                }
            }
            if (i != 0) {
                Figure a = null;
                for (Figure f : scene) {
                    if (f.GetName().equals(sepCmd.get(i).get(0).split(" ")[0]))
                    {
                        a = f;
                    }
                }

                assert a != null;
                a.SolveSystem();
            }
        }
        rendering = true;
        /*if(plane != null) {
            plane.dispose();
            for (Point d : dots)
                d.dispose();
            for (Line d : lines)
                d.dispose();
        }
        Vector3[] f = new Vector3[]{new Vector3(r.nextInt()%100-80,r.nextInt()%100-80,r.nextInt()%100-80),new Vector3(r.nextInt()%100-80,r.nextInt()%100-80,r.nextInt()%100-80),new Vector3(r.nextInt()%100-80,r.nextInt()%100-80,r.nextInt()%100-80)};
        dots = new Point[]{new Point(f[0]),
                new Point(f[1]), new Point(f[2]), new Point(new Vector3(0,0,0))};
        lines = new Line[]{new Line(f[0],f[1]),new Line(f[1],f[2]),new Line(f[2],f[0])};
        plane = new Plane(f[0],f[1],f[2], new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1.f));
        plane2 = plane.GetParallelPlane(new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1.f));
        plane3 = plane2.GetPerpendicularPlane(new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1.f));

        scene = new Figure[]{dots[0], dots[1], dots[2], lines[0], lines[1], lines[2], plane, plane2, plane3};
        Vector3 sceneCenter = CalcSceneCenter();

        for(Figure figure : scene)
            figure.TranslateCenter(sceneCenter.cpy().scl(-1));*/
    }

    Vector3 CalcSceneCenter()
    {
        Vector3 sceneCenter = new Vector3(0,0,0);
        boolean ifPlanes = false, ifLines = false;
        int addsCount = 0;

        for(Figure figure : scene)
        {
            if (figure.getClass() == Plane.class)
                if(ifPlanes);
                else
                {
                    addsCount = 0;
                    sceneCenter.setZero();
                    ifPlanes = true;
                }
            else if(figure.getClass() == Line.class && !ifPlanes)
                if(ifLines);
                else
                {
                    addsCount = 0;
                    sceneCenter.setZero();
                    ifLines = true;
                }
            else continue;

            sceneCenter.add(figure.GetCenter());
            addsCount++;
        }
        sceneCenter.scl((float)1/(float)addsCount);
        return sceneCenter;
    }
}

//model = modelBuilder.createCone(20,120,20,3, new Material(ColorAttribute.createDiffuse(Color.GOLD)),
//VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

//model = modelBuilder.createBox(20,20,0.3f, new Material(ColorAttribute.createDiffuse(Color.GOLD)),
//      VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

//model = modelBuilder.createRect(-10*k, -10*k, 0, 10*k, -10*k, 0, 10*k, 10*k, 0, -10*k, 10*k, 0, 0.5f,0.5f,0.5f, new Material(ColorAttribute.createDiffuse(Color.GOLD)),
// VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

// model = modelBuilder.createCylinder(20,20,20,10,new Material(ColorAttribute.createDiffuse(Color.GOLD)),
//               VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, 90f, 90);

//model = modelBuilder.createLineGrid(20,20,5,5, new Material(ColorAttribute.createDiffuse(Color.GOLD)),
//                     VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

//model = modelBuilder.createCylinder(2,20,2,8, new Material(ColorAttribute.createDiffuse(Color.GOLD)),
//                        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);