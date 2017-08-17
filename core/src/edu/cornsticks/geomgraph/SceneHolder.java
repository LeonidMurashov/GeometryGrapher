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

import java.util.Random;

import edu.cornsticks.geomgraph.Figures.Figure;
import edu.cornsticks.geomgraph.Figures.Line;
import edu.cornsticks.geomgraph.Figures.Plane;
import edu.cornsticks.geomgraph.Figures.Point;


class SceneHolder {
    private ModelInstance instance;
    private Point[] dots;
    private Line[] lines;
    private Plane plane, plane2, plane3;
    private Figure[] scene;

    SceneHolder() {

        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createXYZCoordinates(200, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        instance = new ModelInstance(model);

        changePlane();
    }

    void render(ModelBatch modelBatch, Environment environment)
    {
        //Gdx.app.log(Float.toString(sceneCenter.x), Float.toString(sceneCenter.y));

        //instance.transform.set(sceneCenter, new Quaternion());
        for(Point d : dots)
            d.Draw(modelBatch,environment);
        for(Line d : lines)
            d.Draw(modelBatch,environment);

        plane.Draw(modelBatch,environment);
        plane2.Draw(modelBatch, environment);
        plane3.Draw(modelBatch, environment);
        modelBatch.render(instance, environment);
    }

    void changePlane()
    {
        if(plane != null) {
            plane.dispose();
            for (Point d : dots)
                d.dispose();
            for (Line d : lines)
                d.dispose();
        }
        Random r = new Random();
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
            figure.TranslateCenter(sceneCenter.cpy().scl(-1));
    }

    private Vector3 CalcSceneCenter()
    {
        Vector3 sceneCenter = new Vector3(0,0,0);
        int addsCount = 0;

        for(Figure figure : scene)
        {
            if (figure instanceof Plane) {
                    addsCount = 0;
                    sceneCenter.setZero();
                }
            else if(figure instanceof Line) {
                    addsCount = 0;
                    sceneCenter.setZero();
                    sceneCenter.setZero();
                }
            else
                continue;

            sceneCenter.add(figure.GetCenter());
            addsCount++;
        }
        sceneCenter.scl((float)1/(float)addsCount);
        return sceneCenter;
    }
}
