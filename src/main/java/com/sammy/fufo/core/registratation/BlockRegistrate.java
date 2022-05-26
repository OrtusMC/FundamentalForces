package com.sammy.fufo.core.registratation;

import com.sammy.fufo.FufoMod;
import com.sammy.fufo.common.block.*;
import com.sammy.fufo.common.blockentity.*;
import com.sammy.fufo.core.setup.content.item.tabs.ContentTab;
import com.sammy.ortus.systems.block.OrtusBlockProperties;
import com.sammy.ortus.systems.block.OrtusEntityBlock;
import com.sammy.ortus.systems.blockentity.OrtusBlockEntity;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRegistrate {
    private static final Registrate REGISTRATE = FufoMod.registrate().creativeModeTab(ContentTab::get);


    public static final BlockEntry<Block> BLOCK_OF_CRACK = simpleBlock("block_of_crack", Block::new,
            new OrtusBlockProperties(Material.METAL, MaterialColor.FIRE).needsPickaxe().sound(SoundType.METAL).requiresCorrectToolForDrops().strength(5.0F, 6.0F).isRedstoneConductor(Blocks::never));

    //public static final BlockEntry<OrbBlock<OrbBlockEntity>> FORCE_ORB = simpleBlock("force_orb", OrbBlock::new,
    //        new OrtusBlockProperties(Material.WOOL, MaterialColor.COLOR_BLUE).sound(SoundType.WOOL).noCollission().instabreak().lightLevel((b) -> 14));

    public static final BlockEntry<PipeAnchorBlock<AnchorBlockEntity>> ANCHOR = simpleBlock("anchor", PipeAnchorBlock::new,
            new OrtusBlockProperties(Material.METAL, MaterialColor.METAL).sound(SoundType.METAL).dynamicShape().instabreak().isCutoutLayer());

    public static final BlockEntry<BurnerExtractorBlock<BurnerExtractorBlockEntity>> BURNER_EXTRACTOR = simpleBlock("burner_extractor", BurnerExtractorBlock::new,
            new OrtusBlockProperties(Material.METAL, MaterialColor.METAL).sound(SoundType.METAL).dynamicShape().isCutoutLayer());

    public static final BlockEntry<UITestBlock<UITestBlockEntity>> UI_TEST_BLOCK = simpleBlock("ui_test_block", UITestBlock::new,
            new OrtusBlockProperties(Material.STONE, MaterialColor.COLOR_GRAY).sound(SoundType.STONE));
    //public static final BlockEntry<OrbBlock<OrbBlockEntity>> FORCE_ORB = simpleTileBlock("force_orb",OrbBlock.class,BlockEntityRegistrate.ORB,new OrtusBlockProperties(Material.WOOL, MaterialColor.COLOR_BLUE).sound(SoundType.WOOL).noCollission().instabreak().lightLevel((b) -> 14));
    public static final BlockEntry<OrbBlock<OrbBlockEntity>> FORCE_ORB = simpleBlock("force_orb",
            (props) -> (OrbBlock) new OrbBlock(props).setBlockEntity(BlockEntityRegistrate.ORB),new OrtusBlockProperties(Material.WOOL, MaterialColor.COLOR_BLUE).sound(SoundType.WOOL).noCollission().instabreak().lightLevel((b) -> 14));

    public static final BlockEntry<ArrayBlock<ArrayBlockEntity>> CRUDE_ARRAY = simpleBlock("crude_array", (props) ->  new ArrayBlock<>(props).setBlockEntity(BlockEntityRegistrate.CRUDE_ARRAY), new OrtusBlockProperties(Material.HEAVY_METAL, MaterialColor.COLOR_BROWN).sound(SoundType.LODESTONE).isCutoutLayer());
    public static final BlockEntry<CrudePrimerBlock<CrudePrimerBlockEntity>> CRUDE_PRIMER = simpleBlock("crude_primer", (props) -> new CrudePrimerBlock<>(props).setBlockEntity(BlockEntityRegistrate.CRUDE_PRIMER), new OrtusBlockProperties(Material.HEAVY_METAL, MaterialColor.COLOR_BROWN).sound(SoundType.METAL).isCutoutLayer());
    public static final BlockEntry<CrudeNeedleBlock<CrudeNeedleBlockEntity>> CRUDE_NEEDLE = simpleBlock("crude_needle", (props) -> new CrudeNeedleBlock<>(props).setBlockEntity(BlockEntityRegistrate.CRUDE_NEEDLE), new OrtusBlockProperties(Material.HEAVY_METAL, MaterialColor.COLOR_BROWN).sound(SoundType.METAL).isCutoutLayer());


    public static final BlockEntry<MeteorFlameBlock<MeteorFlameBlockEntity>> METEOR_FIRE = simpleBlock("meteor_fire", p -> new MeteorFlameBlock<>(p).setBlockEntity(BlockEntityRegistrate.METEOR_FLAME),
            new OrtusBlockProperties(Material.FIRE, MaterialColor.FIRE).isCutoutLayer().noDrops().sound(SoundType.WOOL).noCollission().instabreak().lightLevel(b -> 15));

    public static final BlockEntry<FlammableMeteoriteBlock> ORTUSITE =
            REGISTRATE.block("ortusite", (prop) -> new FlammableMeteoriteBlock(prop, (s, p) -> METEOR_FIRE.get().defaultBlockState()))
                    .properties((properties) -> new OrtusBlockProperties(Material.STONE, MaterialColor.STONE).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.25F, 9.0F))
                    .simpleItem()
                    .register();

    public static final BlockEntry<Block> DEPLETED_ORTUSITE = simpleBlock("depleted_ortusite", Block::new,
            new OrtusBlockProperties(Material.STONE, MaterialColor.STONE).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.25F, 9.0F));

    private static final OrtusBlockProperties CHARRED_ROCK_PROPERTIES = new OrtusBlockProperties(Material.STONE, MaterialColor.STONE).needsPickaxe().sound(SoundType.BASALT).requiresCorrectToolForDrops().strength(1.5F, 9.0F);
    private static final NonNullUnaryOperator<BlockBehaviour.Properties> SHAPE_CHARRED_ROCK_PROPERTIES = (properties) -> CHARRED_ROCK_PROPERTIES;

    public static final BlockEntry<Block> CHARRED_ROCK = simpleBlock("charred_rock", Block::new, CHARRED_ROCK_PROPERTIES);
    public static final BlockEntry<Block> POLISHED_CHARRED_ROCK = simpleBlock("polished_charred_rock", Block::new, CHARRED_ROCK_PROPERTIES);

    //TODO create helper methods for slabs and stairs
    public static final BlockEntry<SlabBlock> CHARRED_ROCK_SLAB =
            FufoMod.registrate().block("charred_rock_slab", SlabBlock::new)
                    .properties(SHAPE_CHARRED_ROCK_PROPERTIES)
                    .blockstate(slab(CHARRED_ROCK))
                    .simpleItem()
                    .register();

    public static final BlockEntry<SlabBlock> POLISHED_CHARRED_ROCK_SLAB =
            FufoMod.registrate().block("polished_charred_rock_slab", SlabBlock::new)
                    .properties(SHAPE_CHARRED_ROCK_PROPERTIES)
                    .blockstate(slab(POLISHED_CHARRED_ROCK))
                    .simpleItem()
                    .register();

    public static final BlockEntry<StairBlock> CHARRED_ROCK_STAIRS =
            FufoMod.registrate().block("charred_rock_stairs", o -> new StairBlock(() -> CHARRED_ROCK.get().defaultBlockState(), o))
                    .properties(SHAPE_CHARRED_ROCK_PROPERTIES)
                    .blockstate(stairs(CHARRED_ROCK))
                    .simpleItem()
                    .register();

    public static final BlockEntry<StairBlock> POLISHED_CHARRED_ROCK_STAIRS =
            REGISTRATE.block("polished_charred_rock_stairs", o -> new StairBlock(() -> CHARRED_ROCK.get().defaultBlockState(), o))
                    .properties(SHAPE_CHARRED_ROCK_PROPERTIES)
                    .blockstate(stairs(CHARRED_ROCK))
                    .simpleItem()
                    .register();

    public static OrtusBlockProperties VOLCANIC_GLASS_PROPERTIES() {
        return new OrtusBlockProperties(Material.GLASS).sound(SoundType.GLASS).noOcclusion().strength(0.4f);
    }

    public static final BlockEntry<GlassBlock> VOLCANIC_GLASS = simpleBlock("volcanic_glass", GlassBlock::new, VOLCANIC_GLASS_PROPERTIES());
    public static final BlockEntry<ScorchedEarthBlock> SCORCHED_EARTH = simpleBlock("scorched_earth", ScorchedEarthBlock::new, VOLCANIC_GLASS_PROPERTIES());

    public static <T extends Block> BlockEntry<T> simpleBlock(String name, NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties) {
        return REGISTRATE.block(name, factory)
                .properties((x) -> properties)
                .simpleItem()
                .register();
    }
