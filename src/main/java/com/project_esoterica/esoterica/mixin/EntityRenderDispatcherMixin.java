package com.project_esoterica.esoterica.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.project_esoterica.esoterica.core.handlers.MeteorFireHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void esotericaMeteorFireWorldRendering(Entity pEntity, double pX, double pY, double pZ, float pRotationYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci)
    {
        MeteorFireHandler.ClientOnly.renderWorldMeteorFire(pMatrixStack, pBuffer, Minecraft.getInstance().gameRenderer.getMainCamera(), pEntity);
    }
}
