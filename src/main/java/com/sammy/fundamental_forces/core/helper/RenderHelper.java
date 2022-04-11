package com.sammy.fundamental_forces.core.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.sammy.fundamental_forces.core.systems.rendering.ShaderHolder;
import com.sammy.fundamental_forces.core.systems.rendering.TrailNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.sammy.fundamental_forces.core.helper.MathHelper.invDist;
import static com.sammy.fundamental_forces.core.systems.rendering.TrailNode.renderTrailNodes;

public class RenderHelper {
    public static final int FULL_BRIGHT = 15728880;

    public static ShaderInstance getShader(RenderType type) {
        if (type instanceof RenderType.CompositeRenderType compositeRenderType) {
            Optional<Supplier<ShaderInstance>> shader = compositeRenderType.state.shaderState.shader;
            if (shader.isPresent()) {
                return shader.get().get();
            }
        }
        return null;
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, shader, x, y, w, h, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, shader, x, y, w, h, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float canvasSize) {
        innerBlit(stack, shader, x, y, w, h, u / canvasSize, v / canvasSize, (float) (x + w) / canvasSize, (float) (y + h) / canvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh, float canvasSize) {
        innerBlit(stack, shader, x, y, w, h, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, shader, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, shader, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float canvasSize) {
        innerBlit(stack, shader, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
    }

    public static void blit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float canvasSize) {
        innerBlit(stack, shader, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
    }

    public static void innerBlit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh) {
        Matrix4f last = stack.last().pose();
        RenderSystem.setShader(shader.getInstance());
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).color(r, g, b, a).uv(u, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).color(r, g, b, a).uv(u + uw, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).color(r, g, b, a).uv(u + uw, v).endVertex();
        bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    public static void innerBlit(PoseStack stack, ShaderHolder shader, int x, int y, double w, double h, float u, float v, float uw, float vh) {
        Matrix4f last = stack.last().pose();
        RenderSystem.setShader(shader.getInstance());
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).uv(u, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).uv(u + uw, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).uv(u + uw, v).endVertex();
        bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float u, float v, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, x, y, w, h, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, x, y, w, h, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float u, float v, float canvasSize) {
        innerBlit(stack, x, y, w, h, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float u, float v, float uw, float vh, float canvasSize) {
        innerBlit(stack, x, y, w, h, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, (float) w / xCanvasSize, (float) h / yCanvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float xCanvasSize, float yCanvasSize) {
        innerBlit(stack, x, y, w, h, r, g, b, a, u / xCanvasSize, v / yCanvasSize, uw / xCanvasSize, vh / yCanvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float canvasSize) {
        innerBlit(stack, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, (float) w / canvasSize, (float) h / canvasSize);
    }

    public static void blit(PoseStack stack, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh, float canvasSize) {
        innerBlit(stack, x, y, w, h, r, g, b, a, u / canvasSize, v / canvasSize, uw / canvasSize, vh / canvasSize);
    }

    public static void innerBlit(PoseStack stack, int x, int y, double w, double h, float r, float g, float b, float a, float u, float v, float uw, float vh) {
        Matrix4f last = stack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).color(r, g, b, a).uv(u, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).color(r, g, b, a).uv(u + uw, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).color(r, g, b, a).uv(u + uw, v).endVertex();
        bufferbuilder.vertex(last, (float) x, (float) y, 0).color(r, g, b, a).uv(u, v).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    public static void innerBlit(PoseStack stack, int x, int y, double w, double h, float u, float v, float uw, float vh) {
        Matrix4f last = stack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(last, (float) x, (float) y + (float) h, 0).uv(u, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y + (float) h, 0).uv(u + uw, v + vh).endVertex();
        bufferbuilder.vertex(last, (float) x + (float) w, (float) y, 0).uv(u + uw, v).endVertex();
        bufferbuilder.vertex(last, (float) x, (float) y, 0).uv(u, v).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    public static VertexBuilder create() {
        return new VertexBuilder();
    }

    public static class VertexBuilder {
        float r = 1, g = 1, b = 1, a = 1;
        float xOffset = 0, yOffset = 0, zOffset = 0;
        int light = FULL_BRIGHT;
        float u0 = 0, v0 = 0, u1 = 1, v1 = 1;


        public VertexBuilder setColor(Color color) {
            return setColor(color.getRed(), color.getGreen(), color.getBlue());
        }

        public VertexBuilder setColor(Color color, float a) {
            return setColor(color).setAlpha(a);
        }

        public VertexBuilder setColor(float r, float g, float b, float a) {
            return setColor(r, g, b).setAlpha(a);
        }

        public VertexBuilder setColor(float r, float g, float b) {
            this.r = r / 255f;
            this.g = g / 255f;
            this.b = b / 255f;
            return this;
        }

        public VertexBuilder setAlpha(float a) {
            this.a = a;
            return this;
        }

        public VertexBuilder setOffset(float xOffset, float yOffset, float zOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            return this;
        }

        public VertexBuilder setLight(int light) {
            this.light = light;
            return this;
        }

        public VertexBuilder setUV(float u0, float v0, float u1, float v1) {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }

        public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, PoseStack stack, float size) {
            return renderTriangle(vertexConsumer, stack, size, size);
        }

        public VertexBuilder renderTriangle(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Matrix4f last = stack.last().pose();

            vertexPosColorUVLight(vertexConsumer, last, -width, -height, 0, r, g, b, a, 0, 1, light);
            vertexPosColorUVLight(vertexConsumer, last, width, -height, 0, r, g, b, a, 1, 1, light);
            vertexPosColorUVLight(vertexConsumer, last, 0, height, 0, r, g, b, a, 0.5f, 0, light);
            return this;
        }

        public VertexBuilder renderTrail(VertexConsumer vertexConsumer, PoseStack stack, int packedLight, List<Vector4f> poss, Function<Float, Float> widthFunc) {
            if (poss.size() < 3) {
                return this;
            }
            PoseStack.Pose stackEntry = stack.last();
            Matrix4f pose = stackEntry.pose();
            Matrix3f normal = stackEntry.normal();

            for (Vector4f pos : poss) {
                pos.add(xOffset, yOffset, zOffset, 0);
                pos.transform(pose);
            }
            int count = poss.size() - 1;
            TrailNode[] nodes = new TrailNode[count];
            float increment = 1.0F / (count - 1);
            for (int i = 0; i < count; ++i) {
                Vector4f start = poss.get(i);
                Vector4f end = poss.get(i + 1);
                float width = widthFunc.apply(increment * i);
                nodes[i] = new TrailNode(mid(start, end), axialPerp(start, end, width), width);
            }
            renderTrailNodes(normal, vertexConsumer, packedLight, nodes, r, g, b, a, u0, v0, u1, v1);
            return this;
        }

        public VertexBuilder renderBeam(VertexConsumer vertexConsumer, PoseStack stack, Vec3 start, Vec3 end, float width) {
            Minecraft minecraft = Minecraft.getInstance();
            start.add(xOffset, yOffset, zOffset);
            end.add(xOffset, yOffset, zOffset);
            stack.translate(-start.x, -start.y, -start.z);
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply(width / 2f, width / 2f, width / 2f);
            Matrix4f last = stack.last().pose();
            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
            vertexPosColorUVLight(vertexConsumer, last, (float) positions[0].x, (float) positions[0].y, (float) positions[0].z, r, g, b, a, u0, v1, light);
            vertexPosColorUVLight(vertexConsumer, last, (float) positions[1].x, (float) positions[1].y, (float) positions[1].z, r, g, b, a, u1, v1, light);
            vertexPosColorUVLight(vertexConsumer, last, (float) positions[2].x, (float) positions[2].y, (float) positions[2].z, r, g, b, a, u1, v0, light);
            vertexPosColorUVLight(vertexConsumer, last, (float) positions[3].x, (float) positions[3].y, (float) positions[3].z, r, g, b, a, u0, v0, light);
            stack.translate(start.x, start.y, start.z);
            return this;
        }

        public VertexBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, float size) {
            return renderQuad(vertexConsumer, stack, size, size);
        }

        public VertexBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Vector3f[] positions = new Vector3f[]{new Vector3f(-width, -height, 0), new Vector3f(width, -height, 0), new Vector3f(width, height, 0), new Vector3f(-width, height, 0)};
            return renderQuad(vertexConsumer, stack, positions, width, height);
        }

        public VertexBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, Vector3f[] positions, float size) {
            return renderQuad(vertexConsumer, stack, positions, size, size);
        }

        public VertexBuilder renderQuad(VertexConsumer vertexConsumer, PoseStack stack, Vector3f[] positions, float width, float height) {
            Matrix4f last = stack.last().pose();
            stack.translate(xOffset, yOffset, zOffset);
            for (Vector3f position : positions) {
                position.mul(width, height, width);
            }
            vertexPosColorUVLight(vertexConsumer, last, positions[0].x(), positions[0].y(), positions[0].z(), r, g, b, a, u0, v1, light);
            vertexPosColorUVLight(vertexConsumer, last, positions[1].x(), positions[1].y(), positions[1].z(), r, g, b, a, u1, v1, light);
            vertexPosColorUVLight(vertexConsumer, last, positions[2].x(), positions[2].y(), positions[2].z(), r, g, b, a, u1, v0, light);
            vertexPosColorUVLight(vertexConsumer, last, positions[3].x(), positions[3].y(), positions[3].z(), r, g, b, a, u0, v0, light);
            stack.translate(-xOffset, -yOffset, -zOffset);
            return this;
        }

        public VertexBuilder renderSphere(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, int lats) {
            Matrix4f last = stack.last().pose();
            float startU = 0;
            float startV = 0;
            float endU = Mth.PI * 2;
            float endV = Mth.PI;
            float stepU = (endU - startU) / longs;
            float stepV = (endV - startV) / lats;
            for (int i = 0; i < longs; ++i) {
                // U-points
                for (int j = 0; j < lats; ++j) {
                    // V-points
                    float u = i * stepU + startU;
                    float v = j * stepV + startV;
                    float un = (i + 1 == longs) ? endU : (i + 1) * stepU + startU;
                    float vn = (j + 1 == lats) ? endV : (j + 1) * stepV + startV;
                    Vector3f p0 = parametricSphere(u, v, radius);
                    Vector3f p1 = parametricSphere(u, vn, radius);
                    Vector3f p2 = parametricSphere(un, v, radius);
                    Vector3f p3 = parametricSphere(un, vn, radius);

                    float textureU = u / endU * radius;
                    float textureV = v / endV * radius;
                    float textureUN = un / endU * radius;
                    float textureVN = vn / endV * radius;
                    vertexPosColorUVLight(vertexConsumer, last, p0.x(), p0.y(), p0.z(), r, g, b, a, textureU, textureV, light);
                    vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
                    vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);

                    vertexPosColorUVLight(vertexConsumer, last, p3.x(), p3.y(), p3.z(), r, g, b, a, textureUN, textureVN, light);
                    vertexPosColorUVLight(vertexConsumer, last, p1.x(), p1.y(), p1.z(), r, g, b, a, textureU, textureVN, light);
                    vertexPosColorUVLight(vertexConsumer, last, p2.x(), p2.y(), p2.z(), r, g, b, a, textureUN, textureV, light);
                }
            }
            return this;
        }
    }

