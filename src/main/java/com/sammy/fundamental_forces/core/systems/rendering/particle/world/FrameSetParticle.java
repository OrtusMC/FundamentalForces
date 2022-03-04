package com.sammy.fundamental_forces.core.systems.rendering.particle.world;

import com.sammy.fundamental_forces.core.systems.rendering.particle.SimpleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;

import java.util.ArrayList;

public class FrameSetParticle extends GenericParticle {
    public ArrayList<Integer> frameSet = new ArrayList<>();

    public FrameSetParticle(ClientLevel world, WorldParticleOptions data, ParticleEngine.MutableSpriteSet spriteSet, double x, double y, double z, float xd, float yd, float zd) {
        super(world, data, spriteSet, x, y, z, xd, yd, zd);
    }

    @Override
    public void tick() {
        pickSprite(frameSet.get(age));
        super.tick();
    }

    @Override
    public SimpleParticleOptions.Animator getAnimator() {
        return SimpleParticleOptions.Animator.FIRST_INDEX;
    }

    protected void addLoop(int min, int max, int times) {
        for (int i = 0; i < times; i++) {
            addFrames(min, max);
        }
    }

    protected void addFrames(int min, int max) {
        for (int i = min; i <= max; i++) {
            frameSet.add(i);
        }
    }

    protected void insertFrames(int insertIndex, int min, int max) {
        for (int i = min; i <= max; i++) {
            frameSet.add(insertIndex, i);
        }
    }
}