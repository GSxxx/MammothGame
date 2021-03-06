package com.gsxxx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import static com.gsxxx.game.Mammoth.MammothStates.STATE_RUNNING;
import static com.gsxxx.game.Mammoth.MammothStates.STATE_STRUCK;
import static com.gsxxx.game.PlayScreen.MAMMOTH;
import static com.gsxxx.game.PlayScreen.SPEAR_HEAD;

public final class Mammoth {
    //singleton
    private static final Mammoth INSTANCE = new Mammoth();
    //mammoth look variables
    private SpriteBatch batch;
    private Animation<TextureRegion> mammothRunAnimation;
    //private Animation<TextureRegion> mammothStruckAnimation;  TODO: solve struck mammoth time
    private Texture mammothStruck;
    //private TextureAtlas atlas; todo
    private float stateTime;

    //mammoth image properties variables
    private float mammothImagePositionX;
    private float mammothImagePositionY;
    private float mammothImageWidth;
    private float mammothImageHeight;
    float health = 1f;
    private Body mammothBody;

    //mammoth states
    public enum MammothStates {
        STATE_RUNNING,
        STATE_STRUCK
    }

    private MammothStates mammothState = STATE_RUNNING;

    private Mammoth() {
        batch = new SpriteBatch();
        batch.setProjectionMatrix(PlayScreen.camera.combined);
        //counting animation time
        stateTime = 0f;

        //setting variables
        mammothImagePositionX = 0.3f;
        mammothImagePositionY = 1.2f;
        mammothImageWidth = 3f;
        mammothImageHeight = 1.87f;
        float runningAnimationFrameDuration = 0.1f;

        //setting up mammoth's hitbox
        BodyDef mammothBodyDef = new BodyDef();
        mammothBodyDef.type = BodyDef.BodyType.StaticBody;
        mammothBodyDef.position.set(mammothImagePositionX + mammothImageWidth / 2, mammothImagePositionY + mammothImageHeight / 2);

        mammothBody = PlayScreen.world.createBody(mammothBodyDef);
        PolygonShape mammothHitBox = new PolygonShape();

        Vector2[] verticesShaft = new Vector2[8];
        verticesShaft[0] = new Vector2(-0.8f, -0.85f);
        verticesShaft[1] = new Vector2(-0.7f, -0.2f);
        verticesShaft[2] = new Vector2(-0.45f, 0.18f);
        verticesShaft[3] = new Vector2(0.25f, 0.60f);
        verticesShaft[4] = new Vector2(0.55f, 0.60f);
        verticesShaft[5] = new Vector2(0.8f, -0.3f);
        verticesShaft[6] = new Vector2(0.75f, -0.5f);
        verticesShaft[7] = new Vector2(0.55f, -0.85f);
        mammothHitBox.set(verticesShaft);

        //mammoth fixture definition
        FixtureDef mammothFixture = new FixtureDef();
        mammothFixture.shape = mammothHitBox;
        mammothFixture.density = 1f;
        mammothFixture.filter.categoryBits = MAMMOTH;
        mammothFixture.filter.maskBits = SPEAR_HEAD;
        mammothBody.createFixture(mammothFixture);
        mammothHitBox.dispose();

        //set user data name to recognize collisions
        mammothBody.setUserData("mammoth");

        //packing running animation frames
        TextureRegion[] walkFrames = new TextureRegion[4];
        int index = 0;
        for (int i = 1; i < 5; i++) {
            walkFrames[index++] = new TextureRegion(new Texture("mammoth/runningMammoth_" + i + ".png"));
        }
        mammothRunAnimation =
                new Animation<TextureRegion>(runningAnimationFrameDuration, walkFrames);

        //struck mammoth frame
        mammothStruck = new Texture("mammoth/struckMammoth_1.png");
    }

    void render() {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.begin();
        switch (this.getState()) {
            case STATE_STRUCK:
                batch.draw(mammothStruck, mammothBody.getPosition().x - mammothImageWidth / 2,
                        mammothBody.getPosition().y - mammothImageHeight / 2, mammothImageWidth, mammothImageHeight);
                if (stateTime > 1) {
                    setState(STATE_RUNNING);
                }
                break;
            case STATE_RUNNING:
                mammothRunningAnimation();
                break;
        }
        batch.end();
    }

    private void mammothRunningAnimation() {
        batch.draw(mammothRunAnimation.getKeyFrame(stateTime, true), mammothBody.getPosition().x - mammothImageWidth / 2,
                mammothBody.getPosition().y - mammothImageHeight / 2, mammothImageWidth, mammothImageHeight);
    }

    public void mammothGotHit() {
        health -= 0.2;
        setState(STATE_STRUCK);
        stateTime = 0;
    }

    private MammothStates getState() {
        return mammothState;
    }

    private void setState(MammothStates state) {
        mammothState = state;
    }

    public static Mammoth getInstance() {
        return INSTANCE;
    }

    void dispose() {
        this.health = 1;
    }
}