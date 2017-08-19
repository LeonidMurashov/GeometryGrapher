package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import java.io.IOException;

public class SpaceFragment extends AndroidFragmentApplication implements AndroidDealer {

    private BaseSpaceClass space;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast toast = new Toast(this.getContext());

        space = new BaseSpaceClass(this);

        return initializeForView(space);
    }

    void parseAndAddObject(String newObject) {
        space.parseAndAdd(newObject);
    }

    void addPlane(float a, float b, float c, float d){
        //Random r = new Random();
        //space.addPlane(new Plane(new Vector3(1,4,5), new Vector3(0,2,0), new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1f)));
    }

    void resetScene(){
    //    space.resetScene();
    }

    @Override
    public void MakeToast(final String str) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), str,
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}
