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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

import edu.cornsticks.geomgraph.Figures.Figure;

class Space extends InputAdapter implements ApplicationListener {

	private ModelBatch modelBatch;
	private Environment environment;

	private CameraController cameraController;

	private Stage stage;
	private Label label;

	@SuppressWarnings({"FieldCanBeLocal", "unused"})
	private Messenger messenger;

	private Model XYZ;

	private ArrayList<Figure> figures;

	Space(Messenger messenger) {
		this.messenger = messenger;
	}

	@Override
	public void create() {
		cameraController = new CameraController();
		Gdx.input.setInputProcessor(new GestureDetector(cameraController));

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		environment.add(new PointLight().set(0.3f, 0.3f, 0.3f, 100f, 100f, 100f, 50));

		BitmapFont font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);

		figures = new ArrayList<Figure>();

		XYZ = new ModelBuilder().createXYZCoordinates(200, new Material(ColorAttribute.createDiffuse(Color.FIREBRICK)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
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

	void parse(@SuppressWarnings("UnusedParameters") String newObject) {
		//TODO
	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		label.setText(" FPS: " + Gdx.graphics.getFramesPerSecond());
		stage.draw();

		modelBatch.begin(cameraController.getCamera());
		modelBatch.render(new ModelInstance(XYZ), environment);
		for(Figure f : figures)
			f.draw(modelBatch,environment);
		modelBatch.end();
	}


	@SuppressWarnings("unused")
	void reset(){
		for(Figure f : figures)
			f.dispose();
		figures.clear();
	}

}
