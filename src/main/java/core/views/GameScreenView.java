package core.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import core.GameData;
import core.Money;
import core.MusicPlayer;
import core.database.MissionConnection;
import core.objects.Account;
import core.objects.Mission;
import core.popup.GameFinnishPopup;
import core.popup.GameOverPopup;
import core.popup.PausePopup;
import core.screens.GameScreen;
import core.screens.helper.DurationTimer;

public class GameScreenView {

    private final GameScreen gameScreen;
    private final Stage stage;
    private final Skin skin;
    private final Table mainTable, emptyTable;
    private final NavigationBar navigationBar;
    private final PausePopup pausePopup;
    private final GameFinnishPopup gameFinnishPopup;
    private final GameOverPopup gameOverPopup;
    private final Account account;

    public GameScreenView(GameScreen gameScreen, Stage stage, Mission mission) {
        this.gameScreen = gameScreen;
        this.stage = stage;
        this.skin = new Skin(Gdx.files.internal(GameData.SKIN));
        this.mainTable = new Table();
        this.emptyTable = new Table();
        this.navigationBar = new NavigationBar();
        this.pausePopup = new PausePopup(this, skin);
        this.gameFinnishPopup = new GameFinnishPopup(this, skin, mission);
        this.gameOverPopup = new GameOverPopup(this, skin);
        this.account = GameData.PLAYER_ACCOUNT;
    }

    public Table getView(String missionName) {
        mainTable.setFillParent(true);
        mainTable.add(navigationBar.gameScreenNavigationBar(missionName)).growX();
        mainTable.row();
        mainTable.add(emptyTable).growX().growY();
        return mainTable;
    }

    public void showPausePopup() {
        pausePopup.setPopup();
        gameScreen.gameStats = GameScreen.GameStats.PAUSE;
        stage.addActor(pausePopup);
    }

    public void closePausePopup() {
        var actors = stage.getActors();
        actors.get(actors.size - 1).remove();
        gameScreen.gameStats = GameScreen.GameStats.IN_PROCESS;
    }

    public void showGameFinnishPopup(Mission mission, DurationTimer durationTimer) {
        gameScreen.gameStats = GameScreen.GameStats.OVER;

        var enemyKilled = (GameData.ENEMY_FINAL_COUNT - GameData.ENEMY_KILLED_COUNT);

        var update = MissionConnection.insertMissionData(mission.getId(), GameData.PLAYER_FIRED_BULLETS, enemyKilled, GameData.HOSTAGE_KILLED_COUNT, durationTimer.getTime());

        var maxScore = Math.max(mission.getMaxScore(), update.getTotalMoney());

        mission.setCompleted(1);
        mission.setLastScore(update.getTotalMoney());
        mission.setMaxScore(maxScore);

        account.setMoney(account.getMoney() + update.getTotalMoney());

        gameFinnishPopup.setPopup(Money.format(update.getEarnedMoney()), Money.format(update.getHostageKilledMoney()), Money.format(update.getEnemyKilledMoney()), Money.format(update.getAmmoCosts()), update.getUsedTime(), Money.format(update.getTotalMoney()), update.getTotalMoney());
        stage.addActor(gameFinnishPopup);
    }

    public void showGameOverPopup(String cause) {

        var ammoCosts = GameData.PLAYER_FIRED_BULLETS * 3;

        if (MissionConnection.gameOver(ammoCosts).matches("1")) {
            if (account.getMoney() > ammoCosts) {
                account.setMoney(account.getMoney() - ammoCosts);
            } else {
                account.setMoney(0);
            }
        }

        gameOverPopup.setPopup(cause, Money.format(ammoCosts));
        gameScreen.gameStats = GameScreen.GameStats.OVER;
        stage.addActor(gameOverPopup);
    }

    public void changeMusic() {
        MusicPlayer.stop();
        MusicPlayer.setMusic(GameData.BASIC_MUSIC);
        if (GameData.SETTINGS.getMusic() == 1) {
            MusicPlayer.play();
        }
    }

    public PausePopup getPausePopup() {
        return pausePopup;
    }

    public GameFinnishPopup getGameFinnishPopup() {
        return gameFinnishPopup;
    }

    public GameOverPopup getGameOverPopup() {
        return gameOverPopup;
    }

    public NavigationBar getNavigationBar() {
        return navigationBar;
    }
}