    public static void vertexPos(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z) {
        vertexConsumer.vertex(last, x, y, z).endVertex();
    }

    public static void vertexPosUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(last, x, y, z).uv(u, v).endVertex();
    }

    public static void vertexPosUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
        vertexConsumer.vertex(last, x, y, z).uv(u, v).uv2(light).endVertex();
    }

    public static void vertexPosColor(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).endVertex();
    }

    public static void vertexPosColorUV(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).endVertex();
    }

    public static void vertexPosColorUVLight(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float r, float g, float b, float a, float u, float v, int light) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
    }

    public static Vector3f parametricSphere(float u, float v, float r) {
        return new Vector3f(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
    }


    public static Vec2 axialPerp(Vector4f start, Vector4f end, float width) {
        width *= 0.5F;
        float ratio = end.z() / start.z();
        float x = end.x() - start.x() * ratio;
        float y = end.y() - start.y() * ratio;
        float normalize = invDist(x, y) * width;
        x *= normalize;
        y *= normalize;
        return new Vec2(-y, x);
    }

    public static Vec2 axialPerp(Vector3f start, Vector3f end, float width) {

        width *= 0.5F;
        float ratio = end.z() / start.z();
        float x = end.x() - start.x() * ratio;
        float y = end.y() - start.y() * ratio;
        float normalize = invDist(x, y) * width;
        x *= normalize;
        y *= normalize;
        return new Vec2(-y, x);
    }

    public static Vector4f mid(Vector4f a, Vector4f b) {
        return new Vector4f((a.x() + b.x()) * 0.5F, (a.y() + b.y()) * 0.5F, (a.z() + b.z()) * 0.5F, (a.w() + b.w()) * 0.5F);
    }
}