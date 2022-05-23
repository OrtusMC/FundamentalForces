package com.sammy.fufo.common.entity.magic.spell.tier0;

import com.sammy.fufo.core.setup.content.entity.EntityRegistry;
import com.sammy.fufo.core.systems.magic.element.MagicElement;
import com.sammy.ortus.setup.OrtusParticleRegistry;
import com.sammy.ortus.systems.easing.Easing;
import com.sammy.ortus.systems.rendering.particle.ParticleBuilders;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;

public class SpellMissile extends AbstractHurtingProjectile {
    protected static final EntityDataAccessor<Integer> DATA_FIRST_COLOR = SynchedEntityData.defineId(SpellMissile.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Integer> DATA_SECOND_COLOR = SynchedEntityData.defineId(SpellMissile.class, EntityDataSerializers.INT);
    public final ArrayList<Vec3> pastPositions = new ArrayList<>();
    public float duration = 100;
    public float damage = 5;
    public Color firstColor = new Color(16777215);
    public Color secondColor = new Color(16777215);
    public MagicElement element;

    public SpellMissile(Level level) {
        super(EntityRegistry.SPELL_MISSILE.get(), level);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    public SpellMissile setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public SpellMissile setFirstColor(Color color) {
        this.firstColor = color;
        return this;
    }

    public SpellMissile setSecondColor(Color color) {
        this.secondColor = color;
        return this;
    }

    public SpellMissile setElement(MagicElement element) {
        this.element = element;
        return this;
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.ASH;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(DamageSource.MAGIC, damage);
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        super.baseTick();
        if (this.tickCount > this.duration) {
            this.discard();
        }

        if (level.isClientSide) {
            double x = this.getX();
            double y = this.getY();
            double z = this.getZ();
            float scale = 0.17f + level.random.nextFloat() * 0.03f;
            Vec3 velocity = this.getDeltaMovement();
            ParticleBuilders.create(OrtusParticleRegistry.WISP_PARTICLE)
                    .setScale(scale / 2f, 0)
                    .setLifetime(4 + level.random.nextInt(5))
                    .setAlpha(0.8f, 0.5f)
                    .setColor(firstColor, secondColor)
                    .setColorCoefficient(0.2f)
                    .setColorEasing(Easing.SINE_OUT)
                    .setSpinOffset((level.getGameTime() * 0.2f) % 6.28f)
                    .setSpin(0, 0.3f)
                    .setSpinEasing(Easing.QUARTIC_IN)
                    .addMotion(velocity.x / 15, velocity.y / 15, velocity.z / 15)
                    .spawn(level, x, y, z);
            ParticleBuilders.create(OrtusParticleRegistry.SMOKE_PARTICLE)
                    .setScale(scale / 3f, 0)
                    .setLifetime(3 + level.random.nextInt(6))
                    .setAlpha(0.8f, 0.5f)
                    .setColor(firstColor, secondColor)
                    .setColorCoefficient(0.2f)
                    .setColorEasing(Easing.SINE_OUT)
                    .setSpinOffset((level.getGameTime() * 0.2f) % 6.28f)
                    .setSpin(0, 0.3f)
                    .setSpinEasing(Easing.QUARTIC_IN)
                    .addMotion(velocity.x / 20, velocity.y / 20, velocity.z / 20)
                    .spawn(level, x, y, z);
            ParticleBuilders.create(OrtusParticleRegistry.TWINKLE_PARTICLE)
                    .setScale(scale / 5f)
                    .setLifetime(2 + level.random.nextInt(2))
                    .setAlpha(0.8f, 0.2f)
                    .setColor(secondColor, firstColor)
                    .setColorCoefficient(0.3f)
                    .setColorEasing(Easing.SINE_OUT)
                    .setSpinOffset((level.getGameTime() * 0.2f) % 6.28f)
                    .randomOffset(0.4f)
                    .setSpin(0, 0.3f)
                    .setSpinEasing(Easing.QUARTIC_IN)
                    .addMotion(velocity.x / 3, velocity.y / 3, velocity.z / 3)
                    .spawn(level, x, y, z);
            ParticleBuilders.create(OrtusParticleRegistry.STAR_PARTICLE)
                    .setScale(scale, 0)
                    .setLifetime(1)
                    .setAlpha(0.8f, 0.5f)
                    .setColor(firstColor, secondColor)
                    .setColorCoefficient(0.2f)
                    .setColorEasing(Easing.SINE_OUT)
                    .setSpinOffset((level.getGameTime() * 0.2f) % 6.28f)
                    .setSpin(0, 0.3f)
                    .setSpinEasing(Easing.QUARTIC_IN)
                    .spawn(level, x, y, z);
        }
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
    }
}
