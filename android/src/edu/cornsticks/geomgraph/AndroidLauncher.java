package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class AndroidLauncher extends AppCompatActivity implements AndroidFragmentApplication.Callbacks{
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_launcher);



		Log.d("GEGRGRAPH", "onCreate: started!!!");

		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.add(R.id.gdxfragment, new SpaceFragment())
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_app_bar, menu);
		return true;
	}

	@Override
	public void exit() {

	}
}
