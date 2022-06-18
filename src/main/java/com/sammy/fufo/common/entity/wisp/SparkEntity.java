package com.sammy.fufo.common.entity.wisp;

import com.sammy.fufo.core.setup.content.entity.EntityRegistry;
import com.sammy.ortus.helpers.DataHelper;
import com.sammy.ortus.helpers.EntityHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class SparkEntity extends AbstractWispEntity {
    public final ArrayList<EntityHelper.PastPosition> pastPositions = new ArrayList<>();

    public float magnetism = 0.75f + random.nextFloat();
    public float rotationOffset = random.nextFloat() * 360;
    public boolean isOrbiting;

    public SparkEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public SparkEntity(Level level) {
        this(EntityRegistry.METEOR_FIRE_SPARK.get(), level);
    }

    public SparkEntity(Level level, double posX, double posY, double posZ, double velX, double velY, double velZ) {
        super(EntityRegistry.METEOR_FIRE_SPARK.get(), level, posX, posY, posZ, velX, velY, velZ);
        findTarget();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putBoolean("isOrbiting", isOrbiting);
        pCompound.putFloat("magnetism", magnetism);
        pCompound.putFloat("rotationOffset", rotationOffset);
        super.addAdditionalSaveData(pCompound);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        isOrbiting = pCompound.getBoolean("isOrbiting");
        magnetism = pCompound.getFloat("magnetism");
        rotationOffset = pCompound.getFloat("rotationOffset");
        super.readAdditionalSaveData(pCompound);
    }

    @Override
    public void tick() {
        super.tick();
        float friction = fadingOut ? 0.92f : 0.995f - (Math.max(0, 10 - age) / 10f) * 0.2f;
        setDeltaMovement(getDeltaMovement().multiply(friction, friction, friction));
        trackPastPositions();
        if (level.isClientSide) {
            return;
        }

        if (targetEntity != null && targetEntity.isAlive()) {
            followTarget();
        }
        if (!fadingOut && !isOrbiting) {
            findTarget();
        }
    }

    @Override
    protected void sparkLockedOn(SparkEntity entity) {
        targetEntity = entity;
    }

    protected void createWisp(SparkEntity entity) {
        Vec3 wispPosition = entity.position().add(position()).multiply(0.5f, 0.5f, 0.5f);
        WispEntity wispEntity = new WispEntity(level, wispPosition.x, wispPosition.y, wispPosition.z, 0, 0.02f, 0);
        wispEntity.setColor(entity.color);
        level.addFreshEntity(wispEntity);
        entity.startFading();
        startFading();
    }

    public void trackPastPositions() {
        removeOldPositions(pastPositions);
        if (fadingOut) {
            pastPositions.forEach(p -> p.time += 2);
            if (pastPositions.isEmpty()) {
                remove(RemovalReason.DISCARDED);
            }
            fadeOut -= 0.25f;
        }
        EntityHelper.trackPastPositions(pastPositions, position(), 0.01f);
    }

    public void removeOldPositions(ArrayList<EntityHelper.PastPosition> pastPositions) {
        int amount = pastPositions.size() - 1;
        ArrayList<EntityHelper.PastPosition> toRemove = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            EntityHelper.PastPosition excess = pastPositions.get(i);
            if (excess.time > 15) {
                toRemove.add(excess);
            }
        }
        pastPositions.removeAll(toRemove);
    }

    public void followTarget() {
        float windUp = Math.min(30, this.age / 30f);
        float speed = windUp * 0.05f;
        float easing = 0.08f;

        Vec3 positionOffset = targetEntity.position().subtract(position());
        if (targetEntity instanceof WispEntity wispEntity) {
            float multiplier = (wispEntity.sparksOrbiting / 32f);
            float offsetScale = Math.max(0.5f, magnetism * (1-multiplier));
            speed *= (1-multiplier);
            if (offsetScale != 0) {
                positionOffset = DataHelper.rotatingRadialOffset(positionOffset, offsetScale, 0, 1, (long) (level.getGameTime() + rotationOffset), 80);
            }
        }
        Vec3 desiredMotion = positionOffset.normalize().multiply(speed, speed, speed);
        float xMotion = (float) Mth.lerp(easing, getDeltaMovement().x, desiredMotion.x);
        float yMotion = (float) Mth.lerp(easing, getDeltaMovement().y, desiredMotion.y);
        float zMotion = (float) Mth.lerp(easing, getDeltaMovement().z, desiredMotion.z);
        Vec3 resultingMotion = new Vec3(xMotion, yMotion, zMotion);
        setDeltaMovement(resultingMotion);
        if (targetEntity instanceof SparkEntity sparkEntity) {
            tryMerge(sparkEntity, position());
            tryMerge(sparkEntity, position().add(resultingMotion.multiply(0.5f, 0.5f, 0.5f)));
        }
    }

    public void tryMerge(SparkEntity sparkEntity, Vec3 position) {
        if (sparkEntity.isSparkValidForMerge(this)) {
            double distanceTo = targetEntity.distanceToSqr(position);
            if (distanceTo <= 0.02) {
                createWisp(sparkEntity);
            }
        }
    }

    public void findTarget() {
        if (findNearestCooldown == 0) {
            if (level instanceof ServerLevel) {
                List<AbstractWispEntity> entities = level.getEntities(EntityTypeTest.forClass(AbstractWispEntity.class), this.getBoundingBox().inflate(2, 4, 2), e -> e.isSparkValidForMerge(this));
                entities.remove(this);
                AbstractWispEntity nearestEntity = null;
                float windUp = Math.min(20, this.age / 20f);
                double distance = (0.2f + windUp * 2f) * magnetism;
                boolean foundPriorityTarget = entities.stream().anyMatch(AbstractWispEntity::hasPriority);
                if (targetEntity != null && targetEntity.isAlive() && !foundPriorityTarget) {
                    return;
                }
                for (AbstractWispEntity entity : entities) {
                    if (foundPriorityTarget && !entity.hasPriority()) {
                        continue;
                    }
                    if (entity.isSparkValidForMerge(this)) {
                        float distanceTo = entity.distanceTo(this);
                        if (distanceTo < distance) {
                            nearestEntity = entity;
                            distance = distanceTo;
                        }
                    }
                }
                targetEntity = nearestEntity;
                if (targetEntity != null) {
                    targetEntity.sparkLockedOn(this);
                }
            }
            findNearestCooldown = 5;
        } else {
            findNearestCooldown--;
        }
    }
}