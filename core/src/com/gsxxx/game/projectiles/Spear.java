package com.gsxxx.game.projectiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.gsxxx.game.MammothGame;

public class Spear extends ProjectilesPrototype {

    private SpriteBatch batch;
    private Sprite projectileSprite;

    private Body spearHead;
    private Body spearShaft;


    public Spear(int projectileStartingPositionX, int projectileStartingPositionY, int projectileStaringAngle) {

        //spear look
        batch = new SpriteBatch();
        batch.setProjectionMatrix(MammothGame.camera.combined);
        projectileSprite = new Sprite(new Texture("spear.png"));
        // 2 m x 0.3 m
        projectileSprite.setSize(2f, 0.3f);

        //spear shaft body
        BodyDef spearBodyDefShaft = new BodyDef();
        spearBodyDefShaft.type = BodyDef.BodyType.DynamicBody;
        spearBodyDefShaft.position.set(projectileStartingPositionX, projectileStartingPositionY);
        spearShaft = MammothGame.world.createBody(spearBodyDefShaft);

        //shaft hitbox
        PolygonShape spearHitboxShaft = new PolygonShape();
        spearHitboxShaft.setAsBox(projectileSprite.getWidth() / 2 * 859 / 1109, projectileSprite.getHeight() / 4);

        //shaft fixture
        FixtureDef fixtureDefShaft = new FixtureDef();
        fixtureDefShaft.shape = spearHitboxShaft;
        fixtureDefShaft.density = 500f;
        fixtureDefShaft.friction = 0f;
        fixtureDefShaft.restitution = 0f; // make it not bounce at all

        //spear head body
        BodyDef spearBodyDefHead = new BodyDef();
        spearBodyDefHead.type = BodyDef.BodyType.DynamicBody;
        spearBodyDefHead.position.set(projectileStartingPositionX - projectileSprite.getWidth() / 2, projectileStartingPositionY);
        spearHead = MammothGame.world.createBody(spearBodyDefHead);

        //head hitbox
        PolygonShape spearHitboxHead = new PolygonShape();
        spearHitboxHead.setAsBox(projectileSprite.getWidth() / 2 * 250 / 1109, projectileSprite.getHeight() / 2);

        //head fixture
        FixtureDef fixtureDefHead = new FixtureDef();
        fixtureDefHead.shape = spearHitboxHead;
        fixtureDefHead.density = 500f;
        fixtureDefHead.friction = 0f;
        fixtureDefHead.restitution = 0f; // make it not bounce at all


        //apply fixtures to body
        spearHead.createFixture(fixtureDefHead);
        spearShaft.createFixture(fixtureDefShaft);

        //dispose hitbox
        spearHitboxHead.dispose();
        spearHitboxShaft.dispose();


        //weld the spear head to the shaft
        WeldJointDef weldJointDef = new WeldJointDef();
        weldJointDef.bodyA = spearHead;
        weldJointDef.bodyB = spearShaft;
        weldJointDef.type = JointDef.JointType.WeldJoint;
        weldJointDef.collideConnected = true;
        weldJointDef.frequencyHz = 0;
        weldJointDef.dampingRatio = 0;
        Vector2 weldpoint = spearHead.getWorldCenter();
        weldJointDef.initialize(weldJointDef.bodyB, weldJointDef.bodyA, weldpoint);
        MammothGame.world.createJoint(weldJointDef);

        //set angle and origin point for sprite
        projectileSprite.setOrigin(projectileSprite.getWidth() * 680 / 1109 , projectileSprite.getHeight() / 2);
        spearShaft.setTransform(spearShaft.getPosition(), projectileStaringAngle);

        //apply force to spear
//       spearHead.applyForceToCenter(0.0f, -100.0f, true);
//       spearHead.applyLinearImpulse(new Vector2(-1500,1500), spearHead.getWorldCenter(), true);

    }

    public void render() {
        batch.begin();
        projectileSprite.setRotation((float) Math.toDegrees(spearShaft.getAngle()));
        projectileSprite.setPosition(spearShaft.getPosition().x - projectileSprite.getWidth() / 2 - projectileSprite.getWidth() / 2 * 250 / 1109,
                spearShaft.getPosition().y - projectileSprite.getHeight() / 2);
        projectileSprite.draw(batch);
        batch.end();
    }

    public void dispose() {
        batch.dispose();
    }
}
