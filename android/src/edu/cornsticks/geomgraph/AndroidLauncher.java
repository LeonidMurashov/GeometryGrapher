package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class AndroidLauncher extends AppCompatActivity implements AndroidFragmentApplication.Callbacks, View.OnClickListener{

    SpaceFragment fragment;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_launcher);


		setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

        findViewById(R.id.button_add).setOnClickListener(this);
        findViewById(R.id.button_reset).setOnClickListener(this);

        fragment = new SpaceFragment();

		getSupportFragmentManager().beginTransaction()
				.add(R.id.gdxfragment, fragment)
				.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.menu_app_bar, menu);
		return true;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_open:
                open();
                break;
            case R.id.action_save:
                saveCurrentScene();
                break;
            case R.id.action_share:
                shareCurrentScene();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add:
                addObject();
                break;
            case R.id.button_reset:
                resetScene();
                break;
        }
    }

    public void open(){
        Toast.makeText(getApplicationContext(), "Opening...", Toast.LENGTH_LONG).show();
    }

    public void saveCurrentScene(){
        Toast.makeText(getApplicationContext(), "Saving...", Toast.LENGTH_LONG).show();
    }

    public void shareCurrentScene(){
        Toast.makeText(getApplicationContext(), "Coming Soon!!!", Toast.LENGTH_LONG).show();
    }

    public void addObject(){
        Toast.makeText(getApplicationContext(), "Adding...", Toast.LENGTH_LONG).show();
    }

    public void resetScene(){
        Toast.makeText(getApplicationContext(), "Resetting...", Toast.LENGTH_LONG).show();
    }

    @Override
	public void exit() {

	}
}
