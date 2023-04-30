package core.objects.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import core.GameData;

public class Vip extends VipEntity {
    private Sprite sprite;

    public Vip(float width, float height, Body body) {
        super(width, height, body);
        this.sprite = new Sprite(new Texture("pictures/skins/vip/1/vip-1-stand.png"));
    }

    @Override
    public void update() {
        x = body.getPosition().x * GameData.PPM;
        y = body.getPosition().y * GameData.PPM;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(sprite, x, y, width, height);
    }

    public void destroyVip() {
        if (!body.getFixtureList().isEmpty()) body.destroyFixture(body.getFixtureList().first());
    }
}