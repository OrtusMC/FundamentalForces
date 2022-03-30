package com.sammy.fundamental_forces.common.capability;

import com.sammy.fundamental_forces.common.packets.SyncEntityCapabilityDataPacket;
import com.sammy.fundamental_forces.core.helper.DataHelper;
import com.sammy.fundamental_forces.core.systems.capability.SimpleCapability;
import com.sammy.fundamental_forces.core.systems.capability.SimpleCapabilityProvider;
import com.sammy.fundamental_forces.core.systems.meteorfire.FireEffectInstance;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.network.PacketDistributor;

import static com.sammy.fundamental_forces.core.setup.server.PacketRegistry.INSTANCE;

public class EntityDataCapability implements SimpleCapability {

    public static Capability<EntityDataCapability> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public FireEffectInstance fireEffectInstance;

    public EntityDataCapability() {

    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(EntityDataCapability.class);
    }

    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        final EntityDataCapability capability = new EntityDataCapability();
        event.addCapability(DataHelper.prefix("entity_data"), new SimpleCapabilityProvider<>(EntityDataCapability.CAPABILITY, () -> capability));
    }

    public static void syncEntityCapability(PlayerEvent.StartTracking event) {
        if (event.getEntity().level instanceof ServerLevel) {
            EntityDataCapability.syncTracking(event.getEntityLiving());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        if (fireEffectInstance != null) {
            CompoundTag fireTag = new CompoundTag();
            fireEffectInstance.serializeNBT(fireTag);
            tag.put("fireEffect", fireTag);
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("fireEffect")) {
            fireEffectInstance = FireEffectInstance.deserializeNBT(tag.getCompound("fireEffect"));
        }
    }

    public static void syncTrackingAndSelf(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity));
    }

    public static void syncTracking(Entity entity) {
        sync(entity, PacketDistributor.TRACKING_ENTITY.with(() -> entity));
    }

    public static void sync(Entity entity, PacketDistributor.PacketTarget target) {
        getCapability(entity).ifPresent(c -> INSTANCE.send(target, new SyncEntityCapabilityDataPacket(entity.getId(), c.serializeNBT())));
    }

    public static LazyOptional<EntityDataCapability> getCapability(Entity entity) {
        return entity.getCapability(CAPABILITY);
    }
}