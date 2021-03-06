package com.sammy.fufo.core.eventhandlers;

import com.sammy.fufo.common.capability.*;
import com.sammy.fufo.core.systems.logistics.PipeBuilderAssistant;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupEvents {

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        FufoWorldDataCapability.registerCapabilities(event);
        FufoChunkDataCapability.registerCapabilities(event);
        FufoEntityDataCapability.registerCapabilities(event);
        FufoItemStackCapability.registerCapabilities(event);
        FufoPlayerDataCapability.registerCapabilities(event);
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        PipeBuilderAssistant.registerPlacementAssistant(event);
    }
}