//TODO investigate blockEntityBuilder
    public static <T extends Block> void failedTry1(String name,NonNullFunction<BlockBehaviour.Properties, T> factory, OrtusBlockProperties properties){
        BlockEntityBuilder.BlockEntityFactory<OrbBlockEntity> blockFactory = new BlockEntityBuilder.BlockEntityFactory<OrbBlockEntity>() {
            @Override
            public OrbBlockEntity create(BlockEntityType<OrbBlockEntity> type, BlockPos pos, BlockState state) {
                return type.create(pos,state);
            }
        };
        REGISTRATE.block(name,factory)
                .properties((x) -> properties)
                .simpleItem()
                .simpleBlockEntity(blockFactory)
                .register();
    }
    public static <T extends OrtusEntityBlock<? extends OrtusBlockEntity>> BlockEntry<T> failedTry2(String name, Function<BlockBehaviour.Properties,T> constructor,BlockEntityEntry<? extends OrtusBlockEntity> entity ,OrtusBlockProperties properties){
        return REGISTRATE.block(name,(prop) -> constructor.apply(prop).setBlockEntity(entity))
                .simpleItem()
                .properties((x) -> properties)
                .register();
    }

    @SuppressWarnings("unchecked")
    public static <T extends SlabBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> slab(RegistryEntry<? extends Block> parent) {
        return (ctx, p) -> p.slabBlock(ctx.getEntry(), p.blockTexture(parent.get()), p.blockTexture(parent.get()));
    }
    @SuppressWarnings("unchecked")
    public static <T extends StairBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> stairs(RegistryEntry<? extends Block> parent) {
        return (ctx, p) -> p.stairsBlock(ctx.getEntry(), p.blockTexture(parent.get()));
    }
    /**
     * here so java doesnt ignore the class
     */
    public static void register() {
    }
}
