package com.project_esoterica.esoterica.core.setup.client;

import com.project_esoterica.esoterica.EsotericaMod;
import com.project_esoterica.esoterica.client.particles.wisp.WispParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry
{
    public static DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EsotericaMod.MODID);

    public static RegistryObject<WispParticleType> WISP_PARTICLE = PARTICLES.register("wisp_particle", WispParticleType::new);
    
    @SubscribeEvent
    public static void registerParticleFactory(ParticleFactoryRegisterEvent event)
    {
        Minecraft.getInstance().particleEngine.register(WISP_PARTICLE.get(), WispParticleType.Factory::new);
    }
}