package com.gsxxx.game;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Ground {

    Ground() {
        BodyDef groundDef = new BodyDef();
        groundDef.position.set(MammothGame.camera.viewportWidth / 2, 1);

        Body groundBody = MammothGame.world.createBody(groundDef);


        PolygonShape groundBox = new PolygonShape();

        groundBox.setAsBox(MammothGame.camera.viewportWidth / 2, 0);
        groundBody.createFixture(groundBox, 0);
    }
}
