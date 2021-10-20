package com.project_esoterica.esoterica.core.systems.ancientparticlecode.rendertypes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.project_esoterica.esoterica.core.registry.misc.ShaderRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.lwjgl.opengl.GL11;

public class SpriteParticleRenderType implements ParticleRenderType {
    public static final SpriteParticleRenderType INSTANCE = new SpriteParticleRenderType();

    public void begin(BufferBuilder builder, TextureManager manager) {
        RenderSystem.depthMask(false);
        RenderSystem.setShader(ShaderRegistry.getAdditiveParticleShader());
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    public void end(Tesselator p_107458_) {
        p_107458_.end();
    }

    public String toString() {
        return "PARTICLE_SHEET_ADDITIVE";
    }
}