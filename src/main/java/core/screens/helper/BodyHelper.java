package core.screens.helper;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import core.GameData;

public class BodyHelper {

    private World world;

    public BodyHelper(World world) {
        this.world = world;
    }

    public Body createObjectBody(float objectWidth, float objectHeight, float x, float y, String type) {

        var baseUnitX = 64;
        var baseUnitY = 64;

        var width = objectWidth * 1.5f;
        var height = objectHeight * 1.5f;

        var hx = width / baseUnitX;
        var hy = height / baseUnitY;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / GameData.PPM, y / GameData.PPM);
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(hx, hy, new Vector2(hx, hy), 0);

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef(shape, type));

        shape.dispose();
        return body;
    }

    public FixtureDef fixtureDef(PolygonShape shape, String type) {
        FixtureDef fixtureDef = new FixtureDef();
        if (type.matches("Player") || type.matches("Bullet")) fixtureDef.friction = 0;
        if (type.matches("Enemy") || type.matches("Hostage") || type.matches("Vip")) fixtureDef.friction = 20;
        fixtureDef.density = 0;
        fixtureDef.shape = shape;
        //fixtureDef.filter.categoryBits = new Byte(null);
        return fixtureDef;
    }
}