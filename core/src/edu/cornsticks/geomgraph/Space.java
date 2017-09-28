package edu.cornsticks.geomgraph;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import edu.cornsticks.geomgraph.Figures.Figure;
import edu.cornsticks.geomgraph.Figures.Line;
import edu.cornsticks.geomgraph.Figures.Plane;
import edu.cornsticks.geomgraph.Figures.Point;

class Space extends InputAdapter implements ApplicationListener {

	private ModelBatch modelBatch;
	private Environment environment;
	private Stage stage;
	private Label label;

	private CameraController cameraController;
	private Model XYZ;
	private ArrayList<Figure> scene;

	private Random r = new Random();
	private String inputLines;

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private Messenger dealer;
	private boolean needRedraw;

	Space(Messenger dealer) {
		this.dealer = dealer;
	}

	@Override
	public void create() {
		cameraController = new CameraController(this);
		Gdx.input.setInputProcessor(new GestureDetector(cameraController));

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 0f));
		//environment.add(new PointLight().set(0.3f, 0.3f, 0.3f, 100f, 100f, 100f, 100f));

		BitmapFont font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);

		scene = new ArrayList<Figure>();

		XYZ = new ModelBuilder().createXYZCoordinates(200, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		requestDrawing("plane a b c\na intersect b d\nc ortho d");
	}

	private Color newColor(){
		return new Color(r.nextFloat(),r.nextFloat(),r.nextFloat(),1.f);
	}

	private boolean reDraw(){
		try {
			drawThis(inputLines);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			dealer.sendMessage(e.getMessage());
			dealer.log(e.getMessage());
		}
		catch (ArithmeticException e) {
			e.printStackTrace();
			dealer.sendMessage("System solver error!");
			dealer.log("shit System solver error!");
		}
		return false;
	}

	void requestDrawing(String str){
		this.inputLines = str;
		needRedraw = true;
	}

	void requestReDrawing(){
		needRedraw = true;
	}

	private Figure findFigure(String str){
		Figure fig = null;
		for (Figure f : scene) {
			if (f.getName().equals(str))
				fig = f;
		}
		return fig;
	}

	@Override
	public void render () {
		if (needRedraw){
			if (!reDraw())
				scene.clear();
			needRedraw = false;
		}

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		label.setText(" FPS: " + Gdx.graphics.getFramesPerSecond());
		stage.draw();

		modelBatch.begin(cameraController.getCamera());
		modelBatch.render(new ModelInstance(XYZ), environment);
		for(Figure f : scene)
			f.draw(modelBatch,environment);
		modelBatch.end();
	}

	private void drawThis(String str) throws IOException {
		scene.clear();
		String [] commands = str.split("\n");

		// Parse commands, separate by initials, actions and derivative
		ArrayList<String> initCmd = new ArrayList<String>();
		ArrayList<String> actCmd = new ArrayList<String>();
		ArrayList<String> derivCmd = new ArrayList<String>();
		for(String row : commands)
		{
			String[] words = row.split(" ");
			if (words[0].equals("plane") || words[0].equals("point") || words[0].equals("line"))
				initCmd.add(row);
			else if (words[1].equals("ortho") || words[1].equals("parallel") ||
					words[1].equals("angle") || words[1].equals("belong") ||
					words[1].equals("have")){
				actCmd.add(row);
			}
			else if (words[1].equals("intersect"))
				derivCmd.add(row);
			else
				throw new IOException("Error in row: " + row);
		}

		/*Plane test = new Plane(newColor());
		test.name = "test";
		test.A = 0.01f;
		test.B = 0.01f;
		test.C = 1;
		test.D = 0.01f;
		test.Solve();
		scene.add(test);*/

        /*Line test = new Line(dealer);
		test.name = "test";
		test.point = new Vector3(0.001f,0.001f,0.001f);
        test.direction = (new Vector3(0.001f,0.001f,1).nor());
		test.Solve();
		scene.add(test);*/

		for (String command : initCmd){
			String[] words = command.split(" ");

			for (int i = 1; i < words.length; i++) {
				Figure fig;
				if (words[0].equals("plane"))
					fig = new Plane(newColor(), dealer);
				else if (words[0].equals("line"))
					fig = new Line(dealer);
				else if (words[0].equals("point"))
					fig = new Point();
				else
					throw new IOException("Object not found: " + words[0]);
				fig.setName(words[i]);
				scene.add(fig);
			}
		}

		// Solve intersections module
		for (String command : derivCmd){
			String[] words = command.split(" ");

			Figure fig1 = findFigure(words[0]);
			Figure fig2 = findFigure(words[2]);

			if (fig1 == null)
				throw new IOException("Object does not exist: " + words[0]);
			if (fig2 == null)
				throw new IOException("Object does not exist: " + words[2]);

			if (words[1].equals("intersect")){
				Figure derivative;
				if(fig1 instanceof Plane && fig2 instanceof Plane)				{
					derivative = new Line(dealer);
					derivative.pushAction(fig1, "belonging");
					derivative.pushAction(fig2, "belonging");
					fig1.pushAction(derivative, "belonging");
					fig2.pushAction(derivative, "belonging");
				}
				else if(fig1 instanceof Plane && fig2 instanceof Line ||
						fig1 instanceof Line && fig2 instanceof Plane){
					derivative = new Point();
					derivative.pushAction(fig1, "belonging");
					derivative.pushAction(fig2, "belonging");
					fig1.pushAction(derivative, "belonging");
					fig2.pushAction(derivative, "belonging");
				}
				else if(fig1 instanceof Line && fig2 instanceof Line){
					derivative = new Point();
					derivative.pushAction(fig1, "belonging");
					derivative.pushAction(fig2, "belonging");
					fig1.pushAction(derivative, "belonging");
					fig2.pushAction(derivative, "belonging");
				}
				else
					throw new IOException("Intersection objects are wrong!");
				derivative.setName(words[3]);
				scene.add(derivative);
			}
		}

		// Commands distributing
		for (String command : actCmd){
			String[] words = command.split(" ");

			Figure fig1 = findFigure(words[0]);
			Figure fig2 = findFigure(words[2]);

			if (fig1 == null)
				throw new IOException("Object does not exist: " + words[0]);
			if (fig2 == null)
				throw new IOException("Object does not exist: " + words[2]);

			// Belong/have operation must be commutative, so convert it to "belonging"
			if (words[1].equals("belong") || words[1].equals("have"))
				words[1] = "belonging";
			if (words.length == 3) {
				fig1.pushAction(fig2, words[1]);
				fig2.pushAction(fig1, words[1]);
			}
			else if (words.length == 4){
				fig1.pushAction(fig2, words[1], words[3]);
				fig2.pushAction(fig1, words[1], words[3]);
			}
		}

		Collections.sort(scene, new Comparator<Figure>() {
			public int compare(Figure o1, Figure o2) {
				return -(o1.actions.size() - o2.actions.size());
			}});

		for (Figure fig : scene){
			fig.solve();
		}

		Vector3 sceneCenter = CalcSceneCenter();
		for(Figure figure : scene)
			figure.translateCenter(sceneCenter.cpy().scl(-1));
	}

	private Vector3 CalcSceneCenter()
	{
		Vector3 sceneCenter = new Vector3(0,0,0);
		boolean ifPlanes = false, ifLines = false, ifPoints = false;
		int addsCount = 0;

		for(Figure figure : scene)
		{
			if (figure instanceof Plane)
				if(!ifPlanes)
				{
					addsCount = 0;
					sceneCenter.setZero();
					ifPlanes = true;
				}
			else if(figure instanceof Line && !ifPlanes)
				if(!ifLines)
				{
					addsCount = 0;
					sceneCenter.setZero();
					ifLines = true;
				}
			else if (ifLines || ifPlanes)
				continue;

			sceneCenter.add(figure.getCenter());
			addsCount++;
		}
		sceneCenter.scl((float)1/(float)addsCount);
		return sceneCenter;
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
		reset();
		stage.dispose();
		XYZ.dispose();
		modelBatch.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	void reset(){
		for(Figure f : scene)
			f.dispose();
		scene.clear();
	}

}
