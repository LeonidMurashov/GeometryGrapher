package edu.cornsticks.geomgraph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

class CameraController implements GestureDetector.GestureListener {

    private PerspectiveCamera cam;
    private Vector3 lookPoint;
    private float angle;


    CameraController() {

        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Vector3 position = new Vector3(150, 150, 150);
        cam.position.set(position);
        lookPoint = new Vector3(0,0,0);
        cam.lookAt(lookPoint);
        cam.near = 1;
        cam.far = 3000;
        cam.up.set(Vector3.Z);
        cam.update();
        angle = 0;
    }

    Camera getCamera() {
        return cam;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if(count == 2) {
            lookPoint.set(0,0,0);
            cam.position.set(100,0,0);
            cam.up.set(Vector3.Z);
            pan(-1, -1, 0, 0);
            angle = 0;
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

       cam.rotateAround(lookPoint, Vector3.Z.cpy(), -deltaX*0.1f);
        if(angle - deltaY*0.1f > -87 && angle - deltaY*0.1f < 87) {
            cam.rotateAround(lookPoint, Vector3.Z.cpy().crs(cam.position), -deltaY * 0.1f);
            angle -= deltaY*0.1f;
        }
        cam.lookAt(lookPoint);
        cam.update();
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if(cam.position.cpy().scl(1 + (initialDistance-distance)*0.0001f).len() != 0)
            cam.position.scl(1 + (initialDistance-distance)*0.0001f);
        cam.lookAt(lookPoint);
        cam.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
