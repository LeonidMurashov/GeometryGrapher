package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

import java.io.IOException;

public class SpaceFragment extends AndroidFragmentApplication {

    private BaseSpaceClass space;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        space = new BaseSpaceClass();
        return initializeForView(space);

    }

    void parseAndAddObject(String newObject) throws IOException {
        space.parseAndAdd(newObject);
    }

    void addPlane(float a, float b, float c, float d){
        //Random r = new Random();
        //space.addPlane(new Plane(new Vector3(1,4,5), new Vector3(0,2,0), new Color(r.nextFloat(), r.nextFloat(), r.nextFloat(), 1f)));
    }

    void resetScene(){
    //    space.resetScene();
    }

}
