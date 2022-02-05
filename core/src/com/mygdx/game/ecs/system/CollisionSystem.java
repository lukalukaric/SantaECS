package com.mygdx.game.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.common.GameManager;
import com.mygdx.game.common.Mappers;
import com.mygdx.game.ecs.component.BoundsComponent;
import com.mygdx.game.ecs.component.PresentComponent;
import com.mygdx.game.ecs.component.SantaComponent;
import com.mygdx.game.ecs.component.SnowmanComponent;
import com.mygdx.game.ecs.system.passive.SoundSystem;

public class CollisionSystem extends EntitySystem {

    private static final Family SANTA_FAMILY = Family.all(SantaComponent.class, BoundsComponent.class).get();
    private static final Family SNOWMAN_FAMILY = Family.all(SnowmanComponent.class, BoundsComponent.class).get();
    private static final Family PRESENT_FAMILY = Family.all(PresentComponent.class, BoundsComponent.class).get();

    private SoundSystem soundSystem;

    public CollisionSystem() {
    }

    @Override
    public void addedToEngine(Engine engine) {
        soundSystem = engine.getSystem(SoundSystem.class);
    }

    @Override
    public void update(float deltaTime) {
        if (GameManager.INSTANCE.isGameOver()) return;

        ImmutableArray<Entity> santas = getEngine().getEntitiesFor(SANTA_FAMILY);
        ImmutableArray<Entity> snowmans = getEngine().getEntitiesFor(SNOWMAN_FAMILY);
        ImmutableArray<Entity> presents = getEngine().getEntitiesFor(PRESENT_FAMILY);

        for (Entity santaEntity : santas) {
            BoundsComponent santaBounds = Mappers.BOUNDS.get(santaEntity);

            // check collision with snowmans
            for (Entity snowmanEntity : snowmans) {
                SnowmanComponent snowman = Mappers.SNOWMAN.get(snowmanEntity);

                if (snowman.hit) {
                    continue;
                }

                BoundsComponent snowmanBounds = Mappers.BOUNDS.get(snowmanEntity);

                if (Intersector.overlaps(santaBounds.rectangle, snowmanBounds.rectangle)) {
//                    snowman.hit = true;
                    GameManager.INSTANCE.damage();
                    soundSystem.pick();
                }
            }

            // check collision with presents
            for (Entity presentEntity : presents) {
                PresentComponent present = Mappers.PRESENT.get(presentEntity);

                if (present.hit) {
                    continue;
                }

                BoundsComponent presentBounds = Mappers.BOUNDS.get(presentEntity);

                if (Intersector.overlaps(santaBounds.rectangle, presentBounds.rectangle)) {
                    present.hit = true;
                    GameManager.INSTANCE.incResult();
                    soundSystem.pick();
                    getEngine().removeEntity(presentEntity);
                }
            }
        }
    }
}
