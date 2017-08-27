package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class SpaceFragment extends AndroidFragmentApplication implements Messenger {

    private Space space;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        space = new Space(this);

        return initializeForView(space);
    }

    void parseAndAddObject(String newObject) {
        space.parse(newObject);
    }

    void addPlane(float a, float b, float c, float d){
        //Random r = new Random();
        //space.addPlane(new Plane(new Vector3(1,4,5), new Vector3(0,2,0), new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1f)));
    }

    void resetScene(){
    //    space.resetScene();
    }

    @Override
    public void sendMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

    }
}
