package com.sammy.fufo.core.setup.content.worldgen;

import com.sammy.fufo.common.world.gen.MeteoriteFeature;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.sammy.fufo.FufoMod.FUFO;
import static net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration.INSTANCE;

@Mod.EventBusSubscriber(modid= FUFO, bus= Mod.EventBusSubscriber.Bus.MOD)
public class FeatureRegistry {

    public static final DeferredRegister<Feature<?>> FEATURE_TYPES = DeferredRegister.create(ForgeRegistries.FEATURES, FUFO);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> METEORITE = FEATURE_TYPES.register("meteorite", MeteoriteFeature::new);

    public static final class ConfiguredFeatures {
        public static final Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> METEORITE = FeatureUtils.register("meteorite", FeatureRegistry.METEORITE.get(), INSTANCE);

    }

    public static final class PlacedFeatures {
    }
}