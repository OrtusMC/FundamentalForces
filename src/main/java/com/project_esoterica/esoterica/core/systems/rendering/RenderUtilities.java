package com.project_esoterica.esoterica.core.systems.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.project_esoterica.esoterica.core.systems.rendering.particle.options.ParticleOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.awt.*;
import java.util.Random;

public class RenderUtilities {
    public static final int FULL_BRIGHT = 15728880;

    public static VertexBuilder create()
    {
        return new VertexBuilder();
    }

    public static class VertexBuilder
    {
        int r=255, g=255, b=255, a=255;
        float xOffset=0, yOffset=0, zOffset=0;
        int light=FULL_BRIGHT;
        float u0=0, v0=0, u1=1, v1=1;


        public VertexBuilder setColor(Color color)
        {
            this.r = color.getRed();
            this.g = color.getGreen();
            this.b = color.getBlue();
            return this;
        }
        public VertexBuilder setColor(int r, int g, int b, int a)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            return this;
        }
        public VertexBuilder setColor(int r, int g, int b)
        {
            this.r = r;
            this.g = g;
            this.b = b;
            return this;
        }
        public VertexBuilder setOffset(float xOffset, float yOffset, float zOffset)
        {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            return this;
        }
        public VertexBuilder setLight(int light)
        {
            this.light = light;
            return this;
        }
        public VertexBuilder setUV(float u0, float v0, float u1, float v1)
        {
            this.u0 = u0;
            this.v0 = v0;
            this.u1 = u1;
            this.v1 = v1;
            return this;
        }
        public void renderTriangle(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Matrix4f last = stack.last().pose();

            vertex(vertexConsumer, last, -width, -height, 0, r,g,b,a, 0, 1, light);
            vertex(vertexConsumer, last, width, -height, 0, r,g,b,a, 1, 1, light);
            vertex(vertexConsumer, last, 0, height, 0, r,g,b,a, 0.5f, 0, light);
        }
        public void renderBeam(VertexConsumer vertexConsumer, PoseStack stack, Vec3 start, Vec3 end, float width)
        {
            Minecraft minecraft = Minecraft.getInstance();
            start.add(xOffset, yOffset, zOffset);
            end.add(xOffset, yOffset, zOffset);
            stack.translate(-start.x, -start.y, -start.z);
            Vec3 cameraPosition = minecraft.getBlockEntityRenderDispatcher().camera.getPosition();
            Vec3 delta = end.subtract(start);
            Vec3 normal = start.subtract(cameraPosition).cross(delta).normalize().multiply(width/2f,width/2f,width/2f);
            Matrix4f last = stack.last().pose();
            Vec3[] positions = new Vec3[]{start.subtract(normal), start.add(normal), end.add(normal), end.subtract(normal)};
            vertex(vertexConsumer, last, (float)positions[0].x,(float)positions[0].y,(float)positions[0].z,r,g,b,a,u0,v1, light);
            vertex(vertexConsumer, last, (float)positions[1].x,(float)positions[1].y,(float)positions[1].z,r,g,b,a,u1,v1, light);
            vertex(vertexConsumer, last, (float)positions[2].x,(float)positions[2].y,(float)positions[2].z,r,g,b,a,u1,v0, light);
            vertex(vertexConsumer, last, (float)positions[3].x,(float)positions[3].y,(float)positions[3].z,r,g,b,a,u0,v0, light);
            stack.translate(start.x, start.y, start.z);
        }
        public void renderQuad(VertexConsumer vertexConsumer, PoseStack stack, float width, float height) {
            Matrix4f last = stack.last().pose();
            stack.translate(xOffset, yOffset, zOffset);
            Vec3[] positions = new Vec3[]{new Vec3(-width, -height, 0), new Vec3(width, -height, 0), new Vec3(width, height, 0), new Vec3(-width, height, 0)};
            vertex(vertexConsumer, last, (float)positions[0].x,(float)positions[0].y,(float)positions[0].z, r,g,b,a, u0, v1, light);
            vertex(vertexConsumer, last, (float)positions[1].x,(float)positions[1].y,(float)positions[1].z, r,g,b,a, u1, v1, light);
            vertex(vertexConsumer, last, (float)positions[2].x,(float)positions[2].y,(float)positions[2].z, r,g,b,a, u1, v0, light);
            vertex(vertexConsumer, last, (float)positions[3].x,(float)positions[3].y,(float)positions[3].z, r,g,b,a, u0, v0, light);
            stack.translate(-xOffset, -yOffset, -zOffset);
        }
        public void renderSphere(VertexConsumer vertexConsumer, PoseStack stack, float radius, int longs, int lats) {
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

                    float textureU = u/endU*radius;
                    float textureV = v/endV*radius;
                    float textureUN = un/endU*radius;
                    float textureVN = vn/endV*radius;
                    vertex(vertexConsumer,last, p0.x(), p0.y(), p0.z(), r,g,b,a,textureU,textureV,light);
                    vertex(vertexConsumer,last, p2.x(), p2.y(), p2.z(), r,g,b,a,textureUN,textureV,light);
                    vertex(vertexConsumer,last, p1.x(), p1.y(), p1.z(), r,g,b,a,textureU,textureVN,light);

                    vertex(vertexConsumer,last, p3.x(), p3.y(), p3.z(), r,g,b,a,textureUN,textureVN,light);
                    vertex(vertexConsumer,last, p1.x(), p1.y(), p1.z(), r,g,b,a,textureU,textureVN,light);
                    vertex(vertexConsumer,last, p2.x(), p2.y(), p2.z(), r,g,b,a,textureUN,textureV,light);
                }
            }
        }
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z) {
        vertexConsumer.vertex(last, x, y, z).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v) {
        vertexConsumer.vertex(last, x, y, z).uv(u, v).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, float u, float v, int light) {
        vertexConsumer.vertex(last, x, y, z).uv(u, v).uv2(light).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, int r, int g, int b, int a) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, int r, int g, int b, int a, float u, float v) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f last, float x, float y, float z, int r, int g, int b, int a, float u, float v, int light) {
        vertexConsumer.vertex(last, x, y, z).color(r, g, b, a).uv(u, v).uv2(light).endVertex();
    }

    public static Vector3f parametricSphere(float u, float v, float r) {
        return new Vector3f(Mth.cos(u) * Mth.sin(v) * r, Mth.cos(v) * r, Mth.sin(u) * Mth.sin(v) * r);
    }

    public static ParticleBuilder create(ParticleType<?> type)
    {
        return new ParticleBuilder(type);
    }

    public static ParticleBuilder create(RegistryObject<?> type)
    {
        return new ParticleBuilder((ParticleType<?>) type.get());
    }

    public static class ParticleBuilder
    {
        static Random random = new Random();

        ParticleType<?> type;
        ParticleOptions data;
        double vx = 0, vy = 0, vz = 0;
        double dx = 0, dy = 0, dz = 0;
        double maxXSpeed = 0, maxYSpeed = 0, maxZSpeed = 0;
        double maxXDist = 0, maxYDist = 0, maxZDist = 0;

        protected ParticleBuilder(ParticleType<?> type)
        {
            this.type = type;
            this.data = new ParticleOptions(type);
        }

        public ParticleBuilder setColor(float r, float g, float b)
        {
            setColor(r, g, b, data.a1, r, g, b, data.a2);
            return this;
        }

        public ParticleBuilder setColor(float r, float g, float b, float a)
        {
            setColor(r, g, b, a, r, g, b, a);
            return this;
        }

        public ParticleBuilder setColor(float r, float g, float b, float a1, float a2)
        {
            setColor(r, g, b, a1, r, g, b, a2);
            return this;
        }

        public ParticleBuilder setColor(float r1, float g1, float b1, float r2, float g2, float b2)
        {
            setColor(r1, g1, b1, data.a1, r2, g2, b2, data.a2);
            return this;
        }

        public ParticleBuilder setColor(float r1, float g1, float b1, float r2, float g2, float b2, float a)
        {
            setColor(r1, g1, b1, a, r2, g2, b2, a);
            return this;
        }

        public ParticleBuilder setColor(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2)
        {
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            return this;
        }

        public ParticleBuilder setColor(Color c1, Color c2)
        {
            data.r1 = c1.getRed()/255f;
            data.g1 = c1.getGreen()/255f;
            data.b1 = c1.getBlue()/255f;
            data.r2 = c2.getRed()/255f;
            data.g2 = c2.getGreen()/255f;
            data.b2 = c2.getBlue()/255f;
            return this;
        }
        public ParticleBuilder setColorCurveMultiplier(float colorCurveMultiplier)
        {
            data.colorCurveMultiplier = colorCurveMultiplier;
            return this;
        }
        public ParticleBuilder setAlpha(float a)
        {
            setAlpha(a, a);
            return this;
        }

        public ParticleBuilder setAlpha(float a1, float a2)
        {
            data.a1 = a1;
            data.a2 = a2;
            return this;
        }

        public ParticleBuilder setScale(float scale)
        {
            setScale(scale, scale);
            return this;
        }

        public ParticleBuilder setScale(float scale1, float scale2)
        {
            data.scale1 = scale1;
            data.scale2 = scale2;
            return this;
        }

        public ParticleBuilder enableGravity()
        {
            data.gravity = true;
            return this;
        }

        public ParticleBuilder disableGravity()
        {
            data.gravity = false;
            return this;
        }
        public ParticleBuilder enableNoClip()
        {
            data.noClip = true;
            return this;
        }

        public ParticleBuilder disableNoClip()
        {
            data.noClip = false;
            return this;
        }

        public ParticleBuilder setSpin(float angularVelocity)
        {
            data.spin = angularVelocity;
            return this;
        }

        public ParticleBuilder setLifetime(int lifetime)
        {
            data.lifetime = lifetime;
            return this;
        }

        public ParticleBuilder randomVelocity(double maxSpeed)
        {
            randomVelocity(maxSpeed, maxSpeed, maxSpeed);
            return this;
        }

        public ParticleBuilder randomVelocity(double maxHSpeed, double maxVSpeed)
        {
            randomVelocity(maxHSpeed, maxVSpeed, maxHSpeed);
            return this;
        }

        public ParticleBuilder randomVelocity(double maxXSpeed, double maxYSpeed, double maxZSpeed)
        {
            this.maxXSpeed = maxXSpeed;
            this.maxYSpeed = maxYSpeed;
            this.maxZSpeed = maxZSpeed;
            return this;
        }

        public ParticleBuilder addVelocity(double vx, double vy, double vz)
        {
            this.vx += vx;
            this.vy += vy;
            this.vz += vz;
            return this;
        }

        public ParticleBuilder setVelocity(double vx, double vy, double vz)
        {
            this.vx = vx;
            this.vy = vy;
            this.vz = vz;
            return this;
        }

        public ParticleBuilder randomOffset(double maxDistance)
        {
            randomOffset(maxDistance, maxDistance, maxDistance);
            return this;
        }

        public ParticleBuilder randomOffset(double maxHDist, double maxVDist)
        {
            randomOffset(maxHDist, maxVDist, maxHDist);
            return this;
        }

        public ParticleBuilder randomOffset(double maxXDist, double maxYDist, double maxZDist)
        {
            this.maxXDist = maxXDist;
            this.maxYDist = maxYDist;
            this.maxZDist = maxZDist;
            return this;
        }

        public ParticleBuilder spawnCircle(Level level, double x, double y, double z, double distance, double currentCount, double totalCount)
        {
            double xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
            double theta = (Math.PI * 2) / totalCount;
            double finalAngle = (currentCount / totalCount) + (theta * currentCount);
            double dx2 = (distance * Math.cos(finalAngle));
            double dz2 = (distance * Math.sin(finalAngle));

            Vector3d vector2f = new Vector3d(dx2,0,dz2);
            this.vx = vector2f.x * xSpeed;
            this.vz = vector2f.z * zSpeed;

            double yaw2 = random.nextFloat() * Math.PI * 2, pitch2 = random.nextFloat() * Math.PI - Math.PI / 2, xDist = random.nextFloat() * maxXDist, yDist = random.nextFloat() * maxYDist, zDist = random.nextFloat() * maxZDist;
            this.dx = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
            this.dy = Math.sin(pitch2) * yDist;
            this.dz = Math.cos(yaw2) * Math.cos(pitch2) * zDist;
            level.addParticle(data, x + dx + dx2, y + dy, z + dz + dz2, vx, ySpeed, vz);
            return this;
        }
        public ParticleBuilder spawn(Level level, double x, double y, double z)
        {
            double yaw = random.nextFloat() * Math.PI * 2, pitch = random.nextFloat() * Math.PI - Math.PI / 2, xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
            this.vx += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
            this.vy += Math.sin(pitch) * ySpeed;
            this.vz += Math.cos(yaw) * Math.cos(pitch) * zSpeed;
            double yaw2 = random.nextFloat() * Math.PI * 2, pitch2 = random.nextFloat() * Math.PI - Math.PI / 2, xDist = random.nextFloat() * maxXDist, yDist = random.nextFloat() * maxYDist, zDist = random.nextFloat() * maxZDist;
            this.dx = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
            this.dy = Math.sin(pitch2) * yDist;
            this.dz = Math.cos(yaw2) * Math.cos(pitch2) * zDist;

            level.addParticle(data, x + dx, y + dy, z + dz, vx, vy, vz);
            return this;
        }

        public ParticleBuilder evenlySpawnAtEdges(Level level, BlockPos pos)
        {
            for (Direction direction : Direction.values())
            {
                double yaw = random.nextFloat() * Math.PI * 2, pitch = random.nextFloat() * Math.PI - Math.PI / 2, xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
                this.vx += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
                this.vy += Math.sin(pitch) * ySpeed;
                this.vz += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

                Direction.Axis direction$axis = direction.getAxis();
                double d0 = 0.5625D;
                this.dx = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : (double) random.nextFloat();
                this.dy = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : (double) random.nextFloat();
                this.dz = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : (double) random.nextFloat();

                level.addParticle(data, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, vx, vy, vz);

            }return this;
        }
        public ParticleBuilder evenlySpawnAtEdges(Level level, BlockPos pos, Direction... directions)
        {
            for (Direction direction : directions)
            {
                double yaw = random.nextFloat() * Math.PI * 2, pitch = random.nextFloat() * Math.PI - Math.PI / 2, xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
                this.vx += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
                this.vy += Math.sin(pitch) * ySpeed;
                this.vz += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

                Direction.Axis direction$axis = direction.getAxis();
                double d0 = 0.5625D;
                this.dx = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : (double) random.nextFloat();
                this.dy = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : (double) random.nextFloat();
                this.dz = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : (double) random.nextFloat();

                level.addParticle(data, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, vx, vy, vz);

            }return this;
        }
        public ParticleBuilder spawnAtEdges(Level level, BlockPos pos)
        {
            Direction direction = Direction.values()[level.random.nextInt(Direction.values().length)];
            double yaw = random.nextFloat() * Math.PI * 2, pitch = random.nextFloat() * Math.PI - Math.PI / 2, xSpeed = random.nextFloat() * maxXSpeed, ySpeed = random.nextFloat() * maxYSpeed, zSpeed = random.nextFloat() * maxZSpeed;
            this.vx += Math.sin(yaw) * Math.cos(pitch) * xSpeed;
            this.vy += Math.sin(pitch) * ySpeed;
            this.vz += Math.cos(yaw) * Math.cos(pitch) * zSpeed;

            Direction.Axis direction$axis = direction.getAxis();
            double d0 = 0.5625D;
            this.dx = direction$axis == Direction.Axis.X ? 0.5D + d0 * (double) direction.getStepX() : (double) random.nextFloat();
            this.dy = direction$axis == Direction.Axis.Y ? 0.5D + d0 * (double) direction.getStepY() : (double) random.nextFloat();
            this.dz = direction$axis == Direction.Axis.Z ? 0.5D + d0 * (double) direction.getStepZ() : (double) random.nextFloat();

            level.addParticle(data, pos.getX() + dx, pos.getY() + dy, pos.getZ() + dz, vx, vy, vz);
            return this;
        }

        public ParticleBuilder repeat(Level level, double x, double y, double z, int n)
        {
            for (int i = 0; i < n; i++) spawn(level, x, y, z);
            return this;
        }
        public ParticleBuilder repeatEdges(Level level, BlockPos pos, int n)
        {
            for (int i = 0; i < n; i++) spawnAtEdges(level, pos);
            return this;
        }
        public ParticleBuilder evenlyRepeatEdges(Level level, BlockPos pos, int n)
        {
            for (int i = 0; i < n; i++) evenlySpawnAtEdges(level, pos);
            return this;
        }
        public ParticleBuilder evenlyRepeatEdges(Level level, BlockPos pos, int n, Direction... directions)
        {
            for (int i = 0; i < n; i++) evenlySpawnAtEdges(level, pos, directions);
            return this;
        }
        public ParticleBuilder repeatCircle(Level level, double x, double y, double z, double distance, int times)
        {
            for (int i = 0; i < times; i++) spawnCircle(level, x, y, z, distance,i, times);
            return this;
        }
    }
}