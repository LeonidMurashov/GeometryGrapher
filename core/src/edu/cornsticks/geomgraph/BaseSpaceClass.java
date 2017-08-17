package edu.cornsticks.geomgraph;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.io.IOException;

public class BaseSpaceClass extends InputAdapter implements ApplicationListener {
	private ModelBatch modelBatch;
	private Environment environment;

	private Label label;
	private Stage stage;
	private SceneHolder sceneHolder;
	private CameraController cameraController;

	@Override
	public void create () {

		sceneHolder = new SceneHolder();
		cameraController = new CameraController(sceneHolder);
		Gdx.input.setInputProcessor(new GestureDetector(cameraController));

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1f));
		//environment.add(new DirectionalLight().set(0.3f,0.3f,0.3f, 10f, 10f ,-20f));

		environment.add(new PointLight().set(0.3f,0.3f,0.3f, 100f, 100f, 100f, 50));

		BitmapFont font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		StringBuilder builder = new StringBuilder();
		builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
		label.setText(builder);
		stage.draw();

		modelBatch.begin(cameraController.getCamera());
		sceneHolder.render(modelBatch, environment);
		modelBatch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
	}

	void parseAndAdd(String newObject) throws IOException {
		sceneHolder.DrawThis(newObject);
	}
}
