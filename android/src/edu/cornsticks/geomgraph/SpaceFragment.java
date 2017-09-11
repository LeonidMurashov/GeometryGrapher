package edu.cornsticks.geomgraph;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

    void parse(String str) {
        space.requestDrawing(str);
    }

    void resetScene(){
        space.reset();
    }

    @Override
    public void sendMessage(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext().getApplicationContext(), str, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void log(String str) {
        Log.e("DEALER",str);
    }
}
