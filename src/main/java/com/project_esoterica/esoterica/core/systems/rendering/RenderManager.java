package com.project_esoterica.esoterica.core.systems.rendering;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.project_esoterica.esoterica.common.capability.WorldDataCapability;
import com.project_esoterica.esoterica.core.registry.worldevent.WorldEventRenderers;
import com.project_esoterica.esoterica.core.systems.worldevent.WorldEventInstance;
import com.project_esoterica.esoterica.core.systems.worldevent.WorldEventRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;

@OnlyIn(Dist.CLIENT)
public class RenderManager {
    public static HashMap<RenderType, BufferBuilder> BUFFERS = new HashMap<>();
    public static MultiBufferSource.BufferSource DELAYED_RENDER = null;
    public static Frustum FRUSTUM;

    public static void onClientSetup(FMLClientSetupEvent event) {
        DELAYED_RENDER = MultiBufferSource.immediateWithBuffers(BUFFERS, new BufferBuilder(256));
    }
    public static void onRenderLast(RenderLevelLastEvent event) {
        prepareFrustum(event.getPoseStack(), Minecraft.getInstance().getEntityRenderDispatcher().camera.getPosition(), event.getProjectionMatrix());
        WorldDataCapability.getCapability(Minecraft.getInstance().level).ifPresent(capability -> {
            for (WorldEventInstance instance : capability.ACTIVE_WORLD_EVENTS) {
                WorldEventRenderer<WorldEventInstance> renderer = WorldEventRenderers.RENDERERS.get(instance.type);
                if (renderer != null) {
                    if (renderer.canRender(instance)) {
                        renderer.render(instance, event.getPoseStack(), RenderManager.DELAYED_RENDER, event.getPartialTick());
                    }
                }
            }
        });
        event.getPoseStack().pushPose();
        for (RenderType type : BUFFERS.keySet())
        {
            DELAYED_RENDER.endBatch(type);
        }
        event.getPoseStack().popPose();
    }

    public static void prepareFrustum(PoseStack p_172962_, Vec3 p_172963_, Matrix4f p_172964_) {
        Matrix4f matrix4f = p_172962_.last().pose();
        double d0 = p_172963_.x();
        double d1 = p_172963_.y();
        double d2 = p_172963_.z();
        FRUSTUM = new Frustum(matrix4f, p_172964_);
        FRUSTUM.prepare(d0, d1, d2);
    }

}