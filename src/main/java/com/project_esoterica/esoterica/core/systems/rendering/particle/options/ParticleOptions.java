package com.project_esoterica.esoterica.core.systems.rendering.particle.options;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;

import java.awt.*;

public class ParticleOptions implements net.minecraft.core.particles.ParticleOptions {
    public float r1 = 1, g1 = 1, b1 = 1, a1 = 1, r2 = 1, g2 = 1, b2 = 1, a2 = 0;
    public float scale1 = 1, scale2 = 0;
    public int lifetime = 20;
    public float startingSpin = 0;
    public float spin = 0;
    public boolean gravity = false;
    public boolean noClip = false;
    public float colorCurveMultiplier = 1f;

    public static Codec<ParticleOptions> codecFor(ParticleType<?> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("r1").forGetter(d -> d.r1),
                Codec.FLOAT.fieldOf("g1").forGetter(d -> d.g1),
                Codec.FLOAT.fieldOf("b1").forGetter(d -> d.b1),
                Codec.FLOAT.fieldOf("a1").forGetter(d -> d.a1),
                Codec.FLOAT.fieldOf("r2").forGetter(d -> d.r2),
                Codec.FLOAT.fieldOf("g2").forGetter(d -> d.g2),
                Codec.FLOAT.fieldOf("b2").forGetter(d -> d.b2),
                Codec.FLOAT.fieldOf("a2").forGetter(d -> d.a2),
                Codec.FLOAT.fieldOf("scale1").forGetter(d -> d.scale1),
                Codec.FLOAT.fieldOf("scale2").forGetter(d -> d.scale2),
                Codec.INT.fieldOf("lifetime").forGetter(d -> d.lifetime),
                Codec.FLOAT.fieldOf("startingSpin").forGetter(d -> d.startingSpin),
                Codec.FLOAT.fieldOf("spin").forGetter(d -> d.spin),
                Codec.BOOL.fieldOf("gravity").forGetter(d -> d.gravity),
                Codec.BOOL.fieldOf("noClip").forGetter(d -> d.noClip),
                Codec.FLOAT.fieldOf("colorCurveMultiplier").forGetter(d -> d.colorCurveMultiplier)
        ).apply(instance, (r1, g1, b1, a1, r2, g2, b2, a2, scale1, scale2,
                           lifetime, spin, startingSpin, gravity, noClip, colorCurveMultiplier) -> {
            ParticleOptions data = new ParticleOptions(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.startingSpin = startingSpin;
            data.spin = spin;
            data.gravity = gravity;
            data.noClip = noClip;
            data.colorCurveMultiplier = colorCurveMultiplier;
            return data;
        }));
    }

    ParticleType<?> type;

    public ParticleOptions(ParticleType<?> type) {
        this.type = type;
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat(r1).writeFloat(g1).writeFloat(b1).writeFloat(a1);
        buffer.writeFloat(r2).writeFloat(g2).writeFloat(b2).writeFloat(a2);
        buffer.writeFloat(scale1).writeFloat(scale2);
        buffer.writeInt(lifetime);
        buffer.writeFloat(startingSpin);
        buffer.writeFloat(spin);
        buffer.writeBoolean(gravity);
        buffer.writeBoolean(noClip);
        buffer.writeFloat(colorCurveMultiplier);
    }

    @Override
    public String writeToString() {
        return Registry.PARTICLE_TYPE.getKey(this.getType()) + " " + r1 + " " + g1 + " " + b1 + " " + a1 + " " + r2 + " " + g2 + " " + b2 + " " + a2 + " " + scale1 + " " + scale2 + " " + lifetime + " " + startingSpin + " " + spin + " " + gravity + " " + noClip + " " + colorCurveMultiplier;
    }

    public void setColor(float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.a1 = a1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.a2 = a2;
    }

    public void setColor(float r1, float g1, float b1, float r2, float g2, float b2) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
    }

    public void setColor(Color color1, Color color2) {
        this.r1 = color1.getRed() / 255f;
        this.g1 = color1.getBlue() / 255f;
        this.b1 = color1.getBlue() / 255f;
        this.r2 = color2.getRed() / 255f;
        this.g2 = color2.getBlue() / 255f;
        this.b2 = color2.getBlue() / 255f;
    }

    public static final Deserializer<ParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public ParticleOptions fromCommand(ParticleType<ParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float r1 = reader.readFloat();
            reader.expect(' ');
            float g1 = reader.readFloat();
            reader.expect(' ');
            float b1 = reader.readFloat();
            reader.expect(' ');
            float a1 = reader.readFloat();
            reader.expect(' ');
            float r2 = reader.readFloat();
            reader.expect(' ');
            float g2 = reader.readFloat();
            reader.expect(' ');
            float b2 = reader.readFloat();
            reader.expect(' ');
            float a2 = reader.readFloat();
            reader.expect(' ');
            float scale1 = reader.readFloat();
            reader.expect(' ');
            float scale2 = reader.readFloat();
            reader.expect(' ');
            int lifetime = reader.readInt();
            reader.expect(' ');
            float startingSpin = reader.readFloat();
            reader.expect(' ');
            float spin = reader.readFloat();
            reader.expect(' ');
            boolean gravity = reader.readBoolean();
            reader.expect(' ');
            boolean noClip = reader.readBoolean();
            reader.expect(' ');
            float colorCurveMultiplier = reader.readFloat();
            ParticleOptions data = new ParticleOptions(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.startingSpin = startingSpin;
            data.spin = spin;
            data.gravity = gravity;
            data.noClip = noClip;
            data.colorCurveMultiplier = colorCurveMultiplier;
            return data;
        }

        @Override
        public ParticleOptions fromNetwork(ParticleType<ParticleOptions> type, FriendlyByteBuf buf) {
            float r1 = buf.readFloat();
            float g1 = buf.readFloat();
            float b1 = buf.readFloat();
            float a1 = buf.readFloat();
            float r2 = buf.readFloat();
            float g2 = buf.readFloat();
            float b2 = buf.readFloat();
            float a2 = buf.readFloat();
            float scale1 = buf.readFloat();
            float scale2 = buf.readFloat();
            int lifetime = buf.readInt();
            float startingSpin = buf.readFloat();
            float spin = buf.readFloat();
            boolean gravity = buf.readBoolean();
            boolean noClip = buf.readBoolean();
            float colorCurveMultiplier = buf.readFloat();
            ParticleOptions data = new ParticleOptions(type);
            data.r1 = r1;
            data.g1 = g1;
            data.b1 = b1;
            data.a1 = a1;
            data.r2 = r2;
            data.g2 = g2;
            data.b2 = b2;
            data.a2 = a2;
            data.scale1 = scale1;
            data.scale2 = scale2;
            data.lifetime = lifetime;
            data.startingSpin = startingSpin;
            data.spin = spin;
            data.gravity = gravity;
            data.noClip = noClip;
            data.colorCurveMultiplier = colorCurveMultiplier;
            return data;
        }
    };
}