package core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import core.GameData;
import core.Utils;
import core.views.ShopView;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShopScreen extends ScreenAdapter {

    private final Stage stage;
    private final ShopView shopView;

    public ShopScreen() {
        this.stage = new Stage(new FitViewport(GameData.GAME_WIDTH, GameData.GAME_HEIGHT));
        this.shopView = new ShopView(stage);

        var missionsList = GameData.MISSIONS_LIST.stream().filter(e -> e.getCompleted() == 0).collect(Collectors.toCollection(ArrayList::new));
        var skinsList = GameData.SKINS_LIST;

        var playerList = skinsList.stream().filter(e -> e.getPicture().contains("player")).collect(Collectors.toCollection(ArrayList::new));
        var bulletList = skinsList.stream().filter(e -> e.getPicture().contains("bullet")).collect(Collectors.toCollection(ArrayList::new));
        var cursorList = skinsList.stream().filter(e -> e.getPicture().contains("cursor")).collect(Collectors.toCollection(ArrayList::new));
        var aimList = skinsList.stream().filter(e -> e.getPicture().contains("aim")).collect(Collectors.toCollection(ArrayList::new));
        var gunList = skinsList.stream().filter(e -> e.getPicture().contains("weapon")).collect(Collectors.toCollection(ArrayList::new));

        stage.addActor(shopView.getView(stage, missionsList, playerList, bulletList, cursorList, aimList, gunList));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.setViewport(new FitViewport(width, height));
        stage.getViewport().update(width, height, true);
        shopView.resize(width, height);
    }
}