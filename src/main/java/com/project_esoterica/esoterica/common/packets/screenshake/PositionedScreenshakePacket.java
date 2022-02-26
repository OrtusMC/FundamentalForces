package com.project_esoterica.esoterica.common.packets.screenshake;

import com.project_esoterica.esoterica.core.systems.screenshake.PositionedScreenshakeInstance;
import com.project_esoterica.esoterica.core.handlers.ScreenshakeHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

public class PositionedScreenshakePacket {
    Vec3 position;
    public float falloffDistance;
    public float maxDistance;
    float intensity;
    float falloffTransformSpeed;
    int timeBeforeFastFalloff;
    float slowFalloff;
    float fastFalloff;

    public PositionedScreenshakePacket(Vec3 position, float falloffDistance, float maxDistance, float intensity, float falloffTransformSpeed, int timeBeforeFastFalloff, float slowFalloff, float fastFalloff) {
        this.position = position;
        this.falloffDistance = falloffDistance;
        this.maxDistance = maxDistance;
        this.intensity = intensity;
        this.falloffTransformSpeed = falloffTransformSpeed;
        this.timeBeforeFastFalloff = timeBeforeFastFalloff;
        this.slowFalloff = slowFalloff;
        this.fastFalloff = fastFalloff;
    }

    public static PositionedScreenshakePacket decode(FriendlyByteBuf buf) {
        return new PositionedScreenshakePacket(new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readFloat());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeDouble(position.x); buf.writeDouble(position.y); buf.writeDouble(position.z);
        buf.writeFloat(falloffDistance);
        buf.writeFloat(maxDistance);
        buf.writeFloat(intensity);
        buf.writeFloat(falloffTransformSpeed);
        buf.writeInt(timeBeforeFastFalloff);
        buf.writeFloat(slowFalloff);
        buf.writeFloat(fastFalloff);
    }

    public void execute(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ScreenshakeHandler.addScreenshake(new PositionedScreenshakeInstance(position,falloffDistance,maxDistance, intensity, falloffTransformSpeed, timeBeforeFastFalloff, slowFalloff, fastFalloff)));
        context.get().setPacketHandled(true);
    }

    public static void register(SimpleChannel instance, int index) {
        instance.registerMessage(index, PositionedScreenshakePacket.class, PositionedScreenshakePacket::encode, PositionedScreenshakePacket::decode, PositionedScreenshakePacket::execute);
    }
}
