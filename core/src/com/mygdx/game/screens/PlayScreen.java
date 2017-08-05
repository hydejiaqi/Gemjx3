package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.Gemjx3;
import com.mygdx.game.model.Coord;
import com.badlogic.gdx.Screen;


/**
 * Created by hydej on 2017/8/3.
 */

public class PlayScreen implements com.badlogic.gdx.Screen{
    // Selected squares
    private Coord selectedeFirstSquare;
    private Coord selectedSecondSquare;

    // Mouse position
    private Vector3 mouse;


    private State state;
    private Gemjx3 game;

    private OrthographicCamera camera;

    public enum State {
        Loading,
        InitialGems,
        Wait,
        SelectedGem,
        ChangingGems,
        DisappearingGems,
        FallingGems,
        DisappearingBoard,
        TimeFinished,
        ShowingAnimation,
        Diaoluo,
    };

    public PlayScreen(Gemjx3 game){
        this.game = game;
        camera = new OrthographicCamera();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

    }

    private void update(float delta) {
        
        handleInput(delta);
    }

    private void handleInput(float dt) {
        mouse.x = Gdx.input.getX();
        mouse.y = Gdx.input.getY();

        camera.unproject(mouse);


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
