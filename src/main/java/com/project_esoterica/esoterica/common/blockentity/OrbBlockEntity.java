package com.project_esoterica.esoterica.common.blockentity;

import com.project_esoterica.esoterica.core.setup.block.BlockEntityRegistry;
import com.project_esoterica.esoterica.core.setup.client.ParticleRegistry;
import com.project_esoterica.esoterica.core.systems.blockentity.SimpleBlockEntity;
import com.project_esoterica.esoterica.core.systems.rendering.RenderUtilities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public class OrbBlockEntity extends SimpleBlockEntity {

    public OrbBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public OrbBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.ORB.get(), pos, state);
    }

    @Override
    public void tick() {
        if (level.isClientSide) {
            double x = worldPosition.getX() + 0.5;
            double y = worldPosition.getY() + 0.5;
            double z = worldPosition.getZ() + 0.5;
            int lifeTime = 3 + level.random.nextInt(9);
            float scale = 0.1f + level.random.nextFloat() * 0.07f;
            RenderUtilities.create(ParticleRegistry.SCREEN_WISP)
                    .setScale(scale, 0)
                    .setLifetime(lifeTime)
                    .setAlpha(0.2f)
                    .randomVelocity(0.02f)
                    .randomOffset(0.05f)
                    .setColor(Color.CYAN, Color.YELLOW)
                    .setColorCurveMultiplier(1.25f)
                    .spawn(level, x, y, z);
            if (level.getGameTime() % 2L == 0) {
                RenderUtilities.create(ParticleRegistry.SQUARE)
                        .setScale(scale * 2, 0)
                        .setLifetime(lifeTime + 4)
                        .setAlpha(0, 0.8f)
                        .addVelocity(0, 0.04f, 0)
                        .randomVelocity(0.02f)
                        .randomOffset(0.1f)
                        .setColor(Color.CYAN, Color.YELLOW)
                        .setColorCurveMultiplier(1.25f)
                        .spawn(level, x, y, z);
            }
            RenderUtilities.create(ParticleRegistry.CIRCLE)
                    .setScale(scale, 0)
                    .setLifetime(lifeTime + 4)
                    .setAlpha(0, 0.3f)
                    .randomVelocity(0.02f)
                    .randomOffset(0.1f)
                    .setColor(Color.CYAN, Color.YELLOW)
                    .setColorCurveMultiplier(1.5f)
                    .spawn(level, x, y+0.025f, z);

        }
    }
}