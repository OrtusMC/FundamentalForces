package com.sammy.fundamental_forces.client.particles.wisp;

import com.sammy.fundamental_forces.core.systems.rendering.screenparticle.GlowingScreenParticle;
import com.sammy.fundamental_forces.core.systems.rendering.screenparticle.options.ScreenParticleOptions;
import com.sammy.fundamental_forces.core.systems.rendering.screenparticle.base.ScreenParticle;
import com.sammy.fundamental_forces.core.systems.rendering.screenparticle.ScreenParticleType;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SpriteSet;

public class WispScreenParticleType extends ScreenParticleType<ScreenParticleOptions> {

    public WispScreenParticleType() {
        super();
    }

    public static class Factory implements ParticleProvider<ScreenParticleOptions> {
        public final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public ScreenParticle createParticle(ClientLevel pLevel, ScreenParticleOptions options, double pX, double pY, double pXSpeed, double pYSpeed) {
            GlowingScreenParticle particle = new GlowingScreenParticle(pLevel, options, pX, pY, pXSpeed, pYSpeed);
            particle.pickSprite(sprite);
            return particle;
        }
    }
}