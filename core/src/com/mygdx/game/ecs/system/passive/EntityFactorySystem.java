package com.mygdx.game.ecs.system.passive;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.assets.AssetDescriptors;
import com.mygdx.game.assets.RegionNames;
import com.mygdx.game.config.GameConfig;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.CleanUpComponent;
import com.mygdx.game.ecs.component.DimensionComponent;
import com.mygdx.game.ecs.component.MovementComponentXYR;
import com.mygdx.game.ecs.component.PositionComponent;
import com.mygdx.game.ecs.component.PresentComponent;
import com.mygdx.game.ecs.component.SantaComponent;
import com.mygdx.game.ecs.component.SnowmanComponent;
import com.mygdx.game.ecs.component.TextureComponent;
import com.mygdx.game.ecs.component.WorldWrapComponent;
import com.mygdx.game.ecs.component.ZOrderComponent;

public class EntityFactorySystem extends EntitySystem {
    private static final int BACKGROUND_Z_ORDER = 1;
    private static final int SNOWMAN_Z_ORDER = 2;
    private static final int PRESENT_Z_ORDER = 3;
    private static final int SANTA_Z_ORDER = 4;

    private final AssetManager assetManager;

    private PooledEngine engine;
    private TextureAtlas gamePlayAtlas;

    public EntityFactorySystem(AssetManager assetManager) {
        this.assetManager = assetManager;
        setProcessing(false);   // passive system
        init();
    }

    private void init() {
        gamePlayAtlas = assetManager.get(AssetDescriptors.GAME_PLAY);
    }

    @Override
    public void addedToEngine(Engine engine) {
        this.engine = (PooledEngine) engine;
    }

    public void createSanta() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = GameConfig.WIDTH / 2f - GameConfig.SANTA_WIDTH / 2f;
        position.y = 20;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.SANTA_WIDTH;
        dimension.height = GameConfig.SANTA_HEIGHT;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        MovementComponentXYR movement = engine.createComponent(MovementComponentXYR.class);

        SantaComponent santa = engine.createComponent(SantaComponent.class);

        WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.SANTA);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = SANTA_Z_ORDER;

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movement);
        entity.add(santa);
        entity.add(worldWrap);
        entity.add(texture);
        entity.add(zOrder);
        engine.addEntity(entity);
    }

    public void createSnowman() {
        PositionComponent position = engine.createComponent(PositionComponent.class);

        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.SNOWMAN_WIDTH);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = -GameConfig.SNOWMAN_SPEED_X_MIN * MathUtils.random(-1f, 1f);
        movementComponent.ySpeed = -GameConfig.SNOWMAN_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        float randFactor = MathUtils.random(1f, 4f);
        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.SNOWMAN_WIDTH * randFactor;
        dimension.height = GameConfig.SNOWMAN_HEIGHT * randFactor;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        SnowmanComponent snowmanComponent = engine.createComponent(SnowmanComponent.class);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.SNOWMAN);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = SNOWMAN_Z_ORDER;

        // WorldWrapComponent worldWrap = engine.createComponent(WorldWrapComponent.class);

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(snowmanComponent);
        entity.add(texture);
        entity.add(zOrder);
        // entity.add(worldWrap);
        entity.add(cleanUp);
        engine.addEntity(entity);
    }

    public void createPresent() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = MathUtils.random(0, GameConfig.WIDTH - GameConfig.PRESENT_SIZE);
        position.y = GameConfig.HEIGHT;

        MovementComponentXYR movementComponent = engine.createComponent(MovementComponentXYR.class);
        movementComponent.xSpeed = GameConfig.SNOWMAN_SPEED_X_MIN * MathUtils.random(-0.1f, 0.1f);
        movementComponent.ySpeed = -GameConfig.SNOWMAN_SPEED_X_MIN * MathUtils.random(1f, 2f);
        movementComponent.rSpeed = MathUtils.random(-1f, 1f);

        PresentComponent present = engine.createComponent(PresentComponent.class);

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.PRESENT_SIZE;
        dimension.height = GameConfig.PRESENT_SIZE;

        BoundsComponent bounds = engine.createComponent(BoundsComponent.class);
        bounds.rectangle.setPosition(position.x, position.y);
        bounds.rectangle.setSize(dimension.width, dimension.height);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.PRESENT);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = PRESENT_Z_ORDER;

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);
        entity.add(bounds);
        entity.add(movementComponent);
        entity.add(present);
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp);
        engine.addEntity(entity);
    }

    public void createBackground() {
        PositionComponent position = engine.createComponent(PositionComponent.class);
        position.x = 0;
        position.y = 0;

        DimensionComponent dimension = engine.createComponent(DimensionComponent.class);
        dimension.width = GameConfig.WIDTH;
        dimension.height = GameConfig.HEIGHT;

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = gamePlayAtlas.findRegion(RegionNames.BACKGROUND);

        ZOrderComponent zOrder = engine.createComponent(ZOrderComponent.class);
        zOrder.z = BACKGROUND_Z_ORDER;

        CleanUpComponent cleanUp = engine.createComponent(CleanUpComponent.class);

        Entity entity = engine.createEntity();
        entity.add(position);
        entity.add(dimension);;
        entity.add(texture);
        entity.add(zOrder);
        entity.add(cleanUp);
        engine.addEntity(entity);
    }
}
