package com.project_esoterica.esoterica.client.renderers.falling;

import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.project_esoterica.esoterica.EsotericaHelper;
import com.project_esoterica.esoterica.common.entity.falling.FallingEntity;
import com.project_esoterica.esoterica.core.systems.rendering.RenderTypes;
import com.project_esoterica.esoterica.core.systems.rendering.RenderUtilities;
import com.project_esoterica.esoterica.core.systems.rendering.Shaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;


import static com.project_esoterica.esoterica.core.systems.rendering.RenderManager.DELAYED_RENDER;
import static com.project_esoterica.esoterica.core.systems.rendering.RenderUtilities.renderTriangle;

public class FallingStarRenderer extends EntityRenderer<FallingEntity> {

    private static final ResourceLocation STAR_LOCATION = EsotericaHelper.prefix("textures/block/test.png");
    public static final RenderType RENDER_TYPE = RenderTypes.createGlowingTextureTrianglesRenderType(STAR_LOCATION);

    public FallingStarRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(FallingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        Minecraft minecraft = Minecraft.getInstance();
        float cameraX = minecraft.gameRenderer.getMainCamera().getXRot();
        float cameraY = Math.abs(minecraft.gameRenderer.getMainCamera().getYRot());
        VertexConsumer vertexConsumer = DELAYED_RENDER.getBuffer(RENDER_TYPE);

        //poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180f));

        Shaders.getMetallicNoiseShader().get().safeGetUniform("Intensity").set(10f);
        Shaders.getMetallicNoiseShader().get().safeGetUniform("Size").set(4.0f);
        Shaders.getMetallicNoiseShader().get().safeGetUniform("Speed").set(1000f);

        poseStack.mulPose(entity.getDirection().getOpposite().getRotation());
        //TODO: redo this shit
        float direction = (Mth.floor(cameraY / 90.0f)) & 3;
        float rotation = direction <= 1 ? -cameraX : cameraX;
        poseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, rotation, 0)));

        int[] colors = new int[]{ 226, 176, 255, 255};
        poseStack.mulPose(Vector3f.YN.rotationDegrees(-90f));
        renderTriangle(vertexConsumer, poseStack, 1, 10, colors[0], colors[1], colors[2], colors[3]);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180f));
        renderTriangle(vertexConsumer, poseStack, 1, 10, colors[0], colors[1], colors[2], colors[3]);

        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(FallingEntity p_114482_) {
        return STAR_LOCATION;
    }
}
