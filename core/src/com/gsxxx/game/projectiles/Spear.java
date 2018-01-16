package com.gsxxx.game.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.gsxxx.game.MammothGame;

public class Spear extends ProjectilesPrototype{

    private SpriteBatch batch;
    private Sprite projectileSprite;

    private Body spearBody;


    public Spear(int projectileStartingPositionX, int projectileStartingPositionY){

        //spear's look
        batch = new SpriteBatch();
        projectileSprite = new Sprite(new Texture("spear.png"));
        // 2 m x 0.3 m
        projectileSprite.setSize(2f, 0.3f);

        //spear's body
        BodyDef spearBodyDef = new BodyDef();
        spearBodyDef.type = BodyDef.BodyType.DynamicBody;
        spearBodyDef.position.set(projectileStartingPositionX, projectileStartingPositionY);
        spearBody = MammothGame.world.createBody(spearBodyDef);
        PolygonShape spearHitbox = new PolygonShape();
        spearHitbox.setAsBox(projectileSprite.getWidth() / 2 , projectileSprite.getHeight() / 2);
        // create a fixture definition to apply our shape to
        FixtureDef spearFixtureDef = new FixtureDef();
        spearFixtureDef.shape = spearHitbox;
        spearFixtureDef.density = 500f;
        spearFixtureDef.friction = 0f;
        spearFixtureDef.restitution = 0f; // make it not bounce at all
        spearBody.createFixture(spearFixtureDef);

        //apply force to spear
//        spearBody.applyForceToCenter(0.0f, -100.0f, true);
//        spearBody.applyLinearImpulse(30.0f, 10f, 800, 400, true);

        spearHitbox.dispose();
    }
    public void render(){
        batch.begin();
        batch.setProjectionMatrix(MammothGame.camera.combined);
        projectileSprite.setRotation((float) Math.toDegrees(spearBody.getAngle()));
        projectileSprite.setPosition(spearBody.getPosition().x - projectileSprite.getWidth() / 2, spearBody.getPosition().y - projectileSprite.getHeight() / 2);
        projectileSprite.draw(batch);
        batch.end();
    }

    public void dispose(){
        batch.dispose();
    }
}
