package core.objects.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import core.GameData;
import core.Utils;
import core.database.objects.Settings;
import core.screens.GameScreen;
import core.screens.helper.BodyHelper;

public class Player extends PlayerEntity {

    private Sprite sprite;
    private final Sprite leftSprite, rightSprite;
    private final GameScreen gameScreen;
    private final OrthographicCamera orthographicCamera;
    private final BodyHelper bodyHelper;
    private final Settings settings;
    private final Utils utils;
    private int jumpCounter;
    public boolean direction = false;
    private float timeSinceLastShot = 0f;

    public Player(float width, float height, Body body, GameScreen gameScreen, World world, OrthographicCamera orthographicCamera, Weapon weapon) {
        super(width, height, body, weapon);
        this.speed = 10f;
        this.jumpCounter = 0;
        this.bodyHelper = new BodyHelper(world);
        this.gameScreen = gameScreen;
        this.orthographicCamera = orthographicCamera;
        this.settings = GameData.SETTINGS;
        this.utils = new Utils();
        this.sprite = new Sprite(new Texture("pictures/skins/player/" + GameData.CURRENT_PLAYER_SKIN + "/player-" + GameData.CURRENT_PLAYER_SKIN + "-stand.png"));
        this.leftSprite = new Sprite(new Texture("pictures/skins/player/" + GameData.CURRENT_PLAYER_SKIN + "/player-" + GameData.CURRENT_PLAYER_SKIN + "-left.png"));
        this.rightSprite = new Sprite(new Texture("pictures/skins/player/" + GameData.CURRENT_PLAYER_SKIN + "/player-" + GameData.CURRENT_PLAYER_SKIN + "-right.png"));
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        spriteBatch.draw(sprite, x, y, width, height);
    }

    @Override
    public void update() {
        x = body.getPosition().x * GameData.PPM;
        y = body.getPosition().y * GameData.PPM;
        checkUserInput();
    }

    private void checkUserInput() {
        velX = 0;

        if (Bullet.diffX(orthographicCamera, this) < 0) {
            setSprite(leftSprite);
            direction = true;
        } else {
            setSprite(rightSprite);
            direction = false;
        }

        if (utils.isButtonOrKeyPressed(settings.getKeyLeftCode())) {
            velX--;
        }

        if (utils.isButtonOrKeyPressed(settings.getKeyRightCode())) {
            velX++;
        }

        if (utils.isButtonOrKeyPressed(settings.getKeyShootCode())) {
            if (timeSinceLastShot >= weapon.getSpeed()) {
                shoot();
                timeSinceLastShot = 0f;
            }
        }

        timeSinceLastShot += Gdx.graphics.getDeltaTime();

        if (utils.isButtonOrKeyJustPressed(settings.getKeyUpCode()) && jumpCounter < 25) {
            float force = body.getMass() * 12;
            body.setLinearVelocity(body.getLinearVelocity().x, 0);
            body.applyLinearImpulse(new Vector2(0, force), body.getPosition(), true);
            jumpCounter++;
        }

        if (body.getLinearVelocity().y == 0) {
            jumpCounter = 0;
        }
        body.setLinearVelocity(velX * speed, body.getLinearVelocity().y < 25 ? body.getLinearVelocity().y : 25);
    }

    private void shoot() {

        float angle = Bullet.getBulletAngle(weapon, orthographicCamera);
        double weaponLength = Math.sqrt(Math.pow(weapon.getHeight(), 2) + Math.pow(weapon.getWidth(),2));

        float bulletX = !this.direction ? (float) (weapon.getX() + Math.cos(angle) * weaponLength) :
                (float) (weapon.getX() - Math.cos(angle) / weaponLength - ((weapon.getWidth() / 2) + this.getWidth()));
        float bulletY = (float) (weapon.getY() + Math.sin(angle) * weaponLength);

        Body body = bodyHelper.createObjectBody(5, 5, bulletX, bulletY, "Bullet");
        body.setUserData("PlayerBullet");
        gameScreen.playerBulletsList.add(new Bullet(body, angle));
        GameData.PLAYER_FIRED_BULLETS++;
    }

    private void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}