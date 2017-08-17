package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;

public class SpaceFragment extends AndroidFragmentApplication {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Toast.makeText(getContext().getApplicationContext(), "FUUUUUUUUUUUUCK", Toast.LENGTH_LONG).show();
        return initializeForView(new BaseSpaceClass());
    }
}
